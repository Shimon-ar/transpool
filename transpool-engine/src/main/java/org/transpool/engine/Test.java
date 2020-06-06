package org.transpool.engine;

import org.transpool.engine.ds.Time;

public class Test {
    public static void main(String[] args) {
        Time time = new Time(30,5,1);
        System.out.println(time);
        time.minToAdd(25);
        System.out.println(25);
        System.out.println(time);
        time.minToAdd(50);
        System.out.println(50);
        System.out.println(time);
        time.minToAdd(153);
        System.out.println(153);
        System.out.println(time);
        time.minToAdd(-27);
        System.out.println(-27);
        System.out.println(time);
time.minToAdd(-1500);
        System.out.println(-1500);
        System.out.println(time);

    }
    //public static void main(String[] args) {
     //   RequestTime time = new RequestTime(1650,"arrivalTime");
//    }
}
