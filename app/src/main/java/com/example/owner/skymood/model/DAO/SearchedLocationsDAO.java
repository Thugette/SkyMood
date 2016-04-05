package com.example.owner.skymood.model.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.example.owner.skymood.model.DatabaseHelper;
import com.example.owner.skymood.model.SearchedLocation;

import java.util.ArrayList;

/**
 * Created by owner on 05/04/2016.
 */
public class SearchedLocationsDAO implements ISearchedLocations{
    private static SearchedLocationsDAO ourInstance;
    DatabaseHelper helper;

    private SearchedLocationsDAO(Context context) {
        helper = DatabaseHelper.getInstance(context);
    }

    public static SearchedLocationsDAO getInstance(Context context) {
        if(ourInstance == null)
            ourInstance = new SearchedLocationsDAO(context);
        return ourInstance;
    }


    @Override
    public ArrayList<SearchedLocation> getAllSearchedLocations() {
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = "SELECT * FROM " + helper.LAST_SEARCHED;
        Cursor c = db.rawQuery(query, null);
        ArrayList<SearchedLocation> cities = new ArrayList<SearchedLocation>();
        if(c.moveToFirst())
            do{
                long id = c.getLong(c.getColumnIndex(helper.SEARCHED_ID));
                String city = c.getString(c.getColumnIndex(helper.CITY));
                String temp = c.getString(c.getColumnIndex(helper.TEMP));
                String condition = c.getString(c.getColumnIndex(helper.CONDITION));
                String moreinfo = c.getString(c.getColumnIndex(helper.MORE_INFO));
                String hourlyJSON = c.getString(c.getColumnIndex(helper.HOURLY_WEATHER));
                String weeklyJSON = c.getString(c.getColumnIndex(helper.WEEKLY_WEATHER));
                String date = c.getString(c.getColumnIndex(helper.DATE));

                SearchedLocation location = new SearchedLocation(id, city, temp, condition, moreinfo, hourlyJSON, weeklyJSON, date);
                cities.add(location);
            }
            while (c.moveToFirst());
        return cities;
    }

    @Override
    public long insertSearchedLocation(SearchedLocation location) {
        if(checkCity(location.getCity())){
            return -1;
        }
        else if(getCount() < 5){
            return insertLocation(location);
        }
        else{
            long id = selectFirstSearchedCity().getId();
            return updateLocation(id, location);
        }
    }

    @Override
    public SearchedLocation selectFirstSearchedCity() {
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = "SELECT * FROM "+ helper.LAST_SEARCHED +" ORDER BY date("+ helper.DATE +") Limit 1";
        Cursor c = db.rawQuery(query, null);
        SearchedLocation location = null;
        if(c.moveToFirst())
            do{
                long id = c.getLong(c.getColumnIndex(helper.SEARCHED_ID));
                String city = c.getString(c.getColumnIndex(helper.CITY));
                String temp = c.getString(c.getColumnIndex(helper.TEMP));
                String condition = c.getString(c.getColumnIndex(helper.CONDITION));
                String moreInfo = c.getString(c.getColumnIndex(helper.MORE_INFO));
                String hourlyJSON = c.getString(c.getColumnIndex(helper.HOURLY_WEATHER));
                String weeklyJSON = c.getString(c.getColumnIndex(helper.WEEKLY_WEATHER));
                String date = c.getString(c.getColumnIndex(helper.DATE));

                location = new SearchedLocation(id, city, temp, condition, moreInfo, hourlyJSON, weeklyJSON, date);

            }
            while (c.moveToFirst());
        return location;
    }

    public long getCount(){
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + helper.LAST_SEARCHED;
        SQLiteStatement statement = db.compileStatement(query);
        long count = statement.simpleQueryForLong();
        return count;
    }

    @Override
    public boolean checkCity(String city) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = "SELECT "+ helper.CITY +" FROM " + helper.LAST_SEARCHED
                + " WHERE " + helper.CITY + " = " + city;
        Cursor c = db.rawQuery(query,  null);
        if(c.moveToFirst())
            return true;
        else
            return false;
    }

    @Override
    public long insertLocation(SearchedLocation location) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(helper.CITY, location.getCity());
        values.put(helper.TEMP, location.getTemp());
        values.put(helper.CONDITION, location.getCondition());
        values.put(helper.MORE_INFO, location.getMoreInfo());
        values.put(helper.HOURLY_WEATHER, location.getHourlyJSON());
        values.put(helper.WEEKLY_WEATHER, location.getWeeklyJSON());
        long id = db.insert(helper.LAST_SEARCHED, null, values);
        return id;
    }

    @Override
    public long updateLocation(long id, SearchedLocation location) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(helper.CITY, location.getCity());
        values.put(helper.TEMP, location.getTemp());
        values.put(helper.CONDITION, location.getCondition());
        values.put(helper.MORE_INFO, location.getMoreInfo());
        values.put(helper.HOURLY_WEATHER, location.getHourlyJSON());
        values.put(helper.WEEKLY_WEATHER, location.getWeeklyJSON());
        long result = db.update(helper.LAST_SEARCHED, values, helper.SEARCHED_ID + " = ? ", new String[]{""+id});
        return result;
    }


}
