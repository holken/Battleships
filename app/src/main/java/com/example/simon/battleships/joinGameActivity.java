package com.example.simon.battleships;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;

public class joinGameActivity extends Activity {
    private Client client;
    private EditText firstIP, secondIP;
    private String formattedIpAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_join_game);

        firstIP = (EditText) findViewById(R.id.firstIP);
        secondIP = (EditText) findViewById(R.id.secondIP);

        WifiManager mManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo mWifiInfo = mManager.getConnectionInfo();
        int ipAddress = mWifiInfo.getIpAddress();
        formattedIpAddress = String.format(Locale.US, "%d.%d.", (ipAddress & 0xFF), (ipAddress >> 8 & 0xFF));

        GameManager.setActivity(this);

        //ConnectButton
        ((Button) findViewById(R.id.findHostButton)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!firstIP.getText().equals("") && !secondIP.getText().equals("")) {

                    int SDK_INT = android.os.Build.VERSION.SDK_INT;
                    if (SDK_INT > 8) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        String ip = formattedIpAddress + firstIP.getText().toString() + "." + secondIP.getText().toString();
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

    public void continueToNextActivity() {
        Intent intent = new Intent(this, PlaceBoatActivity.class);
        startActivity(intent);
    }
}

