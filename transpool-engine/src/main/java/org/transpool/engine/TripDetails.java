package org.transpool.engine;

import org.transpool.engine.ds.MapDb;
import org.transpool.engine.ds.Node;
import org.transpool.engine.ds.Path;
import org.transpool.engine.ds.Time;

import java.util.List;

public class TripDetails {

    private static int lengthRoute(MapDb map, List<String> route) {
        int lengthRoute = 0, length = route.size();
        for (int i = 0; i < length - 1; i++) {
            Path path = findPath(map, route.get(i), route.get(i + 1));
            lengthRoute += path.getLength();
        }
        return lengthRoute;
    }

    public static int cost(MapDb map, int ppk, List<String> route) {
        return lengthRoute(map, route) * ppk;
    }


    private static Path findPath(MapDb map, String from, String to) {
        Node nodeFrom = map.getMap().get(from);
        Node nodeTo = map.getMap().get(to);
        if (nodeFrom == null || nodeTo == null)
            return null;
        List<Path> paths = nodeFrom.getPaths();
        for (Path path : paths) {
            if (path.getTo().getName().equals(to))
                return path;
        }
        return null;
    }

    public static boolean isValidRoute(MapDb map, List<String> route) {
        int length = route.size();
        for (int i = 0; i < length - 1; i++){
            if (findPath(map, route.get(i), route.get(i + 1)) == null)
                return false;
        }
        return true;
    }

    public static int howLong(MapDb map, List<String> route) {
        double timeH = 0;
        int length = route.size();

        for (int i = 0; i < length - 1; i++) {
            Path path = findPath(map, route.get(i), route.get(i + 1));
            timeH += (double)path.getLength() / (double)path.getSpeedLimit();
        }
        return (int)(timeH * 60.0);
    }

    public static int avgFuelCon(MapDb map, List<String> route) {
        int lengthRoute = lengthRoute(map, route);
        int lSum = 0;
        int length = route.size();
        for (int i = 0; i < length - 1; i++) {
            Path path = findPath(map, route.get(i), route.get(i + 1));
            lSum += path.getLength()/path.getFuelConsumption();
        }
        return lengthRoute/lSum;
    }

    public static void updateTime(MapDb map, List<String> route, Time checkoutTime,boolean flag){
        int minutesToAdd = howLong(map,route);
        if(!flag)
            minutesToAdd = -1*minutesToAdd;
        checkoutTime.minToAdd(minutesToAdd);
    }



}


