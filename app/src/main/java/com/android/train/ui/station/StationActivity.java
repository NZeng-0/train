package com.android.train.ui.station;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.android.train.R;

public class StationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, StationFragment.newInstance())
                    .commitNow();
        }
    }

    public void returnSelectedStation(String stationName) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("selected_station", stationName);
        resultIntent.putExtra("type", getIntent().getStringExtra("type"));
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}