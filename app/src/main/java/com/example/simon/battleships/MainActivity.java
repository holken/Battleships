package com.example.simon.battleships;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


public class MainActivity extends Activity {
    public static boolean TEST = false; //TESTING PURPOSES
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Button createButton = (Button) findViewById(R.id.attackButton);
        Button joinButton = (Button) findViewById(R.id.defendingButton);
        Button tutorialButton = (Button) findViewById(R.id.boatPlaceButton);
        //Button testButton = (Button) findViewById(R.id.testButton);
        GameManager.setContext(this);

        //Calls the GameManager to set up game to match resources
        GameManager.initializeGame(getResources().getDisplayMetrics().widthPixels / 9);

        final ConstraintLayout LAYOUT = (ConstraintLayout) findViewById(R.id.parent);

        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), createGameActivity.class);
                startActivity(intent);
            }
        });
        joinButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), joinGameActivity.class);
                startActivity(intent);
            }
        });
        tutorialButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Jag vill gå till PlayActivity utan att behöva gå igenom allt skit först. Dock buggar denna, vet inte varför
                GameManager.clearGrid();
                GameManager.placeShip(4,12);
                GameManager.setTutorial(true);
                Intent intent = new Intent(getApplicationContext(), howToPlayActivity.class);
                startActivity(intent);
            }
        });
    }
}