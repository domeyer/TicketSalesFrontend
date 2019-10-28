package at.fhv.ticketsales.MainView;

import at.fhv.ticketsales.service.DisplayEventsService;
import at.fhv.ticketsales.dto.EventDto;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {


    //init fxml cols and view
    @FXML private ComboBox cbGenre;
    @FXML private DatePicker dpStart, dpEnd;
    @FXML private TableView<EventDto> tfEvent;
    @FXML private TextField txfActor,txfEvent;
    @FXML private TableColumn<EventDto,String> tcActor, tcEvent, tcDestination, tcGenre, tcDate;
    //init observable list for tf
    private ObservableList<EventDto> listOfEvents, sortHelper;
    //private ArrayList<EventDto> allEvents;
    private boolean tfActorEmpty, tfEventEmpty = true;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            if (System.getSecurityManager() == null){
                //System.setProperty("java.security.policy", "C:\\Users\\Toshiba\\TicketSalesFrontend\\src\\main\\java\\at\\fhv\\TicketSalesFrontend\\ClientPolicy\\client.policy");
                System.setProperty("java.security.policy", ".\\src\\main\\java\\at\\fhv\\ticketsales\\ClientPolicy\\client.policy");
                System.setSecurityManager(new RMISecurityManager());
                System.out.println("SecurityManager created-------------------------------");
            }
            DisplayEventsService displayEventsService = (DisplayEventsService) Naming.lookup("rmi://localhost:5099/DisplayEvents");
            listOfEvents = FXCollections.observableArrayList(displayEventsService.getAllEvents());
            //listOfEvents = FXCollections.observableArrayList(allEvents);
            sortHelper = listOfEvents;
            setPropertiesOnTableView(tcActor,tcEvent,tcDestination,tcGenre,tcDate);
            tfEvent.getItems().addAll(listOfEvents);
            //wrap observable list in filtered list
            FilteredList<EventDto> filteredData = new FilteredList<>(listOfEvents);
            //set filter on change
            // bind predicate based on datepicker choices
            filteredData.predicateProperty().bind(Bindings.createObjectBinding(() -> {
                    LocalDate minDate = dpStart.getValue();
                    LocalDate maxDate = dpEnd.getValue();
                    String actor = txfActor.getText().toLowerCase();
                    String event = txfEvent.getText().toLowerCase();

                    // get final values != null
                    final LocalDate finalMin = minDate == null ? LocalDate.MIN : minDate;
                    final LocalDate finalMax = maxDate == null ? LocalDate.MAX : maxDate;

                    // values for openDate need to be in the interval [finalMin, finalMax]
                    return ti -> !finalMin.isAfter(ti.getDate()) && !finalMax.isBefore(ti.getDate()) && ti.getActor().toLowerCase().contains(actor) && ti.getEvent().toLowerCase().contains(event);
                },
                dpStart.valueProperty(),
                txfActor.textProperty(),
                txfEvent.textProperty(),
                dpEnd.valueProperty()));

            //wrap filtered list in sorted list
            SortedList<EventDto> sortedData = new SortedList<EventDto>(filteredData);
            sortHelper = sortedData;
            //Bind the SortedList comparator to the TableView comparator.
            sortedData.comparatorProperty().bind(tfEvent.comparatorProperty());
            //Add sorted (and filtered) data to the table.
            tfEvent.setItems(sortedData);
            //liste zurückhalten damit auf sortierter weiter sortiert wird !!!!!

        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    private void setPropertiesOnTableView(TableColumn actor, TableColumn event, TableColumn destination, TableColumn genre, TableColumn date ){
        //set Cell value factory on property of object attributes
        actor.setCellValueFactory(new PropertyValueFactory<EventDto,String>("actor"));
        event.setCellValueFactory(new PropertyValueFactory<EventDto,String>("event"));
        destination.setCellValueFactory(new PropertyValueFactory<EventDto,String>("destination"));
        genre.setCellValueFactory(new PropertyValueFactory<EventDto,String>("genre"));
        date.setCellValueFactory(new PropertyValueFactory<EventDto,LocalDate>("date"));


    }
}
