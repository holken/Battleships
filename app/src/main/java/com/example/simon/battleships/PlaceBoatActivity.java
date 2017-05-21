package com.example.simon.battleships;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PlaceBoatActivity extends Activity {
    private ImageView imageView;
    private ProgressBar spinner;
    private Handler handler;
    private Button upperReadyButton;
    private Button lowerReadyButton;
    private TextView textView;
    private int gridX;
    private int gridY;

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_boat_placing);
        imageView = (ImageView) findViewById(R.id.boatImage);
        textView = (TextView) findViewById(R.id.placeBoatText);
        spinner = (ProgressBar) findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
        handler = new Handler();
        upperReadyButton = (Button) findViewById(R.id.upperReadyButton);
        lowerReadyButton = (Button) findViewById(R.id.lowerReadyButton);
        hideButtons();
        GameManager.setContext(this);

        //imageView set width/height to GRID_PIXEL_WIDTH
        final ConstraintLayout LAYOUT = (ConstraintLayout) findViewById(R.id.parent);
        LAYOUT.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    textView.setVisibility(View.INVISIBLE);
                    placeShipImage((int) motionEvent.getX(), (int) motionEvent.getY());
                    showReadyButton((int) motionEvent.getY());
                }
                return true;
            }
        });
    }

    private void showReadyButton(int y) {
        hideButtons();
        if (y > 1920 / 2) {
            upperReadyButton.setVisibility(View.VISIBLE);
            upperReadyButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    readyUp();
                }
            });
        } else {
            lowerReadyButton.setVisibility(View.VISIBLE);
            lowerReadyButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    readyUp();
                }
            });
        }
    }

    private void readyUp() {
        if (GameManager.hasClientSocket()){
            GameManager.send("plc"+ gridX + "|" + gridY);
            GameManager.setReady(true);
            Log.e("positions", "Sent - position x: " + gridX + ", position y: " + gridY);
        } else {
            Log.e("ReadyUp fail", "Has no client socket.");
        }

        hideButtons();
        spinner.setVisibility(View.VISIBLE);
    }

    private void hideButtons() {
        upperReadyButton.setVisibility(View.GONE);
        upperReadyButton.setOnClickListener(null);
        lowerReadyButton.setVisibility(View.GONE);
        lowerReadyButton.setOnClickListener(null);
    }

    /**
     * Places the ship's image on specified x and y coordinates
     *
     * @param x X-coordinate for ship
     * @param y Y-coordinate for ship
     */
    public void placeShipImage(int x, int y) {
        int gridPixelWidth = GameManager.getGridPixelWidth();
        gridX = x / gridPixelWidth;
        gridY = y / gridPixelWidth;
        imageView.setX(gridX * gridPixelWidth);
        imageView.setY(gridY * gridPixelWidth);
        imageView.setVisibility(View.VISIBLE);
    }
}
