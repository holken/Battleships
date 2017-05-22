package com.example.simon.battleships;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class howToPlayActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_how_to_play);
        Button attackButton = (Button) findViewById(R.id.attackButton);
        Button defendButton = (Button) findViewById(R.id.defendingButton);
        Button boatPlaceButton = (Button) findViewById(R.id.boatPlaceButton);
        Button salutingButton = (Button) findViewById(R.id.salutingButton);

        GameManager.setContext(this);

        //Calls the GameManager to set up game to match resources
        GameManager.initializeGame(getResources().getDisplayMetrics().widthPixels / 9);

        //Removed all OverPendingTransitions(), har fixat så att det är fade på alla transitions oavsett.
        final ConstraintLayout LAYOUT = (ConstraintLayout) findViewById(R.id.parent);

        defendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), createGameActivity.class);
                startActivity(intent);
            }
        });
        boatPlaceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GameManager.setTutorial(true);
                Intent intent = new Intent(v.getContext(), PlaceBoatActivity.class);
                startActivity(intent);
            }
        });
        attackButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                 GameManager.clearGrid();
                GameManager.placeShip(4,12);
                GameManager.setTutorial(true);
                Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                startActivity(intent);
            }
        });

        salutingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GameManager.setTutorial(true);
                Intent intent = new Intent(v.getContext(), SaluteActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        GameManager.setTutorial(false);
        startActivity(new Intent(howToPlayActivity.this, MainActivity.class));
<<<<<<< HEAD

=======
>>>>>>> origin/master
    }
}