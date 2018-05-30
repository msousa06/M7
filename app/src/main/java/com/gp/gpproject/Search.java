package com.gp.gpproject;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Search extends AppCompatActivity {
    DBManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Spinner spinnerDepartments = findViewById(R.id.spinnerSearchDepartments);
        Spinner spinnerJobs = findViewById(R.id.spinnerSearchCategory);
        Spinner spinnerPoints = findViewById(R.id.spinnerSearchPoints);


        //replace this for the result of search on departamentos database
        String[] items = new String[] {"1", "2", "3"};

        String[] itemsPoints = new String[] {"<=", ">="};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        ArrayAdapter<String> adapterPoints = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsPoints);

        spinnerDepartments.setAdapter(adapter);
        spinnerJobs.setAdapter(adapter);
        spinnerPoints.setAdapter(adapterPoints);


    }



}
