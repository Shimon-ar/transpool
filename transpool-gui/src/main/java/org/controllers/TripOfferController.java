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

import java.util.ArrayList;
import java.util.List;

public class TripOfferController {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox tripOfferVbox;

    @FXML
    private JFXTextField offerName;

    @FXML
    private JFXTextField dayStart;

    @FXML
    private JFXTextField ppk;

    @FXML
    private JFXTextField capacity;

    @FXML
    private JFXTimePicker checkout;

    @FXML
    private JFXComboBox<Label> recurrence;



    @FXML
    private JFXButton clearBut;

    @FXML
    private JFXComboBox<Label> stationPicked;


    @FXML
    private JFXTextArea routeTF;

    private MainController mainController;
    private List<String> route;


    @FXML
    void submitButAction(ActionEvent event) {
        boolean validInput = true;
        if(!offerName.validate())
            validInput = false;
        if(!dayStart.validate())
            validInput = false;
        if(!capacity.validate())
            validInput = false;
        if(!checkout.validate())
            validInput = false;
        if(!recurrence.validate())
            validInput = false;
        if(!stationPicked.validate())
            validInput = false;
        if(!ppk.validate())
            validInput = false;

        if(validInput){
            //send to engine parameters
            JFXDialog dialog = new JFXDialog();
            Node source = (Node)event.getSource();
            Stage tripStage  = (Stage) source.getScene().getWindow();
            FxUtilities.showAlert(tripStage,"trip offer completed successfully",true);
        }

    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void sync(){
        route = new ArrayList<>();
        recurrence.getItems().addAll(new Label("Daily"),
                new Label("BiDaily"),
                new Label("Weekly"),
                new Label("Monthly"));

        List<String> stops = mainController.getStops();

        for(String stop:stops){
            stationPicked.getItems().add(new Label(stop));
        }

        clearBut.setOnAction(myEvent->{
            routeTF.clear();
            route.clear();

        });

        stationPicked.valueProperty().addListener((o,oldValue,newValue)->{
            if(routeTF.getText().isEmpty())
                routeTF.setText(newValue.getText());
            else
                routeTF.setText(routeTF.getText() + " -> " + newValue.getText());
            route.add(newValue.getText());
        });

        setValidations();


    }

    private void setValidations(){
        GenericValidation nameExistValidation = new GenericValidation("name already exist",(value)->{
            if(value.isEmpty())
                return false;
            if(mainController.isOfferNameExist(value))
                return true;
            return false;
        });

        GenericValidation routeValidation = new GenericValidation("invalid route",(value)->{
            if(route.isEmpty())
                return true;
            if(!mainController.isValidRoute(route))
                return true;
            return false;
        });

        offerName.getValidators().addAll(new RequiredFieldValidator("input required"),nameExistValidation);
        dayStart.getValidators().addAll(new RequiredFieldValidator("input required"),new IntegerPositiveValidation("must be positive number"));
        capacity.getValidators().addAll(new RequiredFieldValidator("input required"),new IntegerPositiveValidation("must be positive number"));
        ppk.getValidators().addAll(new RequiredFieldValidator("input required"),new IntegerPositiveValidation("must be positive number"));
        checkout.getValidators().addAll(new RequiredFieldValidator("input required"));
        recurrence.getValidators().addAll(new RequiredFieldValidator("input required"));
        stationPicked.getValidators().add(routeValidation);

    }


}
