package org.transpool.engine.ds;

import java.util.ArrayList;
import java.util.List;

public class StopManager {
    private List<String> upCostumers;
    private List<String> downCostumers;
    private int capacity;


    public StopManager(int capacity) {
        upCostumers = new ArrayList<>();
        downCostumers = new ArrayList<>();
        this.capacity = capacity;
    }

    public void addUpPassenger(String name) {
        upCostumers.add(name);
    }


    public void addDownPassenger(String name) {
        downCostumers.add(name);
    }

    public void inc(){
        capacity++;
    }

    public void dec(){
        capacity--;
    }

    public List<String> getUpCostumers() {
        return upCostumers;
    }

    public List<String> getDownCostumers() {
        return downCostumers;
    }

    public int getCapacity() {
        return capacity;
    }
}
