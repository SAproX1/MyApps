package com.bus.sapro.gpsinfo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView Latitude;
    TextView Longitude;
    TextView Altitude;
    TextView Accuracy;
    TextView Address;
    Button Sync;


    public void goToMap (View v) {
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intent);
    }

    public void goToSavedLocations (View v) {
        Intent intent = new Intent(getApplicationContext(), SavedLocations.class);
        startActivity(intent);
    }


    public void Sync (View v) {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        } else {
            // this make the app launch with a loaded location otherwise we'll not have a location position when app start until it get updated
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            SyncGPS();
        }
    }

    public void SyncGPS () {

        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(lastKnownLocation != null) {

            Latitude.setText("Latitude: " + lastKnownLocation.getLatitude());
            Longitude.setText("Longitude: " + lastKnownLocation.getLongitude());
            Altitude.setText("Altitude: " + lastKnownLocation.getAltitude());
            Accuracy.setText("Accuracy: " + lastKnownLocation.getAccuracy());


            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

            try {
                List<Address> listAddress = geocoder.getFromLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 1);

                if (listAddress != null && listAddress.size() > 0) {
                    Address.setText("Address: " + listAddress.get(0).getThoroughfare() + " " + listAddress.get(0).getPostalCode() + " " + listAddress.get(0).getCountryCode()); // add .toString if only listAddress.get(0)
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            Toast.makeText(MainActivity.this, "Sync done", Toast.LENGTH_LONG).show();

        }
        else Toast.makeText(MainActivity.this, "Shit Happens", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode ==1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                SyncGPS();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Latitude = findViewById(R.id.Latitude);
        Longitude = findViewById(R.id.Longitude);
        Altitude = findViewById(R.id.Altitude);
        Accuracy = findViewById(R.id.Accuracy);
        Address = findViewById(R.id.Address);
        Sync = findViewById(R.id.Sync);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                if (location != null) {

                    Latitude.setText("Latitude: " + location.getLatitude());
                    Longitude.setText("Longitude: " + location.getLongitude());
                    Altitude.setText("Altitude: " + location.getAltitude());
                    Accuracy.setText("Accuracy: " + location.getAccuracy());


                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                    try {
                        List<Address> listAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        if (listAddress != null && listAddress.size() > 0) {
                            Address.setText("Address: " + listAddress.get(0).getThoroughfare() + " " + listAddress.get(0).getPostalCode() + " " + listAddress.get(0).getCountryCode()); // add .toString if only listAddress.get(0)
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                else Toast.makeText(MainActivity.this, "Shit Happens", Toast.LENGTH_LONG).show();


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            } else {
                // this make the app launch with a loaded location otherwise we'll not have a location position when app start until it get updated
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                SyncGPS();
            }
        }
    }

}
