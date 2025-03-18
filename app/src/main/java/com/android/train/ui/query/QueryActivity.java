package com.android.train.ui.query;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.train.R;

public class QueryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, QueryFragment.newInstance())
                    .commitNow();
        }
    }
}