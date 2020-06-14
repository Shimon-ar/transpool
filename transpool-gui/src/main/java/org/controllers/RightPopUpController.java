package org.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class RightPopUpController {

    @FXML
    private VBox rightPopUp;

    @FXML
    private JFXButton animation;

    @FXML
    private JFXButton exitBut;

    private MainController mainController;




    @FXML
    void exitButAction(ActionEvent event) {
        mainController.exit();
    }

    @FXML
    void animationAction(ActionEvent event) {
        if(mainController.isAnimationPlay()) {
            mainController.setAnimationPlay(false);
            animation.setText("animation on");
        }
        else {
            mainController.setAnimationPlay(true);
            animation.setText("animation off");
        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
