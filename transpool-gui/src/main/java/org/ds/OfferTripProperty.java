package org.ds;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.transpool.engine.Engine;
import org.transpool.engine.ds.StopManager;
import org.transpool.engine.ds.Time;
import org.transpool.engine.ds.TranspoolTrip;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OfferTripProperty extends RecursiveTreeObject<OfferTripProperty> {


    private TranspoolTrip offerTrip;
    private SimpleIntegerProperty day;
    private SimpleStringProperty arrival;
    private SimpleStringProperty checkout;
    private SimpleStringProperty name;
    private SimpleStringProperty recurrences;
    private SimpleIntegerProperty id;
    private SimpleStringProperty requestsId;
    private ObservableList<String> timeList;
    private ObservableList<String> capacityList;
    private ObservableList<String> stops;
    private ObservableList<String> upDownPassengers;

    public OfferTripProperty(TranspoolTrip offerTrip) {
        this.offerTrip = offerTrip;
        requestsId = new SimpleStringProperty();
        timeList = FXCollections.emptyObservableList();
        capacityList = FXCollections.emptyObservableList();
        stops = FXCollections.emptyObservableList();
        upDownPassengers = FXCollections.emptyObservableList();
        initial();
    }

    public TranspoolTrip getOfferTrip() {
        return offerTrip;
    }

    public void updateDynamicData(Engine engine) {
        if(offerTrip.getRequestsID().size() > 0)
           requestsId.set("Passengers attached by id: " + offerTrip.getRequestsID().stream()
                   .map(String::valueOf).
                           collect(Collectors.joining(" , ")));
        capacityUpdate(engine);
        passengersUpDownUpdate();
    }

    private void initial() {
        day = new SimpleIntegerProperty(offerTrip.getScheduling().getDay_start());
        arrival = new SimpleStringProperty(offerTrip.getArrivalTime().toString());
        checkout = new SimpleStringProperty(offerTrip.getCheckoutTime().toString());
        name = new SimpleStringProperty(offerTrip.getName());
        recurrences = new SimpleStringProperty(offerTrip.getScheduling().getRecurrences().name());
        id = new SimpleIntegerProperty(offerTrip.getId());
        /*requestsId.set("No passengers attached");
        capacityList.add(Integer.toString(offerTrip.getInitCapacity()));
        timeList.add(offerTrip.getCheckoutTime() + " - " + offerTrip.getArrivalTime());*/
    }

    private void capacityUpdate(Engine engine) {
        List<Integer> passengersID = offerTrip.getRequestsID();
        if (passengersID != null) {
            timeList.clear();
            capacityList.clear();
            Map<String, StopManager> stopManager = offerTrip.getStopsManager();
            List<String> route = offerTrip.getRoute();
            Time leftTime = offerTrip.getCheckoutTime();
            int counter = 0;
            int prevCapacity = stopManager.get(route.get(0)).getCapacity();
            for (String stop : route) {
                counter++;
                int currCapacity = stopManager.get(stop).getCapacity();
                if (currCapacity != prevCapacity) {
                    Time rightTime = offerTrip.whenArrivedToStop(stop, engine.getMap());
                    timeList.add(leftTime + " - " + rightTime);
                    capacityList.add(Integer.toString(prevCapacity));
                    leftTime = rightTime;
                } else if (route.size() == counter) {
                    timeList.add(leftTime + " - " + offerTrip.getArrivalTime());
                    capacityList.add(Integer.toString(prevCapacity));
                }
                prevCapacity = currCapacity;
            }
        }
    }

    private void passengersUpDownUpdate() {
        List<String> route = offerTrip.getRoute();
        Map<String, StopManager> stopManagerMap = offerTrip.getStopsManager();
        upDownPassengers.clear();
        stops.clear();
        for (String stop : route) {
            String upNames = "";
            String downNames = "";
            StopManager stopManager = stopManagerMap.get(stop);
            boolean pass = false;
            if (!stopManager.getUpCostumers().isEmpty()) {
                pass = true;
                upNames = "join passengers: " + String.join(" , ", stopManager.getUpCostumers());
            }
            if (!stopManager.getDownCostumers().isEmpty()) {
                pass = true;
                downNames = "leave passengers: " + String.join(" , ", stopManager.getDownCostumers());
            }
            if(pass)
                stops.add(stop);
            if (!upNames.isEmpty() && !downNames.isEmpty())
                upDownPassengers.add(upNames + downNames);
            else if (!upNames.isEmpty())
                upDownPassengers.add(upNames);
            else if (!downNames.isEmpty())
                upDownPassengers.add(downNames);
        }

    }


    public SimpleStringProperty requestsIdProperty() {
        return requestsId;
    }

    public ObservableList<String> getTimeList() {
        return timeList;
    }

    public ObservableList<String> getCapacityList() {
        return capacityList;
    }

    public ObservableList<String> getStops() {
        return stops;
    }

    public ObservableList<String> getUpDownPassengers() {
        return upDownPassengers;
    }

    public int getDay() {
        return day.get();
    }

    public SimpleIntegerProperty dayProperty() {
        return day;
    }

    public String getArrival() {
        return arrival.get();
    }

    public SimpleStringProperty arrivalProperty() {
        return arrival;
    }

    public String getCheckout() {
        return checkout.get();
    }

    public SimpleStringProperty checkoutProperty() {
        return checkout;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getRecurrences() {
        return recurrences.get();
    }

    public SimpleStringProperty recurrencesProperty() {
        return recurrences;
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public String getRequestsId() {
        return requestsId.get();
    }
}