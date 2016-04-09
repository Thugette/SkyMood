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
    public static final int DATABASE_VERSION = 6;

    //tables
    public static final String MY_LOCATIONS = "my_locations";
    public static final String LAST_SEARCHED = "last_searched";

    public static final String CITY = "city";
    public static final String LOCATION_ID = "id";
    public static final String LOCATION = "location";

    // last searched
    public static final String SEARCHED_ID = "id";
    public static final String HOURLY_WEATHER = "hourly_weather";
    public static final String WEEKLY_WEATHER = "weekly_weather";
    public static final String TEMP = "temp";
    public static final String CONDITION = "condition";
    public static final String MORE_INFO = "more_info";
    public static final String DATE = "date_time";
    public static final String COUNTRY = "country";
    public static final String COUNTRY_CODE = "country_code";
    public static final String ICON = "icon";
    public static final String MAX_TEMP = "max_temp";
    public static final String MIN_TEMP = "min_temp";
    public static final String LAST_UPDATE = "last_update";

    //create table statements
    private static final String CREATE_MY_LOCATIONS = "CREATE TABLE IF NOT EXISTS " + MY_LOCATIONS + " ("
            + LOCATION_ID +" INTEGER PRIMARY KEY AUTOINCREMENT , "
            + CITY + " VARCHAR(30) NOT NULL, "
            + COUNTRY + " VARCHAR(30) NOT NULL, "
            + COUNTRY_CODE + " VARCHAR(30) NOT NULL, "
            + LOCATION + " VARCHAR(80) NOT NULL)";

    private static final String CREATE_LAST_SEARCHED = "CREATE TABLE IF NOT EXISTS " + LAST_SEARCHED + " ("
            + SEARCHED_ID +" INTEGER PRIMARY KEY AUTOINCREMENT , "
            + CITY + " VARCHAR(30) NOT NULL, "
            + HOURLY_WEATHER + " text NOT NULL, "
            + WEEKLY_WEATHER + " text NOT NULL, "
            + TEMP + " text NOT NULL, "
            + CONDITION + " text NOT NULL, "
            + MORE_INFO + " text NOT NULL, "
            + DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
            + COUNTRY + " text NOT NULL, "
            + COUNTRY_CODE + " text NOT NULL, "
            + ICON + " text NOT NULL, "
            + MAX_TEMP + " text NOT NULL, "
            + MIN_TEMP + " text NOT NULL, "
            + LAST_UPDATE + " text NOT NULL "
            +") ";

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_MY_LOCATIONS);
        db.execSQL(CREATE_LAST_SEARCHED);
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
