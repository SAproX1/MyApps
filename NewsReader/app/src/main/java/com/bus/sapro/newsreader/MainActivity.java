package com.bus.sapro.newsreader;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ListView;


import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    SQLiteDatabase DB1;
    ArrayList<String> ArticlesName = new ArrayList<>();
    ArrayList<String> ArticlesUrl = new ArrayList<>();
    int choice = 0;

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
                return null;
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (choice==1) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String title = jsonObject.getString("title");
                    String url = jsonObject.getString("url");
                    String sql = "INSERT INTO CurrentArticles (url, name) VALUES (?, ?)";
                    SQLiteStatement statement = DB1.compileStatement(sql);
                    statement.bindString(1, url);
                    statement.bindString(2, title);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (choice==0) {
                try {
                    Pattern p;
                    Matcher m;
                    p = Pattern.compile("(.*?),");
                    m = p.matcher(s);

                    while (m.find()) {
                        String idx = m.group(1);
                        String sql = "INSERT INTO Updated (id) VALUES (?)";
                        SQLiteStatement statement = DB1.compileStatement(sql);
                        statement.bindString(1, idx);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void Go() {
        try {
            DownloadWebContent PageWebContent = new DownloadWebContent();
            PageWebContent.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty");
            // wait until background finish, otherwise make these treatment inside background function
            new CountDownTimer(2000, 1000) {
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {

                }

            }.start();
            Cursor c = DB1.rawQuery("SELECT * FROM Updated", null);
            int id = c.getColumnIndex("id");
            choice = 1;

            if (c.moveToFirst()) {
                do {
                    DownloadWebContent PageWebContent2 = new DownloadWebContent();
                    PageWebContent2.execute("https://hacker-news.firebaseio.com/v0/item/"+ c.getString(id) +".json?print=pretty");
                    Log.i("ARTICLE ID", c.getString(id));
                } while (c.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.ListView);
        final ArrayAdapter arrayAdapter;
        DB1 = this.openOrCreateDatabase("ARTICLES", MODE_PRIVATE, null);
        DB1.execSQL("CREATE TABLE IF NOT EXISTS Updated (id VARCHAR)");
        //DB1.execSQL("CREATE TABLE IF NOT EXISTS Dfault (id VARCHAR)");
        DB1.execSQL("CREATE TABLE IF NOT EXISTS CurrentArticles (url VARCHAR, name VARCHAR, id INTEGER PRIMARY KEY)");
        Go();
        new CountDownTimer(8000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {

            }

        }.start();
        Cursor c = DB1.rawQuery("SELECT * FROM CurrentArticles", null);
        int ArticleName = c.getColumnIndex("name");
        int ArticleUrl = c.getColumnIndex("url");

        if (c.moveToFirst()) {
            ArticlesUrl.clear();
            ArticlesName.clear();
            do {
                ArticlesName.add(c.getString(ArticleName));
                ArticlesUrl.add(c.getString(ArticleUrl));
                Log.i("TITLE", c.getString(ArticleName));
                Log.i("URL", c.getString(ArticleUrl));
            } while (c.moveToNext());
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ArticlesName);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)

            {
                Intent intent = new Intent(getApplicationContext(), WV.class);
                intent.putExtra("url", ArticlesUrl.get(i));
                startActivity(intent);
            }
        });

    }
}
