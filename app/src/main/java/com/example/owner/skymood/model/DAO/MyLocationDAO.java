package com.example.owner.skymood.model.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.owner.skymood.model.DatabaseHelper;

import java.util.ArrayList;

/**
 * Created by owner on 05/04/2016.
 */
public class MyLocationDAO implements IMyLocationDAO{

    private static MyLocationDAO instance;
    private Context context;
    private DatabaseHelper helper;

    private MyLocationDAO(Context context){
        this.context = context;
        this.helper = DatabaseHelper.getInstance(context);
    }

    public static MyLocationDAO getInstance(Context context){
        if(instance == null)
            instance = new MyLocationDAO(context);
        return instance;
    }


    @Override
    public ArrayList<String> getAllMyLocations() {
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = "SELECT "+ helper.CITY +" FROM " + helper.MY_LOCATIONS;
        Cursor c = db.rawQuery(query, null);
        ArrayList<String> cities = new ArrayList<String>();
        if(c.moveToFirst())
            do{
                cities.add(c.getString(c.getColumnIndex(helper.CITY)));
            }
            while (c.moveToFirst());
        return cities;
    }

    @Override
    public long insertMyLocation(String city) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(helper.CITY, city);
        long id = -1;
        if(selectMyCity(city) == null)
            id = db.insert(helper.MY_LOCATIONS, null, values);
        return id;
    }

    @Override
    public String selectMyCity(String city) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = "SELECT "+ helper.CITY +" FROM " + helper.MY_LOCATIONS
                + " WHERE " + helper.CITY + " = " + city;
        Cursor c = db.rawQuery(query, null);

        if(c.moveToFirst())
            return c.getString(c.getColumnIndex(helper.CITY));
        else
            return null;
    }

    @Override
    public long deleteMyLocation(String city) {
        SQLiteDatabase db = helper.getReadableDatabase();
        return db.delete(helper.MY_LOCATIONS, helper.CITY + " = ?", new String[] {city});
    }
}
