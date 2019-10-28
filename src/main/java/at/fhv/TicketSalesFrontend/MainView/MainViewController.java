package at.fhv.TicketSalesFrontend.MainView;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {


    //init fxml cols and view
    @FXML private Button btClear;
    @FXML private ComboBox<String> cbGenre;
    @FXML private DatePicker dpStart, dpEnd;
    @FXML private TableView<EventDto> tfEvent;
    @FXML private TextField txfActor,txfEvent;
    @FXML private TableColumn<EventDto,String> tcActor, tcEvent, tcDestination, tcGenre, tcDate;
    //init observable list for tf
    private ObservableList<EventDto> listOfEvents, sortHelper;
    private ArrayList<EventDto> allEvents;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        //controller.getAllEventForDate();
        allEvents = new ArrayList<EventDto>();
        for (int i = 0; i < 10; i++){
            EventDto event = null;
            if(i < 3){
                event = new EventDto("Meyer Dominik","Konzert","Nüziders","Deutsch Rap",LocalDate.now());
            }else if (i < 8){
                event = new EventDto("ABC MUSTER","KEINE","DORNBIRN","Rock",LocalDate.now());
            }else{
                event = new EventDto("NEUER TEST","WAS NE","BLUDENZ","Klassiker",LocalDate.now());
            }
            allEvents.add(event);
        }
        listOfEvents = FXCollections.observableArrayList(allEvents);
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
                    String genre = cbGenre.getSelectionModel().getSelectedItem();

                    // get final values != null
                    final String genreVal = genre == null ? "" : genre;
                    final LocalDate finalMin = minDate == null ? LocalDate.MIN : minDate;
                    final LocalDate finalMax = maxDate == null ? LocalDate.MAX : maxDate;

                    //return matching values
                    return ti -> !finalMin.isAfter(ti.getDate()) && !finalMax.isBefore(ti.getDate()) && ti.getActor().toLowerCase().contains(actor) && ti.getEvent().toLowerCase().contains(event) && ti.getGenre().toLowerCase().contains(genreVal.toLowerCase());

                },
                dpStart.valueProperty(),
                txfActor.textProperty(),
                txfEvent.textProperty(),
                cbGenre.valueProperty(),
                dpEnd.valueProperty()));




        //wrap filtered list in sorted list
        SortedList<EventDto> sortedData = new SortedList<EventDto>(filteredData);
        sortHelper = sortedData;
        //Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tfEvent.comparatorProperty());
        //Add sorted (and filtered) data to the table.
        tfEvent.setItems(sortedData);

        btClear.setOnAction(event -> {
            txfEvent.clear();
            txfActor.clear();
            dpStart.getEditor().clear();
            dpEnd.getEditor().clear();
            cbGenre.getSelectionModel().clearSelection();
        });




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
