package com.unnc.zy18717.jiaodelivery;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager manager;
    private LocationListener listener;
    private MapActivity.MyConnection conn;
    private MapService.MyBinder myBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create service
        Intent intent = new Intent(this, MapService.class);
        conn = new MapActivity.MyConnection();
        // open service
        startService(intent);
        bindService(intent, conn, BIND_AUTO_CREATE);

        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private class MyConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName cn, IBinder binder) {
            // get binder
            myBinder = (MapService.MyBinder) binder;
        }

        @Override
        public void onServiceDisconnected(ComponentName cn) {
            unbindService(conn);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final ArrayList<LatLng> points = new ArrayList<LatLng>();
//        LatLng current = new LatLng(-34, 151);
//        LatLng current1 = new LatLng(-34, 51);
//        points.add(current);
//        points.add(current1);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (myBinder != null)
                    myBinder.onLocationChanged(location, mMap, points);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {}

            @Override
            public void onProviderEnabled(String s) {}

            @Override
            public void onProviderDisabled(String s) {}
        };

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> listAddress = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (listAddress != null && listAddress.size() > 0) {
                        String address = listAddress.get(0).getFeatureName();
                        Toast.makeText(MapActivity.this, address, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);

                Location lastLocation;
                List<String> providers = manager.getProviders(true);
                if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
                    lastLocation = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    Log.e("jiao", "net");
                } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
                    lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    Log.e("jiao", "gps");
                } else if (providers.contains(LocationManager.PASSIVE_PROVIDER)) {
                    lastLocation = manager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                    Log.e("jiao", "passive");
                } else {
                    Toast.makeText(this, "No available position provider", Toast.LENGTH_SHORT).show();
                    Log.e("jiao", "wu");
                    return;
                }

                if (lastLocation == null)
                    Log.e("jiao", "nulllll");
                LatLng lastKnown = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(lastKnown).title("Last Known Position"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(lastKnown));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastKnown, 15.0f));
            }
        }
    }
}