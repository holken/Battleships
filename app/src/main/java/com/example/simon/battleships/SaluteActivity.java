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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import pl.droidsonroids.gif.GifTextView;

//TODO: Implement connection, call tryToStart() when opponent starts saluting. Call cancelCountdown() if opponent stops saluting.

public class SaluteActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mProximity;
    private GifTextView gifSalute;
    private TextView saluteText;
    private TextView saluteTextTutorial;
    private Handler handler;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_salute);
        gifSalute = (GifTextView) findViewById(R.id.gifSalute);
        saluteText = (TextView) findViewById(R.id.saluteText);
        saluteTextTutorial = (TextView) findViewById(R.id.saluteTextTut);
        handler = new Handler();
        GameManager.setContext(this);
        Log.e("Entering Salute", "asd");

        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if(GameManager.isTutorial()){
            gifSalute.setVisibility(View.VISIBLE);
            saluteText.setVisibility(View.INVISIBLE);
            saluteTextTutorial.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float distance = event.values[0];

        if (distance < 5) {
            if(GameManager.isTutorial()) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SaluteActivity.this, howToPlayActivity.class));
                    }
                }, 4000);
            }
            GameManager.setIsSaluting(true);
        } else if (distance > 5) {
            GameManager.setIsSaluting(false);
        }
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        GameManager.setContext(this);
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}