package com.example.simon.battleships;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class PlayActivity extends Activity {
    //    private Timer currentTimer = new Timer();
    private Handler handler;
    private Vibrator VIBRATOR;
    private final long[] HIT_VIBRATION_PATTERN = {0, 80, 0};
    private final long[] NEAR_HIT_VIBRATION_PATTERN = {0, 40, 40}; //Delay, On-duration, Off-duration
    private final long[] MISS_VIBRATION_PATTERN = {0, 40, 120};
    private final int HIT_OR_NEAR_HIT_DELAY = 80;
    private final int MISS_DELAY = 160;
    private boolean isVibrating = false;
    private MediaPlayer mMediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play);
        handler = new Handler();

        /*if (!MainActivity.TEST) {
            new ConnectionManager(this);
        }*/
        final ConstraintLayout LAYOUT = (ConstraintLayout) findViewById(R.id.parent);
        VIBRATOR = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        LAYOUT.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //  COORDS_TEXT.setText("Touch at " + (int) (motionEvent.getY()/120) + ", " + (int) (motionEvent.getX()/120));
                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();
                if (!isVibrating) {
                    int delay = 0;
                    if (initiateVibration(x, y)) { //Typecast? //Fixa x och y
                        delay = HIT_OR_NEAR_HIT_DELAY;
                    } else delay = MISS_DELAY;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isVibrating = false;
                        }
                    }, delay);
                    isVibrating = true;
                }
                if (motionEvent.getAction() == android.view.MotionEvent.ACTION_UP) {
                    VIBRATOR.cancel();
                    isVibrating = false;
                    launchMissile();
                    if (GameManager.isHit(x, y) == 2) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                playSound("boom");
                            }
                        }, 3000);
                    } else {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                playSound("splash");
                            }
                        }, 3000);
                    }
                }

                return true;
            }
        });
    }

    private void launchMissile() {
        playSound("fire");
    }

    /**
     * Handles mMediaPlayer to make sounds easy to manage
     *
     * @param sound: the sound to be played. Choose from: "fire", "boom", "splash"
     */
    private void playSound(String sound) {
        if (mMediaPlayer != null) {
            //if(!mMediaPlayer.isPlaying()) {   //This enables sounds to overlap, but crashes the media player after a while. Probably something to do with MediaPlayer.create()
                mMediaPlayer.release();         // returning a new instance of media player each time, leaving the old one unreleased. Could be fixed with some effort but
                mMediaPlayer = null;            // once cooldowns are implemented this will not matter
            //}
        }
        switch (sound) {
            case "fire":
                mMediaPlayer = MediaPlayer.create(PlayActivity.this, R.raw.missile_launch);
                mMediaPlayer.start();
                break;
            case "boom":
                mMediaPlayer = MediaPlayer.create(PlayActivity.this, R.raw.explosion);
                mMediaPlayer.start();
                break;
            case "splash":
                mMediaPlayer = MediaPlayer.create(PlayActivity.this, R.raw.splash);
                mMediaPlayer.start();
                break;
            default:
                break;
        }
    }

    /**
     * Initiates vibration depending on hit or miss and returns true if hit
     *
     * @param x X-coordinate for touch
     * @param y Y-coordinate for touch
     * @return true if ship is hit, else returns false
     */
    private boolean initiateVibration(int x, int y) {
        switch (GameManager.isHit(x, y)) {
            case 2:
                VIBRATOR.vibrate(HIT_VIBRATION_PATTERN, 0);
                //COORDS_TEXT.setText("HIT!!!");
                return true;
            case 1:
                VIBRATOR.vibrate(NEAR_HIT_VIBRATION_PATTERN, 0);
                //COORDS_TEXT.setText("GETTING CLOSER");
                return true;
            case 0:
                VIBRATOR.vibrate(MISS_VIBRATION_PATTERN, 0);
                //COORDS_TEXT.setText("MISS....");
                return false;
            default:
                return false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        GameManager.clearGrid();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}