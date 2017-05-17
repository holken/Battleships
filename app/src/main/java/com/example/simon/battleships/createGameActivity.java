package com.example.simon.battleships;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

public class createGameActivity extends AppCompatActivity  {

    Host host;
    SocketHandler socketHandler;
    TextView statusText;
    TextView ipText;
    WifiManager mManager;
    WifiInfo mWifiInfo;
    String ip;
    String code;
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

        statusText = (TextView) findViewById(R.id.statusText);

        while (!code.equals("con")){

            try {
                Thread.sleep(250);
            } catch (Exception e){

            }

        }
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);

    }

    public void setCode(String code){
        this.code = code;
    }

    }


