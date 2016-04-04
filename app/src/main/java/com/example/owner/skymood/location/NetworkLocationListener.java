package com.example.owner.skymood.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Golemanovaa on 3.4.2016 г..
 */
public class NetworkLocationListener implements LocationListener {

    private LocationReceiver receiver;

    public NetworkLocationListener(Context context){
        Log.e("VVV", "Listener constructor");
        this.receiver = (LocationReceiver) context;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("VVV", "Listener onLocationChanged");
        receiver.receiveLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.e("VVV", "Listener onStatusChanged");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.e("VVV", "Listener onProviderEnabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
      //TODO: what to do when there is no network connection
        Log.e("VVV", "Listener onProvideeDisabled");
    }

    public interface LocationReceiver {
        void receiveLocation(Location location);
    }
}
