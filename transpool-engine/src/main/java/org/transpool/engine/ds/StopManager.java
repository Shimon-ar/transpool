package org.transpool.engine.ds;

import java.util.ArrayList;
import java.util.List;

public class StopManager {
    private List<String> upCostumers;
    private List<String> downCostumers;


    public StopManager() {
        upCostumers = new ArrayList<>();
        downCostumers = new ArrayList<>();
    }

    public void addUpPassenger(String name) {
        upCostumers.add(name);
    }

    public void addDownPassenger(String name) {
        downCostumers.add(name);
    }

    public List<String> getUpCostumers() {
        return upCostumers;
    }

    public List<String> getDownCostumers() {
        return downCostumers;
    }
}
