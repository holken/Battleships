package com.example.simon.battleships;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Created by simon on 2017-04-29.
 */

public class Host extends AsyncTask<Void, Void, Socket> {

    boolean loop = true;
    Socket client;
    public Socket client2;
    public ServerSocket hostSocket;
    createGameActivity createAct;

    public Host(createGameActivity createGame){
        createAct = createGame;
    }

    /**
     *  Creates server socket, waits for opponent to connect and adds the opponents socket to the GameManager
     */
    protected Socket doInBackground(Void... voids) {

        try {
            hostSocket = new ServerSocket(4001);
            GameManager.setHostSocket(hostSocket);
            while (loop){

                try {
                    client = hostSocket.accept();
                    GameManager.setClientSocket(client);
                    ClientObservable clientObservable = new ClientObservable();
                    ClientRead clientRead = new ClientRead(client, clientObservable);
                    clientRead.start();
                    GameManager.setClientObservable(clientObservable);
                    GameManager.setClientRead(clientRead);
                } catch (IOException e){
                    e.printStackTrace();
                }
                hostSocket.close();
                loop = false;
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
        return client;
    }

    protected void onPostExecute() {

    }

    }


