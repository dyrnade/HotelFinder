package com.example.cem.hotel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SearchActivity extends AppCompatActivity implements SearchActivityFragment.OnFragmentInteractionListener {

    private FloatingActionButton showListBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (findViewById(R.id.searchActivityFragment) != null) {
            if (savedInstanceState != null) {
                return;
            }

            showListBtn = (FloatingActionButton) findViewById(R.id.fab);

            showListBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(this, HotelListActivity.class);
                    startActivity(new Intent(getApplicationContext(), HotelListActivity.class));
                }
            });

            SearchActivityFragment searchActivityFragment = new SearchActivityFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.searchActivityFragment, searchActivityFragment).commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
