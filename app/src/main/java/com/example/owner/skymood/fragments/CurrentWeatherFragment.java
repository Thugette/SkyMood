package com.example.owner.skymood.fragments;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.example.owner.skymood.model.LocationPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;


public class CurrentWeatherFragment extends Fragment implements Swideable {

    private ProgressBar progressBar;
    private TextView chosenCity;
    private Spinner spinner;
    private ImageView sync;
    private ImageView gpsSearch;
    private ImageView citySearch;
    private AutoCompleteTextView writeCityEditText;
    private TextView temperature;
    private TextView condition;
    private TextView feelsLike;
    private TextView lastUpdate;
    private ImageView weatherImage;

    private static final String DEFAULT_CITY = "Sofia";
    private String city;
    private String cityToDisplay;
    private HashMap<String, String> cities;
    private ArrayList<String> autoCopleteNames;
    ArrayAdapter adapterAutoComplete;

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

        sync = (ImageView) rootView.findViewById(R.id.synchronize);
        gpsSearch = (ImageView) rootView.findViewById(R.id.gpsSearch);
        citySearch = (ImageView) rootView.findViewById(R.id.citySearch);
        writeCityEditText = (AutoCompleteTextView) rootView.findViewById(R.id.writeCityEditText);
        temperature = (TextView) rootView.findViewById(R.id.temperatureTextView);
        condition = (TextView) rootView.findViewById(R.id.conditionTextView);
        feelsLike = (TextView) rootView.findViewById(R.id.feelsLikeTextView);
        lastUpdate = (TextView) rootView.findViewById(R.id.lastUpdateTextView);
        weatherImage = (ImageView) rootView.findViewById(R.id.weatherImageView);
        chosenCity = (TextView) rootView.findViewById(R.id.chosenCity);
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


        citySearch.setOnClickListener(new OnCitySearchClickListener());

        View.OnKeyListener onSearchPressed = new OnSearchPressedListener();
        writeCityEditText.setOnKeyListener(onSearchPressed);

        sync.setOnClickListener(new OnSyncListener());

        writeCityEditText.addTextChangedListener(new TextChanged());


        //shared prefs
        locPref = LocationPreference.getInstance(context);

        //for now hard coded for demo
       // locPref.setPreferredLocation("Burgas, Bulgaria", "Burgas", "19.9°", "Clear", "Feels like: 19.9℃", "Last update: 05.04.2016, 18:00", "clear");


        if(isOnline()){
            //first: check shared prefs
            if(locPref.isSetLocation()){
                setCity(locPref.getCity());
            } else {
                //API autoIP
            }

            if(city == null) {
                setCity("Sofia");
            }

            MyTask task = new MyTask();
            task.execute();

        } else {
            if(locPref.isSetLocation()){
                Toast.makeText(context, "NO INTERNET CONNECTION\nFor up to date info connect to Internet", Toast.LENGTH_LONG).show();
                setCity(locPref.getCity());
                getWeatherInfoFromSharedPref();
            } else {
                feelsLike.setText("Please connect to Internet");
            }
        }

        return rootView;
    }

    public void getWeatherInfoFromSharedPref(){
        chosenCity.setVisibility(View.VISIBLE);
        chosenCity.setText(locPref.getLocation());
        temperature.setText(locPref.getTemperature());
        feelsLike.setText(locPref.getMoreInfo());
        lastUpdate.setText(locPref.getLastUpdate());
        condition.setText(locPref.getCondition());

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
        setCity(city);
        writeCityEditText.setText("");
        writeCityEditText.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);
        sync.setVisibility(View.VISIBLE);
        gpsSearch.setVisibility(View.VISIBLE);
        MyTask task = new MyTask();
        task.execute();
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    class MyTask extends AsyncTask<Void, Void, Void> {

        String location;
        String conditionn;
        String icon;
        String temp;
        String feelsLikee;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                URL url = new URL("http://api.wunderground.com/api/b4d0925e0429238f/conditions/q/BG/" + city + ".json");
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
                icon = observation.getString("icon");

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

            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            String update = "Last update: " + hour + ":" + c.get(Calendar.MINUTE) + " " +
                    c.get(Calendar.DATE) + "." + (c.get(Calendar.MONTH) + 1) + "." + c.get(Calendar.YEAR);
            lastUpdate.setText(update);


            Context con = weatherImage.getContext();
            int id = 0;
            if(icon == null) {
                weatherImage.setImageResource(R.drawable.nulll);
            } else {
                if (hour >= 6 && hour <= 19) {
                    id = context.getResources().getIdentifier(icon, "drawable", con.getPackageName());
                } else {
                    icon = icon + "_night";
                    id = context.getResources().getIdentifier(icon, "drawable", con.getPackageName());
                }
                weatherImage.setImageResource(id);

                if (locPref.isSetLocation() && city.equalsIgnoreCase(locPref.getLocation())) {
                    locPref.setPreferredLocation(location, city, temp, conditionn, feelsLikee, update, icon);
                }
            }
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
            if(isOnline()) {
                autoCopleteNames = new ArrayList<>();
                if (writeCityEditText.getVisibility() == View.GONE) {
                    spinner.setVisibility(View.GONE);
                    sync.setVisibility(View.GONE);
                    gpsSearch.setVisibility(View.GONE);
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
                    sync.setVisibility(View.VISIBLE);
                    gpsSearch.setVisibility(View.VISIBLE);
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
                        MyTask task = new MyTask();
                        task.execute();
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
                MyTask task = new MyTask();
                task.execute();
            } else {
                Toast.makeText(context, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class StringFiller extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            try {
                Log.e("VVV", "Do in background");
                URL url = new URL("http://autocomplete.wunderground.com/aq?query=" + params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.connect();

                Scanner sc = new Scanner(con.getInputStream());
                StringBuilder body = new StringBuilder();
                while(sc.hasNextLine()){
                    body.append(sc.nextLine());
                }
                String info = body.toString();
                Log.e("VVV", " json string" + info.length() + "");

                JSONObject jsonObj = new JSONObject(info);
                JSONArray results = jsonObj.getJSONArray("RESULTS");
                Log.e("VVV","json array" + results.length() + "");
                cities = new HashMap<>();
                for(int i = 0; i < results.length(); i++){
                    JSONObject location = (JSONObject) results.get(i);
                    String name = location.getString("name");
                    String country = location.getString("c");
                    cities.put(name, country);
                }


                autoCopleteNames = new ArrayList<>();
                for(String name : cities.keySet()){
                    Log.e("VVV", name);
                    autoCopleteNames.add(name);
                }

                Log.e("VVV", autoCopleteNames.size() + "");

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
            Log.e("VVV", "on post execute");
            adapterAutoComplete = new ArrayAdapter(context, android.R.layout.simple_list_item_1, autoCopleteNames);
            writeCityEditText.setThreshold(2);
            writeCityEditText.setAdapter(adapterAutoComplete);
        }
    }

    private class TextChanged implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int chars = writeCityEditText.getText().toString().length();
            if(chars == 2){
                StringFiller filler = new StringFiller();
                filler.execute(writeCityEditText.getText().toString());
            }
            if (chars > 2) {

                Log.e("VVV", adapterAutoComplete.getCount() + "");
                //TODO
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

}
