package org.transpool.engine.ds;

class Scheduling {

    private Recurrences recurrences;
    private int day_start;
    private int hour_start;

    public Scheduling(String recurrences, int day_start, int hour_start) {
        this.recurrences = Recurrences.valueOf(recurrences);
        this.day_start = day_start;
        this.hour_start = hour_start;
    }

    public Scheduling(int hour_start) {
        this.hour_start = hour_start;
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

    public int getHour_start() {
        return hour_start;
    }
}
