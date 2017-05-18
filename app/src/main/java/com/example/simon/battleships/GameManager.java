package com.example.simon.battleships;

import android.app.Activity;
import android.util.Log;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class GameManager {
    private static int[][] grid = new int[9][16];
    private static final int HIT = 2;
    private static final int NEAR_HIT = 1;
    private static int GRID_PIXEL_WIDTH = 120;
    private static int xPos;
    private static int yPos;
    private static int xPosOpponent;
    private static int yPosOpponent;

    private static Socket client;
    private static Socket localClient;
    private static ServerSocket host;
    private static ClientRead clientRead;
    private static ClientObservable clientObservable;
    private static ClientWrite clientWrite;
    private static String message;
    private static Activity currActivity;

    private static boolean saluting;
    private static boolean opponentSaluting;

    /**
     * Sets up game to match resources
     * @param gridPixelWidth
     */
    public static void initializeGame(int gridPixelWidth) {
        GRID_PIXEL_WIDTH = gridPixelWidth;
        opponentSaluting = false;
        saluting = false;
    }

    /**
     * Places the ship on specified x and y coordinates and sets adjacent tiles to near hits
     * @param x X-coordinate for ship, between 0-8
     * @param y Y-coordinate for ship, between 0-15
     */
    public static void placeShip(int x, int y) {
        xPos = x;
        yPos = y;
        for(int i = y-1; i <= y+1; i++) {
            for(int j = x-1; j <= x+1; j++) {
                if(!(i < 0 || j < 0 || i > 15 || j > 8)) {
                    grid[j][i] = NEAR_HIT;
                }
            }
        }
        grid[x][y] = HIT;
    }

    public static void clearGrid(){
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 16; j++){
                grid[i][j] = 0;
            }
        }
    }

    /**
     * Checks if the current finger position matches the ships position
     * @param x X-coordinate for current finger position
     * @param y Y-coordinate for current finger position
     * @return 2 if Hit, 1 if Near Hit and 0 if Miss
     */
    public static int isHit(int x, int y) {
        return grid[x / GRID_PIXEL_WIDTH][y / GRID_PIXEL_WIDTH];
    }

    /**
     * @return Width of one grid tile in pixels, e.g 120 pixels for 1080p resolution
     */
    public static int getGridPixelWidth() {
        return GRID_PIXEL_WIDTH;
    }
    public static int getShipX(){ return xPos; }
    public static int getShipY(){ return yPos; }

    /**
     *
     * @return Socket of the opponent
     */
    public static synchronized Socket getClientSocket(){
        return client;
    }

    /**
     *
     * @return Socket of this player
     */
    public static synchronized Socket getLocalClientSocket(){
        return localClient;
    }

    /**
     * Sets the socket for the opponent player
     * @param socket for the opponent
     */
    public static synchronized void setClientSocket(Socket socket){
        client = socket;
    }

    /**
     * Sets the socket for this player
     * @param socket for this player
     */
    public static synchronized void setLocalClientSocket(Socket socket){
        localClient = socket;
    }

    /**
     * Returns the serversocket
     * @return ServerSocket
     */
    public static synchronized ServerSocket getHostSocket(){
        return host;
    }

    /**
     * Sets the HostSocket
     * @param socket that acts like server
     */
    public static synchronized void setHostSocket(ServerSocket socket){
        host = socket;
    }

    public static boolean hasHostSocket(){
        if (host == null){
            return false;
        }
        return true;
    }

    public static boolean hasClientSocket(){
        if (client == null){
            return false;
        }
        return true;
    }

    public static boolean hasLocalClientSocket(){
        if (localClient == null){
            return false;
        }
        return true;
    }

    public static synchronized ClientRead getClientRead(){
        return clientRead;
    }

    public static synchronized void send(String message) {
        Log.e("Trying to send ", message);
        clientWrite = new ClientWrite(client, message);
        Log.e("Starting Thread", "");
        clientWrite.start();
    }

    public static synchronized void setClientRead(ClientRead clientR){
        clientRead = clientR;
    }

    public static synchronized void setClientWrite(ClientWrite clientW){
        clientWrite = clientW;
    }

    public static synchronized ClientObservable getClientObservable(){
        return clientObservable;
    }

    public static synchronized void setClientObservable(ClientObservable clientO){
        clientObservable = clientO;
    }

    public static boolean hasClientRead(){
        if (clientRead == null){
            return false;
        }
        return true;
    }

    public static boolean hasClientWrite(){
        if (clientWrite == null){
            return false;
        }
        return true;
    }

    public static boolean hasLocalClientObservable(){
        if (clientObservable == null){
            return false;
        }
        return true;
    }

    public static void setActivity(Activity activity){
        currActivity = activity;
    }

    public static Activity getActivity(){
        return currActivity;
    }

    /**
     * Method that is called from clientWrite when it receives a message which it then forward to this method which handle what to do with the information
     * @param code is the message received from clientWrite
     */
    public static void receiveCode(String code){
        if (code != null){
        String beginning = code.substring(0,3);
            Log.e("beginning", beginning);

        switch(beginning){
            //ServerClient receives con from the client that wants to connect
            case "con":
                send("ack");
                createGameActivity temp = (createGameActivity) currActivity;
                temp.continueToNextActivity();
                break;

            //Client that connect to serverclient receives ACK to finish the connection
            case "ack":
                joinGameActivity temp2 = (joinGameActivity) currActivity;
                temp2.continueToNextActivity();
                break;
            //client sends boatCoords
            case "plc":
                Log.e("message", code);
                Log.e("message", code.substring(0, code.indexOf("|")));
                Log.e("message", code.substring(code.indexOf("|")+1));
                xPosOpponent = Integer.parseInt(code.substring(3, code.indexOf("|")));
                yPosOpponent = Integer.parseInt(code.substring(code.indexOf("|")+1));
                placeShip(xPosOpponent, yPosOpponent);

                Log.e("positions", "Received - position x: " + xPosOpponent + "position y: " + yPosOpponent);
                break;
            //Opponent has begun saluting
            case "slt":
                opponentSaluting = true;
                beginSalute();
                break;
            case "fire":
                break;
            default:
                break;
        }
        }
    }

    public static boolean beginSalute(){
        return false;
    }

    public static void setSaluting(Boolean b){
        saluting = b;
    }

    /*
    public void update(Observable o, Object arg) {
        message = ((ClientObservable) o).getMessage();
        Log.e("con", "inside update");
        switch(message){
            //Client has connected

            case "con":     clientWrite.sendToOpponent("ack");
                createGameActivity temp = (createGameActivity) currActivity;
                 temp.continueToNextActivity();
                break;
            case "ack":
        }
    }
    */

}