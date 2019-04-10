package com.example.crimespotv1.Views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.android.volley.VolleyError;
import com.example.crimespotv1.Algorithms.Capitalize;
import com.example.crimespotv1.Controllers.AppController;
import com.example.crimespotv1.Data.DataCrimeCategory;
import com.example.crimespotv1.Data.DataRecentCrime;
import com.example.crimespotv1.Data.DataRecentSearch;
import com.example.crimespotv1.Data.DataSavedCrime;
import com.example.crimespotv1.Data.DataStreetLevelCrime;
import com.example.crimespotv1.Interfaces.IResult;
import com.example.crimespotv1.R;
import com.example.crimespotv1.ViewAdapters.CrimeAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class ActivityHome extends AppCompatActivity implements OnMapReadyCallback {

    private static AppController appController;

    // LOOK INTO USING DIALOGS FOR THE MENU WHEN A USER INPUTS DATA

    // UI ELEMENTS DECLARATION
    private Toolbar uiToolbar;
    private EditText uiInputLocationName, uiInputDateYear, uiInputDateMonth;
    private Spinner uiInputCategoryList, uiInputFavouriteSearchesList, uiInputFavouriteCrimesList, uiInputDatabaseCrimesList, uiInputDatabaseSearchList;
    private DrawerLayout uiDrawerLayout;
    private InputMethodManager virtualKeyboard;
    private RecyclerView uiRecyclerView;
    private ProgressBar uiProgressBar;
    private LinearLayout uiLoadingLayout, uiResultsHeader, uiLinearLayoutMapView, uiLinearLayoutListView;
    private ImageView uiSwipeRightIcon;
    private ToggleButton uiToggleType, uiToggleTypeDatabase;
    private Switch uiSwitchView;
    private Button uiButtonSearchRecent, uiButtonSearchDatabase;

    private GoogleMap uiMainMapView;
    private MarkerOptions markerOptions;

    // Other Declarations
    private CrimeAdapter crimeAdapter;
    private Capitalize capitalize;
    private IResult mResultCallback = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Default Title
        setTitle("App Loading, Please Wait");

        // Setup the Volley Callback
        VolleyResponseCallback();

        // Reference Singleton Controller
        appController = AppController.appController(this);

        // Calls Category API
        appController.QueryCrimeCategory(mResultCallback);

        // Instantiate all UI Elements
        UIReferenceElements();
        // Create Drawer Action Listeners
        DrawerLayoutActionListeners();
        // Create & setup toolbar.
        UIManagerSetupToolbar();
        // Create Category Dropdown Menu
        UICreateCategoryDropdown();
        // Setup Custom Event Listeners (View Toggle, Recents Toggle)
        CustomEventListeners();
        // Get Map Fragment
        GetMapFragment();

        appController.ReadDB();



    }

    protected void onResume() {
        super.onResume();
        CloseDrawers();
    }

    protected void onDestroy() {
        super.onDestroy();

        // Close the DB connection when activity destroys
        appController.getDB().close();
    }

    // BUTTON ON CLICKS -----------------------------------------------------------------------------

public void Debug(View view) {
    System.out.println(uiRecyclerView.getLayoutManager().getItemCount());
}


    public void ReadDB(View view) {
        appController.ReadDB();
    }

    public void UpdateDB(View view) {
        appController.UpdateDB();
    }

    public void DeleteTableDB(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Warning");
        builder.setMessage("Do you want to delete all of your saved crimes? ");

        builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                appController.DeleteTableDB();
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }


    public void ButtonSearchSavedCrime(View view) {
        if (appController.getDataSavedCrimeList().size() > 0) {


            // PUT THIS IN A METHOD IT IS USED TWICE?

            int selectedValue = uiInputDatabaseCrimesList.getSelectedItemPosition();
            boolean savedCrime = true;

            Intent intent = new Intent(this, ActivityCrimeInfo.class);

            intent.putExtra("category", appController.getDataSavedCrimeList().get(selectedValue).getCategory());
            intent.putExtra("street_name", appController.getDataSavedCrimeList().get(selectedValue).getStreet_name());
            intent.putExtra("month", appController.getDataSavedCrimeList().get(selectedValue).getMonth());
            intent.putExtra("location_type", appController.getDataSavedCrimeList().get(selectedValue).getLocation_type());
            intent.putExtra("outcome_status", appController.getDataSavedCrimeList().get(selectedValue).getOutcome_status());
            intent.putExtra("street_id", appController.getDataSavedCrimeList().get(selectedValue).getStreet_id());
            intent.putExtra("latitude", appController.getDataSavedCrimeList().get(selectedValue).getLatitude());
            intent.putExtra("latitude", appController.getDataSavedCrimeList().get(selectedValue).getLatitude());
            intent.putExtra("longitude", appController.getDataSavedCrimeList().get(selectedValue).getLongitude());
            intent.putExtra("crimeIndex", appController.getDataSavedCrimeList().get(selectedValue).getUniqueID());
            intent.putExtra("savedCrime", savedCrime);
            intent.putExtra("crimeIndex", selectedValue);

            startActivity(intent);

        }
    }

    // Search Specific Location
    public void ButtonSearchSpecific(View view) {

        // Capitalizes first letter of location using capatilize class
        setTitle("Searching: " + capitalize.getCapitalized(uiInputLocationName.getText().toString()) + ", Please Wait");

        // Hide List & Header Whilst Getting Data
        UIHideRecyclerView();
        UIHideResultsHeader();

        // Street Crime Request Starts here (have to get location before getting crimes)
        appController.QueryGoogleGeoCodingLocation(mResultCallback, uiInputLocationName.getText().toString(), uiInputDateYear.getText().toString(), uiInputDateMonth.getText().toString(), uiInputCategoryList.getSelectedItem().toString());

        // Close Drawers
        CloseDrawers();

        // Show Loading Layout & Progress Bar Wilst Searching
        UIShowLoadingLayout();
        UIShowProgressBar();

    }

    public void ButtonOpenMenu(View view) {
        OpenDrawers();
    }

    // VOLLEY RESPONSE LISTENERS --------------------------------------------------------------------
    void VolleyResponseCallback(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String ID, String response) {
                switch (ID) {
                    case "APICrime":
                        //setTitle("Location: " + capitalize.getCapitalized(uiInputLocationName.getText().toString()) + " | Category: " + uiInputCategoryList.getSelectedItem().toString() + " | Date: " + uiInputDateYear.getText().toString() + "-" + uiInputDateMonth.getText().toString());

                        setTitle("Crimes: " + appController.getDataStreetLevelCrimeList().size());

                        // Output Crimes to List
                        OutputCrimesToRecyclerView();

                        // Fill Map with Markers
                        PushMarkersToMap();

                        // Hide loading layout & Progress Bar
                        UIHideLoadingLayout();
                        UIHideProgressBar();

                        // Show List & Header
                        UIShowRecyclerView();
                        UIShowResultsHeader();



                        break;

                    case "APICategory":
                        setTitle("Swipe Side Menu Open From Left To Right");

                        // Unlocks the side drawer
                        uiDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

                        // Hide progress bar when query can be made
                        UIHideProgressBar();

                        // Set icon notify swipe icon visible
                        UIShowSwipeRightIcon();

                        break;

                    default:
                        break;
                }

            }

            @Override
            public void notifyError(String ID, VolleyError error) {

                if (appController.getDataCrimeCategoryList().size() > 0) {
                    setTitle("Search Failed, Try Again");
                } else {
                    setTitle("No API Connection, Restart or Try Again Later");
                }




                // Hide loading layout
                UIHideLoadingLayout();
                UIHideProgressBar();
            }
        };
    }



    // Outputs Street Crime Data to Recycler View
    public void OutputCrimesToRecyclerView() {
        crimeAdapter = new CrimeAdapter(appController.getDataStreetLevelCrimeList());
        uiRecyclerView.setAdapter(crimeAdapter);
        uiRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void OutputFavouriteSearchesToRecyclerView() {
        crimeAdapter = new CrimeAdapter(appController.getDataRecentSearchList().get(uiInputFavouriteSearchesList.getSelectedItemPosition()).getCrimeListAtLocation());
        uiRecyclerView.setAdapter(crimeAdapter);
        uiRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // CREATE DROPDOWN FROM CATEGORY API
    public void UICreateCategoryDropdown() {
        List<String> spinnerArray = new ArrayList<>();
        for (DataCrimeCategory data : appController.getDataCrimeCategoryList()) {
            spinnerArray.add(data.getName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                spinnerArray
        );
        uiInputCategoryList.setAdapter(arrayAdapter);
    }

    public void UICreateFavouriteSearchesDropdown() {

        List<String> spinnerArray = new ArrayList<>();

        for (DataRecentSearch data : appController.getDataRecentSearchList()) {
            spinnerArray.add(capitalize.getCapitalized(data.getIdentifier()));
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                spinnerArray
        );

        uiInputFavouriteSearchesList.setAdapter(arrayAdapter);

    }

    // Create Dropdown for Favourite Crime
    public void UICreateFavouriteCrimeDropdown() {
        List<String> spinnerArray = new ArrayList<>();

        for (DataRecentCrime data : appController.getDataRecentCrimeSearchList()) {
            // CHANGE TO IDENTIFIER
            spinnerArray.add(capitalize.getCapitalized(data.getIdentifier()));
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                spinnerArray
        );

        uiInputFavouriteCrimesList.setAdapter(arrayAdapter);
    }


    public void UICreateSavedCrimeDropdown() {
        List<String> spinnerArray = new ArrayList<>();

        for (DataSavedCrime data : appController.getDataSavedCrimeList()) {
            spinnerArray.add(capitalize.getCapitalized(data.getIdentifier()));
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                spinnerArray
        );
        uiInputDatabaseCrimesList.setAdapter(arrayAdapter);
    }






    // MENU DRAWER ACTIONS -------------------------------------------------------------------------
    public void DrawerLayoutActionListeners() {

        uiDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {

            public void onDrawerSlide(@NonNull View view, float v) {

            }

            public void onDrawerOpened(@NonNull View view) {

                // Update Category Dropdown
                UICreateCategoryDropdown();

                // Update Favourite Searches
                UICreateFavouriteSearchesDropdown();

                // Create Favourite Crime Dropdown
                UICreateFavouriteCrimeDropdown();

                // Create Saved Crime Dropdown
                UICreateSavedCrimeDropdown();

                // Hide the loading layout (app has loaded)
                UIHideLoadingLayout();
                UIHideSwipeRightIcon();

                // Set search title if a search hasn't been made
                if (appController.getDataStreetLevelCrimeList().size() == 0) {
                    setTitle("Search For a Crime!");
                }



            }

            public void onDrawerClosed(@NonNull View view) {
                // Hide keyboard when closed
                virtualKeyboard.hideSoftInputFromWindow(view.getWindowToken(), 0);

            }

            public void onDrawerStateChanged(int i) {

            }
        });
    }

    // Manual Close Drawers
    public void CloseDrawers() {
        uiDrawerLayout.closeDrawers();
    }

    // Manual Open Drawers
    public void OpenDrawers(){
        uiDrawerLayout.openDrawer(Gravity.LEFT);
    }

    public void CustomEventListeners() {

        uiSwitchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    uiSwitchView.setText("List View");
                    uiLinearLayoutMapView.setVisibility(View.GONE);
                    uiLinearLayoutListView.setVisibility(View.VISIBLE);
                } else {
                    uiSwitchView.setText("Map View");
                    uiLinearLayoutListView.setVisibility(View.GONE);
                    uiLinearLayoutMapView.setVisibility(View.VISIBLE);
                }
            }
        });

        uiToggleType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // Display UI if a search or crime has been viewed
                if (appController.getDataRecentSearchList().size() > 0 || appController.getDataRecentCrimeSearchList().size() > 0) {
                    if (isChecked) {
                        // Crimes
                        uiButtonSearchRecent.setVisibility(View.VISIBLE);
                        uiToggleType.setTextOn("RECENT CRIMES");
                        uiButtonSearchRecent.setText("SELECT CRIME");
                        uiInputFavouriteSearchesList.setVisibility(View.GONE);
                        uiInputFavouriteCrimesList.setVisibility(View.VISIBLE);
                        uiButtonSearchRecent.setEnabled(true);
                    } else {
                        // Searches
                        uiButtonSearchRecent.setVisibility(View.VISIBLE);
                        uiToggleType.setTextOff("RECENT SEARCHES");
                        uiButtonSearchRecent.setText("SELECT SEARCH");
                        uiInputFavouriteSearchesList.setVisibility(View.VISIBLE);
                        uiInputFavouriteCrimesList.setVisibility(View.GONE);
                        uiButtonSearchRecent.setEnabled(true);
                    }
                } else {
                    // Default Text when not displaying anything
                    uiToggleType.setTextOff("NO CRIMES/SEARCHES");
                    uiToggleType.setTextOn("TAP TO CHANGE TYPE");
                }
            }
        });

    }


    public void ButtonSearchFavourites(View view) {

        if (uiToggleType.isChecked()) {

            // Recent Crimes

            if (appController.getDataRecentCrimeSearchList().size() > 0) {

                int selectedValue = uiInputFavouriteCrimesList.getSelectedItemPosition();

                Intent intent = new Intent(this, ActivityCrimeInfo.class);

                intent.putExtra("category", appController.getDataRecentCrimeSearchList().get(selectedValue).getCategory());
                intent.putExtra("street_name", appController.getDataRecentCrimeSearchList().get(selectedValue).getStreet_name());
                intent.putExtra("month", appController.getDataRecentCrimeSearchList().get(selectedValue).getMonth());
                intent.putExtra("location_type", appController.getDataRecentCrimeSearchList().get(selectedValue).getLocation_type());
                intent.putExtra("outcome_status", appController.getDataRecentCrimeSearchList().get(selectedValue).getOutcome_status());
                intent.putExtra("street_id", appController.getDataRecentCrimeSearchList().get(selectedValue).getStreet_id());
                intent.putExtra("latitude", appController.getDataRecentCrimeSearchList().get(selectedValue).getLatitude());
                intent.putExtra("latitude", appController.getDataRecentCrimeSearchList().get(selectedValue).getLatitude());
                intent.putExtra("longitude", appController.getDataRecentCrimeSearchList().get(selectedValue).getLongitude());

                startActivity(intent);

            }


        } else {

            // Recent Searches
            if (appController.getDataRecentSearchList().size() > 0) {

                OutputFavouriteSearchesToRecyclerView();

                PushFavouriteMarkersToMap();

                CloseDrawers();

            }


        }
    }


    //UI MANAGEMENT METHODS ------------------------------------------------------------------------

    // Default toolbar
    public void UIManagerSetupToolbar() {
        setSupportActionBar(uiToolbar);
    }

    // Inflates the action menu into the toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Map View Management
    public void GetMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.uiMainMapView);
        mapFragment.getMapAsync(this);
    }

    public void onMapReady(GoogleMap googleMap) {
        uiMainMapView = googleMap;
    }

    public void PushMarkersToMap() {
        // Causes Background concurrent copying GC freed (clearing memory) which results in slow load from garbage collection, fix if possible. Could be from clearing old map markers?

        uiMainMapView.clear();

        markerOptions = new MarkerOptions();

        for(int i = 0; i < appController.getDataLastLocationMarkers().size(); i++) {
            markerOptions.position(appController.getDataLastLocationMarkers().get(i));
            markerOptions.title(appController.getDataStreetLevelCrimeList().get(i).getCategory());
            markerOptions.snippet(appController.getDataStreetLevelCrimeList().get(i).getStreet_name());
            uiMainMapView.addMarker(markerOptions);
        }

        FocusMapOnLocation();
    }

    public void PushFavouriteMarkersToMap() {

        // Clear Map
        uiMainMapView.clear();

        // Clear old location markers
        appController.getDataLastLocationMarkers().clear();

        // New markers
        markerOptions = new MarkerOptions();

        // Drop down selector
        int selectedValue = uiInputFavouriteSearchesList.getSelectedItemPosition();

        // Set title to total crimes on search
        setTitle("Crimes: " + appController.getDataRecentSearchList().get(selectedValue).getCrimeListAtLocation().size());

        for (int i = 0; i < appController.getDataRecentSearchList().get(selectedValue).getCrimeListAtLocation().size(); i++) {

            // Add each marker from the favourite search crime list to last location markers
            appController.getDataLastLocationMarkers().add(new LatLng(
                    appController.getDataRecentSearchList().get(selectedValue).getCrimeListAtLocation().get(i).getLatitude(),
                    appController.getDataRecentSearchList().get(selectedValue).getCrimeListAtLocation().get(i).getLongitude()
            ));
        }

        for ( int i = 0; i < appController.getDataLastLocationMarkers().size(); i++) {
            markerOptions.position(appController.getDataLastLocationMarkers().get(i));
            markerOptions.title(appController.getDataRecentSearchList().get(selectedValue).getCrimeListAtLocation().get(i).getCategory());
            markerOptions.snippet(appController.getDataRecentSearchList().get(selectedValue).getCrimeListAtLocation().get(i).getStreet_name());
            uiMainMapView.addMarker(markerOptions);

        }

        LatLng currentTown = new LatLng(
                appController.getDataRecentSearchList().get(selectedValue).getSearchLatitude(),
                appController.getDataRecentSearchList().get(selectedValue).getSearchLongitude()
        );

        uiMainMapView.moveCamera(CameraUpdateFactory.newLatLngZoom(currentTown,13));
        // Zoom in, animating the camera.
        uiMainMapView.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        uiMainMapView.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);

    }

    public void FocusMapOnLocation() {
        // Save the lat/lng for the last searched location
        LatLng currentTown = new LatLng(appController.getDataLocationList().get(appController.getDataLocationList().size() - 1).getLatitude(), appController.getDataLocationList().get(appController.getDataLocationList().size() - 1).getLongitude());

        uiMainMapView.moveCamera(CameraUpdateFactory.newLatLngZoom(currentTown,13));
        // Zoom in, animating the camera.
        uiMainMapView.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        uiMainMapView.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);
    }

    public void UIReferenceElements() {
        capitalize = new Capitalize();
        uiToolbar = findViewById(R.id.uiToolbar);
        virtualKeyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        uiInputLocationName = findViewById(R.id.uiInputLocationName);
        uiInputCategoryList = findViewById(R.id.uiInputCategoryList);
        uiDrawerLayout = findViewById(R.id.uiDrawerLayout);
        uiInputDateYear = findViewById(R.id.uiInputDateYear);
        uiInputDateMonth = findViewById(R.id.uiInputDateMonth);
        uiRecyclerView = findViewById(R.id.uiRecyclerView);
        uiDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        uiProgressBar = findViewById(R.id.uiProgressBar);
        uiLoadingLayout = findViewById(R.id.uiLoadingLayout);
        uiSwipeRightIcon = findViewById(R.id.uiSwipeRightIcon);

        uiInputDatabaseCrimesList = findViewById(R.id.uiInputDatabaseCrimes);
        uiInputDatabaseCrimesList.setVisibility(View.VISIBLE);

        uiButtonSearchRecent = findViewById(R.id.uiButtonSearchRecent);
        uiButtonSearchRecent.setVisibility(View.INVISIBLE);

        uiButtonSearchDatabase = findViewById(R.id.uiButtonSearchDatabase);
        uiButtonSearchDatabase.setVisibility(View.VISIBLE);

        uiInputFavouriteSearchesList = findViewById(R.id.uiInputFavouriteSearchesList);
        uiInputFavouriteSearchesList.setVisibility(View.INVISIBLE);

        uiInputFavouriteCrimesList = findViewById(R.id.uiInputFavouriteCrimesList);
        uiInputFavouriteCrimesList.setVisibility(View.GONE);

        uiToggleType = findViewById(R.id.uiToggleType);
        uiToggleType.setText("TAP TO CHANGE TYPE");

        uiSwitchView = findViewById(R.id.uiSwitchView);
        uiSwitchView.setText("List View");
        uiSwitchView.setChecked(true);

        uiLinearLayoutListView = findViewById(R.id.uiLinearLayoutListView);
        uiLinearLayoutListView.setVisibility(View.VISIBLE);

        uiSwipeRightIcon.setVisibility(View.GONE);
        // Setup Loading Layout
        uiLoadingLayout.setVisibility(View.VISIBLE);
        uiResultsHeader = findViewById(R.id.uiResultsHeader);

        uiLinearLayoutMapView = findViewById(R.id.uiLinearLayoutMapView);
        uiLinearLayoutMapView.setVisibility(View.GONE);

        // Hide the results header
        UIHideResultsHeader();
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

    // Results Header
    public void UIHideResultsHeader() {
        uiResultsHeader.setVisibility(View.GONE);
    }
    public void UIShowResultsHeader() {
        uiResultsHeader.setVisibility(View.VISIBLE);
    }

    // Loading Layout
    public void UIHideLoadingLayout() {
        uiLoadingLayout.setVisibility(View.GONE);
    }
    public void UIShowLoadingLayout() {
        uiLoadingLayout.setVisibility(View.VISIBLE);
    }

    // Recycler View
    public void UIHideRecyclerView() {
        uiRecyclerView.setVisibility(View.GONE);
    }
    public void UIShowRecyclerView() {
        uiRecyclerView.setVisibility(View.VISIBLE);
    }

    // Progress Bar
    public void UIHideProgressBar() {
        uiProgressBar.setVisibility(View.INVISIBLE);
    }
    public void UIShowProgressBar() {
        uiProgressBar.setVisibility(View.VISIBLE);
    }

    // Swipe right icon
    public void UIHideSwipeRightIcon() {
        uiSwipeRightIcon.setVisibility(View.INVISIBLE);
    }
    public void UIShowSwipeRightIcon() {
        uiSwipeRightIcon.setVisibility(View.VISIBLE);
    }

}
