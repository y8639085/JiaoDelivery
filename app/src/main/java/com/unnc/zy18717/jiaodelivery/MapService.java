package com.unnc.zy18717.jiaodelivery;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class MapService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public class MyBinder extends Binder {

        public MapService getService() {
            return MapService.this;
        }

        // get current location on backstage
        public void onLocationChanged(Location location, GoogleMap mMap) {
            mMap.clear();
            LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(current).title("Current Location"));

            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

            try {
                List<Address> listAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (listAddress != null && listAddress.size() > 0) {
                    Log.i("placeInfo", listAddress.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}