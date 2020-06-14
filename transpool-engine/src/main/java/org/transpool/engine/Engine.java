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

    public RequestTrip inRequest(String name, String from, String to, Time time, String whichTime, boolean comfortable, int hourFlex) {
        RequestTrip requestTrip = new RequestTrip(name, to, from, time, whichTime, comfortable, hourFlex);
        requestTrips.put(requestTrip.getId(), requestTrip);
        return requestTrip;
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


    public TranspoolTrip addTransPoolTrip(String name, List<String> route, Time time, String recurrences, int ppk, int capacity) {
        Scheduling scheduling = new Scheduling(recurrences,time);
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

    public Map<String,TripsManager> getUpdateMapData(Time time) {
        Map<String, TripsManager> map = new HashMap<>();
        getStops().forEach(s -> map.put(s, new TripsManager()));

        getTransPoolTrips().forEach(transpoolTrip -> {
            String stop = transpoolTrip.whichStopILocated(time, this.map);
            if (stop != null) {
                map.get(stop).addTrip(transpoolTrip);
            }
        });

        return map;
    }


     /*   List<TranspoolTrip> transpoolTrips = getTransPoolTrips().stream().
                filter(transpoolTrip -> transpoolTrip.getInitCheckout().before(time)
        ).collect(Collectors.toList());

        transpoolTrips.stream().filter(transpoolTrip -> !transpoolTrip.getArrivalTime().before(time)).forEach(transpoolTrip -> {
            List<String> route = transpoolTrip.getRoute();
            boolean update = false;
            for(int i=0;i<route.size() && !update ;i++){
                if(transpoolTrip.whenArrivedToStop(route.get(i),getMap()).equals(time)) {
                    updateMapStopManager(map, route.get(i), transpoolTrip.getStopsManager().get(route.get(i)));
                    update = true;
                }
                if(!update && transpoolTrip.whenArrivedToStop(route.get(i),getMap()).before(time) &&
                   !transpoolTrip.whenArrivedToStop(route.get(i+1),getMap()).before(time)) {
                    updateMapStopManager(map, route.get(i), transpoolTrip.getStopsManager().get(route.get(i)));
                    update = true;
                }
            }
            });

        transpoolTrips.stream().filter(transpoolTrip -> transpoolTrip.getArrivalTime().equals(time)).forEach(transpoolTrip -> {
            String stopName = transpoolTrip.getRoute().get(transpoolTrip.getRoute().size() - 1);
            updateMapStopManager(map,stopName,transpoolTrip.getStopsManager().get(stopName));
        } );

        return map;
    }*/

 /*  private void updateMapStopManager(Map<String,StopManager> map,String stopName,StopManager stopManager) {
            map.get(stopName).getUpCostumers().addAll(stopManager.getUpCostumers());
            map.get(stopName).getDownCostumers().addAll(stopManager.getDownCostumers());
            map.get(stopName).inc();
        }*/
   }


