package com.bus.sapro.gpsinfo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.security.KeyStore;
import java.security.spec.ECField;
import java.util.ArrayList;

public class SavedLocations extends AppCompatActivity {


    static ArrayList<String> LocationsByName = new ArrayList<>();
    ListView LvLocations;
    static ArrayList<String> Lat = new ArrayList<>();
    static ArrayList<String> Lng = new ArrayList<>();
    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_locations);

        LvLocations = findViewById(R.id.ListViewLocations);
        sharedPreferences = this.getSharedPreferences("com.bus.sapro.gpsinfo", Context.MODE_PRIVATE);

        final ArrayAdapter arrayAdapter;



        try {
            Lat = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("Lat", ObjectSerializer.serialize(new ArrayList<String>())));
            Lng = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("Lng", ObjectSerializer.serialize(new ArrayList<String>())));
            LocationsByName = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("LocationByName", ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LocationsByName);
        LvLocations.setAdapter(arrayAdapter);



        LvLocations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)

            {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("LocationID", i);
                intent.putExtra("LocationReceived", 1);
                startActivity(intent);
            }
        });


        LvLocations.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int pos, long id) {


                new AlertDialog.Builder(SavedLocations.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Delete")
                        .setMessage("Delete the selected location?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Lat.remove(pos);
                                Lng.remove(pos);
                                LocationsByName.remove(pos);

                                try {
                                    sharedPreferences.edit().putString("LocationByName", ObjectSerializer.serialize(LocationsByName)).apply();
                                    sharedPreferences.edit().putString("Lat", ObjectSerializer.serialize(Lat)).apply();
                                    sharedPreferences.edit().putString("Lng", ObjectSerializer.serialize(Lng)).apply();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                arrayAdapter.notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();


                return true;
            }
        });


    }
}
