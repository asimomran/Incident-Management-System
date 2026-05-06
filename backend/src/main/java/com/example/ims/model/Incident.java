package com.example.ims.model;

import java.time.LocalDateTime;
import java.time.Duration;

public class Incident {

    private static int counter = 1;

    private int id;
    private String component;
    private String status;
    private String rca;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String mttr;

    public Incident(String component) {
        this.id = counter++;
        this.component = component;
        this.status = "OPEN";
        this.startTime = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public String getComponent() {
        return component;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRca() {
        return rca;
    }

    public void setRca(String rca) {
        this.rca = rca;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getMttr() {
        return mttr;
    }

    public void close() {
        this.endTime = LocalDateTime.now();
        Duration duration = Duration.between(startTime, endTime);
        long minutes = duration.toMinutes();
        long seconds = duration.toSeconds() % 60;
        this.mttr = minutes + "m " + seconds + "s";
    }
}