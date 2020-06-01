package org.transpool.engine.ds;

import org.transpool.engine.TripDetails;

import java.util.*;
import java.util.stream.Collectors;

public class TranspoolTrip {
    private static int counter = 1;
    private final String name;
    private final int id;
    private final int ppk;
    private final int initCapacity;
    private List<String> route;
    private Map<String, StopManager> stopsManager;
    private Scheduling scheduling;
    private List<Integer> requestsID;
    private Time checkoutTime;
    private Time arrivalTime;
    private int cost;
    private int fuelCon;


    public TranspoolTrip(String name, int capacity, int ppk, List<String> route, Scheduling scheduling, MapDb map) {
        this.name = name;
        this.id = counter;
        this.ppk = ppk;
        this.route = route;
        this.scheduling = scheduling;
        initCapacity = capacity;
        requestsID = new ArrayList<>();
        stopsManager = new LinkedHashMap<>();
        stopsManager = route.stream().collect(Collectors.toMap(x -> x, x -> new StopManager(capacity)));
        counter++;
        checkoutTime = scheduling.getTime();
        arrivalTime = checkoutTime.clone();
        TripDetails.updateTime(map, route, arrivalTime, true);
        cost = TripDetails.cost(map, ppk, route);
        fuelCon = TripDetails.avgFuelCon(map, route);
    }

    public boolean isAvailableToAddTrip(List<String> route) {
        if (requestsID.isEmpty())
            return true;
        if (route.stream().allMatch(x -> stopsManager.containsKey(x)))
            return route.subList(0,route.size()-1).stream().allMatch(x -> stopsManager.get(x).getCapacity() > 0);
        return false;
    }

    public boolean addRequestTrip(int RequestID, String name, String from, String to) {

        if (!stopsManager.containsKey(to) || !stopsManager.containsKey(from) || requestsID.contains(RequestID))
            return false;

        getPath(from,to).forEach(x->stopsManager.get(x).dec());
        stopsManager.get(from).addUpPassenger(name);
        stopsManager.get(to).addDownPassenger(name);
        stopsManager.get(to).inc();
        requestsID.add(RequestID);
        return true;
    }

    public List<String> getPath(String from, String to) {
        if (!route.contains(from) || !route.contains(to) || to.equals(from))
            return null;
        int inxTo, inxFrom;
        inxFrom = route.indexOf(from);
        inxTo = route.indexOf(to);
        if (inxFrom > inxTo)
            return null;
        return route.subList(inxFrom, inxTo + 1);
    }

    public Time whenArrivedToStop(String to, MapDb map) {
        Time time = checkoutTime.clone();
        String from = route.get(0);
        if (from.equals(to))
            return time;
        List<String> path = getPath(from, to);
        if (path == null)
            return null;
        time.minToAdd(TripDetails.howLong(map, path));
        return time;
    }

    public List<Integer> getRequestsID() {
        if (requestsID.isEmpty())
            return null;
        return requestsID;
    }

    @Override
    public String toString() {
        return "Id:" + id + ", " +
                "Name:" + name +
                ", Price per kilometer:" + ppk +
                ", Trip cost:" + cost +
                ", Average fuel utilization:" + fuelCon +
                ", Checkout:" + checkoutTime + ", Arrival:" + arrivalTime;
    }

    public int getId() {
        return id;
    }

    public int getInitCapacity() {
        return initCapacity;
    }


    public int getPpk() {
        return ppk;
    }

    public List<String> getRoute() {
        return route;
    }

    public Scheduling getScheduling() {
        return scheduling;
    }

    public String getName() {
        return name;
    }

    public Map<String, StopManager> getStopsManager() {
        return stopsManager;
    }

    public Time getCheckoutTime() {
        return checkoutTime;
    }

    public Time getArrivalTime() {
        return arrivalTime;
    }

    public int getCost() {
        return cost;
    }

    public int getFuelCon() {
        return fuelCon;
    }
}
