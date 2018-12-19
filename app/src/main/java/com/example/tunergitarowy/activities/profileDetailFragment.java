package com.example.tunergitarowy.activities;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.tunergitarowy.R;
import com.example.tunergitarowy.profiles.Profile;
import com.example.tunergitarowy.profiles.TunerApp;

import java.util.ArrayList;

public class profileDetailFragment extends Fragment {
    /**
     * Argument fragmentu reprezentujący item ID fragmentu.
     */
    public static final String ARG_ITEM_ID = "item_id";
    /**
     * Obiekt wykorzystywany przez fragment
     */
    private Profile mItem;
    /**
     * Konstruktor inicjalizujący fragment
     */
    public profileDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            Log.i("profileetailFragment: ", getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            mItem = ((TunerApp) activity.getApplication()).findProfile(getArguments().getString(ARG_ITEM_ID));
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getName());
            }
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_detail_spinners, container, false);
        final Spinner stringSpinner = (Spinner) rootView.findViewById(R.id.stringSpinner);
        final Spinner noteSpinner = (Spinner) rootView.findViewById(R.id.noteSpinner);
        final Spinner octaveSpinner = (Spinner) rootView.findViewById(R.id.octaveSpinner);





        if(mItem != null) {
            final Button button = (Button) rootView.findViewById(R.id.button);
            if (mItem.getTones().isEmpty()) { button.setEnabled(false);}


            final ArrayAdapter<Integer> stringArrayAdapter;

            ArrayList<Integer> strings = this.mItem.getTones();

            ArrayList<Integer> lst1 = new ArrayList<Integer>();
            for (Integer i = 0; i<strings.size(); i++) {
                lst1.add(i+1);
            }
            stringArrayAdapter = new ArrayAdapter<Integer>(rootView.getContext(), android.R.layout.simple_spinner_dropdown_item, lst1);

            stringSpinner.setAdapter(stringArrayAdapter);


            final ArrayAdapter<String> noteArrayAdapter;

            final ArrayList<Integer> notes = this.mItem.getTones();

            ArrayList<String> lst2 = new ArrayList<>();
            lst2.add("A");
            lst2.add("A#");
            lst2.add("H");
            lst2.add("C");
            lst2.add("C#");
            lst2.add("D");
            lst2.add("D#");
            lst2.add("E");
            lst2.add("F");
            lst2.add("F#");
            lst2.add("G");
            lst2.add("G#");


            noteArrayAdapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_spinner_dropdown_item, lst2);

            noteSpinner.setAdapter(noteArrayAdapter);

            final ArrayAdapter<Integer> octaveArrayAdapter;

            ArrayList<Integer> octaves = this.mItem.getTones();

            ArrayList<Integer> lst3 = new ArrayList<Integer>();
            lst3.add(2);
            lst3.add(3);
            lst3.add(4);
            lst3.add(5);
            octaveArrayAdapter = new ArrayAdapter<Integer>(rootView.getContext(), android.R.layout.simple_spinner_dropdown_item, lst3);

            octaveSpinner.setAdapter(octaveArrayAdapter);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int string =(int) stringSpinner.getSelectedItemId();
                    int note = (int) noteSpinner.getSelectedItemId();
                    int octave = (int) octaveSpinner.getSelectedItemId() + 2;
                    int index;
                    if (note < 3) {
                        index = (octave - 1)*12 + note;
                    } else {
                        index = (octave - 2)*12 + note;
                    }
                    mItem.changeTone(string, index);
                }
            });

            final Button fab1 = (Button) rootView.findViewById(R.id.button2);
            if (mItem.getTones().isEmpty()) { fab1.setEnabled(false);}
            fab1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: button usuwa strune
                    int string =(int) stringSpinner.getSelectedItemId();
                    mItem.getTones().remove(string);
                    final ArrayAdapter<Integer> stringArrayAdapter;

                    ArrayList<Integer> strings = mItem.getTones();

                    ArrayList<Integer> lst1 = new ArrayList<Integer>();
                    for (Integer i = 0; i<strings.size(); i++) {
                        lst1.add(i+1);
                    }
                    stringArrayAdapter = new ArrayAdapter<Integer>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, lst1);

                    stringSpinner.setAdapter(stringArrayAdapter);
                    Snackbar.make(view, "Usunięto strunę " + (string + 1), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    if (mItem.getTones().isEmpty()) { fab1.setEnabled(false);}
                    if (mItem.getTones().isEmpty()) { button.setEnabled(false);}
                }
            });
            Button fab = (Button) rootView.findViewById(R.id.button1);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: button dodaje strune
                    mItem.addTone(3);
                    final ArrayAdapter<Integer> stringArrayAdapter;

                    ArrayList<Integer> strings = mItem.getTones();

                    ArrayList<Integer> lst1 = new ArrayList<Integer>();
                    for (Integer i = 0; i<strings.size(); i++) {
                        lst1.add(i+1);
                    }
                    stringArrayAdapter = new ArrayAdapter<Integer>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, lst1);

                    stringSpinner.setAdapter(stringArrayAdapter);
                    Snackbar.make(view, "Dodano nową strunę", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    fab1.setEnabled(true);
                    button.setEnabled(true);
                }
            });

            stringSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    int index = notes.get(i);
                    int note = (index%12);
                    int octaveNumber = ((index+9) / 12) + 1;

                    noteSpinner.setSelection(note);
                    octaveSpinner.setSelection(octaveNumber-2);

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

        // Show the dummy content as text in a TextView.

        return rootView;
    }
}