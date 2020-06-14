package components;

import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTabPane;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.transpool.engine.ds.StopManager;
import org.transpool.engine.ds.Time;
import org.transpool.engine.ds.TranspoolTrip;
import org.transpool.engine.ds.TripsManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Cell extends Pane {

    private String cellId;
    private StationController stationController;
    private int numOfCars;
    private List<TranspoolTrip> trips;
    List<Cell> children = new ArrayList<>();
    List<Cell> parents = new ArrayList<>();
    Time time;
    private double x,y;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    Node view;

    public Cell(String cellId,double x,double y,int corX,int corY) {
       trips = new ArrayList<>();
       setTime(new Time(0,0,1));
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
        Tooltip.install(stationController.getImageArea(), tooltip);
        setDetails();

    }

    public void setTime(Time time) {
        this.time = time;
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

   public void setTrips(List<TranspoolTrip> trips){
        this.trips = trips;
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
        try {
            try (InputStream input = getClass().getResourceAsStream("/car.jpg")) {
                Image image = new Image(input);
                for (int i = 0; i < num; i++) {
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(30);
                    imageView.setFitWidth(25);
                    stationController.getCarArea().getChildren().add(imageView);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDetails(){
        stationController.setOnClick(stage -> {
            if(trips.isEmpty()){
                CellDetails.showAlert(stage,
                        cellId , "\n" +
                                "There are no trips passing through this station"
                        , false);
            }
            else{
            /*   // VBox vBox = new VBox();
                vBox.setAlignment(Pos.TOP_CENTER);
                vBox.setSpacing(10);
                vBox.getChildren().add(new Label("Trips"));*/
                JFXTabPane tabPane = new JFXTabPane();
                for(TranspoolTrip transpoolTrip:trips){
                    Tab tab = new Tab();
                    tab.setText(transpoolTrip.getName());
                    String details = "";
                    String upNames = "";
                    String downNames = "";
                    StopManager stopManager = transpoolTrip.getStopsManager(time.getDay()).get(cellId);
                        boolean pass = false;
                    if (!stopManager.getUpCostumers().isEmpty()) {
                        pass = true;
                        upNames = "join: " + String.join(" , ", stopManager.getUpCostumers());
                    }
                    if (!stopManager.getDownCostumers().isEmpty()) {
                        pass = true;
                        downNames = "leave: " + String.join(" , ", stopManager.getDownCostumers());
                    }
                    if (!upNames.isEmpty() && !downNames.isEmpty())
                        details = upNames + " , " + downNames;
                    else if (!upNames.isEmpty())
                        details = upNames;
                    else if (!downNames.isEmpty())
                        details = downNames;

                    String attachedPassengers = "";
                    List<Integer> requestsId = transpoolTrip.getRequestsID(time.getDay());
                    if(!requestsId.isEmpty())
                        attachedPassengers = "attached passengers ID's: " + requestsId.stream().map(String::valueOf)
                            .collect(Collectors.joining(","));


                    Label label = new Label("id: " + transpoolTrip.getId() +
                            "\ncapacity: " + stopManager.getCapacity() +
                            "\n" + attachedPassengers + "\n"
                            + details);
                   // JFXDialogLayout content = new JFXDialogLayout();
                    //content.setBody(label);
                    //tab.setContent(content);
                    tab.setContent(label);
                    tabPane.getTabs().add(tab);

                }

                CellDetails.showMassage(stage,"Trips",tabPane,false);
            }




        });
    }

    /*public void setDetailMessage(){
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
*/

    }


