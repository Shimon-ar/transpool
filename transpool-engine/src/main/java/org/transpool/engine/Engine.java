package org.transpool.engine;


import org.transpool.engine.ds.*;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Engine {
    private MapDb map;
    private Map<Integer, TranspoolTrip> transPoolTrips;
    private Map<Integer, RequestTrip> requestTrips;
    private Matcher matcher;
    private boolean mapLoaded;
    private String errorDes;

    public String getErrorDes() {
        return errorDes;
    }

    public Engine() {
        mapLoaded = false;
    }

    public boolean loadMap(File xmlPath) throws JAXBException, IOException {
        MapBuilder mapBuilder = new MapBuilder();
        if (!mapBuilder.build(xmlPath)) {
            errorDes = mapBuilder.getDescription();
            return false;
        }
        map = mapBuilder.getMapDb();
        transPoolTrips = mapBuilder.getAllTransPoolTrips();
        requestTrips = new HashMap<>();
        mapLoaded = true;
        return true;
    }

    public RequestTrip inRequest(String name, String from, String to, int day, int hour, int minutes, String whichTime, boolean comfortable, int hourFlex) {
        Time time = new Time(minutes, hour, day);
        RequestTrip requestTrip = new RequestTrip(name, to, from, time, whichTime, comfortable, hourFlex);
        return requestTrips.put(requestTrip.getId(), requestTrip);
    }

    public List<Match> getMatches(RequestTrip requestTrip, int limit) {
        matcher = new Matcher(map, transPoolTrips, requestTrip);
        try {
            return matcher.getOptionalMatches(limit);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean setMatch(Match match){
        if(!matcher.setMatch(match))
            return false;
        return true;
    }

    public List<TranspoolTrip> getTransPoolTrips() {
        if (!mapLoaded)
            return null;
        return new ArrayList<>(transPoolTrips.values());
    }

    public List<RequestTrip> getRequestTrips() {
        if (!mapLoaded)
            return null;
        return new ArrayList<>(requestTrips.values());
    }

    public List<String> getStops() {
        if (!mapLoaded)
            return null;
        return new ArrayList<>(map.getStops());
    }

    public List<RequestTrip> getUnMatchRequested() {
        if(!mapLoaded)
            return null;
        return getRequestTrips().stream().filter(r -> !r.isMatch()).collect(Collectors.toList());
    }


    public TranspoolTrip addTransPoolTrip(String name, List<String> route, int dayStart, int minutes, int hours, String recurrences, int ppk, int capacity) {
        Scheduling scheduling = new Scheduling(recurrences,new Time(minutes, hours,dayStart));
        TranspoolTrip transpoolTrip = new TranspoolTrip(name, capacity, ppk, route, scheduling, map);
        transPoolTrips.put(transpoolTrip.getId(), transpoolTrip);
        TripDetails.updateTripForEachStop(route,transpoolTrip.getId(),map);
        return transpoolTrip;
    }

    public MapDb getMap() {
        return map;
    }

    public boolean isValidRoute(List<String> route){
        return TripDetails.isValidRoute(map,route);
    }

    public boolean isNameExistTransPool(String name){
       return transPoolTrips.values().stream().anyMatch(t->t.getName().equals(name));
    }

    public boolean isNameExistRequestTrip(String name){
        return requestTrips.values().stream().anyMatch(t->t.getName().equals(name));
    }


}
