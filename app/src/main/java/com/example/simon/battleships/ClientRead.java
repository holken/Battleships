package com.example.simon.battleships;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Observable;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by simon on 2017-04-29.
 */

public class ClientRead extends Thread  {

    Socket socket;
    InputStream input;
    InputStreamReader reader;
    BufferedReader buffReader;
    ClientObservable clientObservable;

    public volatile AtomicBoolean haveReceviedCoords = new AtomicBoolean(false);

    public volatile AtomicBoolean haveReceivedAK = new AtomicBoolean(false);

    public ClientRead(Socket socket, ClientObservable clientObservable) {
        this.socket = socket;
        this.clientObservable = clientObservable;
    }

    public void run() {
        try {
            input = socket.getInputStream();
            reader = new InputStreamReader(input);
            buffReader = new BufferedReader(reader);
            String character;

            while (!socket.isClosed()) {
                Log.e("con", "Has entered while loop in clientRead");
                character = buffReader.readLine();
                Log.e("con", "ClientRead read: " + character);
                //clientObservable.changeMessage(character);
                GameManager.receiveCode(character);

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
