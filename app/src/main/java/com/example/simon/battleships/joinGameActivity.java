package com.example.simon.battleships;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;

public class joinGameActivity extends ConnectActivity {
    private Client client;
    private String formattedIpAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        ipText = (EditText) findViewById(R.id.IP);
        ipText.setTransformationMethod(null);

        GameManager.setActivity(this);

        //ConnectButton
        ((Button) findViewById(R.id.findHostButton)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!ipText.getText().equals("")) {

                    int SDK_INT = android.os.Build.VERSION.SDK_INT;
                    if (SDK_INT > 8) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        String ip = buildIP();
                        Log.e("IP", ip);
                        client = new Client(ip);
                        client.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ip);
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

    private String buildIP() {
        code = ipText.getText().toString();
        StringBuilder sb = new StringBuilder();
        int i;
        for(i = 0; i < ip.length - numberOfSignificantSegments(); i++) {
            sb.append(ip[i] + ".");
        }
        int length;
        switch(i) {
            case 1:
                length = code.length();
                sb.append(code.substring(0, length - 6) + "." + code.substring(length - 6, length - 3) + "." + code.substring(length - 3));
                break;
            case 2:
                length = code.length();
                sb.append(code.substring(0, length - 3) + "." + code.substring(length - 3));
                break;
            case 3:
                sb.append(code);
                break;
            default:
                break;
        }
        return sb.toString();
    }
}

