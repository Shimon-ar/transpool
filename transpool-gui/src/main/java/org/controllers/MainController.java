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
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import org.fxUtilities.FxUtilities;
import org.tasks.FindMatchesTask;
import org.tasks.LoadMapTask;
import org.transpool.engine.Engine;
import org.transpool.engine.ds.*;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private SimpleBooleanProperty animationPlay;
    private String pathFile;
    private Engine engine;


    @FXML
    private void initialize() throws IOException, JAXBException {


        Engine engine = new Engine();
        /*engine.loadMap(new File("/Users/shimon/Desktop/Transpool/transpool/transpool-gui/src/main/resources/org/xmlMaps/ex1-real.xml"));*/
        this.engine = engine;


        isFileSelected = new SimpleBooleanProperty(false);
        isMapLoaded = new SimpleBooleanProperty(false);
        animationPlay = new SimpleBooleanProperty(true);

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

    public boolean isAnimationPlay() {
        return animationPlay.get();
    }

    public void setAnimationPlay(boolean animationPlay) {
        this.animationPlay.set(animationPlay);
    }

    public SimpleBooleanProperty animationPlayProperty() {
        return animationPlay;
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
                isMapLoaded.set(t);
                isFileSelected.set(false);
                progressController.hide();
        },this);
        progressController.build(getStage(),loadMapTask);
        progressController.show();
        new Thread(loadMapTask).start();

    }



    public void build(Graph graph){
       ObservableList<OfferTripProperty> offersTripsProperty = FXCollections.observableArrayList();
        if(engine.getTransPoolTrips() != null)
            engine.getTransPoolTrips().forEach(transpoolTrip -> offersTripsProperty.add(new OfferTripProperty(transpoolTrip)));
        offerTableController = new OfferTableController(offersTripsProperty);
        offerTableController.setMainController(this);
        requestTableController = new RequestTableController(FXCollections.observableArrayList());
        requestTableController.setMainController(this);
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



    public void showUnMatchRequests(){
        FXMLLoader loader = new FXMLLoader();
        VBox vBox = null;

        URL mainFXML = getClass().getResource("/org/fxml/FindMatch.fxml");
        loader.setLocation(mainFXML);
        try {
            vBox = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FindMatchController findMatchController = loader.getController();
        findMatchController.setMainController(this);
        findMatchController.init();
        FxUtilities.openNewStage(vBox,"unmatched requested ");
    }

    public void actionOnFindMatch(RequestTripProperty requestTripProperty,int limit){
        FXMLLoader loader = new FXMLLoader();

        URL mainFXML = getClass().getResource("/org/fxml/Progress.fxml");
        loader.setLocation(mainFXML);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ProgressController progressController = loader.getController();
        FindMatchesTask findMatchesTask = new FindMatchesTask(engine,requestTripProperty,limit);
        progressController.build(getStage(),findMatchesTask);
        findMatchesTask.valueProperty().addListener((o,l,n)->{
            progressController.hide();
            showMatches(findMatchesTask.getMatches(),requestTripProperty);
        });

        progressController.show();
        new Thread(findMatchesTask).start();
    }

    public void showMatches(List<Match> matches,RequestTripProperty requestTripProperty){
        FXMLLoader loader = new FXMLLoader();
        VBox vBox = null;

        URL mainFXML = getClass().getResource("/org/fxml/OptionalMatches.fxml");
        loader.setLocation(mainFXML);
        try {
            vBox = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        OptionalMatchesControllers optionalMatchesControllers = loader.getController();
        optionalMatchesControllers.init(matches,this,requestTripProperty);
        FxUtilities.openNewStage(vBox,"results");



    }


    public void addRequest(String name,String from,String to,Time time,String whichTime,boolean comfort,int hourFlex){
        RequestTrip requestTrip = engine.inRequest(name,from,to,time,whichTime,comfort,hourFlex);
        requestTableController.addRequest(new RequestTripProperty(requestTrip));
    }

    public void addOffer(String name,List<String> route,Time time,String recurrences,int ppk,int capacity){
        TranspoolTrip transpoolTrip = engine.addTransPoolTrip(name,route,time,recurrences,ppk,capacity);
        offerTableController.addOffer(new OfferTripProperty(transpoolTrip));
    }

    public void makeAnimation(List<String> route) {
        if (engine.isValidRoute(route))
            liveMapController.makeAnimation(route);
    }

    public Engine getEngine() {
        return engine;
    }

    public List<RequestTripProperty> getUnMatchedRequested(){
        return requestTableController.getRequestTripPropertyObservableList().stream().filter(r -> !r.getRequestTrip().isMatch()).collect(Collectors.toList());
    }
}
