package com.example.tunergitarowy.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.tunergitarowy.profiles.ConfigLoader;
import com.example.tunergitarowy.R;
import com.example.tunergitarowy.profiles.TunerApp;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_INT = "com.example.tunergitarowy.INT";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button4 = this.findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open ProfileSelectorActivity
                startProfileSelector();
            }
        });

        Button button6 = this.findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startProfileEdit();
            }
        });

        Button button5 = this.findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTuner();
            }
        });


        // Load config
        try {
            ((TunerApp) this.getApplication()).setProfiles(ConfigLoader.loadConfig(this.getApplicationContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ConfigLoader.saveProfilesToJSON(((TunerApp) this.getApplication()).getProfiles(), this.getApplicationContext());
        Log.i("MainActivity", "Settings check: " + ((TunerApp) this.getApplication()).getProfiles().toString());



    }

    @Override
    protected void onPause() {
        try {
            ConfigLoader.saveProfilesToJSON(((TunerApp) this.getApplication()).getProfiles(),this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onPause();
    }

    private void startProfileSelector(){
        Intent intent = new Intent(this, ProfileSelector.class);
        startActivity(intent);
    }

    private void startTuner(){
        Intent intent = new Intent(this, TunerActivity.class);
        intent.putExtra(this.EXTRA_INT, 0);
        startActivity(intent);
    }

    private void startProfileEdit(){
        Intent intent = new Intent(this, ProfileListActivity.class);
        startActivity(intent);
    }

}