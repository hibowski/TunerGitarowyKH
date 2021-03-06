package com.example.tunergitarowy.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tunergitarowy.profiles.Profile;
import com.example.tunergitarowy.R;
import com.example.tunergitarowy.profiles.TunerApp;

import java.util.List;


public class ProfileListActivity extends AppCompatActivity {

    /**
     * zmienna mTwoPane, przechowująca informację czy aplikacja działa na tablecie
     */
    private boolean mTwoPane;
    private View recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        recyclerView = findViewById(R.id.profile_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileListActivity.this);
                builder.setTitle("Nazwa profilu");


                final EditText input = new EditText(getApplicationContext());


                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);


                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int profile_no = ((TunerApp) getApplication()).getProfileListSize();
                        Profile newProfile = new Profile(profile_no+1, input.getText().toString());
                        ((TunerApp) getApplication()).addProfile(newProfile);
                        setupRecyclerView((RecyclerView) recyclerView);

                    }

                });



                builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        });

        if (findViewById(R.id.profile_detail_container) != null) {

        }
        mTwoPane = false;

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        //TODO: podmienic DummyContent na liste profili w TunerApp
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, ((TunerApp) this.getApplication()).getProfiles(), mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ProfileListActivity mParentActivity;
        private final List<Profile> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: przeportowac to na class Profiles z TunerApp
                Profile item = (Profile) view.getTag();
                //TODO: Olac TwoPane?
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ProfileDetailFragment.ARG_ITEM_ID, item.getName());
                    ProfileDetailFragment fragment = new ProfileDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.profile_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ProfileDetailActivity.class);
                    intent.putExtra(ProfileDetailFragment.ARG_ITEM_ID, item.getName());

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(ProfileListActivity parent,
                                      List<Profile> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.profile_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(new String(String.valueOf(mValues.get(position).getId())));
            holder.mContentView.setText(mValues.get(position).getName());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            // TODO: To jak lista wyglada? Nie potrzebujemy w sumie iD (No i nie przechowujemy)
            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }
}
