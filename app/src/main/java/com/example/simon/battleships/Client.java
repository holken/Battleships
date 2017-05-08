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
                //Random rand = new Random();
                //int nmr = rand.nextInt(4000);
                int nmr = 4001;
                socket = new Socket(address, 4001);
                SocketHandler.setClientSocket(socket);
                Log.e("clientCon", socket.toString());
                //t1 = new ClientRead(socket, nmr);
                //t2 = new ClientWrite(socket, nmr);
                //t1.start();
                //t2.start();
                while (true){

                }
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
/*
        public void run(){
            InetAddress address = null;
            Log.d("fish", "before try");
            try {
                address = InetAddress.getByName(ipAddress);
                Log.d("fish", "before try");
                try {
                    //Random rand = new Random();
                    //int nmr = rand.nextInt(4000);
                    int nmr = 4000;
                    Log.d("fish", address.toString());
                    socket = new Socket(address, 4000);
                    Log.d("fish", socket.toString());
                    t1 = new ClientRead(socket, nmr);
                    t2 = new ClientWrite(socket, nmr);
                    t1.start();
                    t2.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            }
        }*/

        public ClientRead getReaderThread(){
            return t1;
        }

        public synchronized void setText(String text){
            t2.setText(text);

        }

        public boolean checkConnection(){
            if (socket != null){
                return socket.isConnected();
            }
            return false;
        }

    }

