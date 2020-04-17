package org.transpool.engine;

import org.transpool.engine.ds.MapDb;
import org.transpool.engine.ds.Time;
import org.transpool.engine.ds.TranspoolTrip;
import org.transpool.engine.ds.schema.Path;
import org.transpool.engine.ds.schema.Stop;
import org.transpool.engine.ds.schema.TransPool;
import org.transpool.engine.ds.schema.TransPoolTrip;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapBuilder {
    private final static String PATH_GENER_CLASS = "org.transpool.engine.ds.schema";
    private MapDb map;
    private Map<Integer, TranspoolTrip> allTransPoolTrips = new HashMap<>();
    private String description;

    public MapDb getMapDb() {
        return map;
    }

    public Map<Integer, TranspoolTrip> getAllTransPoolTrips() {
        return allTransPoolTrips;
    }

    public String getDescription() {
        return description;
    }


    private TransPool xmlToObj(File path) throws JAXBException, IOException {
        try (InputStream inputStream = new FileInputStream(path)) {
            JAXBContext jc = JAXBContext.newInstance(PATH_GENER_CLASS);
            Unmarshaller u = jc.createUnmarshaller();
            return (TransPool) u.unmarshal(inputStream);
        }
    }

    private boolean setMap(int width, int length) {
        if (!MapDb.checkBoundaries(width, length)) {
            description = "ERROR: map boundaries are not between 6 to 100";
            return false;
        }
        map = new MapDb(width, length);
        return true;
    }

    private boolean setStops(List<Stop> stops) {
        for (Stop stop : stops) {
            description = map.addStop(stop.getName(), stop.getX(), stop.getY());
            if (description != null)
                return false;
        }
        return true;
    }

    private boolean setPaths(List<Path> paths) {
        for (Path path : paths) {
            if (!map.addPath(path.getTo(), path.getFrom(), path.isOneWay(), path.getLength(), path.getFuelConsumption(), path.getSpeedLimit())) {
                description = "ERROR: invalid path";
                return false;
            }
        }
        return true;
    }

    private boolean setTranspoolTrips(List<TransPoolTrip> trips) {

        for (TransPoolTrip trip : trips) {
            List<String> stops = new ArrayList<>();
            String path = trip.getRoute().getPath();
            String[] route = path.split(",");
            for (String stop : route) {
                if (stops.contains(stop)) {
                    description = "ERROR: " + trip.getOwner() + " have route with duplicate station";
                    return false;
                }
                stops.add(stop);
            }
            if (!TripDetails.isValidRoute(map, stops)) {
                description = "ERROR: " + trip.getOwner() + " have invalid travel route";
                return false;
            }
            int hourStart = trip.getScheduling().getHourStart();
            if (hourStart < 0 || hourStart > 23) {
                description = "ERROR: trip of: " + trip.getOwner() + "have invalid start hour";
                return false;
            }

            Time time = new Time(0, hourStart);
            org.transpool.engine.ds.Scheduling scheduling = new org.transpool.engine.ds.Scheduling(time);
            TranspoolTrip transpoolTrip = new TranspoolTrip(trip.getOwner(), trip.getCapacity(), trip.getPPK(), stops, scheduling, map);
            allTransPoolTrips.put(transpoolTrip.getId(), transpoolTrip);
            updateTripForEachStop(stops, transpoolTrip.getId());

        }
        return true;
    }


    private void updateTripForEachStop(List<String> route, int tripID) {
        for (String stopName : route)
            map.getMap().get(stopName).getTrips().add(tripID);
    }

    public boolean build(File xml_path) throws JAXBException, IOException {
        TransPool transPool = xmlToObj(xml_path);

        if (!setMap(transPool.getMapDescriptor().getMapBoundries().getWidth(), transPool.getMapDescriptor().getMapBoundries().getLength()))
            return false;

        if (!setStops(transPool.getMapDescriptor().getStops().getStop()))
            return false;

        if (!setPaths(transPool.getMapDescriptor().getPaths().getPath()))
            return false;

        if (!setTranspoolTrips(transPool.getPlannedTrips().getTransPoolTrip()))
            return false;

        return true;
    }

}
