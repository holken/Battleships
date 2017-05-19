package com.example.simon.battleships;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class joinGameActivity extends AppCompatActivity {
    EditText ipEnter;
    Button connectButton;
    SocketHandler socketHandler;
    Client client;
    //TextView clientStatusText;
    String code;
    private EditText firstIP, secondIP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        //clientStatusText = (TextView) findViewById(R.id.clientStatusText);
        //ipEnter = (EditText) findViewById(R.id.ipEnter);
        firstIP = (EditText) findViewById(R.id.firstIP);
        secondIP = (EditText) findViewById(R.id.secondIP);
        code = "";
        connectButton = (Button) findViewById(R.id.findHostButton);
        GameManager.setActivity(this);
        connectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO IP fix
                if (!firstIP.getText().equals("") && !secondIP.getText().equals("")) {

                    int SDK_INT = android.os.Build.VERSION.SDK_INT;
                    if (SDK_INT > 8) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        client = new Client(firstIP.getText().toString());//TODO fix
                        client.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ipEnter.getText().toString());

                    }

                }

                try {
                    Thread.sleep(500);
                } catch (Exception e) {

                }

                GameManager.send("con");

            }
        });
    }

    public void continueToNextActivity() {
        Intent intent = new Intent(this, PlaceBoatActivity.class);
        startActivity(intent);

    }
}

