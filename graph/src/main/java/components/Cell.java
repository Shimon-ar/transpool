package components;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Cell extends Pane {

    private String cellId;
    private StationController stationController;
    private int numOfCars;
    private List<String> up;
    private List<String> down;
    List<Cell> children = new ArrayList<>();
    List<Cell> parents = new ArrayList<>();
    private double x,y;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    Node view;

    public Cell(String cellId,double x,double y,int corX,int corY) {
       up = new ArrayList<>();
       down = new ArrayList<>();
       this.cellId = cellId;
        this.x = x;
        this.y = y;
        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource("/stop.fxml");
        loader.setLocation(mainFXML);
        try {
            view = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setView(view);
        stationController = loader.getController();

        Tooltip tooltip = new Tooltip(cellId + "\n" +"("+ corX + "," + corY + ")");
        hackTooltipStartTiming(tooltip);
        Tooltip.install(stationController.getStackPane(), tooltip);
        setDetailMessage();

    }

    public void addCellChild(Cell cell) {
        children.add(cell);
    }

    public List<Cell> getCellChildren() {
        return children;
    }

    public void addCellParent(Cell cell) {
        parents.add(cell);
    }

    public List<Cell> getCellParents() {
        return parents;
    }

    public void removeCellChild(Cell cell) {
        children.remove(cell);
    }

    public void setView(Node view) {

        this.view = view;
        getChildren().add(view);

    }

    public Node getView() {
        return this.view;
    }

    public String getCellId() {
        return cellId;
    }

    public void setUp(List<String> up) {
        this.up = up;
    }

    public void setDown(List<String> down) {
        this.down = down;
    }

    public StationController getStationController() {
        return stationController;
    }

    private void hackTooltipStartTiming(Tooltip tooltip) {
        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(20)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setCars(int num){
        this.numOfCars = num;
        stationController.getCarArea().getChildren().clear();
        FileInputStream input = null;
        try {
            input = new FileInputStream("/Users/shimon/Desktop/projects/transpool/graph/src/main/resources/car.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Image image = new Image(input);
       for(int i = 0;i<num;i++){
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(18);
            imageView.setFitWidth(20);
            stationController.getCarArea().getChildren().add(imageView);
        }
    }

    public void setDetailMessage(){
        stationController.setOnClick(stage -> {
            if(!up.isEmpty() && !down.isEmpty()) {
                CellDetails.showAlert(stage,
                        cellId , "\n" + "Passengers going up:" +
                                String.join(",", up) + "\n" +
                                "Passengers going down:" + String.join(",", down)
                        , false);
            }

            else   if(!up.isEmpty()) {
                CellDetails.showAlert(stage,
                        cellId , "\n" + "Passengers going up:" +
                                String.join(",", up) + "\n"
                        , false);
            }

            else   if(!down.isEmpty()) {
                CellDetails.showAlert(stage,
                        cellId , "\n" +
                                "Passengers going down:" + String.join(",", down)
                        , false);
            }

            else {
                CellDetails.showAlert(stage,
                        cellId , "\n" +
                                "No passengers going down nor going up"
                        , false);
            }

            });

    }


    }


