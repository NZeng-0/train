package com.android.train.ui.booking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.train.R;

public class BookingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, BookingFragment.newInstance())
                    .commitNow();
        }
    }
}