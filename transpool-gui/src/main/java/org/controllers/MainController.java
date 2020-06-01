package org.controllers;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXToolbar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.ds.OfferTripProperty;
import org.ds.RequestTripProperty;
import org.transpool.engine.Engine;
import org.transpool.engine.ds.TranspoolTrip;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

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
    private ObservableList<RequestTripProperty> requestTripProperty;
    private ObservableMap<TranspoolTrip, OfferTripProperty> mapOffersTripProperty;
    private Engine engine;

    @FXML
    private void initialize() throws IOException, JAXBException {


        Engine engine = new Engine();
        engine.loadMap(new File("/Users/shimon/Desktop/Transpool/transpool/transpool-gui/src/main/resources/org/xmlMaps/ex1-real.xml"));
        this.engine = engine;
        setMapOffersTrips();
        requestTripProperty = FXCollections.emptyObservableList();


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

    public void setMapOffersTrips(){
        mapOffersTripProperty = FXCollections.observableHashMap();
        List<TranspoolTrip> offerTrips = engine.getTransPoolTrips();
        for(TranspoolTrip transpoolTrip:offerTrips){
            mapOffersTripProperty.put(transpoolTrip,new OfferTripProperty(transpoolTrip));
        }
    }

    public ObservableList<RequestTripProperty> getRequestTripProperty() {
        return requestTripProperty;
    }

    public ObservableMap<TranspoolTrip, OfferTripProperty> getMapOffersTripProperty() {
        return mapOffersTripProperty;
    }
}
