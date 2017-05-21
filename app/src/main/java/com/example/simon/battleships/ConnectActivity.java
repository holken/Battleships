package com.example.simon.battleships;

import android.app.Activity;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

public class ConnectActivity  extends Activity {
    protected EditText ipText;
    protected String[] ip;
    protected String[] netMask;
    protected String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WifiManager mManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        DhcpInfo mDhcpInfo = mManager.getDhcpInfo();

       ip = intToIp(mDhcpInfo.ipAddress).split("\\.");
       netMask = intToIp(mDhcpInfo.netmask).split("\\.");
    }

    public void continueToNextActivity(){
        Log.e("con", "inside ContinueToNextActivity");
        Intent intent = new Intent(this, PlaceBoatActivity.class);
        startActivity(intent);

    }

    private String intToIp(int addr) {
        return  ((addr & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF));
    }

    protected int numberOfSignificantSegments() {
        int i;
        for(i = netMask.length; i >= 0; i--) {
            if(!netMask[netMask.length - i].equals("255")) {
                break;
            }
        }
        return i;
    }
}
