package com.example.simon.battleships;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class PlaceBoatActivity extends Activity {
    private ImageView imageView;
    private AlertDialog dialog;

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_boat_placing);

        imageView = (ImageView) findViewById(R.id.boatImage);
        //imageView set width/height to GRID_PIXEL_WIDTH
        final Context context = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(context, SaluteActivity.class);
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
        dialog = builder.create();

        final ConstraintLayout LAYOUT = (ConstraintLayout) findViewById(R.id.parent);
        LAYOUT.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    placeShipImage((int) motionEvent.getX(), (int) motionEvent.getY());
                    dialog.show();
                }
                return true;
            }
        });
    }

    /**
     * Places the ship's image on specified x and y coordinates
     *
     * @param x X-coordinate for ship
     * @param y Y-coordinate for ship
     */
    public void placeShipImage(int x, int y) {
        int gridPixelWidth = GameManager.getGridPixelWidth();
        int gridX = x / gridPixelWidth;
        int gridY = y / gridPixelWidth;
        GameManager.placeShip(gridX, gridY);
        imageView.setX(gridX * gridPixelWidth);
        imageView.setY(gridY * gridPixelWidth);
        imageView.setVisibility(View.VISIBLE);
    }

}
