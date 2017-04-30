package com.example.simon.battleships;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by simon on 2017-04-29.
 */

public class ClientRead extends Thread{

    Socket socket;
    int nmr;
    PlayActivity activity;

    InputStream input;

    InputStreamReader reader;
    BufferedReader buffReader;

    public boolean haveReceviedCoords = false;

    boolean haveReceivedAK = false;

    public ClientRead(Socket socket, int nmr, PlayActivity activity){
        this.socket = socket;
        this.nmr = nmr;
        this.activity = activity;
    }

    public void run(){
        try {
            input = socket.getInputStream();
            Log.e("fish3", socket.toString());
            reader = new InputStreamReader(input);
            buffReader = new BufferedReader(reader);
            String character;

            while (!socket.isClosed()){
                character = buffReader.readLine();
                Log.e("fish2", character);
                if (character.startsWith("PB")){
                    updateShipPosition(character.substring(2));
                } else if (character.startsWith("AK")){
                    haveReceivedAK = true;
                }
            }
            socket.close();

        } catch (IOException e) {

        }
    }

    private void updateShipPosition(String boatPosition){
        Log.e("fish", "first position: " + boatPosition.substring(0,1) + " Second position: " + boatPosition.substring(1,2));
        activity.placeShip(Integer.parseInt(boatPosition.substring(0,1)), Integer.parseInt(boatPosition.substring(1,2)));
        haveReceviedCoords = true;
    }


}
