package com.example.crimespotv1.Data;

public class DataLocation {

    private String searchDate;
    private String searchCategory;

    private String long_name;
    private String short_name;
    private double longitude;
    private double latitude;

    public String getLong_name() {
        return long_name;
    }

    public void setLong_name(String long_name) {
        this.long_name = long_name;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(String searchDate) {
        this.searchDate = searchDate;
    }

    public String getSearchCategory() {
        return searchCategory;
    }

    public void setSearchCategory(String searchCategory) {
        this.searchCategory = searchCategory;
    }

}
