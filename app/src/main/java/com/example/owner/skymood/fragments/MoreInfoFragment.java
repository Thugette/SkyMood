package com.example.owner.skymood.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.owner.skymood.R;
import com.example.owner.skymood.SwipeViewActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

public class MoreInfoFragment extends Fragment  implements Swideable{

    private Context context;
    private String city;
    private String code;


    private TextView day;
    private TextView date;
    private TextView temp;
    private TextView feels;
    private TextView min;
    private TextView max;
    private TextView uv;
    private TextView humidity;
    private TextView preassure;
    private TextView visibility;
    private TextView sunrise;
    private TextView sunset;
    private TextView moonAge;
    private TextView moonIlluminated;
    private TextView windSpeed;
    private TextView condition;
    private TextView moonPhase;

    private String dayTxt;
    private String dateTxt;
    private String tempTxt;
    private String feelsTxt;
    private String minTxt;
    private String maxTxt;
    private String uvTxt;
    private String  humidityTxt;
    private String  preassureTxt;
    private String windsSpeedtxt;
    private String visibilityTxt;
    private String sunriseTxt;
    private String sunsetTxt;
    private String conditionTxt;
    private String moonPhaseTxt;
    private int moonAgeTxt;
    private int moonIllumitatedTxt;
    private static final String API_KEY = "7fc23227bbbc9a36";
    private SwipeViewActivity activity;

    public MoreInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_more_info, container, false);

        this.day = (TextView) root.findViewById(R.id.more_day);
        this.date = (TextView) root.findViewById(R.id.more_date);
        this.temp = (TextView) root.findViewById(R.id.more_temp);
        this.feels = (TextView) root.findViewById(R.id.more_realfeel);
        this.min = (TextView) root.findViewById(R.id.more_min);
        this.max = (TextView) root.findViewById(R.id.more_max);
        this.uv = (TextView) root.findViewById(R.id.more_uv);
        this.preassure = (TextView) root.findViewById(R.id.more_preassure);
        this.humidity = (TextView) root.findViewById(R.id.more_humidity);
        this.visibility = (TextView) root.findViewById(R.id.more_visibility);
        this.sunrise = (TextView) root.findViewById(R.id.more_sunrize);
        this.sunset = (TextView) root.findViewById(R.id.more_sunset);
        this.moonIlluminated = (TextView) root.findViewById(R.id.more_moonIllumitated);
        this.moonAge = (TextView) root.findViewById(R.id.more_moonAge);
        this.condition = (TextView) root.findViewById(R.id.more_condition);
        this.windSpeed = (TextView) root.findViewById(R.id.more_windsSpeed);
        this.moonPhase = (TextView)root.findViewById(R.id.more_phase_of_moon);

        new GetMoreInfoTask().execute();
        activity = (SwipeViewActivity) context;
        return root;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    public void setData(){
        this.temp.setText(tempTxt + "℃");
        this.feels.setText(feelsTxt + "℃");
        this.min.setText(minTxt + "℃");
        this.max.setText(maxTxt + "℃");
        this.uv.setText(uvTxt);
        this.humidity.setText(humidityTxt);
        this.preassure.setText(preassureTxt + " hPa");
        this.visibility.setText(visibilityTxt+" km");
        this.sunrise.setText(sunriseTxt);
        this.sunset.setText(sunsetTxt);
        this.moonAge.setText(moonAgeTxt+"");
        this.moonIlluminated.setText(moonIllumitatedTxt+" % illuminated");
        this.windSpeed.setText(windsSpeedtxt + " kmh");
        this.condition.setText(conditionTxt);
        this.date.setText(dateTxt);
        this.day.setText(dayTxt);
        this.moonPhase.setText(moonPhaseTxt);
    }


    class GetMoreInfoTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL("http://api.wunderground.com/api/"+API_KEY+"/conditions/q/"+code+"/"+city+".json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                Scanner sc = new Scanner(connection.getInputStream());
                StringBuilder body = new StringBuilder();
                while(sc.hasNextLine()){
                    body.append(sc.nextLine());
                }
                String info = body.toString();

                activity.setMoreInfoJSON(info);
                JSONObject jsonData = new JSONObject(info);
                JSONObject observation = (JSONObject) jsonData.get("current_observation");
                conditionTxt = observation.getString("weather");
                tempTxt = observation.getString("temp_c");
                feelsTxt = observation.getString("feelslike_c");
                uvTxt = observation.getString("UV");
                humidityTxt = observation.getString("relative_humidity");
                windsSpeedtxt = observation.getString("wind_kph");
                visibilityTxt = observation.getString("visibility_km");
                preassureTxt = observation.getString("pressure_mb");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                URL astronomyUrl = new URL("http://api.wunderground.com/api/"+API_KEY+"/astronomy/q/"+code+"/"+city+".json");
                HttpURLConnection secontConnection = (HttpURLConnection) astronomyUrl.openConnection();
                secontConnection.connect();
                Scanner sc2 = new Scanner(secontConnection.getInputStream());
                StringBuilder bodyBuilder = new StringBuilder();
                while(sc2.hasNextLine()){
                    bodyBuilder.append(sc2.nextLine());
                }
                String astronomyJSON = bodyBuilder.toString();


                JSONObject jsonData = new JSONObject(astronomyJSON);
                JSONObject moon = (JSONObject) jsonData.get("moon_phase");
                JSONObject sun = (JSONObject) jsonData.get("sun_phase");
                JSONObject sun_rise = sun.getJSONObject("sunrise");
                JSONObject sun_set = sun.getJSONObject("sunset");
                moonAgeTxt = Integer.valueOf(moon.getString("ageOfMoon"));
                moonIllumitatedTxt = Integer.valueOf(moon.getString("percentIlluminated"));
                moonPhaseTxt = moon.getString("phaseofMoon");
                sunriseTxt = sun_rise.get("hour") + ":" + sun_rise.get("minute");

                sunsetTxt = sun_set.get("hour") + ":" + sun_set.get("minute");
                Date d = new Date();
                dayTxt = (String) android.text.format.DateFormat.format("EEEE", d);

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
            super.onPostExecute(aVoid);
            setData();
        }
    }

    public void setInfo(String city, String code, String date, String min, String max){
        this.city = city;
        this.code = code;
        this.minTxt = min;
        this.maxTxt = max;
        this.dateTxt = date;
        if(city != null && code != null)
            new GetMoreInfoTask().execute();
    }
}
