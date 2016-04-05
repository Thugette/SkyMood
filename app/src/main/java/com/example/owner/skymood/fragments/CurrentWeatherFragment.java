package com.example.owner.skymood.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.owner.skymood.R;
import com.example.owner.skymood.location.NetworkLocationListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;


public class CurrentWeatherFragment extends Fragment implements NetworkLocationListener.LocationReceiver, Swideable {

    private ProgressBar progressBar;
    private TextView chosenCity;
    private Spinner spinner;
    private ImageView sync;
    private ImageView gpsSearch;
    private ImageView citySearch;
    private EditText writeCityEditText;
    private TextView temperature;
    private TextView condition;
    private TextView feelsLike;
    private TextView lastUpdate;
    private ImageView weatherImage;

    private Location location;
    private LocationManager locationManager;
    private NetworkLocationListener listener;
    private double latitude;
    private double longtitude;
    String city;
    String cityToDisplay;

    InputMethodManager keyboard;
    Context context;

    public CurrentWeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_current_weather, container, false);

        sync = (ImageView) rootView.findViewById(R.id.synchronize);
        gpsSearch = (ImageView) rootView.findViewById(R.id.gpsSearch);
        citySearch = (ImageView) rootView.findViewById(R.id.citySearch);
        writeCityEditText = (EditText) rootView.findViewById(R.id.writeCityEditText);
        temperature = (TextView) rootView.findViewById(R.id.temperatureTextView);
        condition = (TextView) rootView.findViewById(R.id.conditionTextView);
        feelsLike = (TextView) rootView.findViewById(R.id.feelsLikeTextView);
        lastUpdate = (TextView) rootView.findViewById(R.id.lastUpdateTextView);
        weatherImage = (ImageView) rootView.findViewById(R.id.weatherImageView);
        chosenCity = (TextView) rootView.findViewById(R.id.chosenCity);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        ArrayList<String> cities =  new ArrayList<>();
        cities.add("My Locations");
        cities.add("Sofia");
        cities.add("London");
        cities.add("Paris");
        spinner = (Spinner) rootView.findViewById(R.id.locationSpinner);
        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, cities);
        spinner.setAdapter(adapter);

        //spinner.setOnItemClickListener();

        citySearch.setOnClickListener(new OnCitySearchClickListener());

        View.OnKeyListener onSearchPressed = new OnSearchPressedListener();
        writeCityEditText.setOnKeyListener(onSearchPressed);

        //TODO: shared prefs

        //network location
        //getNetworkLocation();

        //TODO: gps

        //for now by deafault will be sofia
        setCity("Sofia");
        chosenCity.setText(city);
        MyTask task = new MyTask();
        task.execute();

        return rootView;
    }

    public void setCity(String city){
        this.city = city.replace(" ", "_");
        this.cityToDisplay = city.toUpperCase();
    }

    public void getWeatherInfoByCity(String city){
        setCity(city);
        writeCityEditText.setText("");
        writeCityEditText.setVisibility(View.GONE);
        MyTask task = new MyTask();
        task.execute();
    }

   public void getNetworkLocation() {
        listener = new NetworkLocationListener(context);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
    }

    @Override
    public void receiveLocation(Location location) {
        this.location = location;
        this.latitude = location.getLatitude();
        this.longtitude = location.getLongitude();

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        locationManager.removeUpdates(listener);
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }


    class MyTask extends AsyncTask<Void, Void, Void> {

        String location;
        String conditionn;
        String iconUrl;
        String temp;
        String feelsLikee;
        Bitmap pic;

        @Override
        protected Void doInBackground(Void... params) {

            try {

                URL url = new URL("http://api.wunderground.com/api/b4d0925e0429238f/conditions/q/" + city + ".json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                Scanner sc = new Scanner(connection.getInputStream());
                StringBuilder body = new StringBuilder();
                while(sc.hasNextLine()){
                    body.append(sc.nextLine());
                }
                String info = body.toString();

                JSONObject jsonData = new JSONObject(info);
                JSONObject observation = (JSONObject) jsonData.get("current_observation");
                JSONObject locationObject = (JSONObject) observation.get("display_location");
                location = locationObject.getString("full");
                conditionn = observation.getString("weather");
                temp = observation.getString("temp_c") + "°";
                feelsLikee = "Feels like: " + observation.getString("feelslike_c") + "℃";
                iconUrl = observation.getString("icon_url");

                pic = BitmapFactory.decodeStream((InputStream) new URL(iconUrl).getContent());
                
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            chosenCity.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.GONE);
            chosenCity.setVisibility(View.VISIBLE);
            chosenCity.setText(cityToDisplay);
            temperature.setText(temp);
            condition.setText(conditionn);
            feelsLike.setText(feelsLikee);

            weatherImage.setImageBitmap(pic);
            weatherImage.setAdjustViewBounds(true);
        }
    }

    private class OnSearchPressedListener implements View.OnKeyListener{

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if(keyCode == KeyEvent.KEYCODE_ENTER){
                getWeatherInfoByCity(writeCityEditText.getText().toString());
                keyboard.hideSoftInputFromWindow(writeCityEditText.getWindowToken(), 0);
                return true;
            }
            return false;
        }
    }

    private class OnCitySearchClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (writeCityEditText.getVisibility() == View.GONE) {
                //TODO: change animation
                Animation slide = new AnimationUtils().loadAnimation(getContext(), android.R.anim.fade_in);
                slide.setDuration(1000);
                writeCityEditText.startAnimation(slide);
                writeCityEditText.setVisibility(View.VISIBLE);
                writeCityEditText.setFocusable(true);
                writeCityEditText.requestFocus();
                keyboard = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(writeCityEditText, 0);
            } else {
                writeCityEditText.setVisibility(View.GONE);
                keyboard.hideSoftInputFromWindow(writeCityEditText.getWindowToken(), 0);
            }
        }
    }

}
