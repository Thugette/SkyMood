package com.example.owner.skymood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.owner.skymood.adapters.MyCardViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MyLocationsActivity extends AppCompatActivity {

    CardView cardView;
    RecyclerView recycler;
    ArrayList<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_locations);

        data.add("Sofia");
        data.add("Plovdiv");
        data.add("London");
        this.cardView = (CardView) findViewById(R.id.mylocation_cardview);
        this.recycler = (RecyclerView) findViewById(R.id.mylocation_recycler);
        this.recycler.setLayoutManager(new LinearLayoutManager(this));
        MyCardViewAdapter adapter = new MyCardViewAdapter(this, data);
        recycler.setAdapter(adapter);

    }
}
