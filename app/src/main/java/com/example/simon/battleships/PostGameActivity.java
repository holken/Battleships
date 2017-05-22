package com.example.simon.battleships;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class PostGameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_post_game);

        GameManager.setContext(this);

        ((TextView) findViewById(R.id.resultText)).setText(GameManager.result());
        ((TextView) findViewById(R.id.myScoreText)).setText("YOU: " + GameManager.getMyScore());
        ((TextView) findViewById(R.id.opponentScoreText)).setText("OPPONENT: " + GameManager.getOpponentScore());


        Button playAgainButton = (Button) findViewById(R.id.playAgainButton);
        Button menuButton = (Button) findViewById(R.id.menuButton);

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
