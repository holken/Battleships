package com.example.simon.battleships;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    private ProgressBar reloadProgressBar;
    private TextView reloadingText;

    //Cooldown
    private boolean fireOnCooldown = false;
    private boolean dodgeOnCooldown = false;
    private final long FIRE_COOLDOWN = 3000;
    private final long DODGE_COOLDOWN = 5000;

    //Shake Detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    // Tutorial
    private TextView tutorialStep1;
    private TextView tutorialStep2;
    private ImageView holdTouch;
    private TextView warningText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play);
        tutorialStep1 = (TextView) findViewById(R.id.text1);
        tutorialStep2 = (TextView) findViewById(R.id.text2);
        holdTouch = (ImageView) findViewById(R.id.holdtouch);
        warningText = (TextView) findViewById(R.id.warningtext);

        handler = new Handler();


        final ConstraintLayout LAYOUT = (ConstraintLayout) findViewById(R.id.parent);
        VIBRATOR = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        reloadProgressBar = (ProgressBar) findViewById(R.id.reloadingProgress);
        reloadingText = (TextView) findViewById(R.id.reloadingText);
        reloadProgressBar.setVisibility(View.GONE);
        reloadingText.setVisibility(View.GONE);

        if(GameManager.isTutorial()){
            holdTouch.setVisibility(View.VISIBLE);

        }
        GameManager.setContext(this);

        LAYOUT.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //  COORDS_TEXT.setText("Touch at " + (int) (motionEvent.getY()/120) + ", " + (int) (motionEvent.getX()/120));
                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();

                //Tutorial
                warningText.setVisibility(View.INVISIBLE);
                holdTouch.setVisibility(View.INVISIBLE);
                tutorialStep2.setVisibility(View.INVISIBLE);
                tutorialStep2.setVisibility(View.INVISIBLE);
                if(GameManager.isTutorial()){  ///
                    LAYOUT.setBackgroundResource(R.drawable.bluewatertexturelearn);
                    tutorialStep1.setVisibility(View.VISIBLE);
                    tutorialStep1.setText("Can you feel the vibrations? Drag your finger to the yellow area" );
                    if(GameManager.isHit(x, y) == 1){  ///
                        tutorialStep1.setVisibility(View.INVISIBLE);
                        tutorialStep2.setVisibility(View.VISIBLE);
                        tutorialStep2.setText("The vibration pattern changed! You are close to the target. " +
                                "Drag your finger to the red area" );
                    }
                    if(GameManager.isHit(x, y) == 2){  ///
                        tutorialStep1.setVisibility(View.INVISIBLE);
                        tutorialStep2.setVisibility(View.VISIBLE);
                        tutorialStep2.setText("The target is right under your finger. Release your finger to destroy your target" );
                    }

                }

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
                    launchMissile(x, y);


                    if(GameManager.isTutorial() && GameManager.isHit(x, y) != 1){
                        reloadProgressBar.setVisibility(View.INVISIBLE);
                        reloadingText.setVisibility(View.INVISIBLE);
                        warningText.setVisibility(View.VISIBLE);
                            tutorialStep1.setVisibility(View.INVISIBLE);
                            LAYOUT.setBackgroundColor(Color.BLACK);

                        if (GameManager.isHit(x,y) == 2 ) {
                            tutorialStep2.setVisibility(View.INVISIBLE);
                            warningText.setText("Note: The rings are only for tutorial");
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    warningText.setText("Good job!");
                                    tutorialStep2.setVisibility(View.INVISIBLE);
                                }
                            }, 4000);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(PlayActivity.this, MainActivity.class);
                                    PlayActivity.this.startActivity(intent);
                                }
                            }, 6000);

                        }
                    }


                }

                return true;
            }
        });

        //ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                if(!dodgeOnCooldown) {
                    dodgeOnCooldown = true;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dodgeOnCooldown = false;
                        }
                    }, DODGE_COOLDOWN);
                    GameManager.dodge();
                }
            }
        });
    }

    /**
     * Launches missile
     *
     * @param x X-coordinate for launch
     * @param y Y-coordinate for launch
     */
    private void launchMissile(int x, int y) {
        if (!fireOnCooldown) {
            fireOnCooldown = true;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fireOnCooldown = false;
                }
            }, FIRE_COOLDOWN);
            animateCooldown();
            int gridPixelWidth = GameManager.getGridPixelWidth();
            GameManager.playSound("fire");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    GameManager.playSound("splash");
                }
            }, 3000);
            if (GameManager.isHit(x, y) == 2) {
                GameManager.send("hit");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        GameManager.playSound("boom");
                    }
                }, 3000);
            } else {
                GameManager.send("mis");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        GameManager.playSound("splash");
                    }
                }, 3000);
            }
        }
    }

    private void animateCooldown() {
        reloadProgressBar.setVisibility(View.VISIBLE);
        reloadingText.setVisibility(View.VISIBLE);
        reloadProgressBar.setProgress(0);
        /** CountDownTimer runs for FIRE_COOLDOWN milliseconds with a tick every 100 milliseconds */
        CountDownTimer cdt = new CountDownTimer(FIRE_COOLDOWN, 10) {
            public void onTick(long millisUntilFinished) {
                reloadProgressBar.setProgress((int) (FIRE_COOLDOWN - millisUntilFinished));
            }

            public void onFinish() {
                reloadProgressBar.setVisibility(View.GONE);
                reloadingText.setVisibility(View.GONE);
            }
        }.start();
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
    public void onResume() {
        super.onResume();
        GameManager.setContext(this);
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onStop() {
        super.onStop();
        GameManager.clearGrid();
        GameManager.setTutorial(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mShakeDetector);
    }

    //Disables back button
    @Override
    public void onBackPressed() {}
}