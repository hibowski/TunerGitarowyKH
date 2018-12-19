package com.example.tunergitarowy.profiles;

import android.app.Application;
import android.util.Log;

import com.example.tunergitarowy.recording.RecordingThread;

import java.util.ArrayList;

public class TunerApp extends Application{

    private ArrayList<Profile> profiles;

    public ArrayList<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(ArrayList<Profile> profiles) {
        this.profiles = profiles;
    }

    public ArrayList<String> getProfilesNames() {

               ArrayList<String> profilesNames = new ArrayList<String>();

        for (int i = 0; i<profiles.size(); i++) {
           profilesNames.add(profiles.get(i).getName());
        }
        return profilesNames;
    }

    public void addProfile(Profile profile){
        this.profiles.add(profile);
    }

    public int getProfileListSize(){
        return this.profiles.size();
    }

    public Profile findProfile(String name) {
        for (int i = 0; i<profiles.size(); i++) {
            if(profiles.get(i).getName().equals(name)) {
                return profiles.get(i);
            }
        }
        Log.i("TunerApp", name);
        return null;
    }

}
