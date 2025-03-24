package com.android.train.ui.ticket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.train.R;

public class TicketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, TicketFragment.newInstance())
                    .commitNow();
        }
    }
}