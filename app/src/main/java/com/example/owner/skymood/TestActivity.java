package com.example.owner.skymood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.owner.skymood.adapters.TestAdapter;
import com.example.owner.skymood.model.MyLocation;
import com.example.owner.skymood.model.MyLocationManager;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {

    private ArrayList<MyLocation> locations;
    private ListView listView;
    MyLocationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        manager = MyLocationManager.getInstance(this);
        locations = manager.getAllMyLocations();
        listView = (ListView) findViewById(R.id.list_item);

        TestAdapter adapter = new TestAdapter(this, R.layout.my_locations_row, locations);
        listView.setAdapter(adapter);
    }
}
