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
    TextView clientStatusText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        clientStatusText = (TextView) findViewById(R.id.clientStatusText);
        socketHandler = new SocketHandler();
        ipEnter = (EditText) findViewById(R.id.ipEnter);
        connectButton = (Button) findViewById(R.id.findHostButton);

        connectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!ipEnter.getText().equals("")){

                    int SDK_INT = android.os.Build.VERSION.SDK_INT;
                    if (SDK_INT > 8)
                    {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        client = new Client(ipEnter.getText().toString());
                        Log.d("fish", "before execute");
                        client.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ipEnter.getText().toString());

                    }


                    socketHandler.setClientSocket(client.socket);

                }
                try {
                    Thread.sleep(500);
                } catch (Exception e){

                }/*
                while(!socketHandler.hasClientSocket()){

                }
                */
                if (socketHandler.getClientSocket() != null)
                    clientStatusText.setText("connected to: " + socketHandler.getClientSocket().toString());

                Intent intent = new Intent(v.getContext(), PlayActivity.class);
                startActivity(intent);


            }
        });
    }
}
