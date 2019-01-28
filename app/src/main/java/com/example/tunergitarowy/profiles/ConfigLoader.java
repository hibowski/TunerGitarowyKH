package com.example.tunergitarowy.profiles;

import android.content.Context;

import com.example.tunergitarowy.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.String;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfigLoader {

    public static ArrayList<Profile> loadConfig(Context context) throws Exception {
        File directory = context.getFilesDir();
        File config = new File(directory, "config.json");

        if (!config.exists()) {
            InputStream inputStream = context.getResources().openRawResource(R.raw.config);
            OutputStream outputStream = new FileOutputStream(config);
            copyConfigFromResource(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        }

        InputStream configInputStream = new FileInputStream(config);
        String jsonString = loadJSONFromAsset(configInputStream);
        return loadProfilesFromJSON(jsonString);
    }

    private static ArrayList<Profile> loadProfilesFromJSON(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);

            ArrayList<Profile> profiles = new ArrayList<Profile>();

            JSONArray profilesArray = json.getJSONArray("profiles");
            for (int i = 0; i < profilesArray.length(); i++) {
                JSONObject jsonObject = profilesArray.getJSONObject(i);

                String profileName = jsonObject.getString("name");
                JSONArray pithIndexes = jsonObject.getJSONArray("pitchIndexes");
                Profile profile = new Profile(i + 1, profileName);
                int[] indexes = new int[pithIndexes.length()];

                for (int j = 0; j < pithIndexes.length(); ++j) {
                    indexes[j] = pithIndexes.optInt(j);
                    profile.addTone(indexes[j]);
                }
                profiles.add(profile);
            }
            return profiles;
        } catch (JSONException je) {
            je.printStackTrace();
            return null;
        }
    }

    private static void copyConfigFromResource(InputStream in, OutputStream out) {
        try {
            int size = in.available();
            byte[] buffer = new byte[size]; // or other buffer size
            int read;

            in.read(buffer);
            out.write(buffer);

            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String loadJSONFromAsset(InputStream is) {
        String json = null;
        try {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static void saveProfilesToJSON(ArrayList<Profile> profiles, Context context) {
        String output = "{\n\"profiles\": [{\n";
//        ArrayList<Profile> profiles = ((TunerApp) this.getApplication()).getProfiles();
        for (int i = 0; i < profiles.size(); i++) {
            output += ("\"name\":");
            output += ("\"" + profiles.get(i).getName() + "\",\n");
            output += "\"pitchIndexes\": [\n";

            for (int j = 0; j < profiles.get(i).getTones().size(); j++) {
                output += profiles.get(i).getTones().get(j);
                if (j != profiles.get(i).getTones().size() -1) {
                    output += ",\n";
                } else {
                    output += "\n]}";
                }
            }
            if (i != profiles.size()-1) {
                output += ",{\n";
            } else {
                output += "\n]\n}";
            }
        }
        try {

            byte[] buffer = output.getBytes();
            File directory = context.getFilesDir();
            File config = new File(directory, "config.json");
            OutputStream out = new FileOutputStream(config);
            out.write(buffer);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
