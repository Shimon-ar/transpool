package org.transpool.engine.ds;

public class Scheduling {

    private Recurrences recurrences;
    private int day_start;
    private Time time;

    public Scheduling(String recurrences, int day_start, Time time) {
        this.recurrences = Recurrences.valueOf(recurrences);
        this.day_start = day_start;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Scheduling{" +
                "recurrences=" + recurrences +
                ", day_start=" + day_start +
                "," + time +
                '}';
    }

    public Scheduling(Time time) {
        this.time = time;
        recurrences = Recurrences.OneTime;
        day_start = -1;
    }

    public enum Recurrences {
        OneTime, Daily, Bidaily, Weekly, Monthly;
    }

    public Recurrences getRecurrences() {
        return recurrences;
    }

    public int getDay_start() {
        return day_start;
    }

    public Time getTime() {
        return time;
    }
}
