package com.csci571.forecast.dummy;

public class Weekly {
    public String date;
    public String icon;
    public String minimumTemperature;
    public String maximumTemperature;

    public Weekly(String date, String icon, String minTemp, String maximumTemperature)
    {
        this.date = date;
        this.icon = icon;
        this.minimumTemperature = minTemp;
        this.maximumTemperature = maximumTemperature;
    }
}


