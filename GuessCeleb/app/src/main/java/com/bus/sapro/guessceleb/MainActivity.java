package com.bus.sapro.guessceleb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ImageDownloader imageDownloader;
    ImageView CelebPhoto;
    GridLayout Grid;
    String FullContent=null;
    String NeededContent=null;
    Bitmap CelebBitmap;
    ArrayList<String> CelebName = new ArrayList<String>();
    ArrayList<String> CelebPhotoURL = new ArrayList<String>();
    String CorrectAnswer=null;
    Random X = new Random();


    public class DownloadWebContent extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {


            try {

                String HtmlContent = null;
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
    }


    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {


            try {

                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
    }


    public void ResultCheck(View v)
    {
        Button b = (Button) v;

        if(b.getText().equals(CorrectAnswer))
        {
            Toast.makeText(this, "Good", Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this, "Not Good", Toast.LENGTH_LONG).show();

        InitializeRound();
    }


    public void InitializeRound() {

        ArrayList<String> DuplicateCelebNames = new ArrayList<String>(CelebName);
        int A = X.nextInt(CelebName.size());
        int CorrectAnswerId = X.nextInt(4);
        CorrectAnswer = CelebName.get(A);

        try {

            imageDownloader = new ImageDownloader();
            CelebBitmap = imageDownloader.execute(CelebPhotoURL.get(A)).get();

        } catch (Exception e) {
            e.printStackTrace();
        }

        CelebPhoto.setImageBitmap(CelebBitmap);
        Button bG = (Button) Grid.getChildAt(CorrectAnswerId);
        bG.setText(CorrectAnswer);

        for(int i=0; i<4; i++)
        {
          if(i!=CorrectAnswerId)
          {
              bG = (Button) Grid.getChildAt(i);
              int R = X.nextInt(DuplicateCelebNames.size());
              while (DuplicateCelebNames.get(R).equals(CorrectAnswer))
              {
                  R = X.nextInt(DuplicateCelebNames.size());
              }
              bG.setText(DuplicateCelebNames.get(R));
              DuplicateCelebNames.remove(R);
          }
        }
    }

    public void InitializeStatements() {

        // Select needed content only

        String[] splitContent = FullContent.split("<div class=\"listedArticles\">");
        NeededContent = splitContent[0];

        // Fill ArrayLists with needed information

        Pattern p;
        Matcher m;
        p = Pattern.compile("alt=\"(.*?)\"");
        m = p.matcher(NeededContent);

        while(m.find())
        {
            CelebName.add();
        }

        p = Pattern.compile("img src=\"(.*?)\"");
        m = p.matcher(NeededContent);

        while(m.find())
        {
            CelebPhotoURL.add(m.group(1));
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CelebPhoto = findViewById(R.id.imageView);
        Grid = findViewById(R.id.Grid);

        try {

            DownloadWebContent PageWebContent = new DownloadWebContent();
            FullContent = PageWebContent.execute("http://www.posh24.se/kandisar").get();

        } catch (Exception e) {
            e.printStackTrace();
        }

        InitializeStatements();

        InitializeRound();

    }
}
