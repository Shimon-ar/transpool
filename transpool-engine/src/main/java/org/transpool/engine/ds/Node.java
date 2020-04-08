package org.transpool.engine.ds;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private final Stop stop;
    private List<Path> paths;
    private List<TranspoolTrip> trips;

    public Node(Stop stop) {
        this.stop = stop;
        this.paths = new ArrayList<>();
        this.trips = new ArrayList<>();
    }

    public Stop getStop() {
        return stop;
    }

    public List<Path> getPaths() {
        return paths;
    }

    public boolean addPath(Path path){
        if(paths.contains(path))
            return false;
        paths.add(path);
        return true;
    }

    public boolean addTrip(TranspoolTrip trip){
        if(trips.contains(trip))
            return false;
        trips.add(trip);
        return true;
    }

    public List<TranspoolTrip> getTrips() {
        return trips;
    }
}
