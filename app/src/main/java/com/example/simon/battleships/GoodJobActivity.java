package com.example.simon.battleships;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


public class GoodJobActivity extends Activity {

    private Handler handler;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        handler = new Handler();
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_good_job);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(GoodJobActivity.this, howToPlayActivity.class));
            }
        }, 3000);

    }
}
