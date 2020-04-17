package org.transpool.engine;


import org.transpool.engine.ds.*;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Api {
    private MapDb map;
    Map<Integer, TranspoolTrip> transPoolTrips;
    Map<Integer, RequestTrip> requestTrips;
    boolean mapLoaded;
    String errorDes;

    public String getErrorDes() {
        return errorDes;
    }

    public Api() {
        mapLoaded = false;
    }

    public boolean loadMap(File xmlPath) throws JAXBException, IOException {
        MapBuilder mapBuilder = new MapBuilder();
        mapLoaded = mapBuilder.build(xmlPath);
        if (!mapLoaded) {
            errorDes = mapBuilder.getDescription();
            return false;
        }
        map = mapBuilder.getMapDb();
        transPoolTrips = mapBuilder.getAllTransPoolTrips();
        requestTrips = new HashMap<>();
        mapLoaded = true;
        return true;
    }

    public RequestTrip inRequest(String name, String from, String to, int hour, int minutes, String whichTime) {
        if (!mapLoaded) {
            errorDes = "ERROR: map didnt loaded";
            return null;
        }
        if (!map.isStopExist(from)) {
            errorDes = "ERROR: station name: " + from + " do not exist on the map";
            return null;
        }

        if (!map.isStopExist(to)) {
            errorDes = "ERROR: station name: " + to + " do not exist on the map";
            return null;
        }

        Time time = new Time(minutes, hour);
        RequestTrip requestTrip = new RequestTrip(name, to, from, time, whichTime);
        requestTrips.put(requestTrip.getId(), requestTrip);
        return requestTrip;
    }

    public List<TranspoolTrip> getTransPoolTrips() {
        if (!mapLoaded || transPoolTrips.isEmpty())
            return null;
        return new ArrayList<>(transPoolTrips.values());
    }

    public List<RequestTrip> getRequestTrips() {
        if (!mapLoaded || requestTrips.isEmpty())
            return null;
        return new ArrayList<>(requestTrips.values());
    }

    public List<String> getStops() {
        if (!mapLoaded)
            return null;
        return map.getStops().stream().collect(Collectors.toList());
    }

    public List<RequestTrip> getUnMatchRequested() {
        return getRequestTrips().stream().filter(r -> !r.isMatch()).collect(Collectors.toList());
    }

    private List<OptionalTrip> makeOptionalTripsList(List<Integer> trips, String from, String to) {
        List<OptionalTrip> optionalTrips = new ArrayList<>();
        for (int transPoolID : trips) {
            TranspoolTrip transpoolTrip = transPoolTrips.get(transPoolID);
            if (transpoolTrip != null) {
                if (transpoolTrip.isAvailableToAddTrip()) {
                    List<String> route = transpoolTrip.getPath(from, to);
                    if (route != null) {
                        int cost = TripDetails.cost(map, transpoolTrip.getPpk(), route);
                        int fuelCon = TripDetails.avgFuelCon(map, route);
                        Time checkoutTime = transpoolTrip.whenArrivedToStop(from, map);
                        Time arrivalTime = transpoolTrip.whenArrivedToStop(to, map);
                        String name = transpoolTrip.getName();
                        optionalTrips.add(new OptionalTrip(transPoolID, name, cost, fuelCon, arrivalTime, checkoutTime));
                    }
                }
            }
        }
        return optionalTrips;
    }


    public List<OptionalTrip> findMatch(int requestID, int limit) {
        RequestTrip requestTrip = requestTrips.get(requestID);
        if (requestTrip == null)
            return null;
        List<Integer> trips = map.getMap().get(requestTrip.getFrom()).getTrips();
        List<OptionalTrip> optionalTrips = makeOptionalTripsList(trips, requestTrip.getFrom(), requestTrip.getTo());
        if (optionalTrips.isEmpty())
            return null;
        RequestTime requestTime = requestTrip.getRequestTime();
        if (requestTime.getWhichTime() == RequestTime.WhichTime.checkout) {
            return optionalTrips.stream().filter(t -> t.getCheckoutTime().equals(requestTime.getTime()))
                    .sorted(Comparator.comparingInt(OptionalTrip::getFuelCon))
                    .sorted(Comparator.comparingInt(OptionalTrip::getCost))
                    .limit(limit)
                    .collect(Collectors.toList());
        } else {
            return optionalTrips.stream().filter(t -> t.getArrivalTime().equals(requestTime.getTime()))
                    .sorted(Comparator.comparingInt(OptionalTrip::getFuelCon))
                    .sorted(Comparator.comparingInt(OptionalTrip::getCost))
                    .limit(limit)
                    .collect(Collectors.toList());
        }

    }

    public boolean setMatch(int requestTripID, OptionalTrip optionalTrip) {
        RequestTrip requestTrip = requestTrips.get(requestTripID);
        TranspoolTrip transpoolTrip = transPoolTrips.get(optionalTrip.getTransPoolID());
        if (requestTrip == null || transpoolTrip == null)
            return false;
        if (!requestTrip.setTransPoolTrip(optionalTrip)
                || !transpoolTrip.addRequestTrip(requestTripID, requestTrip.getName(), requestTrip.getFrom(), requestTrip.getTo()))
            return false;
        return true;
    }

    public String addTransPoolTrip(String name, List<String> route, int dayStart, int minutes, int hours, String recurrences, int ppk, int capacity) {
        if (minutes % 5 != 0)
            return "ERROR: minutes should be multiple of 5";
        if (hours > 23 || hours < 0)
            return "ERROR: hours must be between 0-23";
        if (!TripDetails.isValidRoute(map, route))
            return "ERROR: the route is invalid";
        if (Arrays.stream(Scheduling.Recurrences.values()).noneMatch(x -> x.toString().equals(recurrences)))
            return "ERROR: recurrences must be exactly(letter sensitive) one of those: OneTime, Daily, Bidaily, Weekly, Monthly ";
        if (ppk < 1)
            return "ERROR: price per kilometer must be positive";
        if (capacity < 1)
            return "ERROR: capacity of passengers must be positive";

        Scheduling scheduling = new Scheduling(recurrences,dayStart,new Time(minutes,hours));
        TranspoolTrip transpoolTrip = new TranspoolTrip(name, capacity, ppk, route, scheduling,map);
        transPoolTrips.put(transpoolTrip.getId(),transpoolTrip);
        return null;
    }


}
