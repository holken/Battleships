package com.example.simon.battleships;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

public class createGameActivity extends AppCompatActivity  {
    private SocketHandler socketHandler;
    private EditText firstIP, secondIP;
    private String[] ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_game);

        firstIP = (EditText) findViewById(R.id.firstIP);
        secondIP = (EditText) findViewById(R.id.secondIP);

        WifiManager mManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo mWifiInfo = mManager.getConnectionInfo();
        ip = new String[4];
        displayIP(mWifiInfo.getIpAddress());

        GameManager.setActivity(this);
        Host host = new Host(this);
        host.execute();
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


