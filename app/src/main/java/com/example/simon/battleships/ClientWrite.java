package com.example.simon.battleships;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

/**
 * Created by simon on 2017-04-29.
 */

public class ClientWrite extends Thread{

    Socket socket;
    Socket matchedClient;
    boolean hasTextToWrite = false;
    PlayActivity activity;
    int nmr;
    String text = "";

    InputStreamReader input;
    BufferedReader buffReader;
    OutputStream output;
    PrintWriter writer;

    public ClientWrite(Socket socket, int nmr, PlayActivity activity){
        this.socket = socket;
        this.nmr = nmr;
        this.activity = activity;
        try {
            input = new InputStreamReader(socket.getInputStream());
            buffReader = new BufferedReader(input);
            output = socket.getOutputStream();
            writer = new PrintWriter(output);
        } catch (Exception e){

        }
    }

    private boolean hasSomethingToWrite(){
        return hasTextToWrite;
    }

    public void setWriteStatus(boolean bool){
        hasTextToWrite = bool;
    }



    public String parseString(String text){
        text = text.substring(3);
        return text;
    }

    public synchronized void setText(String text){
        this.text = text;
        notifyAll();
    }

    private synchronized void sendText(PrintWriter writer){
        while (text == "") {
            try {
                wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        writer.println(text);
        writer.flush();
        text = "";
    }

    public void run(){
        try {
            //input = new InputStreamReader(socket.getInputStream());
            //buffReader = new BufferedReader(input);
            //output = socket.getOutputStream();
            //writer = new PrintWriter(output);

            String character;
            int count = 100;
            while (count > 0){
                sendRandomCoords();
                count--;
            }

            while (!socket.isClosed()) {
                sendText(writer);

            }


            socket.close();

        } catch (IOException e) {

        }
    }

    public void sendRandomCoords(){
        //initialize startingposition
        Random rand = new Random();
        int xCoord = rand.nextInt(5);
        int yCoord = rand.nextInt(5);
        Log.e ("fish", "Writer placing boat: " + "PB" + xCoord + yCoord);
        writer.println("PB" + xCoord + yCoord);
    }

    public void sendToOpponent(String word){
        Log.e ("fish4", "writing to opponent: " + word);
        writer.println("AK");
    }
}
