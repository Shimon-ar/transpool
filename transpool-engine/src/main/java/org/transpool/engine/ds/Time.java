package org.transpool.engine.ds;

import java.util.Objects;

public class Time implements Cloneable {
    private int minutes;
    private int hours;
    private int day;


    public Time(int minutes, int hours,int day) {
        this.minutes = minutes;
        this.hours = hours;
        this.day = day;
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
        String hourS,minuteS;
        if(hours<10)
            hourS = "0" + hours;
        else
            hourS = Integer.toString(hours);

        if(minutes<10)
            minuteS = "0" + minutes;
        else
            minuteS = Integer.toString(minutes);
        return hourS + ":" + minuteS;

    }

    public void minToAdd(int minToAdd){
        int sumMinutes = minutes+minToAdd;
        minutes = sumMinutes%60;
        if(minutes<0)
            minutes = 60 - minutes;
        int mod = minutes%5;
        if(mod>2)
            minutes = minutes - mod + 5;
        else minutes -= mod;
        if((sumMinutes/60 + hours) > 23 )
            day++;
        hours = (hours + (sumMinutes/60))%24;
        if(hours<0)
            hours = 24 - hours;

    }

    public int getDay() {
        return day;
    }
}
