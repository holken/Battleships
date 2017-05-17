package com.example.simon.battleships;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * Created by simon on 2017-04-29.
 */


    public class Client extends AsyncTask<String, Void, String> {
        public Socket socket;
        ClientRead t1;
        ClientWrite t2;
        String ipAddress;
        public Client(String ipAddress){
            this.ipAddress = ipAddress;
        }

    public void setSocket(Socket socket){
        this.socket = socket;
    }

    protected String doInBackground(String... strings) {
        InetAddress address = null;
        try {
            address = InetAddress.getByName(ipAddress);
            try {
                socket = new Socket(address, 4001);
                GameManager.setClientSocket(socket);
                ClientObservable clientObservable = new ClientObservable();
                ClientRead clientRead = new ClientRead(socket, clientObservable);
                ClientWrite clientWrite = new ClientWrite(socket);
                clientWrite.start();
                GameManager.setClientObservable(clientObservable);
                GameManager.setClientRead(clientRead);
                GameManager.setClientWrite(clientWrite);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute() {

    }


    }

