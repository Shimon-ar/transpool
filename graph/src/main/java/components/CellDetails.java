package components;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CellDetails {
    public static void showAlert(Stage stage,String head, String text, boolean closeCurrentStage){


        JFXAlert alert = new JFXAlert(stage);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);
        alert.setSize(500,190);
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label(head));
        layout.setBody(new Label(text));
        JFXButton closeButton = new JFXButton("Got it!");
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

    public static void showMassage(Stage stage, String head, Node node, boolean closeCurrentStage){


        JFXAlert alert = new JFXAlert(stage);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);
        alert.setSize(500,300);
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label(head));
        layout.setBody(node);
        JFXButton closeButton = new JFXButton("Got it!");
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
