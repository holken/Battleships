package com.example.simon.battleships;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.text.CollationElementIterator;
import java.util.Observable;
import java.util.Observer;

public class createGameActivity extends AppCompatActivity  {

    Host host;
    SocketHandler socketHandler;
    //TextView statusText;
    TextView ipText;
    private EditText firstIP, secondIP;
    WifiManager mManager;
    WifiInfo mWifiInfo;
    String[] ip;
    String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        code = "";
        ipText = (TextView) findViewById(R.id.ipText);
        firstIP = (EditText) findViewById(R.id.firstIP);
        secondIP = (EditText) findViewById(R.id.secondIP);
        mManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        mWifiInfo = mManager.getConnectionInfo();
        ip = new String[4];
        displayIP(mWifiInfo.getIpAddress());
        GameManager.setActivity(this);
        host = new Host(this);
        host.execute();

        //statusText = (TextView) findViewById(R.id.statusText);


        //Intent intent = new Intent(this, PlayActivity.class);
        //startActivity(intent);
    }

    public void continueToNextActivity(){
        Log.e("con", "inside ContinueToNextActivity");
        Intent intent = new Intent(this, PlaceBoatActivity.class);
        startActivity(intent);

    }

    /**
     * Formats and displays your IP
     */
    private void displayIP(int ipAddress) {
        for(int i = 0; i <4; i++) {
            ip[i] = String.valueOf((ipAddress >> (i*8) & 0xFF));
        }
        firstIP.setText(ip[2]);
        secondIP.setText(ip[3]);
    }

    }


