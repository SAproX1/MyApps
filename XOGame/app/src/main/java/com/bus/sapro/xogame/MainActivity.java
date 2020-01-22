package com.bus.sapro.xogame;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    MediaPlayer StarBucks;
    MediaPlayer Jazz;
    ImageView coffee1;
    ImageView coffee2;
    ImageView armP2;
    ImageView armP1;
    ImageView reset;
    TextView player1Score;
    TextView player2Score;
    ImageView BackgroundTable;
    ImageView PlayAgain;
    GridLayout Grid;
    int i=0;
    int z=0; // z = win possibilities counter 0 - 12
    int o=0;
    int p=0;
    int m=0;
    int t=0;
    int w=0;
    int p1s = 0;
    int p2s = 0;
    int length1 = 0;
    int length2 = 0;
    int MagnetTag = 0;
    int [] gameState = {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2};
    int [][] winPositions = {{0,1,2,3,4},{5,6,7,8,9},{10,11,12,13,14},{15,16,17,18,19},{20,21,22,23,24},{0,5,10,15,20},{1,6,11,16,21},{2,7,12,17,22},{3,8,13,18,23},{4,9,14,19,24},{0,6,12,18,24},{4,8,12,16,20}};


    // Win check algorithm

    public boolean WinCheck(ImageView V) {

        for (int [] winPos : winPositions)
        {
            if (gameState[winPos[0]] == gameState[winPos[1]] && gameState[winPos[1]] == gameState[winPos[2]] && gameState[winPos[2]] == gameState[winPos[3]] && gameState[winPos[3]] == gameState[winPos[4]] &&  gameState[winPos[0]] != 2)
            {
                if(i==0)
                {
                    w=1;
                }
                else
                {
                    w=2;
                }
                ENorDSClickable(Grid, "Disable"); //disable Grids of GridLayout
                playMp3("applause");
                return true;
            }
        }
        return false;
    }

    // Animation when a Player wins

    public void WinAnim(View v) {

        if(w==1)
        {
            Toast.makeText(this, "Yellow Player Wins!", Toast.LENGTH_LONG).show();
            p2s++;
            player2Score.animate().scaleXBy(20f).scaleYBy(20f).alpha(0).setDuration(1000);
            new CountDownTimer(1000, 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    player2Score.animate().scaleXBy(-20f).scaleYBy(-20f).alpha(0.3f).setDuration(1000);
                    player2Score.setText(String.valueOf(p2s));
                }
            }.start();
        }
        else
        {
            Toast.makeText(this, "Red Player Wins!", Toast.LENGTH_LONG).show();
            p1s++;
            player1Score.animate().scaleXBy(20f).scaleYBy(20f).alpha(0).setDuration(1000);
            new CountDownTimer(1000, 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    player1Score.animate().scaleXBy(-20f).scaleYBy(-20f).alpha(0.3f).setDuration(1000);
                    player1Score.setText(String.valueOf(p1s));
                }
            }.start();
        }
    }
    // Draw check algorithm

    public void DrawCheck(ImageView V) {

        for (int [] winPos : winPositions)
        {
            if ((gameState[winPos[0]] != gameState[winPos[1]] && gameState[winPos[0]] != 2 && gameState[winPos[1]] != 2) ||
                (gameState[winPos[0]] != gameState[winPos[2]] && gameState[winPos[0]] != 2 && gameState[winPos[2]] != 2) ||
                (gameState[winPos[0]] != gameState[winPos[3]] && gameState[winPos[0]] != 2 && gameState[winPos[3]] != 2) ||
                (gameState[winPos[0]] != gameState[winPos[4]] && gameState[winPos[0]] != 2 && gameState[winPos[4]] != 2) ||
                (gameState[winPos[1]] != gameState[winPos[2]] && gameState[winPos[1]] != 2 && gameState[winPos[2]] != 2) ||
                (gameState[winPos[1]] != gameState[winPos[3]] && gameState[winPos[1]] != 2 && gameState[winPos[3]] != 2) ||
                (gameState[winPos[1]] != gameState[winPos[4]] && gameState[winPos[1]] != 2 && gameState[winPos[4]] != 2) ||
                (gameState[winPos[2]] != gameState[winPos[3]] && gameState[winPos[2]] != 2 && gameState[winPos[3]] != 2) ||
                (gameState[winPos[2]] != gameState[winPos[4]] && gameState[winPos[2]] != 2 && gameState[winPos[4]] != 2) ||
                (gameState[winPos[3]] != gameState[winPos[4]] && gameState[winPos[3]] != 2 && gameState[winPos[4]] != 2))
            {
                z++;
                if (z==12)
                {
                    Toast.makeText(this, "It's a Draw, Press Reset!", Toast.LENGTH_LONG).show();
                    ENorDSClickable(Grid, "Disable"); //disable Grids of GridLayout
                    playMp3("herewego");
                }
            }
        }
        z=0;
    }

    // Disable or enable imageviews on GridLayout

    public void ENorDSClickable(GridLayout layout, String S) {

        if (S == "Disable")
        {
            for(int k=0; k<Grid.getChildCount(); k++)
            {
                ImageView child = (ImageView) Grid.getChildAt(k);
                child.setEnabled(false);
            }
        }
        else if (S == "Enable")
        {
            for(int k=0; k<Grid.getChildCount(); k++)
            {
                ImageView child = (ImageView) Grid.getChildAt(k);
                child.setEnabled(true);
            }
        }

        // I still don't know how to make an Untouchable Touchable again that's why I can't use this function
/*
        ArrayList<View> layoutTouchables = layout.getTouchables();

        if (S == "Disable")
        {
            for (View v : layoutTouchables)
            {
                if(v instanceof ImageView)
                    ((ImageView)v).setEnabled(false);
            }
        }
        else if (S == "Enable")
        {
            for (View v : layoutTouchables)
            {
                if(v instanceof ImageView)
                    ((ImageView)v).setEnabled(true);
            }
        }
        */
    }


    // Audio Player Functions

    private void playMp3(String nameOfFile) {
        MediaPlayer mPlayer = MediaPlayer.create(this, getResources().getIdentifier(nameOfFile, "raw", getPackageName()));
        mPlayer.start();
    }

    private void stopMp3(String nameOfFile) {
        MediaPlayer mPlayer = MediaPlayer.create(this, getResources().getIdentifier(nameOfFile, "raw", getPackageName()));
        mPlayer.stop();
    }

    // Function when player click  a Grid Layout playing area

    public void PutMagnet(View V) {

        ImageView V1 = (ImageView) V;

        if(i==0)
        {
            V1.setTranslationY(1000);
            V1.setImageResource(R.drawable.red);
            V1.animate().translationYBy(-1000).rotationXBy(-360).setDuration(500);
            playMp3("magnetputsound");
            V1.setEnabled(false); // disable the area to make it unclickable next time
            i=1;
        }
        else
        {
            V1.setTranslationY(-1000);
            V1.setImageResource(R.drawable.yellow);
            V1.animate().translationYBy(1000).rotationXBy(360).setDuration(500);
            playMp3("magnetputsound");
            V1.setEnabled(false); // disable the area to make it unclickable next time
            i=0;
        }
        MagnetTag = Integer.parseInt(V.getTag().toString());
        gameState[MagnetTag] = i; // i = 0 Red ; = 1 Yellow ; = 2 empty
        WinCheck(V1);
        if (WinCheck(V1))
        {
            PlayAgain.setVisibility(View.VISIBLE);
            WinAnim(V1);
        }
        DrawCheck(V1);


        // SOON FEATURE - ADD CUSTOM PLAY TIMER HARD MODE - ADD PC MODE - ADD ONLINE MODE

    }

    // PlayAgain function

    public void playAgain(View V) {

        ENorDSClickable(Grid, "Enable");
        new CountDownTimer(400, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                for(int k=0; k<Grid.getChildCount(); k++)
                {
                    ImageView child = (ImageView) Grid.getChildAt(k);
                    child.setImageDrawable(null);
                }
            }
        }.start();
        i=0;
        p=0;
        m=0;
        MagnetTag = 0;
        PlayAgain.animate().alpha(0).setDuration(200);
        new CountDownTimer(200, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                PlayAgain.setVisibility(View.INVISIBLE);
                PlayAgain.animate().alpha(1).setDuration(0);
                //playMp3("letsdothis");
            }
        }.start();

        armP2.animate().translationYBy(1500).rotation(70).setDuration(1000);
        new CountDownTimer(200, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                armP2.animate().translationXBy(1100).rotation(-70).setDuration(600);
                new CountDownTimer(200, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        armP2.animate().translationYBy(-1500).translationXBy(-1100).setDuration(200);
                    }
                }.start();
            }
        }.start();

        armP1.animate().translationYBy(-1500).rotation(70).setDuration(1000);
        new CountDownTimer(200, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                armP1.animate().translationXBy(-1100).rotation(-70).setDuration(600);
                new CountDownTimer(200, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        armP1.animate().translationYBy(1500).translationXBy(1100).setDuration(200);
                    }
                }.start();
            }
        }.start();

        playMp3("cointake");

        // gameState variable reset
        for(int r=0; r<gameState.length; r++)
        {
            gameState[r] = 2;
        }

    }

    public void Reset(View v) {

        reset.setEnabled(false);
        playMp3("buttonpush");
        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                reset.setEnabled(true);
            }
        }.start();
        //check if table is clean
        if (Arrays.equals(gameState, new int[] {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2}))
        {
            Toast.makeText(this, "Table is Clean, Play or Geraarahere", Toast.LENGTH_LONG).show();
            playMp3("bruh");
            return;
        }

        ENorDSClickable(Grid, "Enable");
        new CountDownTimer(400, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                for(int k=0; k<Grid.getChildCount(); k++)
                {
                    ImageView child = (ImageView) Grid.getChildAt(k);
                    child.setImageDrawable(null);
                }
            }
        }.start();
        i=0;
        p=0;
        m=0;
        MagnetTag = 0;
        PlayAgain.animate().alpha(0).setDuration(200);
        new CountDownTimer(200, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                PlayAgain.setVisibility(View.INVISIBLE);
                PlayAgain.animate().alpha(1).setDuration(0);
                //playMp3("letsdothis");
            }
        }.start();

        armP2.animate().translationYBy(1500).rotation(70).setDuration(1000);
        new CountDownTimer(200, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                armP2.animate().translationXBy(1100).rotation(-70).setDuration(600);
                new CountDownTimer(200, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        armP2.animate().translationYBy(-1500).translationXBy(-1100).setDuration(200);
                    }
                }.start();
            }
        }.start();

        armP1.animate().translationYBy(-1500).rotation(70).setDuration(1000);
        new CountDownTimer(200, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                armP1.animate().translationXBy(-1100).rotation(-70).setDuration(600);
                new CountDownTimer(200, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        armP1.animate().translationYBy(1500).translationXBy(1100).setDuration(200);
                    }
                }.start();
            }
        }.start();

        playMp3("cointake");

        // gameState variable reset
        for(int r=0; r<gameState.length; r++)
        {
            gameState[r] = 2;
        }

    }

    // Functions when clicking at coffee icons, it includes the fact of disabling multiple clicks at the same time

    public void TakeCoffee(View V) {

        playMp3("cofesound");
        coffee1.setEnabled(false);
        //animate taking own coffee
        coffee1.animate().translationYBy(1000).scaleXBy(20).scaleYBy(20).rotationXBy(60).setDuration(1000);
        player1Score.animate().alpha(0).setDuration(0);
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                coffee1.animate().translationYBy(-1000).scaleXBy(-20).scaleYBy(-20).rotationXBy(-60).setDuration(1000);
                player1Score.animate().alpha(0.3f).setDuration(2000);
            }
        }.start();


        new CountDownTimer(10000, 1000) { // 10000 = 10 sec

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                coffee1.setEnabled(true);
            }
        }.start();
    }

    public void NoTakeCoffee(View V) {

        //animate taking opponent coffee
        coffee2.animate().translationYBy(4000).scaleXBy(20).scaleYBy(20).rotationXBy(60).setDuration(1000);
        player2Score.animate().alpha(0).setDuration(0);
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                coffee2.animate().translationYBy(-4000).scaleXBy(-20).scaleYBy(-20).rotationXBy(-60).setDuration(1000);
                player2Score.animate().alpha(0.3f).setDuration(2000);
            }
        }.start();

        if(o==0)
        {
            playMp3("nooo");
            coffee2.setEnabled(false);
            o=1;
        }
        else
        {
            playMp3("gerarahere");
            coffee2.setEnabled(false);
        }

        new CountDownTimer(10000, 1000) { // 10000 = 10 sec

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                coffee2.setEnabled(true);
            }
        }.start();

    }

    // function ON/OFF (Pause/Resume) of people talking background sound

    public void PeopleTalking(View V) {
        ImageView V2 = (ImageView) V;
        if(p==0)
        {
            p=1;
            StarBucks.pause();
            length1 = StarBucks.getCurrentPosition();
            V2.setImageResource(R.drawable.talkingoff);
        }
        else
        {
            p=0;
            StarBucks.seekTo(length1);
            StarBucks.start();
            V2.setImageResource(R.drawable.talkingon);
        }
    }

    // function ON/OFF (Pause/Resume) of Jazz music background sound

    public void MusicPlaying(View V) {
        ImageView V3 = (ImageView) V;
            if(m==0)
            {
                m = 1;
                Jazz.pause();
                length2 = Jazz.getCurrentPosition();
                V3.setImageResource(R.drawable.musiciconoff);
            }
            else
            {
                m = 0;
                Jazz.seekTo(length2);
                Jazz.start();
                V3.setImageResource(R.drawable.musiciconon);
            }
    }

    // Choose the type of playing table // I don't know why changing table ImageResource make some sounds stop

    public void TableSwitch(View V) {

        if(t==0)
        {
            BackgroundTable.setImageResource(R.drawable.wood2);
            t=1;
            return;
        }
        else if(t==1)
        {
            BackgroundTable.setImageResource(R.drawable.wood);
            t=2;
            return;
        }
        BackgroundTable.setImageResource(R.drawable.wood3);
        t=0;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BackgroundTable = findViewById(R.id.BackgroundTable);
        Grid = findViewById(R.id.Grid);
        coffee1 = findViewById(R.id.coffee1);
        coffee2 = findViewById(R.id.coffee2);
        armP2 = findViewById(R.id.armP2);
        armP1 = findViewById(R.id.armP1);
        reset = findViewById(R.id.resetIcon);
        player1Score = findViewById(R.id.scorePlayer1);
        player2Score = findViewById(R.id.scorePlayer2);
        PlayAgain = findViewById(R.id.playAgain);
        StarBucks = MediaPlayer.create(this, R.raw.starbucks);
        Jazz = MediaPlayer.create(this, R.raw.jazz);
        Jazz.start();
        StarBucks.start();
        Jazz.setVolume(0.2f,0.2f);
        Toast.makeText(this, "Game is ready", Toast.LENGTH_LONG).show();
    }
}
