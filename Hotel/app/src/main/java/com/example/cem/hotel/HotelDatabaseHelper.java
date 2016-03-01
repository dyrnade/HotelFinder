package com.example.cem.hotel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cemg on 1/4/16.
 */

public class HotelDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "hotels.db";
    private static final int VERSION = 1;
    private static final String TABLE_HOTELS = "hotels";
    private static final String COLUMN_HOTEL_ID = "id";
    private static final String COLUMN_HOTEL_NAME= "hotel_name";
    private static final String COLUMN_HOTEL_ADDRESS= "hotel_address";

    private SQLiteDatabase database;

    //    private static final String COLUMN_HOTEL_RATING = "place";
    public HotelDatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_HOTELS + " (" + COLUMN_HOTEL_ID + " integer primary key autoincrement,"
                + COLUMN_HOTEL_NAME + " text not null unique," + COLUMN_HOTEL_ADDRESS + " text not null unique)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade database, in the simplest case just drop all old data
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOTELS);
        onCreate(db);
    }

    public boolean insertHotel(String hotelName, String hotelAddress) {
        database = getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_HOTEL_NAME, hotelName);
        cv.put(COLUMN_HOTEL_ADDRESS, hotelAddress);
        long i = database.insertWithOnConflict(TABLE_HOTELS, null, cv, SQLiteDatabase.CONFLICT_IGNORE);

        if (i == -1){
            return false;
        }else {
            return true;
        }
    }

    public void clearHotels() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_HOTELS, null, null);
    }

    public List<Hotel> getAllHotels() {
        List<Hotel> hotels = new ArrayList<Hotel>();
        String selectQuery = "SELECT  * FROM " + TABLE_HOTELS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        c.moveToFirst();
        if (c.getCount() > 0) {
            do {
                Hotel hotel = new Hotel();
                hotel.setId(c.getString((c.getColumnIndex(COLUMN_HOTEL_ID))));
                hotel.setHotelName((c.getString(c.getColumnIndex(COLUMN_HOTEL_NAME))));
                hotel.setHotelAddress(c.getString(c.getColumnIndex(COLUMN_HOTEL_ADDRESS)));

                // adding to hotel list
                hotels.add(hotel);
            } while (c.moveToNext());
        }

        return hotels;
    }
}
