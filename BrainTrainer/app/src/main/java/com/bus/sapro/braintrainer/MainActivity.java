package com.bus.sapro.braintrainer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.params.CoreConnectionPNames;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    GridLayout Grid;
    Button B1;
    Button B2;
    Button B3;
    Button B4;
    Button Start;
    TextView Counter;
    TextView Equation;
    TextView Score;
    CountDownTimer Timer;
    int A;
    int B;
    int x,y = 0;
    int CorrectAnswer;

    public void StartFonction () {

        y++;
        Score.setText(x + "/" + y);
        Random X = new Random();

        A = X.nextInt(100);
        B = X.nextInt(100);
        CorrectAnswer = A + B;

        Equation.setText(A + " + " + B);

        int RandomCorrectID = X.nextInt(4);

        Button Bi = (Button) Grid.getChildAt(RandomCorrectID);
        Bi.setText(String.valueOf(CorrectAnswer));


        for (int i = 0; i<4; i++)
        {
            if(i != RandomCorrectID)
            {
                int F = X.nextInt(200);
                Bi = (Button) Grid.getChildAt(i);
                Bi.setText(String.valueOf(F));
            }
        }

    }

    public void Start (final View v) {

        final Button b = (Button) v;
        b.setEnabled(false);
        Reset();
        StartFonction();
        Timer = new CountDownTimer(30000, 1000) {
            public void onTick(long l) { // l represent how many millsecs left

                Counter.setText(String.valueOf(l/1000) + "s");

            }

            public void onFinish() {
                for(int k=0; k<Grid.getChildCount(); k++)
                {
                    Button b = (Button) Grid.getChildAt(k);
                    b.setEnabled(false);
                    b.setText("-");
                }
                Equation.setText("- + -");
                b.setText("Restart");
                b.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Your Score: " +x + "/" +y, Toast.LENGTH_LONG).show();
            }
        }.start();
    }

    public void Reset () {
        x = 0;
        y = 0;
        for(int k=0; k<Grid.getChildCount(); k++)
        {
            Button b = (Button) Grid.getChildAt(k);
            b.setEnabled(true);
        }
    }

    public void ResultCheck (View v) {

        Button b = (Button) v;
        if (b.getText().equals(String.valueOf(CorrectAnswer)))
        {
            Toast.makeText(this, "Good", Toast.LENGTH_LONG).show();
            x++;
            StartFonction();
        }
        else
        {
            Toast.makeText(this, "Not Good", Toast.LENGTH_LONG).show();
            StartFonction();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Grid = findViewById(R.id.Grid);
        B1 = findViewById(R.id.Button0);
        B2 = findViewById(R.id.Button1);
        B3 = findViewById(R.id.Button2);
        B4 = findViewById(R.id.Button3);
        Counter = findViewById(R.id.Counter);
        Equation = findViewById(R.id.Equation);
        Score = findViewById(R.id.Score);
        Start = findViewById(R.id.Start);

    }
}
