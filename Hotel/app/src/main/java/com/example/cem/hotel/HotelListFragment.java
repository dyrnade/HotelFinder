package com.example.cem.hotel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class HotelListFragment extends Fragment {

    private HotelAdapter hotelAdapter;
    private HotelDatabaseHelper hotelDB;
    RecyclerView hotelView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hotelDB = new HotelDatabaseHelper(getContext());

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.hotel_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        this.setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_item_list, container, false);
        hotelView = (RecyclerView) v.findViewById(R.id.hotel_list);
        setHotelAdapter();

        return v;
    }

    public void setHotelAdapter() {


        RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity());
        hotelView.setLayoutManager(lm);
        hotelAdapter = new HotelAdapter(hotelDB.getAllHotels());
        hotelView.setAdapter(hotelAdapter);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_hotel_list, menu);
        super.onCreateOptionsMenu(menu,menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.deleteItems:
                Intent intent = new Intent(getActivity(), HotelPreferencesActivity.class);
                startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        setHotelAdapter();
    }

    @Override
    public void onStop() {
        super.onStop();
        hotelDB.close();
    }
}