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
            description = "map boundaries are not between 6 to 100";
            return false;
        }
        map = new MapDb(width, length);
        return true;
    }

    private boolean setStops(List<Stop> stops) {
        for (Stop stop : stops) {
            description = map.addStop(stop.getName().trim(), stop.getX(), stop.getY());
            if (description != null)
                return false;
        }
        return true;
    }

    private boolean setPaths(List<Path> paths) {
        for (Path path : paths) {
            if (!map.addPath(path.getTo().trim(), path.getFrom().trim(), path.isOneWay(), path.getLength(), path.getFuelConsumption(), path.getSpeedLimit())) {
                description = "invalid path";
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
                if (stops.contains(stop.trim())) {
                    description = "trip of: " + trip.getOwner() + " have route with duplicate station";
                    return false;
                }
                stops.add(stop.trim());
            }
            if (!TripDetails.isValidRoute(map, stops)) {
                description = "trip of: " +  trip.getOwner() + " have invalid travel route";
                return false;
            }
            int hourStart = trip.getScheduling().getHourStart();
            if (hourStart < 0 || hourStart > 23) {
                description = "trip of: " + trip.getOwner() + "have invalid start hour";
                return false;
            }

            if (trip.getCapacity() < 0 || trip.getPPK() < 0) {
                description = "trip of: " + trip.getOwner() + " have negative price per hour or , negative capacity";
                return false;
            }
            Time time;
            if(trip.getScheduling().getDayStart() == null)
            {
                description = "trip of: " + trip.getOwner() + " must have day start";
                return false;
            }
            int day = trip.getScheduling().getDayStart();
            if(day<1)
            {
                description = "trip of: " + trip.getOwner() + " day start must be positive";
                return false;
            }
            if(trip.getScheduling().getMinuteStart() != null )
                time = new Time(trip.getScheduling().getMinuteStart(), trip.getScheduling().getHourStart(), day);
            else time = new Time(0, trip.getScheduling().getHourStart(), day);
            org.transpool.engine.ds.Scheduling scheduling = new org.transpool.engine.ds.Scheduling(trip.getScheduling().getRecurrences(),time);
            TranspoolTrip transpoolTrip = new TranspoolTrip(trip.getOwner().trim(), trip.getCapacity(), trip.getPPK(), stops, scheduling, map);
            allTransPoolTrips.put(transpoolTrip.getId(), transpoolTrip);
            TripDetails.updateTripForEachStop(stops, transpoolTrip.getId(), map);

        }
        return true;
    }


    public boolean build(File xml_path) throws JAXBException, IOException {
        TransPool transPool = xmlToObj(xml_path);

        if (!setMap(transPool.getMapDescriptor().getMapBoundries().getWidth(), transPool.getMapDescriptor().getMapBoundries().getLength()))
            return false;

        if (!setStops(transPool.getMapDescriptor().getStops().getStop()))
            return false;

        if (!setPaths(transPool.getMapDescriptor().getPaths().getPath()))
            return false;

        if (transPool.getPlannedTrips() != null)
            if (!setTranspoolTrips(transPool.getPlannedTrips().getTransPoolTrip()))
                return false;

        return true;
    }

}
