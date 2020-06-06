package org.controllers;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXToolbar;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Graph;
import org.components.Tabs;
import org.ds.OfferTripProperty;
import org.ds.RequestTripProperty;
import org.tasks.LoadMapTask;
import org.transpool.engine.Engine;
import org.transpool.engine.ds.MapDb;
import org.transpool.engine.ds.StopManager;
import org.transpool.engine.ds.Time;
import org.transpool.engine.ds.TranspoolTrip;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class MainController {


    @FXML
    private ScrollPane mainScroll;

    @FXML
    private BorderPane borderPane;

    @FXML
    private JFXToolbar mainToolBar;

    @FXML
    private JFXHamburger mainLeftHam;

    @FXML
    private Label labelTransPool;

    @FXML
    private JFXHamburger mainRightHam;

    private JFXPopup leftPopUp;
    private JFXPopup rightPopUp;

    private MenuController menuController;
    private TripOfferController tripOfferController;
    private TripRequestController tripRequestController;
    private RightPopUpController rightPopUpController;
    private LiveMapController liveMapController;
    private OfferTableController offerTableController;
    private RequestTableController requestTableController;
    private SimpleBooleanProperty isFileSelected;
    private SimpleBooleanProperty isMapLoaded;
    private String pathFile;
    private Engine engine;

    @FXML
    private void initialize() throws IOException, JAXBException {


        Engine engine = new Engine();
        /*engine.loadMap(new File("/Users/shimon/Desktop/Transpool/transpool/transpool-gui/src/main/resources/org/xmlMaps/ex1-real.xml"));*/
        this.engine = engine;


        isFileSelected = new SimpleBooleanProperty(false);
        isMapLoaded = new SimpleBooleanProperty(false);

        isFileSelected.addListener((o,l,n)->{
            if(n)
                actionOnFileSelected();
        });

        isMapLoaded.addListener((o,l,n)->{
          if(n)
              menuController.disableButtons(true);
        });


        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource("/org/fxml/Menu.fxml");
        loader.setLocation(mainFXML);
        VBox menu = loader.load();
        leftPopUp = new JFXPopup(menu);
        setMenuController(loader.getController());


        loader = new FXMLLoader();
        URL rightFXML = getClass().getResource("/org/fxml/RightPopUp.fxml");
        loader.setLocation(rightFXML);
        VBox rightMenu = loader.load();
        rightPopUp = new JFXPopup(rightMenu);
        setRightPopUpController(loader.getController());


    }

    public boolean isIsFileSelected() {
        return isFileSelected.get();
    }

    public SimpleBooleanProperty isFileSelectedProperty() {
        return isFileSelected;
    }

    public void setIsFileSelected(boolean isFileSelected) {
        this.isFileSelected.set(isFileSelected);
    }

    @FXML
    void mainLeftHamClicked(MouseEvent event) {

        leftPopUp.show(mainLeftHam,
                JFXPopup.PopupVPosition.TOP,
                JFXPopup.PopupHPosition.LEFT,
                0,
                0);

    }

    @FXML
    void mainRightHamClicked(MouseEvent event) {
               rightPopUp.show(mainRightHam,
                JFXPopup.PopupVPosition.TOP,
                JFXPopup.PopupHPosition.LEFT,
                0,
               0);

    }

    public Stage getStage(){
        return (Stage)mainScroll.getScene().getWindow();
    }

    public void setMenuController(MenuController menuController){
        this.menuController = menuController;
        menuController.setMainController(this);

    }

    public void setLiveMapController(LiveMapController liveMapController) {
        this.liveMapController = liveMapController;
        liveMapController.setMainController(this);
    }

    public void setEngine(Engine engine){
        this.engine = engine;
    }

    public void setTripOfferController(TripOfferController tripOfferController) {
        this.tripOfferController = tripOfferController;
        tripOfferController.setMainController(this);

    }

    public void setTripRequestController(TripRequestController tripRequestController) {
        this.tripRequestController = tripRequestController;
        tripRequestController.setMainController(this);
    }

    public void setRightPopUpController(RightPopUpController rightPopUpController) {
        this.rightPopUpController = rightPopUpController;
        rightPopUpController.setMainController(this);
    }

    public List<String> getStops(){
        return engine.getStops();
    }

    public boolean isRequestNameExist(String name){
        return engine.isNameExistRequestTrip(name);
    }

    public boolean isOfferNameExist(String name){
        return engine.isNameExistTransPool(name);
    }

    public boolean isValidRoute(List<String> route){
        return engine.isValidRoute(route);
    }


    public Map<String, StopManager> getUpdateMapDetail(Time time){
        return engine.getUpdateMapData(time);
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

    public void actionOnFileSelected(){

        FXMLLoader loader = new FXMLLoader();

        URL mainFXML = getClass().getResource("/org/fxml/Progress.fxml");
        loader.setLocation(mainFXML);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ProgressController progressController = loader.getController();
        LoadMapTask loadMapTask = new LoadMapTask(engine,pathFile,t->{
            if(t)
            {
                isMapLoaded.set(true);
                isFileSelected.set(false);
            }
            progressController.hide();
        },this);
        progressController.build(getStage(),loadMapTask);
        progressController.show();
        new Thread(loadMapTask).start();



        //build progress,run task,bind to components and finally show map





    }

    public void build(Graph graph){
       ObservableList<OfferTripProperty> offersTripsProperty = FXCollections.observableArrayList();
        if(engine.getTransPoolTrips() != null)
            engine.getTransPoolTrips().forEach(transpoolTrip -> offersTripsProperty.add(new OfferTripProperty(transpoolTrip)));
        offerTableController = new OfferTableController(offersTripsProperty);
        requestTableController = new RequestTableController(FXCollections.observableArrayList());

        FXMLLoader loader = new FXMLLoader();

        URL mainFXML = getClass().getResource("/org/fxml/LiveMap.fxml");
        loader.setLocation(mainFXML);
        VBox liveMap = null;
        try {
            liveMap = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LiveMapController liveMapController = loader.getController();
        setLiveMapController(liveMapController);
        liveMapController.setMap(graph);
        Tabs tabs = new Tabs(offerTableController.getOffersTable().getTreeTableView(),requestTableController.getRequestsTable().getTreeTableView());
        borderPane.setCenter(liveMap);
        borderPane.setBottom(tabs.getTabPane());
    }

    public void actionOnFindMatches(){

    }
}
