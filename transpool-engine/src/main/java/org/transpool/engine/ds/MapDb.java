package org.transpool.engine.ds;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class MapDb {
    private final int width;
    private final int length;
    private Map<String, Node> map;
    private String[][] stopTable;

    public MapDb(int width, int length) {
        this.width = width;
        this.length = length;
        map = new HashMap<>();
        stopTable = new String[width][length];
    }

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }

    public Map<String, Node> getMap() {
        return map;
    }

    public void setMap(Map<String, Node> map) {
        this.map = map;
    }

    public String[][] getStopTable() {
        return stopTable;
    }

    public static boolean checkBoundaries(int width, int length) {
        if (width < 6 || width > 100)
            return false;
        if (length < 6 || width > 100)
            return false;
        return true;
    }


    public String addStop(String name, int width, int length) {
        if (map.containsKey(name))
            return "Stop name: " + name + " already exist";

        if (this.width < width || width < 0 || length > this.length || length < 0)
            return "Stop name: " + name + " are not between the correct boundary";

        if (stopTable[width][length] != null)
            return "Stop name: " + name + " clash with another stop coordinate";

        map.put(name, new Node(new Stop(width, length, name)));
        stopTable[width][length] = name;
        return null;
    }

    public boolean addPath(String to, String from, boolean one_way, int length, int fuelConsumption, int speedLimit) {
        Path path = createPath(to, from, one_way, length, fuelConsumption, speedLimit);
        if (path == null)
            return false;
        map.get(from).addPath(path);
        if (!one_way) {
            path = createPath(from, to, false, length, fuelConsumption, speedLimit);
            map.get(to).addPath(path);
        }
        return true;
    }

    private Path createPath(String to, String from, boolean one_way, int length, int fuelConsumption, int speedLimit) {
        if (to.equals(from) || !map.containsKey(to) || !map.containsKey(from))
            return null;
        return new Path(map.get(to).getStop(), map.get(from).getStop(), one_way, length, fuelConsumption, speedLimit);
    }

    public boolean isStopExist(String stopName){
        if(map.containsKey(stopName))
            return true;
        return false;
    }

    public Set<String> getStops(){
        return map.keySet();
    }


}
