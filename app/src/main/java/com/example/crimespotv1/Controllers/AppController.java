package com.example.crimespotv1.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.BaseColumns;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crimespotv1.Data.DBFeedReaderContract;
import com.example.crimespotv1.Data.DataCrimeCategory;
import com.example.crimespotv1.Data.DataLocation;
import com.example.crimespotv1.Data.DataRecentCrime;
import com.example.crimespotv1.Data.DataRecentSearch;
import com.example.crimespotv1.Data.DataSavedCrime;
import com.example.crimespotv1.Data.DataStreetLevelCrime;
import com.example.crimespotv1.Interfaces.IResult;
import com.example.crimespotv1.Model.AppModel;
import com.example.crimespotv1.Views.ActivityLanding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AppController {

    // DECLARATION
    private static AppController instance;
    private AppModel appModel;
    private RequestQueue APIRequestQueue;
    private MarkerOptions markerOptions;
    IResult mResultCallback = null;
    SQLiteDatabase db;


    public AppController(Context context) {

        // INSTANTIATION
        appModel = new AppModel(context);
        APIRequestQueue = Volley.newRequestQueue(context);


    }

    // Controller is singleton, only create one instance to keep data persistent
    public static AppController appController(Context context) {
        if (instance == null) {
            instance = new AppController(context);
        }
        return instance;
    }

    public void WriteCrimeToDB() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                // Repo for the DB in write mode
                db = appModel.getDbHelper().getWritableDatabase();

                // Create a map of values where columns equal the keys
                ContentValues values = new ContentValues();

                // Get's the last recent crime search list and adds it to values

                values.put(DBFeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY, getDataRecentCrimeSearchList().get(getDataRecentCrimeSearchList().size() - 1).getCategory());
                values.put(DBFeedReaderContract.FeedEntry.COLUMN_NAME_LOCATION_TYPE, getDataRecentCrimeSearchList().get(getDataRecentCrimeSearchList().size() - 1).getLocation_type());
                values.put(DBFeedReaderContract.FeedEntry.COLUMN_NAME_LATITUDE, getDataRecentCrimeSearchList().get(getDataRecentCrimeSearchList().size() - 1).getLatitude());
                values.put(DBFeedReaderContract.FeedEntry.COLUMN_NAME_STREET_ID, getDataRecentCrimeSearchList().get(getDataRecentCrimeSearchList().size() - 1).getStreet_id());
                values.put(DBFeedReaderContract.FeedEntry.COLUMN_NAME_STREET_NAME, getDataRecentCrimeSearchList().get(getDataRecentCrimeSearchList().size() - 1).getStreet_name());
                values.put(DBFeedReaderContract.FeedEntry.COLUMN_NAME_LONGITUDE, getDataRecentCrimeSearchList().get(getDataRecentCrimeSearchList().size() - 1).getLongitude());
                values.put(DBFeedReaderContract.FeedEntry.COLUMN_NAME_CONTEXT, getDataRecentCrimeSearchList().get(getDataRecentCrimeSearchList().size() - 1).getContext());
                values.put(DBFeedReaderContract.FeedEntry.COLUMN_NAME_OUTCOME_STATUS, getDataRecentCrimeSearchList().get(getDataRecentCrimeSearchList().size() - 1).getOutcome_status());
                values.put(DBFeedReaderContract.FeedEntry.COLUMN_NAME_PERSISTENT_ID, getDataRecentCrimeSearchList().get(getDataRecentCrimeSearchList().size() - 1).getPersistent_id());
                values.put(DBFeedReaderContract.FeedEntry.COLUMN_NAME_ID, getDataRecentCrimeSearchList().get(getDataRecentCrimeSearchList().size() - 1).getId());
                values.put(DBFeedReaderContract.FeedEntry.COLUMN_NAME_MONTH, getDataRecentCrimeSearchList().get(getDataRecentCrimeSearchList().size() - 1).getMonth());

                // Insert to DB and store unique id.
                //values.put(DBFeedReaderContract.FeedEntry.COLUMN_NAME_UNIQUE_ID, );
                db.insert(DBFeedReaderContract.FeedEntry.TABLE_NAME, null, values);


                System.out.println("Row Created.");

                // Create a new local copy of the DB
                ReadDB();

            }
        });
    }

    public void ReadDB() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                // Repo for readable database
                db = appModel.getDbHelper().getReadableDatabase();

                /*
                // Name of the actual data in the database you want to search for
                String title = "anti-social-behaviour";

                // Columns i plan to use from the database
                String[] projection = {BaseColumns._ID, DBFeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY, DBFeedReaderContract.FeedEntry.COLUMN_NAME_LOCATION_TYPE};

                // Filter / query
                String selection = DBFeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY + " = ?";
                //String[] selectionArgs = { title };

                // Sort results
                String sortOrder = DBFeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY + " DESC";

                // Add everything to cursor
                Cursor cursor = db.query(
                        DBFeedReaderContract.FeedEntry.TABLE_NAME,
                        null, // use projection
                        selection,
                        null,
                        null,
                        null,
                        sortOrder
                );


                // Read the database and store the information to print from memory

                List itemIds = new ArrayList<>();
                while(cursor.moveToNext()) {
                    Integer itemId = cursor.getInt(

                            //cursor.getColumnIndexOrThrow(DBFeedReaderContract.FeedEntry._ID)
                            cursor.getColumnIndexOrThrow(DBFeedReaderContract.FeedEntry._ID)

                    );

                    itemIds.add(itemId);
                }
                cursor.close();
*/

                // Turns sqltable into object
                TableToObject(db, "tbl_saved_crimes_test");

            }
        });


    }

    public void TableToObject(SQLiteDatabase db, String tableName) {

        if (getDataSavedCrimeList().size() > 0) {
            appModel.GenerateNewSavedCrimeList();
        }

        // Store the table name
        String tableNameString = String.format("Table %s: \n", tableName);
        // Store all rows in cursor
        Cursor allRows = db.rawQuery("SELECT * FROM " + tableName, null);

        // Get the first row
        if (allRows.moveToFirst()) {
            // Store column names
            String[] columnNames = allRows.getColumnNames();

            do {
                // Counter to select correct data
                int counter = 0;

                // Create instance of dataSaveCrime
                DataSavedCrime dataSavedCrime = new DataSavedCrime();

                for (String name : columnNames) {

                    //System.out.println(name);
                    // PRINTS ALL VALUES FROM EACH COLUMN PER EACH ROW

                    // Use counter to determine where to store the data

                    switch (counter) {
                        case 0:
                            // ID ADD TO SAVED CRIME OBJECT
                            dataSavedCrime.setUniqueID(allRows.getString(allRows.getColumnIndex(name)));
                            counter++;
                            break;
                        case 1:
                            // CATEGORY ADD TO SAVED CRIME OBJECT
                            dataSavedCrime.setCategory(allRows.getString(allRows.getColumnIndex(name)));
                            counter++;
                            break;
                        case 2:
                            // LOCATION TYPE
                            dataSavedCrime.setLocation_type(allRows.getString(allRows.getColumnIndex(name)));
                            counter++;
                            break;
                        case 3:
                            dataSavedCrime.setLatitude(allRows.getDouble(allRows.getColumnIndex(name)));
                            counter++;
                            break;
                        case 4:
                            dataSavedCrime.setStreet_id(allRows.getInt(allRows.getColumnIndex(name)));
                            counter++;
                            break;
                        case 5:
                            dataSavedCrime.setStreet_name(allRows.getString(allRows.getColumnIndex(name)));
                            counter++;
                            break;
                        case 6:
                            dataSavedCrime.setLongitude(allRows.getDouble(allRows.getColumnIndex(name)));
                            counter++;
                            break;
                        case 7:
                            dataSavedCrime.setContext(allRows.getString(allRows.getColumnIndex(name)));
                            counter++;
                            break;
                        case 8:
                            dataSavedCrime.setOutcome_status(allRows.getString(allRows.getColumnIndex(name)));
                            counter++;
                            break;
                        case 9:
                            dataSavedCrime.setPersistent_id(allRows.getString(allRows.getColumnIndex(name)));
                            counter++;
                            break;
                        case 10:
                            dataSavedCrime.setId(allRows.getInt(allRows.getColumnIndex(name)));
                            counter++;
                            break;
                        case 11:
                            dataSavedCrime.setLocation_subtype(allRows.getString(allRows.getColumnIndex(name)));
                            counter++;
                            break;
                        case 12:
                            dataSavedCrime.setMonth(allRows.getString(allRows.getColumnIndex(name)));
                            getDataSavedCrimeList().add(dataSavedCrime);
                            counter = 0;
                            break;



                    }

                }

                // Keep looking whilst there are rows
            } while (allRows.moveToNext());

        }

        // Close the cursor
        allRows.close();
    }

    public void DeleteFromDB(String id) {
        // Deletes all entries in Database where "COLUMN == value". E.g COLUMN_TABLE_NAME == "title test".

        // Define SQL 'WHERE'
        String selection = DBFeedReaderContract.FeedEntry._ID + " LIKE ?";

        // specify arguments
        String[] selectionArgs = { id };

        // issue SQL statement
        db.delete(DBFeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);

        System.out.println("Crime Deleted");

        ReadDB();

    }


    public void DeleteTableDB() {

        db = appModel.getDbHelper().getWritableDatabase();

        db.execSQL("delete from " + DBFeedReaderContract.FeedEntry.TABLE_NAME);

        ReadDB();
    }



    public void UpdateDB() {

       AsyncTask.execute(new Runnable() {
           @Override
           public void run() {

               db = appModel.getDbHelper().getWritableDatabase();

               // New value for one column
               String title = "MyNewTitle";
               ContentValues values = new ContentValues();
               values.put(DBFeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY, title);

               // Select row to update based on title
               String selection = DBFeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY + " LIKE ?";
               // Old title name, the one to update
               String[] selectionArgs = { "title test" };

               int count = db.update(
                       DBFeedReaderContract.FeedEntry.TABLE_NAME,
                       values,
                       selection,
                       selectionArgs
               );

           }
       });

    }

    // API QUERIES AND REQUESTS --------------------------------------------------------------------

    // API Request to get all crime categories.
    public void QueryCrimeCategory(IResult callback) {

        // Instantiate Callback
        mResultCallback = callback;
        // Set Callback to null so it can be used.
        //mResultCallback = null;

        // URL for all crime categories.
        String url = "https://data.police.uk/api/crime-categories?date=2011-08";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        try {
                            // Convert URL String to JSONArray
                            JSONArray jsonArr = new JSONArray(response);
                            for (int i = 0; i < jsonArr.length(); i++) {
                                // Store JSONArray into JSONObjects into class objects.
                                DataCrimeCategory dataCrimeCategory = new DataCrimeCategory();
                                JSONObject jsonObj = jsonArr.getJSONObject(i);
                                dataCrimeCategory.setName(jsonObj.getString("url"));
                                dataCrimeCategory.setDescription(jsonObj.getString("name"));
                                getDataCrimeCategoryList().add(dataCrimeCategory);


                            }

                            // Notify Volley Success Listener
                            if(mResultCallback != null) {
                                mResultCallback.notifySuccess("APICategory", response);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                System.out.println("CATEGORY QUERY ERROR: " + error.toString());


                // Notify Volley Error Listener
                if(mResultCallback != null) {
                    mResultCallback.notifyError("APICategory", error);
                }
            }
        });
        APIRequestQueue.add(stringRequest);
    }

    public void QueryGoogleGeoCodingLocation(IResult callback, final String locationName, final String date, final String month, final String category) {

        // Instantiate Callback
        mResultCallback = callback;



        // Google GEO Coding URL for location based query "UK" ensures locations are UK based.
        String concatURL = "https://maps.googleapis.com/maps/api/geocode/json?address=" + locationName + "UK" + "&key=AIzaSyDIGDGzWeTO1RkXSOkWNX5ntnt2AzvbpFk";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, concatURL,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        try {
                            DataLocation dataLocation = new DataLocation();

                            JSONObject jsonObj = new JSONObject(response);
                            JSONArray jsonArr = new JSONArray(jsonObj.get("results").toString());

                            JSONObject jsonObjResults = jsonArr.getJSONObject(0);
                            JSONArray jsonArrAddressComp = new JSONArray(jsonObjResults.get("address_components").toString());
                            JSONObject jsonObjAddressComp = jsonArrAddressComp.getJSONObject(0);
                            dataLocation.setLong_name(jsonObjAddressComp.getString("long_name"));
                            dataLocation.setShort_name(jsonObjAddressComp.getString("short_name"));

                            JSONObject jsonObjGeo = new JSONObject(jsonObjResults.get("geometry").toString());
                            JSONObject jsonObjGeoLocation = new JSONObject(jsonObjGeo.get("location").toString());
                            dataLocation.setLatitude(jsonObjGeoLocation.getDouble("lat"));
                            dataLocation.setLongitude(jsonObjGeoLocation.getDouble("lng"));
                            dataLocation.setSearchDate(date + " " + month);
                            dataLocation.setSearchCategory(category);

                            QueryStreetCrime(locationName, category, dataLocation.getLatitude(), dataLocation.getLongitude(), date, month);

                            getDataLocationList().add(dataLocation);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("LOCATION QUERY ERROR: " + error.toString());
                    }
                });
        APIRequestQueue.add(stringRequest);
    }

    public void QueryStreetCrime(final String locationName, final String searchCategory, final double searchLocationLat, final double searchLocationLng, final String searchYear, final String searchMonth) {

        // Make sure to garbage collect old lists before making new lists to prevent duplicate data

        // If there is content in the street crime list, clear it.
        if (getDataStreetLevelCrimeList().size() > 0) {
            // GARBAGE COLLECT OLD LIST
            appModel.GenerateNewStreetLevelCrimeList();



            if (appModel.getDataLastLocationMarkers().size() > 0) {
                // Garbage Collect Old List
                appModel.GenerateNewLastLocationMarkersList();
            }

        }

        // Query URL for Street Crime based on location
        String concatURL = "https://data.police.uk/api/crimes-street/" + searchCategory + "?lat=" + searchLocationLat + "&lng=" + searchLocationLng + "&date=" + searchYear + "-" + searchMonth;
        System.out.println(concatURL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, concatURL,
                new Response.Listener<String>() {

                    public void onResponse(String response) {
                        try {
                            System.out.println("Starting Query");
                            JSONArray jsonArr = new JSONArray(response);
                            DataRecentSearch dataRecentSearch = new DataRecentSearch();

                            for (int i = 0; i < jsonArr.length(); i++) {

                                JSONObject jsonObj = jsonArr.getJSONObject(i);
                                DataStreetLevelCrime streetLevelCrime = new DataStreetLevelCrime();

                                streetLevelCrime.setCategory(jsonObj.getString("category"));
                                streetLevelCrime.setLocation_type(jsonObj.getString("location_type"));

                                JSONObject jsonObjLocation = jsonObj.getJSONObject("location");
                                streetLevelCrime.setLatitude(jsonObjLocation.getDouble("latitude"));

                                JSONObject jsonObjStreet = jsonObjLocation.getJSONObject("street");
                                streetLevelCrime.setStreet_id(jsonObjStreet.getInt("id"));
                                streetLevelCrime.setStreet_name(jsonObjStreet.getString("name"));

                                streetLevelCrime.setLongitude(jsonObjLocation.getDouble("longitude"));
                                streetLevelCrime.setContext(jsonObj.getString("context"));


                                if (!jsonObj.isNull("outcome_status")) {
                                    JSONObject jsonObjOutcomeStatus = jsonObj.getJSONObject("outcome_status");
                                    streetLevelCrime.setOutcome_status(jsonObjOutcomeStatus.getString("category"));
                                }


                                streetLevelCrime.setPersistent_id(jsonObj.getString("persistent_id"));
                                streetLevelCrime.setId(jsonObj.getInt("id"));
                                streetLevelCrime.setLocation_subtype(jsonObj.getString("location_subtype"));
                                streetLevelCrime.setMonth(jsonObj.getString("month"));

                                // Add this lat/lng to list of markers.
                                appModel.getDataLastLocationMarkers().add(new LatLng(streetLevelCrime.getLatitude(), streetLevelCrime.getLongitude()));

                                // Add Crime to Crime List
                                getDataStreetLevelCrimeList().add(streetLevelCrime);

                                // Add This Crime to Recent Crime List
                                dataRecentSearch.getCrimeListAtLocation().add(streetLevelCrime);



                            }

                            // Add Extra Info To Recent Search
                            dataRecentSearch.setCategory(searchCategory);
                            dataRecentSearch.setSearchLatitude(searchLocationLat);
                            dataRecentSearch.setSearchLongitude(searchLocationLng);
                            dataRecentSearch.setDate(searchYear + "-" + searchMonth);
                            dataRecentSearch.setShort_name(locationName);

                            // Add info to Recent Search List
                            getDataRecentSearchList().add(dataRecentSearch);

                            // Notify Volley Success Listener
                            if(mResultCallback != null) {
                                mResultCallback.notifySuccess("APICrime", response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                System.out.println("CRIME QUERYs ERROR: " + error);

                // Notify Volley Error Listener
                if(mResultCallback != null) {
                    mResultCallback.notifyError("APICrime", error);
                }

            }
        });

        // Timeout policy, will time out after 10 seconds
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        APIRequestQueue.add(stringRequest);

    }

    public SQLiteDatabase getDB() {
        return db;
    }

    // GETTERS FOR MODEL LISTS ---------------------------------------------------------------------

    public List<DataCrimeCategory> getDataCrimeCategoryList() { return appModel.getDataCrimeCategoryList(); }
    public List<DataRecentCrime> getDataRecentCrimeSearchList() { return appModel.getDataRecentCrimeList(); }
    public List<DataLocation> getDataLocationList() { return appModel.getDataLocationList(); }
    public List<DataStreetLevelCrime> getDataStreetLevelCrimeList() { return appModel.getDataStreetLevelCrimeList(); }
    public List<DataRecentSearch> getDataRecentSearchList() { return appModel.getDataRecentSearchList(); }
    public List<DataSavedCrime> getDataSavedCrimeList() { return appModel.getDataSavedCrimelist(); }
    public List<LatLng> getDataLastLocationMarkers () { return appModel.getDataLastLocationMarkers(); }

}
