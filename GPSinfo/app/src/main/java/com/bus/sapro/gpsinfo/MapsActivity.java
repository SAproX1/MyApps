package com.bus.sapro.gpsinfo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    LatLng MyLocation = new LatLng(0,0);
    String S ="Here you are";
    String LocaName = "Place Unrecognized";
    int LocationReceived = 0;
    Double Lat;
    Double Lng;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode ==1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        LocationReceived = intent.getIntExtra("LocationReceived", 0);



    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        final SharedPreferences sharedPreferences = this.getSharedPreferences("com.bus.sapro.gpsinfo", Context.MODE_PRIVATE);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng LocaToSend) {

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    List<Address> listAddress = geocoder.getFromLocation(LocaToSend.latitude, LocaToSend.longitude, 1);

                    if (listAddress != null && listAddress.size() > 0) {
                        LocaName = listAddress.get(0).getThoroughfare(); // add .toString if only listAddress.get(0)
                        //Toast.makeText(MapsActivity.this, listAddress.get(0).toString(), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(getApplicationContext(), SavedLocations.class);
                SavedLocations.LocationsByName.add(LocaName);

                SavedLocations.Lat.add(String.valueOf(LocaToSend.latitude));
                SavedLocations.Lng.add(String.valueOf(LocaToSend.longitude));

                try {
                    sharedPreferences.edit().putString("LocationByName", ObjectSerializer.serialize(SavedLocations.LocationsByName)).apply();
                    sharedPreferences.edit().putString("Lat", ObjectSerializer.serialize(SavedLocations.Lat)).apply();
                    sharedPreferences.edit().putString("Lng", ObjectSerializer.serialize(SavedLocations.Lng)).apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                startActivity(intent);
                Toast.makeText(MapsActivity.this, "Location Saved", Toast.LENGTH_LONG).show();
            }
        });

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MyLocation, 20f));
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                mMap.clear(); // clear the map otherwise it will show location tag each time it changes
                MyLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(MyLocation).title(S));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MyLocation, 20f));

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
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                mMap.clear();
                if(lastKnownLocation != null) {
                    LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(userLocation).title("Here you are"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                }
                else Toast.makeText(MapsActivity.this, "Check API key", Toast.LENGTH_LONG).show();
            }
        }


        if (LocationReceived == 1 ) {

            Intent intent = getIntent();

            Lat = Double.valueOf(SavedLocations.Lat.get(intent.getIntExtra("LocationID", 0)));
            Lng = Double.valueOf(SavedLocations.Lng.get(intent.getIntExtra("LocationID", 0)));

            LatLng SentLoca = new LatLng(Lat, Lng);
            mMap.addMarker(new MarkerOptions().position(SentLoca).title(SavedLocations.LocationsByName.get(intent.getIntExtra("LocationID", 0))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SentLoca, 10f));

        }

    }
}