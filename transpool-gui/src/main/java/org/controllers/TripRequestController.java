package org.controllers;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.fxUtilities.FxUtilities;
import org.validations.GenericValidation;
import org.validations.IntegerPositiveValidation;

import java.util.List;

public class TripRequestController {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox tripRequestV;

    @FXML
    private Label tripRequestLab;

    @FXML
    private JFXTextField requestNameF;

    @FXML
    private JFXTextField dayCheckOutF;

    @FXML
    private JFXTimePicker time;


    @FXML
    private JFXComboBox<Label> fromCombo;

    @FXML
    private JFXComboBox<Label> toCombo;

    @FXML
    private JFXComboBox<Label> arrivalCheckOutB;

    @FXML
    private JFXButton submitBut;


    private MainController mainController;


    @FXML
    private void initialize(){

    }


    @FXML
    void arrivalBoxAction(ActionEvent event) {

    }

    @FXML
    void checkOutBoxAction(ActionEvent event) {

    }

    @FXML
    void submitButAction(ActionEvent event) {
        boolean validInput = true;
        if(!requestNameF.validate())
            validInput = false;
        if(!dayCheckOutF.validate())
            validInput = false;
        if(!time.validate())
            validInput = false;
        if(!arrivalCheckOutB.validate())
            validInput = false;
        if(!fromCombo.validate())
            validInput = false;
        if(!toCombo.validate())
            validInput = false;

        if(validInput){
            //send to engine parameters
            JFXDialog dialog = new JFXDialog();
            Node source = (Node)event.getSource();
            Stage tripStage  = (Stage) source.getScene().getWindow();
            FxUtilities.showAlert(tripStage,"trip request completed successfully",true);
        }

    }

    public void sync(){
        time.set24HourView(true);

        List<String> stops = mainController.getStops();
        for(String stop:stops){
            fromCombo.getItems().add(new Label(stop));
            toCombo.getItems().add(new Label(stop));
        }
        arrivalCheckOutB.getItems().addAll(new Label("Arrival"),new Label("Check-out"));

        GenericValidation nameExistValidation = new GenericValidation("name already exist",(value)->{
            if(value.isEmpty())
                return false;
            if(mainController.isRequestNameExist(value))
                return true;
            return false;
        });

        GenericValidation diffStations = new GenericValidation("dest = source",(value)->{
            if(value.isEmpty() || toCombo.getValue() == null)
                return false;
            if((value.equals(toCombo.getValue().getText())))
                return true;
            return false;

        });


        requestNameF.getValidators().addAll(new RequiredFieldValidator("input required"),nameExistValidation);
        dayCheckOutF.getValidators().addAll(new RequiredFieldValidator("input required"),new IntegerPositiveValidation("number must be positive"));
        time.getValidators().add(new RequiredFieldValidator("input required"));
        arrivalCheckOutB.getValidators().add(new RequiredFieldValidator("input required"));
        fromCombo.getValidators().addAll(new RequiredFieldValidator("input required"),diffStations);
        toCombo.getValidators().add(new RequiredFieldValidator("input required"));


    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }


}
