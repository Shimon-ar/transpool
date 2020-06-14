package org.guiMain;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Graph;
import org.controllers.TripOfferController;
import org.fxUtilities.FxUtilities;
import org.transpool.engine.Engine;
import org.transpool.engine.ds.MapDb;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();

        URL mainFXML = getClass().getResource("/org/fxml/Main.fxml");
        loader.setLocation(mainFXML);
        BorderPane root = loader.load();

        primaryStage.setTitle("transpool");

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
