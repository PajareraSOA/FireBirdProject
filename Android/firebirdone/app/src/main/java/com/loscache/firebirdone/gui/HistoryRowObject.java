package com.loscache.firebirdone.gui;

import java.util.Date;

/**
 * Created by cdsac on 27/10/2017.
 */

public class HistoryRowObject {

    Date time;
    String temperature;
    String smoke;
    String flame;
    String food;
    String water;

    public HistoryRowObject(Date time, String temperature, String smoke, String flame, String food, String water){
        this.time = time;
        this.temperature = temperature;
        this.smoke = smoke;
        this.flame = flame;
        this.food = food;
        this.water = water;
    }

    public String getTime() { return time.toLocaleString(); }

    public String getTemperature() {
        return temperature;
    }

    public String getSmoke() {
        return smoke;
    }

    public String getFlame() {
        return flame;
    }

    public String getFood() {
        return food;
    }

    public String getWater() {
        return water;
    }
}
