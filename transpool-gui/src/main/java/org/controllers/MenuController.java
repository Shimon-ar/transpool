package org.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.fxUtilities.FxUtilities;

import java.io.IOException;
import java.net.URL;

public class MenuController {

    @FXML
    private VBox leftPopup;

    @FXML
    private JFXButton loadMapBut;

    @FXML
    private JFXButton offerBut;

    @FXML
    private JFXButton requestBut;

    @FXML
    private JFXButton matchBut;

    private MainController mainController;


    @FXML
    private void initialize() throws IOException {

    }

    @FXML
    void loadMapAction(ActionEvent event) {

    }

    @FXML
    void matchButAction(ActionEvent event) {


    }

    @FXML
    void offerButAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL tripOfferFXML = getClass().getResource("/org/fxml/TripOffer.fxml");
        loader.setLocation(tripOfferFXML);
        ScrollPane tripOffer = loader.load();
        TripOfferController tripOfferController = loader.getController();
        mainController.setTripOfferController(tripOfferController);
        tripOfferController.sync();
        FxUtilities.openNewStage(tripOffer,"trip offer");
    }

    @FXML
    void requestButAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL tripRequestFXML = getClass().getResource("/org/fxml/TripRequest.fxml");
        loader.setLocation(tripRequestFXML);
        ScrollPane tripRequest = loader.load();
        TripRequestController tripRequestController = loader.getController();
        mainController.setTripRequestController(tripRequestController);
        tripRequestController.sync();
        FxUtilities.openNewStage(tripRequest,"trip request");

    }



    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }



    public void toggleButtons(boolean bool){
        //implemetation needed
    }

}
