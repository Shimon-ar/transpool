package org.transpool.engine.ds;

import java.util.Objects;

public class Time implements Cloneable {
    private int minutes;
    private int hours;

    public Time(int minutes, int hours) {
        this.minutes = minutes;
        this.hours = hours;
    }

    @Override
    public Time clone() {
        try {
            return (Time)super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Time time = (Time) o;
        return minutes == time.minutes &&
                hours == time.hours;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minutes, hours);
    }

    public int getMinutes() {
        return minutes;
    }

    public int getHours() {
        return hours;
    }

    @Override
    public String toString() {
        return "Time{" + hours + ":" + minutes +
                '}';
    }

    public void minToAdd(int minToAdd){
        minutes = (minutes+minToAdd)%60;
        if(minutes<0)
            minutes = 60 - minutes;
        int mod = minutes%5;
        if(mod>2)
            minutes = minutes - mod + 5;
        else minutes -= mod;
        hours = (hours + (minToAdd/60))%24;
        if(hours<0)
            hours = 24 - hours;
    }
}
