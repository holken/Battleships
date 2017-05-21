package com.example.simon.battleships;

import android.app.Activity;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

public class createGameActivity extends ConnectActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        ipText = (EditText) findViewById(R.id.IP);
        ipText.setText(getCode());

        GameManager.setActivity(this);
        Host host = new Host(this);
        host.execute();
    }

    private String getCode() {
        StringBuilder sb = new StringBuilder();
        boolean hasPrior = false;
        Log.e("Number sgments", String.valueOf(numberOfSignificantSegments()));
        for(int i = ip.length - numberOfSignificantSegments(); i < ip.length; i++) {
            if(!hasPrior) {
                Log.e("IP segment", ip[i]);
                sb.append(ip[i]);
                hasPrior = true;
            } else {
                String segment = ip[i];
                for(int j = 0; j < 3 - segment.length(); j++) {
                    sb.append("0");
                }
                sb.append(ip[i]);
            }
        }
        return sb.toString();
    }

}


