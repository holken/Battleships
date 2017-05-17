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
    String text = "";

    InputStreamReader input;
    BufferedReader buffReader;
    OutputStream output;
    PrintWriter writer;

    public ClientWrite(Socket socket){
        this.socket = socket;

        try {
            input = new InputStreamReader(socket.getInputStream());
            buffReader = new BufferedReader(input);
            output = socket.getOutputStream();
            writer = new PrintWriter(output);
        } catch (Exception e){

        }
    }


    public void sendRandomCoords(){
        //initialize startingposition
        Random rand = new Random();
        int xCoord = rand.nextInt(16);
        int yCoord = rand.nextInt(9);
        writer.println("PB" + xCoord + "|" + yCoord);
    }

    public void sendToOpponent(String word){
        writer.println(word);
    }
}
