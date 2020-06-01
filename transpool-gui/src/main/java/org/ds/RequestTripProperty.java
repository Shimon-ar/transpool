package org.ds;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;
import org.transpool.engine.ds.RequestTrip;

public class RequestTripProperty extends RecursiveTreeObject<RequestTripProperty> {
    private SimpleBooleanProperty isMatched;
    private RequestTrip requestTrip;
    private SimpleIntegerProperty id;
    private SimpleStringProperty to;
    private SimpleStringProperty from;
    private SimpleIntegerProperty day;
    private SimpleStringProperty time;
    private SimpleStringProperty name;
    private SimpleStringProperty checkArrival;
    private SimpleStringProperty matched;

    public RequestTripProperty(RequestTrip requestTrip) {
        this.requestTrip = requestTrip;
        isMatched = new SimpleBooleanProperty(false);
        id = new SimpleIntegerProperty(requestTrip.getId());
        to = new SimpleStringProperty(requestTrip.getTo());
        from = new SimpleStringProperty(requestTrip.getFrom());
        day = new SimpleIntegerProperty(requestTrip.getTime().getTime().getDay());
        time = new SimpleStringProperty(requestTrip.getTime().getTime().toString());
        name = new SimpleStringProperty(requestTrip.getName());
        checkArrival = new SimpleStringProperty(requestTrip.getRequestTime().getWhichTime().name());
        matched = new SimpleStringProperty("No");
        isMatched.addListener((o,c,n)->{
            if(n){
                matched.set("Yes");
            }
        });
    }

    public SimpleBooleanProperty isMatchedProperty() {
        return isMatched;
    }


    public RequestTrip getRequestTrip() {
        return requestTrip;
    }

    public void setIsMatched() {
        this.isMatched.set(true);
    }

    public boolean isIsMatched() {
        return isMatched.get();
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public String getTo() {
        return to.get();
    }

    public SimpleStringProperty toProperty() {
        return to;
    }

    public String getFrom() {
        return from.get();
    }

    public SimpleStringProperty fromProperty() {
        return from;
    }

    public int getDay() {
        return day.get();
    }

    public SimpleIntegerProperty dayProperty() {
        return day;
    }

    public String getTime() {
        return time.get();
    }

    public SimpleStringProperty timeProperty() {
        return time;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getCheckArrival() {
        return checkArrival.get();
    }

    public SimpleStringProperty checkArrivalProperty() {
        return checkArrival;
    }

    public String getMatched() {
        return matched.get();
    }

    public SimpleStringProperty matchedProperty() {
        return matched;
    }
}



