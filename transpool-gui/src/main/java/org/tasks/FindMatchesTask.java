package org.tasks;

import javafx.concurrent.Task;
import org.ds.RequestTripProperty;
import org.transpool.engine.Engine;
import org.transpool.engine.ds.Match;

import java.util.List;

public class FindMatchesTask extends Task<Boolean> {

    private Engine engine;
    private RequestTripProperty requestTripProperty;
    private int limit;
    private List<Match> matches;

    public FindMatchesTask(Engine engine, RequestTripProperty requestTripProperty, int limit) {
        this.engine = engine;
        this.requestTripProperty = requestTripProperty;
        this.limit = limit;
    }

    @Override
    protected Boolean call() {
        updateMessage("start to search matches..");
        updateProgress(0,1);
        sleepForAWhile(300);
        matches = engine.getMatches(requestTripProperty.getRequestTrip(),limit);
        updateMessage("done..");
        sleepForAWhile(300);
        updateProgress(1,1);
        return Boolean.TRUE;
    }

    public static void sleepForAWhile(long sleepTime) {
        if (sleepTime != 0) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ignored) {

            }
        }
    }

    public List<Match> getMatches() {
        return matches;
    }
}
