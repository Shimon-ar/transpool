package org.transpool.engine.ds;

import java.util.List;
import java.util.Map;

public class MapDb {
    private final int width;
    private final int length;
    private Map<String,Node> map;

    public MapDb(int width, int length, List<Stop> stops, Map<String,Node> map) {
        this.width = width;
        this.length = length;
        this.map = map;

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

}
