package com.example.simon.battleships;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by simon on 2017-04-29.
 */

public class ClientRead extends Thread {

    Socket socket;
    int nmr;
    PlayActivity activity;

    InputStream input;

    InputStreamReader reader;
    BufferedReader buffReader;

    public volatile AtomicBoolean haveReceviedCoords = new AtomicBoolean(false);

    public volatile AtomicBoolean haveReceivedAK = new AtomicBoolean(false);

    public ClientRead(Socket socket, int nmr, PlayActivity activity) {
        this.socket = socket;
        this.nmr = nmr;
        this.activity = activity;
    }

    public void run() {
        try {
            input = socket.getInputStream();
            Log.e("fish3", socket.toString());
            reader = new InputStreamReader(input);
            buffReader = new BufferedReader(reader);
            String character;

            while (!socket.isClosed()) {
                character = buffReader.readLine();
                Log.e("fish2", character);
                if (character.startsWith("PB")) {
                    updateShipPosition(character.substring(2));
                } else if (character.startsWith("AK")) {
                    haveReceivedAK.set(true);
                } else if (character.startsWith("CON")) {

                }
            }
            socket.close();

        } catch (IOException e) {

        }
    }

    private void updateShipPosition(String boatPosition) {
        if (!haveReceviedCoords.get()) {
            Log.e("fish", "first position: " + boatPosition.substring(0, boatPosition.indexOf("|")) + " Second position: " + boatPosition.substring(boatPosition.indexOf("|") + 1));
            GameManager.placeShip(Integer.parseInt(boatPosition.substring(0, boatPosition.indexOf("|"))), Integer.parseInt(boatPosition.substring(boatPosition.indexOf("|") + 1)));
            haveReceviedCoords.set(true);
        }
    }
}
