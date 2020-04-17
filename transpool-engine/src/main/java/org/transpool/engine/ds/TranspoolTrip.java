package org.transpool.engine.ds;

import org.transpool.engine.TripDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranspoolTrip {
    private static int counter = 1;
    private final String name;
    private final int id;
    private int capacity;
    private final int ppk;
    private List<String> route;
    private Scheduling scheduling;
    private List<Integer> customersID;
    private Map<String, StopManager> passManager;
    private Time checkoutTime;
    private Time arrivalTime;
    private int cost;
    private int fuelCon;


    public TranspoolTrip(String name, int capacity, int ppk, List<String> route, Scheduling scheduling, MapDb map) {
        this.name = name;
        this.id = counter;
        this.capacity = capacity;
        this.ppk = ppk;
        this.route = route;
        this.scheduling = scheduling;
        customersID = new ArrayList<>();
        passManager = new HashMap<>();
        counter++;
        checkoutTime = scheduling.getTime();
        arrivalTime = checkoutTime.clone();
        TripDetails.updateTime(map, route, arrivalTime, true);
        cost = TripDetails.cost(map, ppk, route);
        fuelCon = TripDetails.avgFuelCon(map, route);
    }

    public boolean isAvailableToAddTrip() {
        return capacity > 0;
    }

    public boolean addRequestTrip(int RequestID, String name, String from, String to) {

        if (!route.contains(to) || !route.contains(from) || customersID.contains(RequestID) || capacity < 1)
            return false;

        if (!passManager.containsKey(from)) {
            StopManager stopManager = new StopManager();
            stopManager.addUpPassenger(name);
            passManager.put(from, stopManager);
        } else passManager.get(from).addUpPassenger(name);

        if (!passManager.containsKey(to)) {
            StopManager stopManager = new StopManager();
            stopManager.addDownPassenger(name);
            passManager.put(to, stopManager);
        } else passManager.get(to).addDownPassenger(name);

        capacity--;
        customersID.add(RequestID);

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

    public Time whenArrivedToStop(String to,MapDb map){
        Time time = checkoutTime.clone();
        String from = route.get(0);
        if(from.equals(to))
            return time;
        List<String> path = getPath(from,to);
        if(path == null)
            return null;
        time.minToAdd(TripDetails.howLong(map,path));
        return time;
    }

    public List<Integer> getCustomersID() {
        return customersID;
    }

    @Override
    public String toString() {
        return "TranspoolTrip{" + "name: " + name + " " +
                "id=" + id +
                ", capacity=" + capacity +
                ", ppk=" + ppk +
                ", route=" + route +
                ", scheduling=" + scheduling + "checkout: " + checkoutTime + "arrival " + arrivalTime + " cost:" + cost + " fuelcon:" + fuelCon +
                "}\n";
    }

    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
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

    public Map<String, StopManager> getPassManager() {
        return passManager;
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
