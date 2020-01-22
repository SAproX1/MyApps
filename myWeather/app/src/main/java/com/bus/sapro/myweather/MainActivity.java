package com.bus.sapro.myweather;

import android.content.Context;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText CityName;
    TextView Temp;
    String TempResult = "";

    public class DownloadWebContent extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {


            try {

                String HtmlContent = ""; //null instead of "" makes it not work
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int data = inputStreamReader.read();

                while (data != -1) // read the URL content until the end
                {
                    char current = (char) data;
                    HtmlContent += current;
                    data = inputStreamReader.read();
                }

                return HtmlContent;



            } catch (Exception e) {
                e.printStackTrace();

                //Toast.makeText(getApplicationContext(), "Please check the entered city name", Toast.LENGTH_LONG).show();

                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                JSONObject jsonObject = new JSONObject(s);
                //String weatherInfo = jsonObject.getString("weather");
                //JSONArray jArray = new JSONArray(weatherInfo);
                String MainInfo = jsonObject.getString("main");
                JSONObject jsonObject1 = new JSONObject(MainInfo);

                // how to access array list on JSONObject
                /*for (int i=0; i < jArray.length(); i++)
                {
                    JSONObject jsonPart = jArray.getJSONObject(i);
                    String temp = jsonPart.getString("id");

                    TempResult += temp;
                }*/

                TempResult = jsonObject1.getString("temp");


            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void Go(View v) {

        try {

            DownloadWebContent PageWebContent = new DownloadWebContent();
            // This string will correct to the correct name and avoid spaces problem
            String encodedCityName = URLEncoder.encode(CityName.getText().toString(),"UTF-8");

            PageWebContent.execute("https://openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&appid=f79c3949cd865910a84a3b0cd5019337");

            //This method makes keyboard hides after taping the needed info
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(CityName.getWindowToken(),0);

            // wait until background finish, otherwise make these treatment inside background function
            new CountDownTimer(1000, 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    Temp.setText(TempResult + "°F");
                }
            }.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CityName = findViewById(R.id.CityName);
        Temp = findViewById(R.id.Temp);

    }
}
