package org.transpool.engine;

import org.transpool.engine.ds.Node;
import org.transpool.engine.ds.Path;

import java.util.List;

public class TripDetails {
    public static Integer cost(int ppk, List<Node> route) {
        int sum = 0, length = route.size();
        for (int i = 0; i < length - 1; i++) {
            boolean valid = false;
            Node currNode = route.get(i);
            Node nextNode = route.get(i + 1);
            List<Path> paths = currNode.getPaths();
            for (Path path : paths) {
                if (path.getTo().getName().equals(nextNode.getStop().getName())) {
                    sum += path.getLength();
                    valid = true;
                    break;
                }
            }
            if(!valid)
                return null;
        }
        return sum*ppk;
    }
}
