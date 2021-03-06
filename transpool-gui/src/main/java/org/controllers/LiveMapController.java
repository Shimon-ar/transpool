package org.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import model.Graph;
import org.transpool.engine.Engine;
import org.transpool.engine.ds.Time;

import java.util.List;

public class LiveMapController {

    @FXML
    private VBox vBox;

    @FXML
    private Label timeLabel;

    @FXML
    private Label headLabel;

    @FXML
    private JFXButton prevB;

    @FXML
    private JFXComboBox<Label> combo;

    @FXML
    private JFXButton nextB;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private AnchorPane anchorPane;

    private MainController mainController;
    private Graph map;
    private Time currentTime;


    public void initialize(){
        combo.getItems().addAll(new Label("5 min"),
                new Label("30 min"),
                new Label("1 hour"),
                new Label("2 hours"),
                new Label("1 day")
        );
        currentTime = new Time(0,0,1);
        combo.valueProperty().addListener((o,r,n)->{
            nextB.setDisable(false);
            setPrevDisable();
        });
        setTimeLabel();
        anchorPane.getStyleClass().add("anchor");
        anchorPane.getStylesheets().add("/org/css/anchorPane.css");
        scrollPane.getStylesheets().add("/org/css/scrollPane.css");
    }

    private void setTimeLabel(){
        timeLabel.setText(currentTime + "  day: " + currentTime.getDay());
    }


    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setMap(Graph map) {

        this.map = map;
        map.updateDetails(mainController.getUpdateMapDetail(currentTime),currentTime);
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(map.getCanvas());
    }

    @FXML
    void clickNext(ActionEvent event) {
       currentTime = updateTime(true);
        map.updateDetails(mainController.getUpdateMapDetail(currentTime),currentTime);
       setPrevDisable();
       setTimeLabel();
    }

    private void setPrevDisable(){
        Time time = updateTime(false);
        System.out.println(time);
        if(time.getDay()<1)
            prevB.setDisable(true);
       else prevB.setDisable(false);
    }

    @FXML
    void clickPrev(ActionEvent event) {
        currentTime = updateTime(false);
        map.updateDetails(mainController.getUpdateMapDetail(currentTime),currentTime);
        setPrevDisable();
        setTimeLabel();
    }

    private Time updateTime(boolean next){
        String scale = combo.getValue().getText();
        Time time = currentTime.clone();
        if(scale.equals("5 min")){
            if(next)
                time.minToAdd(5);
            else time.minToAdd(-5);
        }
        else if(scale.equals("1 hour")){
            if(next)
                time.minToAdd(60);
           else time.minToAdd(-60);
        }

        else if(scale.equals("2 hours")){
            if(next)
                time.minToAdd(120);
          else  time.minToAdd(-120);
        }

        else if(scale.equals("30 min")){
            if(next)
                time.minToAdd(30);
           else time.minToAdd(-30);
        }

        else if(scale.equals("1 day")){
            if(next)
                time.minToAdd(1440);
           else time.minToAdd(-1440);
        }
          return time;
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public void makeAnimation(List<String> route){
        map.playAnimation(route);
    }
}
