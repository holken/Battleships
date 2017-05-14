package com.example.simon.battleships;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class PlaceBoatActivity extends AppCompatActivity {
    private int[][] grid =  new int[16][9];
    private int GRID_PIXEL_WIDTH = 120;
    private ImageView imageView;
    int currentX, currentY;

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_boat_placing);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        GRID_PIXEL_WIDTH = width / 9;
        imageView = (ImageView) findViewById(R.id.boatImage);
        //imageView set width/height to GRID_PIXEL_WIDTH

        final ConstraintLayout LAYOUT = (ConstraintLayout) findViewById(R.id.parent);
        LAYOUT.setOnTouchListener(new View.OnTouchListener() {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            placeShip(currentY, currentX);
                            Intent intent = new Intent(getBaseContext(), PlayActivity.class);
                            startActivity(intent);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.cancel();
                            break;
                    }
                }
            };


            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    currentY = (int) motionEvent.getY();
                    currentX = (int) motionEvent.getX();
                    placeShipImage(currentY, currentX);

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
                return true;
            }
        });
    }

    /**
     * Places the ship's image on specified x and y coordinates
     * @param x X-coordinate for ship
     * @param y Y-coordinate for ship
     */
    public void placeShipImage(int y, int x) {
        int gridY = y/GRID_PIXEL_WIDTH;
        int gridX = x/GRID_PIXEL_WIDTH;
        imageView.setX(gridX * GRID_PIXEL_WIDTH);
        imageView.setY(gridY * GRID_PIXEL_WIDTH);
        imageView.setVisibility(View.VISIBLE);
    }

    /**
     * Places the ship on specified x and y coordinates and sets adjacent tiles to near hits
     * @param x X-coordinate for ship
     * @param y Y-coordinate for ship
     */
    public void placeShip(int y, int x) {
        for(int i = y-1; i <= y+1; i++) {
            for(int j = x-1; j <= x+1; j++) {
                if(!(i < 0 || j < 0 || i > 15 || j > 8)) {
                    grid[i][j] = 1;
                }
            }
        }
        grid[y][x] = 2;
    }
}
