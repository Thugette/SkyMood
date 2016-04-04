package com.example.owner.skymood.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by Golemanovaa on 3.4.2016 г..
 */
public class NetworkLocationListener implements LocationListener {

    private LocationReceiver receiver;

    public NetworkLocationListener(Context context){
        this.receiver = (LocationReceiver) context;
    }

    @Override
    public void onLocationChanged(Location location) {
        receiver.receiveLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
      //TODO: what to do when there is no network connection
    }

    public interface LocationReceiver {
        void receiveLocation(Location location);
    }
}
