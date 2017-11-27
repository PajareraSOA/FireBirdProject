package com.loscache.firebirdone.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by cdsac on 05/11/2017.
 */

public class MeasurementModel {

    private String date;
    private String temperature;
    private String smoke;
    private String flame;
    private String food;
    private String water;
    private boolean isHistory;

    public MeasurementModel(
            String date, 
            String temperature, 
            String smoke, 
            String flame, 
            String food, 
            String water,
            boolean isHistory){
        this.date = date;
        this.temperature = temperature;
        this.smoke = smoke;
        this.flame = flame;
        this.food = food;
        this.water = water;
        this.isHistory = isHistory;
    }

    public String getDate() { return date; }

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

    public boolean isHistory() { return isHistory; }

    public void setIsHistory(boolean value) { isHistory = value; }
}
