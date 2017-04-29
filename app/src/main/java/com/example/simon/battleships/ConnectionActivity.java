package com.example.simon.battleships;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.Socket;
import java.sql.Time;

public class ConnectionActivity extends AppCompatActivity {
    WifiManager mManager;
    String ip;
    WifiInfo mWifiInfo;
    TextView ipTextView;
    EditText ipEnter;
    Button connectButton;
    Button startButton;
    Client client;
    Host host;
    SocketHandler socketHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        mManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        mWifiInfo = mManager.getConnectionInfo();
        ipTextView = (TextView) findViewById(R.id.ipAddress);
        ipEnter = (EditText) findViewById(R.id.enterIP);
        connectButton = (Button) findViewById(R.id.connectButton);
        startButton = (Button) findViewById(R.id.startButton);
        int ipAddress = mWifiInfo.getIpAddress();
        ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff));
        ipTextView.setText("Your Ip: " + ip);

        host = new Host();
        host.execute();


        connectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!ipEnter.getText().equals("")){

                    ipTextView.setText(ipEnter.getText().toString());
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

                    }
                    Intent intent = new Intent(v.getContext(), PlayActivity.class);
                    startActivity(intent);


            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (host.clientHasConnected()){
                    int SDK_INT = android.os.Build.VERSION.SDK_INT;
                   /* if (SDK_INT > 8)
                    {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        client = new Client("localhost");
                        Log.d("fish", "before execute");
                        client.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ipEnter.getText().toString());

                    }*/

                    //socketHandler.setClientSocket(host.client);
                    //socketHandler.setHostSocket(host.hostSocket);
                    Intent intent = new Intent(v.getContext(), PlayActivity.class);
                    startActivity(intent);
                }

            }
        });
    }
}
