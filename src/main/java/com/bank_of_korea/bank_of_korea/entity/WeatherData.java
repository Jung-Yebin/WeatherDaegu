package com.bank_of_korea.bank_of_korea.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class WeatherData {
    @Id
    private int day;
    private String hour;
    private int forecast;
    private double value;
    private String location;
    private String start;

    public int getDay() {
        return day;
    }

    public String getHour() {
        return hour;
    }

    public int getForecast() {
        return forecast;
    }

    public double getValue() {
        return value;
    }

    public String getLocation() {
        return location;
    }

    public String getStart(){
        return start;
    }

    // Setter methods
    public void setDay(int day) {
        this.day = day;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setForecast(int forecast) {
        this.forecast = forecast;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStart(String start){
        this.start = start;
    }

}
