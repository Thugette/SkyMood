package com.example.owner.skymood.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.skymood.R;
import com.example.owner.skymood.SwipeViewActivity;
import com.example.owner.skymood.asyncTasks.APIDataGetter;
import com.example.owner.skymood.model.LocationPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class CurrentWeatherFragment extends Fragment implements Swideable {

    private static final String DEFAULT_CITY = "Sofia";
    private static final String DEFAULT_COUNTRY_CODE = "BG";
    private static final String DEFAULT_COUNTRY = "Bulgaria";

    private ProgressBar progressBar;
    private TextView chosenCityTextView;
    private Spinner spinner;
    private ImageView syncButton;
    private ImageView locationSearchButton;
    private ImageView citySearchButton;
    private AutoCompleteTextView writeCityEditText;
    private TextView temperature;
    private TextView condition;
    private TextView feelsLike;
    private TextView lastUpdate;
    private TextView countryTextView;
    private TextView minTempTextView;
    private TextView maxTempTextView;
    private ImageView weatherImage;

    private String city;
    private String country;
    private String countryCode;
    private String cityToDisplay;
    private String minTemp;
    private String maxTemp;
    private String dateAndTime;
    private HashMap<String, String> cities;
    private ArrayList<String> autoCopleteNames;
    private ArrayAdapter adapterAutoComplete;

    private InputMethodManager keyboard;
    private Context context;
    private LocationPreference locPref;

    public CurrentWeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_current_weather, container, false);

        syncButton = (ImageView) rootView.findViewById(R.id.synchronize);
        locationSearchButton = (ImageView) rootView.findViewById(R.id.gpsSearch);
        citySearchButton = (ImageView) rootView.findViewById(R.id.citySearch);
        writeCityEditText = (AutoCompleteTextView) rootView.findViewById(R.id.writeCityEditText);
        writeCityEditText.setThreshold(3);
        temperature = (TextView) rootView.findViewById(R.id.temperatureTextView);
        countryTextView = (TextView) rootView.findViewById(R.id.country);
        condition = (TextView) rootView.findViewById(R.id.conditionTextView);
        minTempTextView = (TextView) rootView.findViewById(R.id.minTemp);
        maxTempTextView = (TextView) rootView.findViewById(R.id.maxTemp);
        feelsLike = (TextView) rootView.findViewById(R.id.feelsLikeTextView);
        lastUpdate = (TextView) rootView.findViewById(R.id.lastUpdateTextView);
        weatherImage = (ImageView) rootView.findViewById(R.id.weatherImageView);
        chosenCityTextView = (TextView) rootView.findViewById(R.id.chosenCity);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        spinner = (Spinner) rootView.findViewById(R.id.locationSpinner);

        ArrayList<String> cities =  new ArrayList<>();
        cities.add("My Locations");
        cities.add("Sofia");
        cities.add("Burgas");
        cities.add("Plovdiv");
        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, cities);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnMyLocationsSpinnerItemListener());

        citySearchButton.setOnClickListener(new OnCitySearchClickListener());

        TextView.OnEditorActionListener searchButtonPressed = new SearchButtonListener();
        writeCityEditText.setOnEditorActionListener(searchButtonPressed);

        writeCityEditText.addTextChangedListener(new TextChanged());

        syncButton.setOnClickListener(new OnSyncListener());

        locationSearchButton.setOnClickListener(new OnFindLocationListener());

        //shared prefs
        locPref = LocationPreference.getInstance(context);

        //TODO remove, for now hard coded for demo
        //locPref.setPreferredLocation("Burgas", "Bulgaria", "BG", "clear", "19.9", "15", "21", "Clear", "Feels like: 20", "Last update: 05.04.2016, 18:00");

        if(isOnline()){
            APIDataGetter task = new APIDataGetter(this, context, weatherImage);
            //first: check shared prefs
            if(locPref.isSetLocation()){
                setCity(locPref.getCity());
                countryCode = locPref.getCountryCode();
                country = locPref.getCountry();
                task.execute(countryCode, city, country);
            } else {
                //API autoIP
                DetermineCity determineCity = new DetermineCity();
                determineCity.execute();
            }

            if(city == null) {
                setCity(DEFAULT_CITY);
                countryCode = DEFAULT_COUNTRY_CODE;
                country = DEFAULT_COUNTRY;
                task.execute(countryCode, city, country);
            }

        } else {
            if(locPref.isSetLocation()){
                Toast.makeText(context, "NO INTERNET CONNECTION\nFor up to date info connect to Internet", Toast.LENGTH_LONG).show();
                setCity(locPref.getCity());
                country = locPref.getCountry();
                countryCode = locPref.getCountryCode();
                getWeatherInfoFromSharedPref();
            } else {
                feelsLike.setText("Please connect to Internet");
            }
        }

        return rootView;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    public void getWeatherInfoFromSharedPref(){
        chosenCityTextView.setVisibility(View.VISIBLE);
        chosenCityTextView.setText(locPref.getCity());
        countryTextView.setText(country);
        temperature.setText(locPref.getTemperature() + "°");
        minTempTextView.setText("⬇" + locPref.getMinTemp() + "°");
        maxTempTextView.setText("⬆" + locPref.getMaxTemp() + "°");
        condition.setText(locPref.getCondition());
        feelsLike.setText(locPref.getFeelsLike());
        lastUpdate.setText(locPref.getLastUpdate());

        Context con = weatherImage.getContext();
        weatherImage.setImageResource(context.getResources().getIdentifier(locPref.getIcon(), "drawable", con.getPackageName()));
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void setCity(String city){
        this.city = city.replace(" ", "_");
        this.city.toLowerCase();
        this.cityToDisplay = city.toUpperCase();
    }

    public void getWeatherInfoByCity(String city){
        if(city != null && !city.isEmpty()) {
            setCity(city);
            writeCityEditText.setText("");
            writeCityEditText.setVisibility(View.GONE);
            spinner.setVisibility(View.VISIBLE);
            syncButton.setVisibility(View.VISIBLE);
            locationSearchButton.setVisibility(View.VISIBLE);
            APIDataGetter task = new APIDataGetter(this, context, weatherImage);
            task.execute(countryCode, city, country);
        }
    }

    private class OnCitySearchClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(isOnline()) {
                autoCopleteNames = new ArrayList<>();
                if (writeCityEditText.getVisibility() == View.GONE) {
                    spinner.setVisibility(View.GONE);
                    syncButton.setVisibility(View.GONE);
                    locationSearchButton.setVisibility(View.GONE);
                    weatherImage.setAdjustViewBounds(true);
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
                    spinner.setVisibility(View.VISIBLE);
                    syncButton.setVisibility(View.VISIBLE);
                    locationSearchButton.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(context, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class OnMyLocationsSpinnerItemListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!((String) parent.getItemAtPosition(position)).equals("My Locations")) {
                    if (isOnline()) {
                        setCity((String) parent.getItemAtPosition(position));
                        APIDataGetter task = new APIDataGetter(CurrentWeatherFragment.this, context, weatherImage);
                        //TODO remove next line
                        countryCode = DEFAULT_COUNTRY_CODE;
                        task.execute(countryCode, city, country);
                    } else {
                        //TODO check if there is information in DB

                        //if there isn't info in db
                        Toast.makeText(context, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                    }
                }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

    private class OnSyncListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(isOnline()) {
                APIDataGetter task = new APIDataGetter(CurrentWeatherFragment.this, context, weatherImage);
                task.execute(countryCode, city, country);
            } else {
                Toast.makeText(context, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class StringFiller extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            try {
                URL url = new URL("http://autocomplete.wunderground.com/aq?query=" + params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.connect();

                Scanner sc = new Scanner(con.getInputStream());
                StringBuilder body = new StringBuilder();
                while(sc.hasNextLine()){
                    body.append(sc.nextLine());
                }
                String info = body.toString();

                JSONObject jsonObj = new JSONObject(info);
                JSONArray results = jsonObj.getJSONArray("RESULTS");
                cities = new HashMap<>();
                for(int i = 0; i < results.length(); i++){
                    JSONObject location = (JSONObject) results.get(i);
                    String name = location.getString("name");
                    String country = location.getString("c");
                    cities.put(name, country);
                }

                autoCopleteNames = new ArrayList<>();
                for(String name : cities.keySet()){
                    autoCopleteNames.add(name);
                }

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
        protected void onPostExecute(Void aVoid) {
            adapterAutoComplete = new ArrayAdapter(context, android.R.layout.simple_list_item_1, autoCopleteNames);
            writeCityEditText.setAdapter(adapterAutoComplete);
            ((SwipeViewActivity)context).setInfo(city, countryCode, minTemp, maxTemp, dateAndTime);
            ((SwipeViewActivity)context).getNewLocation(city, countryCode);
        }
    }

    private class TextChanged implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            int chars = writeCityEditText.getText().toString().length();
            if(chars >= 3){
                StringFiller filler = new StringFiller();
                filler.execute(writeCityEditText.getText().toString());
            }
        }
    }

    private class SearchButtonListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

            if(writeCityEditText != null && !writeCityEditText.getText().toString().isEmpty()) {
                String location = writeCityEditText.getText().toString();
                countryCode = cities.get(location);
                String[] parts = location.split(",");
                String city = parts[0];
                country = parts[1].trim();

                getWeatherInfoByCity(city);
            } else {
                writeCityEditText.setVisibility(View.GONE);
                keyboard.hideSoftInputFromWindow(writeCityEditText.getWindowToken(), 0);
                spinner.setVisibility(View.VISIBLE);
                syncButton.setVisibility(View.VISIBLE);
                locationSearchButton.setVisibility(View.VISIBLE);
            }
            keyboard.hideSoftInputFromWindow(writeCityEditText.getWindowToken(), 0);
            return false;
        }
    }

    public class DetermineCity extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {

            try {
                URL url = new URL("http://api.wunderground.com/api/b4d0925e0429238f/geolookup/q/autoip.json");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.connect();

                Scanner sc = new Scanner(con.getInputStream());
                StringBuilder body = new StringBuilder();
                while(sc.hasNextLine()){
                    body.append(sc.nextLine());
                }
                String info = body.toString();

                JSONObject data = new JSONObject(info);
                JSONObject location = data.getJSONObject("location");
                countryCode = location.getString("country_iso3166");
                country = location.getString("country_name");
                city = location.getString("city");

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
        protected void onPostExecute(Void aVoid) {
            setCity(city);
            APIDataGetter task = new APIDataGetter(CurrentWeatherFragment.this, context, weatherImage);
            task.execute(countryCode, city, country);
        }
    }

    private class OnFindLocationListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            DetermineCity determineCity = new DetermineCity();
            determineCity.execute();
        }
    }

    public void myTaskPreExecute(){
        chosenCityTextView.setVisibility(View.GONE);
        countryTextView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void myTaskOnPostExecute(String temp, String condition, String feelsLike, String minTemp, String maxTemp, String dateAndTime, String lastUpdate){
        this.progressBar.setVisibility(View.GONE);
        this.chosenCityTextView.setVisibility(View.VISIBLE);
        this.chosenCityTextView.setText(cityToDisplay);
        this.countryTextView.setVisibility(View.VISIBLE);
        this.countryTextView.setText(country);
        this.temperature.setText(temp + "°");
        this.condition.setText(condition);
        this.feelsLike.setText(feelsLike);
        this.minTempTextView.setText("⬇" + minTemp + "°");
        this.minTemp = minTemp;
        this.maxTempTextView.setText("⬆" + maxTemp + "°");
        this.maxTemp = maxTemp;
        this.lastUpdate.setText(lastUpdate);
        this.dateAndTime = dateAndTime;
    }

}
