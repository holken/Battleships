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


    protected Socket doInBackground(Void... voids) {

        try {
            hostSocket = new ServerSocket(4001);
            SocketHandler.setHostSocket(hostSocket);
            while (loop){

                try {

                    Log.e("fish", "before accepted first client");
                    client = hostSocket.accept();
                    SocketHandler.setClientSocket(client);
                    Log.e("fish", "accepted first client. " + client.toString());
                    client2 = hostSocket.accept();
                    SocketHandler.setLocalClientSocket(client2);


                    OutputStream out = client.getOutputStream();
                    createAct.setHasConnected();
                    run();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
            hostSocket.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return client2;
    }

    protected void onPostExecute() {

    }
/*
    public void start(){
        ServerSocket server;
        try {
            server = new ServerSocket(40000);
            while (loop){

                try {
                    client = server.accept();
                    client2 = server.accept();
                    OutputStream out = client.getOutputStream();

                    run();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
            server.close();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

*/
    public boolean clientHasConnected() {
        if (client != null){
            return true;
            }
            return false;
    }


    public void run(){

        InputStream input1;
        OutputStream output1;
        InputStream input2;
        OutputStream output2;

        while (loop) {

            try {

                input1 = client.getInputStream();
                output1 = client.getOutputStream();
                input2 = client2.getInputStream();
                output2 = client2.getOutputStream();


                output1 = new BufferedOutputStream(output1);
                output2 = new BufferedOutputStream(output2);
                input1 = new BufferedInputStream(input1);
                input2 = new BufferedInputStream(input2);
                InputStreamReader readerlol1 = new InputStreamReader(input1);
                InputStreamReader readerlol2 = new InputStreamReader(input2);
                BufferedReader reader1 = new BufferedReader(readerlol1);
                BufferedReader reader2 = new BufferedReader(readerlol2);

                Thread.sleep(1000);

                //client.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

    }
    }

