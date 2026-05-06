package com.example.ims.model;

public class Signal {

    private String component;
    private String error;
    private String timestamp;

    // getters and setters

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Component: " + component + ", Error: " + error + ", Time: " + timestamp;
    }
}