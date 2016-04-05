package com.example.owner.skymood.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by owner on 05/04/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper instance;

    public static final String DATABASE_NAME = "SKY_MOOD_DATABASE";
    public static final int DATABASE_VERSION = 1;

    //tables
    public static final String MY_LOCATIONS = "my_locations";
    public static final String LAST_SEARCHED = "last_searched";

    public static final String CITY = "city";
    public static final String LOCATION_ID = "id";

    // last searched
    public static final String SEARCHED_ID = "id";
    public static final String CURRENT_WEATHER = "current_weather";
    public static final String HOURLY_WEATHER = "hourly_weather";
    public static final String WEEKLY_WEATHER = "weekly_weather";
    public static final String DATE = "date";

    //create table statements
    private static final String CREATE_MY_LOCATIONS = "CREATE TABLE IF NOT EXISTS " + MY_LOCATIONS + " ("
            + LOCATION_ID +" INTEGER PRIMARY KEY AUTOINCREMENT , "
            + CITY + " VARCHAR(30) NOT NULL)";

    private static final String CREATE_LAST_SEARCHED = "CREATE TABLE IF NOT EXISTS " + LAST_SEARCHED + " ("
            + SEARCHED_ID +" INTEGER PRIMARY KEY AUTOINCREMENT , "
            + CITY + " VARCHAR(30) NOT NULL, "
            + CURRENT_WEATHER +" text NOT NULL, "
            + HOURLY_WEATHER + " text NOT NULL, "
            + WEEKLY_WEATHER + " text NOT NULL, "
            + DATE + " DATE NOT NULL, "
            +") ";

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(MY_LOCATIONS);
        db.execSQL(LAST_SEARCHED);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + MY_LOCATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + LAST_SEARCHED);

        onCreate(db);
    }

    public static synchronized DatabaseHelper getInstance(Context context){
        if(instance == null)
            instance = new DatabaseHelper(context);
        return instance;
    }
}
