package com.example.news1.news1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class PrefActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
String item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pref);
        String[] category = {"everything", "business", "entertainment", "general", "health", "science", "sports", "technology"};
        Spinner spinner = (Spinner) findViewById(R.id.spinnerdd);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        category); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(this);

        Button btnPref = (Button) findViewById(R.id.btnPref);

        btnPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),tabbed.class);
                String item2 = item;
                intent.putExtra("SelectedItem",item2);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        item = adapterView.getItemAtPosition(i).toString();
        Log.e("OItemmm",item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
