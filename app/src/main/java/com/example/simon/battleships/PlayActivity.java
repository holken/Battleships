package com.example.simon.battleships;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class PlayActivity extends AppCompatActivity {

    private Timer currentTimer = new Timer();
    private final Handler HANDLER = new Handler();
    private Vibrator VIBRATOR;
    private final int GRID_PIXEL_WIDTH = 120; //Change to dynamic
    private final long[] HIT_VIBRATION_PATTERN = {0, 80, 0};
    private final long[] NEAR_HIT_VIBRATION_PATTERN = {0, 40, 40}; //Delay, On-duration, Off-duration
    private final long[] MISS_VIBRATION_PATTERN = {0, 40, 120};
    private final int HIT_OR_NEAR_HIT_DELAY = 80;
    private final int MISS_DELAY = 160;
    private boolean isVibrating = false;
    private int[][] grid =  new int[16][9];
    private final int HIT = 2;
    private final int NEAR_HIT = 1;

    private TextView COORDS_TEXT;

    private SocketHandler socketHandler;
    TextView clientView;
    TextView serverView;
    TextView localClientView;
    ClientWrite sender;
    ClientRead receiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play);

        clientView = (TextView) findViewById(R.id.clientSocket);
        serverView = (TextView) findViewById(R.id.serverSocket);
        localClientView = (TextView) findViewById(R.id.localClientSocket);
        socketHandler = new SocketHandler();


        receiver = new ClientRead (SocketHandler.getClientSocket(), 4001, this);
        sender = new ClientWrite(SocketHandler.getClientSocket(), 4001, this);


        receiver.start();
        sender.start();

            /*
        try {
            Thread.sleep(500);
        } catch (Exception e){

        }
        */
/*
        if (bothConnected && coordsNotSent){

        }*/

        /*while (!haveReceviedCoords){
            sender.sendRandomCoords();
        }*/

        final ConstraintLayout LAYOUT = (ConstraintLayout) findViewById(R.id.parent);
        COORDS_TEXT = (TextView) findViewById(R.id.Coordinates);
        VIBRATOR = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        LAYOUT.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //  COORDS_TEXT.setText("Touch at " + (int) (motionEvent.getY()/120) + ", " + (int) (motionEvent.getX()/120));
                if(!isVibrating) {
                    int delay = 0;
                    if(initiateVibration((int) motionEvent.getY(), (int) motionEvent.getX())) { //Typecast?
                        delay = HIT_OR_NEAR_HIT_DELAY;
                    } else {
                        delay = MISS_DELAY;
                    }
                    currentTimer.schedule(new TimerTask() {
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
                }

                return true;
            }
        });
    }

    /**
     * Initiates vibration depending on hit or miss and returns true if hit
     * @param x X-coordinate for touch
     * @param y Y-coordinate for touch
     * @return true if ship is hit, else returns false
     */
    private boolean initiateVibration(int x, int y) {
        switch(grid[x / GRID_PIXEL_WIDTH][y / GRID_PIXEL_WIDTH]) {
            case 2: VIBRATOR.vibrate(HIT_VIBRATION_PATTERN, 0);
                COORDS_TEXT.setText("HIT!!!");
                return true;
            case 1: VIBRATOR.vibrate(NEAR_HIT_VIBRATION_PATTERN, 0);
                COORDS_TEXT.setText("GETTING CLOSER");
                return true;
            case 0: VIBRATOR.vibrate(MISS_VIBRATION_PATTERN, 0);
                COORDS_TEXT.setText("MISS....");
                return false;
            default: return false;
        }
    }

    /**
     * Places the ship on specified x and y coordinates and sets adjacent tiles to near hits
     * @param x X-coordinate for ship
     * @param y Y-coordinate for ship
     */
    public void placeShip(int x, int y) {
        for(int i = x-1; i <= x+1; i++) {
            for(int j = y-1; j <= y+1; j++) {
                if(!(i < 0 || j < 0 || i > 15 || j > 8)) {
                    grid[i][j] = 1;
                }
            }
        }
        grid[x][y] = 2;
    }
}
