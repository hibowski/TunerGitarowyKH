package com.example.tunergitarowy.activities;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.tunergitarowy.R;
import com.example.tunergitarowy.profiles.TunerApp;
import com.example.tunergitarowy.algorithms.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ProfileSelector extends AppCompatActivity {

    private String currProfileName;
    private ArrayList<Integer> currProfilePitchIndexes;
    private int currSelectedPitchIndex;

    public static final String EXTRA_INT = "com.example.tunergitarowy.INT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_selector);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayList<String> profileNames = ((TunerApp) this.getApplication()).getProfilesNames();

        final Spinner profileSpinner = (Spinner) findViewById(R.id.spinner2);
        final Spinner stringSpinner = (Spinner) findViewById(R.id.spinner3);
        String[] arrayProfileNames = profileNames.toArray(new String[profileNames.size()]);

        final ArrayAdapter<String> stringArrayAdapter;
        ArrayList<String> lst1 = new ArrayList<String>(Arrays.asList(new String[]{"---"}));
        stringArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                lst1);

        final ArrayAdapter<String> profileArrayAdapter;
        ArrayList<String> lst2 = new ArrayList<String>(Arrays.asList(arrayProfileNames));
        profileArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                lst2);
        profileSpinner.setAdapter(profileArrayAdapter);
        profileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadProfile(profileSpinner.getSelectedItem().toString());

                List<String> spinnerSelectorStrings = new ArrayList<String>();
                for (int s : currProfilePitchIndexes){
                   spinnerSelectorStrings.add(new String(Utils.pitchLetterFromIndex(s)));
                }

                String[] stringSpinnerOptions = spinnerSelectorStrings.toArray(new String[spinnerSelectorStrings.size()]);

                stringArrayAdapter.clear();
                stringArrayAdapter.addAll(stringSpinnerOptions);
                stringSpinner.setAdapter(stringArrayAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                stringArrayAdapter.clear();
                stringArrayAdapter.addAll(new String[] {"---"});
                stringSpinner.setAdapter(stringArrayAdapter);
            }
        });


        stringSpinner.setAdapter(stringArrayAdapter);
        stringSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currSelectedPitchIndex = currProfilePitchIndexes.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                currSelectedPitchIndex = -1;
            }
        });

        Button tunerButton = this.findViewById(R.id.tunerButton);

        tunerButton = this.findViewById(R.id.tunerButton);
        tunerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTuner(currSelectedPitchIndex);
            }
        });

    }

    protected void loadProfile (String name){
        this.currProfilePitchIndexes = ((TunerApp) this.getApplication()).findProfile(name).getTones();
        this.currProfileName = name;
    }

    private void startTuner(int pitchIndex){
        Intent intent = new Intent(this, TunerActivity.class);
        intent.putExtra(this.EXTRA_INT, pitchIndex);
        startActivity(intent);
    }

}
