package com.example.simon.battleships;

import android.content.Context;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.os.Vibrator;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    int counter = 0;
    int short_dot = 200;
    int short_gap = 200;
    int long_dot = 1000;
    int long_gap = 1000;
    Timer currentTimer = new Timer();

    boolean longVibration = true;
    boolean shortVibration = false;
    final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.parent);
        final  TextView coordsText = (TextView) findViewById(R.id.Coordinates);
        final Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        layout.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent ev) {
                counter++;

                coordsText.setText("Touch at " + ev.getX() + ", " + ev.getY());

                if (ev.getX() >= 600 && ev.getX() <= 800 && ev.getY() <= 1200 && ev.getY() >= 1000) {
                    if (!shortVibration && !longVibration){
                        currentTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                shortVibration = false;
                            }
                        }, 1000);
                        long[] pattern = {0, short_dot, short_gap};
                        vib.vibrate(pattern, 0);
                        shortVibration = true;
                    }


                } else {
                    if (!shortVibration && !longVibration){
                        currentTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                longVibration = false;
                            }
                        }, 2000);
                        long[] pattern = {0, long_dot, short_gap};
                        vib.vibrate(pattern, 0);
                        longVibration = true;
                    }

                }

                if (ev.getAction() == android.view.MotionEvent.ACTION_UP) {
                    vib.cancel();
                    //currentTimer.cancel();
                    shortVibration = false;
                    longVibration = false;
                }

                return true;
            }


        });
    }
}
