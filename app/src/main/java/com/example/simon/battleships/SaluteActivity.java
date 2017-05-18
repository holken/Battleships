package com.example.simon.battleships;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.util.Timer;

//TODO: Implement connection, call tryToStart() when opponent starts saluting. Call cancelCountdown() if opponent stops saluting.

public class SaluteActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mProximity;
    private boolean isSaluting;
    private boolean opponentSaluting;
    private MediaPlayer mediaPlayer;
    private Handler mHandler;
    private Runnable rStartGame;
    private long saluteBegan;
    private boolean saluteCompleted;


    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_salute);

        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        isSaluting = false;
        opponentSaluting = true;  //temporarily true, change this to false!!!
        saluteCompleted = false;
        mediaPlayer = MediaPlayer.create(this, R.raw.countdown);
        mHandler = new Handler();
        //Creates a runnable that starts the game by switching to PlayActivity
        rStartGame = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), PlayActivity.class); //Change to PlayActivity
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }

        };

    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float distance = event.values[0];
        if (distance < 5) {
            saluteBegan = System.currentTimeMillis();
            if(GameManager.hasClientSocket()){
                GameManager.send("slt");
            }
            //TODO: Tell opponent that you are saluting
        } else if (distance > 5) {
            GameManager.setSaluting(false);
        }
    }

    /**
     *  Starts the game if both players are saluting
     *  @return true if successful
     */
    private boolean tryToStart() {
        if (isSaluting && opponentSaluting) {
            mediaPlayer.start();

            return true;
        }
        return false;
    }

    private void cancelCountdown() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mHandler.removeCallbacks(rStartGame);
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}