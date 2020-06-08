package org.controllers;

import com.jfoenix.controls.JFXListView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.ds.RequestTripProperty;
import org.fxUtilities.FxUtilities;
import org.guiMain.Main;
import org.transpool.engine.ds.Match;
import org.transpool.engine.ds.RequestTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptionalMatchesControllers {

    @FXML
    private JFXListView<Label> list;
    private Map<Label, Match> map;
    private MainController mainController;
    private RequestTripProperty requestTripProperty;

    public void init(List<Match> matches,MainController mainController,RequestTripProperty requestTripProperty){
        this.mainController = mainController;
        this.requestTripProperty = requestTripProperty;
        list.depthProperty().set(1);
        list.setExpanded(true);
        map = new HashMap<>();
        for(Match match:matches){
            Label label = new Label(createMassage(match));
            map.put(label,match);
            list.getItems().add(label);
        }
    }

    @FXML
    void submitClick(ActionEvent event) {
        Node source = (Node)event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        Label label = list.getSelectionModel().getSelectedItem();
        if(label != null) {
            mainController.getEngine().setMatch(map.get(label));
            requestTripProperty.setIsMatched();
            FxUtilities.showAlert(stage,"match has been set successfully",true);


        }
        else {
            FxUtilities.showAlert(stage,"must choose request trip",false);
        }

    }

    private String  createMassage(Match match){
        List<String> offers = new ArrayList<>();
        List<List<String>> route = match.getRoutes();
        List<RequestTime> times = match.getTimeForEachRoute();
        System.out.println(route.size());
        List<String> names = match.getOffersNames();
        for(int i = 0;i<route.size();i++){
            String source = route.get(i).get(0);
            String destination = route.get(i).get(route.get(i).size() - 1);
            if(offers.isEmpty())
                offers.add("Leaving with " + names.get(0) + ", from " + source + " at " + times.get(0).getCheckoutTime() +
                        " day " + times.get(0).getCheckoutTime().getDay() +
                        ", and arriving to " + destination + " at " + times.get(0).getArrivalTime() +
                        " day " + times.get(0).getArrivalTime().getDay());
            else offers.add("then leaving with " + names.get(i) + " at " + times.get(i).getCheckoutTime() +
                            " day " + times.get(i).getCheckoutTime().getDay() +
                    " and arriving to " + destination + " at " + times.get(i).getArrivalTime() +
                    " day " + times.get(i).getArrivalTime().getDay() );

        }
        System.out.println(offers.size() + "route");

        return String.join("\n", offers);

    }

}
