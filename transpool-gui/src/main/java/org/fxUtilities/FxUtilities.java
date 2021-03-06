package org.fxUtilities;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FxUtilities {
    public static void openNewStage(Parent parent, String title){
        Stage secStage = new Stage();
        secStage.initModality(Modality.APPLICATION_MODAL);
        secStage.setTitle(title);


        Scene scene = new Scene(parent);
        secStage.setScene(scene);
        secStage.show();
    }

    public static void showAlert(Stage stage,String text,boolean closeCurrentStage,String head,int height,int width){


        JFXAlert alert = new JFXAlert(stage);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);
        alert.setSize(width,height);
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label(head));
        layout.setBody(new Label(text));
        JFXButton closeButton = new JFXButton("Got it");
        if(closeCurrentStage) {
            closeButton.setOnAction(myEvent -> {
                alert.hideWithAnimation();
                stage.close();
            });
        }
        else{
            closeButton.setOnAction(myEvent -> {
                alert.hideWithAnimation();
            });
        }
        layout.setActions(closeButton);
        alert.setContent(layout);
        alert.show();
    }

    public static void showMassage(Stage stage,String body,String head,boolean closeCurrentStage){
        JFXAlert alert = new JFXAlert(stage);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);
        alert.setSize(270,400);
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label(head));
        layout.setBody(new Label(body));
        JFXButton closeButton = new JFXButton("Got it");
        if(closeCurrentStage) {
            closeButton.setOnAction(myEvent -> {
                alert.hideWithAnimation();
                stage.close();
            });
        }
        else{
            closeButton.setOnAction(myEvent -> {
                alert.hideWithAnimation();
            });
        }
        layout.setActions(closeButton);
        alert.setContent(layout);
        alert.show();
    }




}
