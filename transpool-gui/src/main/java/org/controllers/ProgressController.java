package org.controllers;

import com.jfoenix.controls.*;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class ProgressController {

    @FXML
    private Label label;

    @FXML
    private JFXProgressBar progressBar;

    @FXML
    private VBox vbox;

    @FXML
    private Label percentLabel;

    private JFXAlert alert;


    public void build(Stage stage,Task<Boolean> task) {
        alert = new JFXAlert(stage);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);
        alert.setSize(200,190);
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setBody(vbox);
        bindToTask(task);
        alert.setContent(layout);
    }

    public void show(){
        alert.show();
    }
    public void hide(){alert.hideWithAnimation();}

     private void bindToTask(Task<Boolean> task){
        label.textProperty().bind(task.messageProperty());
        progressBar.progressProperty().bind(task.progressProperty());
        percentLabel.textProperty().bind(
                Bindings.concat(
                        Bindings.format(
                                "%.0f",
                                Bindings.multiply(
                                        progressBar.progressProperty(),
                                        100)),
                        " %"));

    }

}
