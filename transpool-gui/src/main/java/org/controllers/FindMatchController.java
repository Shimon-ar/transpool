package org.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.sun.deploy.net.proxy.MacOSXSystemProxyHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.ds.RequestTripProperty;
import org.fxUtilities.FxUtilities;
import org.transpool.engine.ds.Match;
import org.transpool.engine.ds.RequestTrip;
import org.validations.IntegerPositiveValidation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindMatchController {

    @FXML
    private JFXButton submitB;

    @FXML
    private JFXTextField limitF;

    private MainController mainController;
    private String selectedKey;
    private Map<String,RequestTripProperty> mapRequest;
    private Map<String,JFXCheckBox> mapCheckBoxes;
    @FXML
    private VBox vBox;



    public FindMatchController() {
        selectedKey = "";
        mapRequest = new HashMap<>();
        mapCheckBoxes = new HashMap<>();

    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void init(){
        List<RequestTripProperty> requestTrips = mainController.getUnMatchedRequested();
        for(RequestTripProperty requestTrip:requestTrips) {
            limitF.getValidators().addAll(new RequiredFieldValidator("input required"),new IntegerPositiveValidation("must be positive number"));
            JFXCheckBox checkBox = new JFXCheckBox();
            checkBox.setText(requestTrip.getName() + " , " + requestTrip.getFrom() + " - " + requestTrip.getTo() + " , "
                    + requestTrip.getRequestTrip().getRequestTime().getWhichTime().name() + " , " + requestTrip.getRequestTrip().getRequestTime().getTime() + " , day:" + requestTrip.getRequestTrip().getRequestTime().getTime().getDay());
            checkBox.setId(requestTrip.getRequestTrip().getName());
            mapRequest.put(checkBox.getId(),requestTrip);
            mapCheckBoxes.put(checkBox.getId(),checkBox);
            vBox.getChildren().add(checkBox);
            checkBox.selectedProperty().addListener((o,old,n)->{
                if(n && selectedKey.isEmpty()){
                    selectedKey = checkBox.getId();
                }
                else if(n) {
                    mapCheckBoxes.get(selectedKey).setSelected(false);
                    selectedKey = checkBox.getId();
                }
                else selectedKey = "";
            });


        }

    }


    @FXML
    void clickSubmit(ActionEvent event) {
        Node source = (Node)event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        if(!selectedKey.isEmpty() && limitF.validate()) {
            stage.close();
            mainController.actionOnFindMatch(mapRequest.get(selectedKey), Integer.parseInt(limitF.getText()));
        }
        else if(selectedKey.isEmpty()){
            FxUtilities.showAlert(stage,"must choose request trip",false);
        }

    }

}
