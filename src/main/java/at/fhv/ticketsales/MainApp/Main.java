package at.fhv.ticketsales.MainApp;

import at.fhv.ticketsales.MainView.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        MainView view = new MainView();
        Scene scene = new Scene(view.getView());
        primaryStage.setScene(scene);
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.show();

    }
}
