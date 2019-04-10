package com.example.crimespotv1.ViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.crimespotv1.Data.DataStreetLevelCrime;
import com.example.crimespotv1.R;
import com.example.crimespotv1.Views.ActivityCrimeInfo;
import java.util.List;

public class CrimeAdapter extends RecyclerView.Adapter<CrimeAdapter.ViewHolder>{

    private List<DataStreetLevelCrime> mStreetLevelCrimesList;

    public CrimeAdapter(List<DataStreetLevelCrime> streetLevelCrimes) {
        mStreetLevelCrimesList = streetLevelCrimes;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView uiTextViewCategory, uiTextViewStreetName, uiTextViewMonth;
        //Spinner uiInputFavouriteSearchesList;
        public Button uiButtonMoreInfo;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            uiTextViewCategory = itemView.findViewById(R.id.uiTextViewCategory);
            uiTextViewStreetName = itemView.findViewById(R.id.uiTextViewStreetName);
            uiTextViewMonth = itemView.findViewById(R.id.uiTextViewMonth);

            //uiInputFavouriteSearchesList = itemView.findViewById(R.id.uiInputFavouriteSearchesList);

            uiButtonMoreInfo = itemView.findViewById(R.id.uiButtonMoreCrimeInfo);
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //Inflate custom layout
        View crimeView = inflater.inflate(R.layout.item_crime, parent, false);

        // Return holder instance
        ViewHolder viewHolder = new ViewHolder(crimeView);
        return viewHolder;
    }

    public void onBindViewHolder(final CrimeAdapter.ViewHolder viewHolder, int position) {

        // Get data based on position
        final DataStreetLevelCrime streetLevelCrime = mStreetLevelCrimesList.get(position);

        // Set views based on data
        TextView uiTextViewCategory = viewHolder.uiTextViewCategory;
        TextView uiTextViewStreetName = viewHolder.uiTextViewStreetName;
        TextView uiTextViewMonth = viewHolder.uiTextViewMonth;

        uiTextViewCategory.setText(streetLevelCrime.getCategory());
        uiTextViewStreetName.setText(streetLevelCrime.getStreet_name());
        uiTextViewMonth.setText(streetLevelCrime.getMonth());

        Button button = viewHolder.uiButtonMoreInfo;
        button.setText("More Info");
        button.setEnabled(true);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), ActivityCrimeInfo.class);

                intent.putExtra("category", streetLevelCrime.getCategory());
                intent.putExtra("street_name", streetLevelCrime.getStreet_name());
                intent.putExtra("month", streetLevelCrime.getMonth());
                intent.putExtra("location_type", streetLevelCrime.getLocation_type());
                intent.putExtra("outcome_status", streetLevelCrime.getOutcome_status());
                intent.putExtra("street_id", streetLevelCrime.getStreet_id());
                intent.putExtra("latitude", streetLevelCrime.getLatitude());
                intent.putExtra("latitude", streetLevelCrime.getLatitude());
                intent.putExtra("longitude", streetLevelCrime.getLongitude());

                v.getContext().startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return mStreetLevelCrimesList.size();
    }

}
