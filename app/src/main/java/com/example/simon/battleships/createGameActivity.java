package com.example.simon.battleships;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class createGameActivity extends AppCompatActivity {

    Host host;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        host = new Host();
        host.execute();
    }
}
