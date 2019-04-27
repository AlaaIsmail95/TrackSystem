package com.example.tracksystem.tracksystem;

public class table_Items {
    String trainID , trainClass , dep_time , arrival_time;

    public table_Items(String trainID, String trainClass, String dep_time, String arrival_time) {
        this.trainID = trainID;
        this.trainClass = trainClass;
        this.dep_time = dep_time;
        this.arrival_time = arrival_time;
    }
}
