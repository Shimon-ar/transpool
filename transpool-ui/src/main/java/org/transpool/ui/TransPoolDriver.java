package org.transpool.ui;

import org.transpool.engine.ds.Stop;

public class TransPoolDriver {

    public static void main(String[] args) {

        Stop stop = new Stop(10, 2, "Nirit");
        System.out.printf("Stop: " + stop);
    }
}
