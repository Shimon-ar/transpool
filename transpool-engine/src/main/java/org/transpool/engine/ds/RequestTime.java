package org.transpool.engine.ds;

public class RequestTime {
    private Time arrivalTime;
    private Time checkoutTime;
    WhichTime whichTime;

    public enum WhichTime {
        arrival, checkout
    }

    public RequestTime(Time time, String whichTime) {
        if (whichTime.equals(WhichTime.arrival.name())) {
            arrivalTime = time;
            this.whichTime = WhichTime.arrival;
        } else {
            checkoutTime = time;
            this.whichTime = WhichTime.checkout;
        }

    }
    public Time getTime(){
        if(WhichTime.arrival == whichTime)
            return arrivalTime;
        else
            return checkoutTime;
    }

    @Override
    public String toString() {
        return "RequestTime{" + whichTime + ": "+
                 getTime() +
                '}';
    }

    public WhichTime getWhichTime() {
        return whichTime;
    }
}
