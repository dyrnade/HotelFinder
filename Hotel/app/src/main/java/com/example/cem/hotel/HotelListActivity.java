package com.example.cem.hotel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class HotelListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_list);


        if (findViewById(R.id.hotel_list_fragment) != null) {
            if (savedInstanceState != null) {
                return;
            }

            HotelListFragment hotelListFragment = new HotelListFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.hotel_list_fragment, hotelListFragment).commit();
        }
    }


}
