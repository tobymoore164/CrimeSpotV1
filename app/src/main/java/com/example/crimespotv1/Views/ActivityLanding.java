package com.example.crimespotv1.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.crimespotv1.R;

public class ActivityLanding extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void ButtonStart(View view) {
        Intent intent = new Intent(this, ActivityHome.class);
        startActivity(intent);
    }
}
