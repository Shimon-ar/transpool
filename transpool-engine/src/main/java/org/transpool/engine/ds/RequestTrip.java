package org.transpool.engine.ds;

public class RequestTrip {
    private static int counter = 100;
    private int id;
    private final String name;
    private final String to;
    private final String from;
    private final RequestTime time;
    private boolean match;
    private int transPoolID;
    private int cost;
    private int fuelCon;
    private Time arrivalTime;
    private Time checkoutTime;

    public RequestTrip(String name, String to, String from,Time hour,String time ) {
        this.name = name;
        this.to = to;
        this.from = from;
        id = counter;
        counter++;
        this.time = new RequestTime(hour,time);
        match = false;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public boolean setTransPoolTrip(OptionalTrip tripToSet) {
        if(match)
            return false;
        this.transPoolID = tripToSet.getTransPoolID();
        match = true;
        fuelCon = tripToSet.getFuelCon();
        cost = tripToSet.getCost();
        checkoutTime = tripToSet.getCheckoutTime();
        arrivalTime = tripToSet.getArrivalTime();
        return true;
    }

    public Integer getTransPoolID() {
        if(!match)
            return null;
        return transPoolID;
    }

    public RequestTime getRequestTime() {
        return time;
    }

    public boolean isMatch() {
        return match;
    }

    public Integer getCost() {
        if(!match)
            return null;
        return cost;
    }

    public Integer getFuelCon() {
        if(!match)
            return null;
        return fuelCon;
    }

    public Time getArrivalTime() {
        if(!match)
            return null;
        return arrivalTime;
    }

    @Override
    public String toString() {
        return "RequestTrip{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", to='" + to + '\'' +
                ", from='" + from + '\'' +
                ", time=" + time +
                ", match=" + match +
                ", transPoolID=" + transPoolID +
                ", cost=" + cost +
                ", fuelCon=" + fuelCon +
                ", arrivalTime=" + arrivalTime +
                '}';
    }

    public Time getCheckoutTime() {
       if(!match)
           return null;
        return checkoutTime;
    }
}
