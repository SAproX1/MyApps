package com.bus.sapro.counter;

import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    SeekBar seekBar;
    EditText minutes;
    EditText seconds;
    Button Start;
    CountDownTimer Timer;
    int m;
    int s;

    public void StartCounter(View view) {

        minutes.setEnabled(false);
        seconds.setEnabled(false);
        seekBar.setEnabled(false);
        Start.setEnabled(false);
        minutes.getText();
        m = Integer.parseInt(minutes.getText().toString());
        seconds.getText();
        s = Integer.parseInt(seconds.getText().toString());

        Timer = new CountDownTimer(((m*60)+s)*1000, 1000) {
            public void onTick(long l) { // l represent how many millsecs left

                UpdateTimer(((int) l)/1000);

            }

            public void onFinish() {
                minutes.setEnabled(true);
                seconds.setEnabled(true);
                seekBar.setEnabled(true);
                Start.setEnabled(true);
            }
        }.start();

    }

    public void ResetCounter(View view) {

        Timer.cancel();
        seekBar.setProgress(0);
        minutes.setText("00");
        seconds.setText("00");

        minutes.setEnabled(true);
        seconds.setEnabled(true);
        seekBar.setEnabled(true);
        Start.setEnabled(true);
        
    }

    public void UpdateTimer (int inSeconds) {
        minutes.setText(String.valueOf(inSeconds/60));
        m = Integer.parseInt(minutes.getText().toString());
        seconds.setText(String.valueOf(inSeconds - (m*60)));
        s = Integer.parseInt(seconds.getText().toString());
        if(seconds.getText().toString().equals("0"))
        {
            seconds.setText("00");
        }
        if(minutes.getText().toString().equals("0"))
        {
            minutes.setText("00");
        }
        if(s <= 9)
        {
            seconds.setText("0" + s);
        }
        if(m <= 9)
        {
            minutes.setText("0" + m);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar = findViewById(R.id.seekBar);
        minutes = findViewById(R.id.Minutes);
        seconds = findViewById(R.id.Seconds);
        Start = findViewById(R.id.Start);

        seekBar.setMax(600);
        seekBar.setProgress(0);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                UpdateTimer(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
