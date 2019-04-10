package com.example.crimespotv1.Data;

public class DataRecentCrime {

    private String category;
    private String location_type;
    private double latitude;
    private int street_id;
    private String street_name;
    private double longitude;
    private String context;
    private String outcome_status;
    private String persistent_id;
    private int id;
    private String location_subtype;
    private String month;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation_type() {
        return location_type;
    }

    public void setLocation_type(String location_type) {
        this.location_type = location_type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getStreet_id() {
        return street_id;
    }

    public void setStreet_id(int street_id) {
        this.street_id = street_id;
    }

    public String getStreet_name() {
        return street_name;
    }

    public void setStreet_name(String street_name) {
        this.street_name = street_name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getOutcome_status() {
        return outcome_status;
    }

    public void setOutcome_status(String outcome_status) {
        this.outcome_status = outcome_status;
    }

    public String getPersistent_id() {
        return persistent_id;
    }

    public void setPersistent_id(String persistent_id) {
        this.persistent_id = persistent_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation_subtype() {
        return location_subtype;
    }

    public void setLocation_subtype(String location_subtype) {
        this.location_subtype = location_subtype;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String toString() {

        String concat = category + " " + month;
        return concat;
    }

    public String getIdentifier() {
        String identifier;

        if (!street_name.isEmpty()) {
            identifier = street_name;
        } else {
            identifier = category + " " + month;
        }

        return identifier;
    }
}
