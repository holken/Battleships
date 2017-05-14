package com.example.simon.battleships;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.os.Vibrator;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Button createButton = (Button) findViewById(R.id.createButton);
        Button joinButton = (Button) findViewById(R.id.joinButton);
        Button howButton = (Button) findViewById(R.id.howButton);


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
        howButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SaluteActivity.class);
                startActivity(intent);
            }
        });
    }
}
