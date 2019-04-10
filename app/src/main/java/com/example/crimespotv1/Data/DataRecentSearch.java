package com.example.crimespotv1.Data;

import java.util.ArrayList;
import java.util.List;

public class DataRecentSearch {

    private String category;

    private String date;
    private String short_name;
    private double searchLongitude;
    private double searchLatitude;

    private List<DataStreetLevelCrime> crimeListAtLocation;

    public DataRecentSearch() {
        crimeListAtLocation = new ArrayList<>();
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public double getSearchLongitude() {
        return searchLongitude;
    }

    public void setSearchLongitude(double searchLongitude) {
        this.searchLongitude = searchLongitude;
    }

    public double getSearchLatitude() {
        return searchLatitude;
    }

    public void setSearchLatitude(double searchLatitude) {
        this.searchLatitude = searchLatitude;
    }

    public List<DataStreetLevelCrime> getCrimeListAtLocation() {
        return crimeListAtLocation;
    }

    public void setCrimeListAtLocation(List<DataStreetLevelCrime> crimeListAtLocation) {
        this.crimeListAtLocation = crimeListAtLocation;
    }

    public String getIdentifier() {
        String indentifier = this.getShort_name() + " | " +  this.getCategory() + " | " + this.getDate();

        return indentifier;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
