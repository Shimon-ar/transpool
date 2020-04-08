package org.transpool.engine.ds;

import java.util.List;

public class TranspoolTrip extends Customer {
    private static int counter = 1;
    private final int  id;
    private int capacity;
    private final int ppk;
    private List<Node> route;
    private Scheduling scheduling;

    public TranspoolTrip(String name, int id, int id1, int capacity, int ppk, List<Node> route, Scheduling scheduling) {
        super(name, id);
        this.id = id1;
        this.capacity = capacity;
        this.ppk = ppk;
        this.route = route;
        this.scheduling = scheduling;
        counter++;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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

    public List<Node> getRoute() {
        return route;
    }

    public Scheduling getScheduling() {
        return scheduling;
    }
}
