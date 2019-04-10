package com.example.crimespotv1.Model;

import android.content.Context;

import com.example.crimespotv1.Controllers.FeedReaderDbHelper;
import com.example.crimespotv1.Data.DataCrimeCategory;
import com.example.crimespotv1.Data.DataLocation;
import com.example.crimespotv1.Data.DataRecentCrime;
import com.example.crimespotv1.Data.DataRecentSearch;
import com.example.crimespotv1.Data.DataSavedCrime;
import com.example.crimespotv1.Data.DataStreetLevelCrime;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class AppModel {

    // DECLARATION
    private List<DataCrimeCategory> dataCrimeCategoryList;
    private List<DataLocation> dataLocationList;
    private List<DataStreetLevelCrime> dataStreetLevelCrimeList;
    private List<DataRecentCrime> dataRecentCrimeList;
    private List<DataRecentSearch> dataRecentSearchList;
    private List<DataSavedCrime> dataSavedCrimelist;
    private List<LatLng> dataLastLocationMarkers;
    //DB
    private FeedReaderDbHelper dbHelper;

    // Make a class and list for category counter
    // Add each category as integer
    // When crime added to crime list check category, then add 1 to the category counter class
    // depending on the category


    public AppModel(Context context) {

        // INSTANTIATE LISTS
        dataCrimeCategoryList = new ArrayList<>();
        dataLocationList = new ArrayList<>();
        dataStreetLevelCrimeList = new ArrayList<>();
        dataRecentCrimeList = new ArrayList<>();
        dataRecentSearchList = new ArrayList<>();
        dataSavedCrimelist = new ArrayList<>();
        dataLastLocationMarkers = new ArrayList();
        //DB
        dbHelper = new FeedReaderDbHelper(context);

    }

    // Null old list for garbage collection, create a new list.
    public void GenerateNewStreetLevelCrimeList(){

        // GARBAGE COLLECT OLD LIST
        dataStreetLevelCrimeList = null;
        // CREATE NEW LIST INSTANCE
        dataStreetLevelCrimeList = new ArrayList<>();
    }

    public void GenerateNewLastLocationMarkersList() {
        // Garbage Collect Old List
        dataLastLocationMarkers = null;
        // Generate New List
        dataLastLocationMarkers = new ArrayList<>();
    }

    public void GenerateNewSavedCrimeList(){
        dataSavedCrimelist = null;
        dataSavedCrimelist = new ArrayList<>();
    }


    // Getters for model lists.
    public List<DataCrimeCategory> getDataCrimeCategoryList() {
        return dataCrimeCategoryList;
    }

    public List<DataLocation> getDataLocationList() { return dataLocationList; }

    public List<DataStreetLevelCrime> getDataStreetLevelCrimeList() { return dataStreetLevelCrimeList; }

    public List<DataRecentSearch> getDataRecentSearchList() { return dataRecentSearchList; }

    public List<DataRecentCrime> getDataRecentCrimeList() { return dataRecentCrimeList; }

    public List<DataSavedCrime> getDataSavedCrimelist() {return dataSavedCrimelist;}

    public List<LatLng> getDataLastLocationMarkers() { return dataLastLocationMarkers; }

    public FeedReaderDbHelper getDbHelper() {
        return dbHelper;
    }

    public void setDbHelper(FeedReaderDbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }
}
