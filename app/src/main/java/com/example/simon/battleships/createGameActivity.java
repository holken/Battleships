package com.example.simon.battleships;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class createGameActivity extends AppCompatActivity {

    Host host;
    static SocketHandler socketHandler;
    static TextView statusText;
    TextView ipText;
    WifiManager mManager;
    WifiInfo mWifiInfo;
    String ip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        ipText = (TextView) findViewById(R.id.ipText);
        mManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        mWifiInfo = mManager.getConnectionInfo();
        int ipAddress = mWifiInfo.getIpAddress();
        ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff));
        ipText.setText("Your Ip: " + ip);

        host = new Host(this);
        host.execute();
        socketHandler = new SocketHandler();
        statusText = (TextView) findViewById(R.id.statusText);

        /*while (!socketHandler.hasClientSocket()){

            try {
                Thread.sleep(500);
            } catch (Exception e){

            }
        }

        statusText.setText(socketHandler.getClientSocket().toString() + " has connected");*/


    }

    public static synchronized void setHasConnected(){
        statusText.setText(SocketHandler.getClientSocket().toString() + " has connected");
    }
}
