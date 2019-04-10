package com.example.crimespotv1.Views;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.example.crimespotv1.Controllers.AppController;
import com.example.crimespotv1.Data.DataRecentCrime;
import com.example.crimespotv1.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ActivityCrimeInfo extends AppCompatActivity implements OnMapReadyCallback {

    // Declaration
    private Bundle extras;
    private TextView uiTextViewCategory, uiTextViewLocationType, uiTextViewOutcomeStatus, uiTextViewStreetName, uiTextViewStreetID, uiTextViewLat, uiTextViewLng, uiTextViewMonth;
    private String thisCategory, thisLocationType, thisOutcomeStatus, thisStreetName, thisMonth;
    private Integer thisStreetID;
    private double thisLatitude, thisLongitude;
    private Toolbar uiToolbar;
    private AppController appController;
    private ImageButton uiButtonDeleteFromDB;
    private boolean thisSavedCrime;
    private String thisCrimeIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_info);

        // Instantiation
        extras = getIntent().getExtras();

        appController = AppController.appController(this);

        // Get Map Fragment for this map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.uiMapView);
        mapFragment.getMapAsync(this);

        // Reference UI Elements
        UIReference();

        // Setup actions on toolbar
        UIManagerSetupToolbar();

        // Store Extras from Intent
        StoreExtras();

        // Fill Text Views with Data
        FillData();

        // Save Crime to Recents
        SaveCrimeToRecents();
    }

    public void ButtonHome(View view) {
        finish();
    }

    public void SaveCrimeToDB(View view) {

        appController.WriteCrimeToDB();

        // Crime added to DB
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Crime Saved");
        alertDialog.setMessage("This crime has been saved to the database.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }

    public void ButtonDeleteCrimeFromDB(View view) {
        appController.DeleteFromDB(thisCrimeIndex);
        finish();
    }

    private void FillData() {
        // Set title to location/street name
        setTitle("Location: " + thisStreetName);

        uiTextViewCategory.setText("Category: " + thisCategory);
        uiTextViewLocationType.setText("Type: " + thisLocationType);
        uiTextViewOutcomeStatus.setText("Status: " + thisOutcomeStatus);
        uiTextViewStreetName.setText("Street Name: " + thisStreetName);
        uiTextViewStreetID.setText("Street ID: " + thisStreetID);
        uiTextViewLat.setText("Latitude: " + thisLatitude);
        uiTextViewLng.setText("Longitude: " + thisLongitude);
        uiTextViewMonth.setText("Date of Crime: " + thisMonth);

        if (thisSavedCrime) {
            uiButtonDeleteFromDB.setVisibility(View.VISIBLE);
        }
    }

    private void StoreExtras() {
        thisCategory = extras.getString("category");
        thisLocationType = extras.getString("location_type");
        thisOutcomeStatus = extras.getString("outcome_status");
        thisStreetName = extras.getString("street_name");
        thisStreetID = extras.getInt("street_id");
        thisMonth = extras.getString("month");
        thisLatitude = extras.getDouble("latitude");
        thisLongitude = extras.getDouble("longitude");
        thisSavedCrime = extras.getBoolean("savedCrime");
        thisCrimeIndex = String.valueOf(extras.getInt("crimeIndex") + 1);

    }

    private void UIReference() {
        uiTextViewCategory = findViewById(R.id.uiTextViewCategory);
        uiTextViewLocationType = findViewById(R.id.uiTextViewLocationType);
        uiTextViewOutcomeStatus = findViewById(R.id.uiTextViewOutcomeStatus);
        uiTextViewStreetName = findViewById(R.id.uiTextViewStreetName);
        uiTextViewStreetID = findViewById(R.id.uiTextViewStreetID);
        uiTextViewLat = findViewById(R.id.uiTextViewLat);
        uiTextViewLng = findViewById(R.id.uiTextViewLng);
        uiTextViewMonth = findViewById(R.id.uiTextViewMonth);
        uiToolbar = findViewById(R.id.uiToolbar);

        uiButtonDeleteFromDB = findViewById(R.id.uiButtonDeleteFromDB);
        // Hide by default
        uiButtonDeleteFromDB.setVisibility(View.INVISIBLE);
    }

    private void SaveCrimeToRecents() {

        DataRecentCrime dataRecentCrime = new DataRecentCrime();

        dataRecentCrime.setCategory(thisCategory);
        dataRecentCrime.setLocation_type(thisLocationType);
        dataRecentCrime.setOutcome_status(thisOutcomeStatus);
        dataRecentCrime.setStreet_name(thisStreetName);
        dataRecentCrime.setStreet_id(thisStreetID);
        dataRecentCrime.setLatitude(thisLatitude);
        dataRecentCrime.setLongitude(thisLongitude);
        dataRecentCrime.setMonth(thisMonth);

        appController.getDataRecentCrimeSearchList().add(dataRecentCrime);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng crimeLocation = new LatLng(thisLatitude, thisLongitude);
        googleMap.addMarker(new MarkerOptions().position(crimeLocation).title("Marker on Crime"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(crimeLocation));

        FocusOnLocation(googleMap, crimeLocation);
    }

    public void FocusOnLocation(GoogleMap googleMap, LatLng currentLocation) {

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15));
        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 15, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

    }

    // Default toolbar
    public void UIManagerSetupToolbar() {
        setSupportActionBar(uiToolbar);
    }

    // Back Button Override
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setTitle("Use App Navigation")
                    .setMessage("Please do not use the standard android button to go back, use the ones provided in the app.")

                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
