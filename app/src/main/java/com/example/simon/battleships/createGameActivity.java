package com.example.simon.battleships;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class createGameActivity extends AppCompatActivity {

    Host host;
    SocketHandler socketHandler;
    TextView statusText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        host = new Host();
        host.execute();
        socketHandler = new SocketHandler();
        statusText = (TextView) findViewById(R.id.statusText);

        while (!socketHandler.hasClientSocket()){

            try {
                Thread.sleep(500);
            } catch (Exception e){

            }
        }

        statusText.setText(socketHandler.getClientSocket().toString() + " has connected");


    }
}
