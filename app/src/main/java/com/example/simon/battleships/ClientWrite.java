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

public class ClientWrite extends Thread {
    private String message;
    Socket socket;
    Socket matchedClient;
    boolean hasTextToWrite = false;
    String text = "";

    InputStreamReader input;
    BufferedReader buffReader;
    OutputStream output;
    PrintWriter writer;

    public ClientWrite(Socket socket, String message) {
        this.socket = socket;
        this.message = message;
        try {
            input = new InputStreamReader(socket.getInputStream());
            buffReader = new BufferedReader(input);
            output = socket.getOutputStream();
            writer = new PrintWriter(output);
        } catch (Exception e) {

        }
    }

    @Override
    public void run() {
        if (!message.equals("")) {
            Log.e("con", "clientWrite writes to opponent this message: " + message);
            writer.println(message);
            writer.flush();
        } else {
            Log.e("No message", "Nothing to send.");
        }
        message = "";
    }
}
