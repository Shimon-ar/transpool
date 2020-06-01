package org.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class RightPopUpController {

    @FXML
    private VBox rightPopUp;

    @FXML
    private JFXButton styleBut;

    @FXML
    private JFXButton exitBut;

    private MainController mainController;


    @FXML
    void exitButAction(ActionEvent event) {

    }

    @FXML
    void styleButAction(ActionEvent event) {

    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
