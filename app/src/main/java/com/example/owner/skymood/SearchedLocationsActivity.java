package com.example.owner.skymood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class SearchedLocationsActivity extends AppCompatActivity implements View.OnClickListener{

    private Button location1;
    private Button location2;
    private Button location3;
    private Button location4;
    private Button location5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_locations);

        //setting toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //initializing buttons
        location1 = (Button) findViewById(R.id.location1);
        location2 = (Button) findViewById(R.id.location2);
        location3 = (Button) findViewById(R.id.location3);
        location4 = (Button) findViewById(R.id.location4);
        location5 = (Button) findViewById(R.id.location5);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.sky_mood:
                intent = new Intent(this, SwipeViewActivity.class);
                startActivity(intent);
                return true;
            case R.id.searched_locations:
                intent = new Intent(this, SearchedLocationsActivity.class);
                startActivity(intent);
                return true;
            case R.id.my_locations:
                intent = new Intent(this, MyLocationsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        String location = ((Button)v).getText().toString();
        //implement logic
    }
}
