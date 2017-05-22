package com.example.simon.battleships;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


public class GoodJobActivity extends Activity {

    private Handler handler;
    private TextView Text;
    private TextView Tip;
   private TextView Tip2;
    @Override
    public final void onCreate(Bundle savedInstanceState) {
        handler = new Handler();
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_good_job);
        Tip = (TextView) findViewById(R.id.textView3);
        Text = (TextView) findViewById(R.id.tipText2);
        Tip2 = (TextView) findViewById(R.id.tipText);

        if(GameManager.isTutorial()) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Text.setVisibility(View.VISIBLE);
                    startActivity(new Intent(GoodJobActivity.this, howToPlayActivity.class));
                }
            }, 3000);
        } else {

            Text.setVisibility(View.INVISIBLE);
            Tip.setVisibility(View.VISIBLE);
            Tip2.setVisibility(View.VISIBLE);
        }
            }
        }



