package com.example.simon.battleships;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by simon on 2017-04-29.
 */

public class SocketHandler {
    private static Socket client;
    private static Socket localClient;
    private static ServerSocket host;

    public static synchronized Socket getClientSocket(){
        return client;
    }

    public static synchronized Socket getLocalClientSocket(){
        return localClient;
    }

    public static synchronized void setClientSocket(Socket socket){
        client = socket;
    }

    public static synchronized void setLocalClientSocket(Socket socket){
        localClient = socket;
    }

    public static synchronized ServerSocket getHostSocket(){
        return host;
    }

    public static synchronized void setHostSocket(ServerSocket socket){
        host = socket;
    }

    public boolean hasHostSocket(){
        if (host == null){
            return false;
        }
        return true;
    }

    public boolean hasClientSocket(){
        if (client == null){
            return false;
        }
        return true;
    }

    public boolean hasLocalClientSocket(){
        if (localClient == null){
            return false;
        }
        return true;
    }
}
