package com.example.simon.battleships;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class PostGameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        GameManager.setContext(this);


        Button playAgainButton = (Button) findViewById(R.id.playAgainButton);
        Button menuButton = (Button) findViewById(R.id.menuButton);

        final ConstraintLayout LAYOUT = (ConstraintLayout) findViewById(R.id.parent);

        playAgainButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GameManager.initializeGame(getResources().getDisplayMetrics().widthPixels / 9);
                Intent intent = new Intent(v.getContext(), PlaceBoatActivity.class);
                startActivity(intent);
            }
        });
        menuButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
