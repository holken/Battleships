package com.example.simon.battleships;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class GameManager {
    //Gameboard, coordinates etc...
    private static int[][] grid = new int[9][16];
    private static final int HIT = 2;
    private static final int NEAR_HIT = 1;
    private static int GRID_PIXEL_WIDTH = 120;
    private static int xPos;
    private static int yPos;
    private static boolean isDodging = false;
    private static int myScore;
    private static int opponentScore;
    private static boolean iWon;

    //All dem network objectz
    private static Socket client;
    private static Socket localClient;
    private static ServerSocket host;
    private static ClientRead clientRead;
    private static ClientObservable clientObservable;
    private static ClientWrite clientWrite;
    private static String message;
    private static Activity currActivity;

    //We really need this guy
    private static Handler mHandler;

    //Everthing to do with Saluting and such
    private static boolean isSaluting;
    private static boolean opponentSaluting;
    private static boolean saluteFinished;

    //Everything to do with checkIfReady
    private static boolean ready;
    private static boolean opponentReady;

    //MediaPlayer
    private static MediaPlayer mMediaPlayer;
    private static MediaPlayer approachPlayer;

    private static Context currentContext;

    //Tutorial
    private static boolean tutorial;

    /**
     * Sets up game to match resources
     *
     * @param gridPixelWidth
     */
    public static void initializeGame(int gridPixelWidth) {
        GRID_PIXEL_WIDTH = gridPixelWidth;
        opponentSaluting = false;
        isSaluting = false;
        saluteFinished = false;
        mHandler = new Handler();
        tutorial = false;
        ready = false;
        opponentReady = false;
        clearGrid();
    }

    public static void setContext(Context c) {
        currentContext = c;
    }

    /**
     * Places the ship on specified x and y coordinates and sets adjacent tiles to near hits
     *
     * @param x X-coordinate for ship, between 0-8
     * @param y Y-coordinate for ship, between 0-15
     */
    public static void placeShip(int x, int y) {
        xPos = x;
        yPos = y;
        for (int i = y - 1; i <= y + 1; i++) {
            for (int j = x - 1; j <= x + 1; j++) {
                if (!(i < 0 || j < 0 || i > 15 || j > 8)) {
                    grid[j][i] = NEAR_HIT;
                }
            }
        }
        grid[x][y] = HIT;
    }

    /**
     * Clears the field from all ships
     */
    public static void clearGrid() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 16; j++) {
                grid[i][j] = 0;
            }
        }
    }

    /**
     * Checks if the current finger position matches the ships position
     *
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

    public static int getShipX() {
        return xPos;
    }

    public static int getShipY() {
        return yPos;
    }

    /**
     * @return Socket of the opponent
     */
    public static synchronized Socket getClientSocket() {
        return client;
    }

    /**
     * @return Socket of this player
     */
    public static synchronized Socket getLocalClientSocket() {
        return localClient;
    }

    /**
     * Sets the socket for the opponent player
     *
     * @param socket for the opponent
     */
    public static synchronized void setClientSocket(Socket socket) {
        client = socket;
    }

    /**
     * Sets the socket for this player
     *
     * @param socket for this player
     */
    public static synchronized void setLocalClientSocket(Socket socket) {
        localClient = socket;
    }

    /**
     * Returns the serversocket
     *
     * @return ServerSocket
     */
    public static synchronized ServerSocket getHostSocket() {
        return host;
    }

    /**
     * Sets the HostSocket
     *
     * @param socket that acts like server
     */
    public static synchronized void setHostSocket(ServerSocket socket) {
        host = socket;
    }

    //Never used?
    public static boolean hasHostSocket() {
        return host != null;
    }

    public static boolean hasClientSocket() {
        return client != null;
    }

    //Never used?
    public static boolean hasLocalClientSocket() {
        return localClient != null;
    }

    public static synchronized ClientRead getClientRead() {
        return clientRead;
    }

    public static synchronized void send(String message) {
        if (client != null) {
            Log.e("Trying to send ", message);
            clientWrite = new ClientWrite(client, message);
            clientWrite.start();
        }
    }

    public static synchronized void setClientRead(ClientRead clientR) {
        clientRead = clientR;
    }

    //Never used?
    public static synchronized void setClientWrite(ClientWrite clientW) {
        clientWrite = clientW;
    }

    //Never used?
    public static synchronized ClientObservable getClientObservable() {
        return clientObservable;
    }

    public static synchronized void setClientObservable(ClientObservable clientO) {
        clientObservable = clientO;
    }

    //Never used?
    public static boolean hasClientRead() {
        return clientRead != null;
    }

    //Never used?
    public static boolean hasClientWrite() {
        return clientWrite != null;
    }

    //Never used?
    public static boolean hasLocalClientObservable() {
        return clientObservable != null;
    }

    public static void setActivity(Activity activity) {
        currActivity = activity;
    }

    public static Activity getActivity() {
        return currActivity;
    }

    public static void dodge() {
        Log.e("Dodge", "Starting");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isDodging = false;
                Log.e("Dodge", "Ending");
            }
        }, 500); //Time for which dodging is active
        isDodging = true;
    }

    /**
     * Method that is called from clientWrite when it receives a message which it then forward to this method which handle what to do with the information
     *
     * @param code is the message received from clientWrite
     */
    public static void receiveCode(String code) {
        if (code != null) {
            String beginning = code.substring(0, 3);
            Log.e("beginning", beginning);

            switch (beginning) {
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
                    int xPosOpponent = Integer.parseInt(code.substring(3, code.indexOf("|")));
                    Log.e("X position", String.valueOf(xPosOpponent));
                    int yPosOpponent = Integer.parseInt(code.substring(code.indexOf("|") + 1));
                    Log.e("Y position", String.valueOf(yPosOpponent));
                    placeShip(xPosOpponent, yPosOpponent);
                    opponentReady = true;
                    checkIfReady();
                    Log.e("positions", "Received - position x: " + xPosOpponent + "position y: " + yPosOpponent);
                    break;
                //begin salute, Opponent has begun isSaluting
                case "bsl":
                    opponentSaluting = true;
                    beginSalute();
                    break;
                //end salute, opponent stopped saluting
                case "esl":
                    opponentSaluting = false;
                    mHandler.removeCallbacks(null);
                    playSound("");
                    break;
                //Opponent just fired a missile that will hit your ship
                case "hit":
                    missileApproaching(true);
                    break;
                //Opponent just fired a missile that will miss your ship
                case "mis":
                    missileApproaching(false);
                    break;
                //U WON BITCH Congratulations, you have won the game
                case "uwb":
                    myScore++;
                    iWon = true;
                    Intent intent = new Intent(currActivity, PostGameActivity.class);
                    currActivity.startActivity(intent);
                    currActivity.finish();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Game is lost
     */
    private static void loseGame() {
        send("uwb");
        iWon = false;
        opponentScore++;
        Intent intent = new Intent(currActivity, PostGameActivity.class);
        currActivity.startActivity(intent);
        currActivity.finish();
    }

    /**
     * Begins countdown if both players are saluting.
     *
     * @return true if both players are saluting
     */
    public static boolean beginSalute() {
        if ((isSaluting && opponentSaluting) || isSaluting && client == null) {
            playSound("countdown");
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (currentContext != null) {
                        if (isSaluting && opponentSaluting || isSaluting && client == null) {
                            saluteFinished = true;
                        }
                    }
                }
            }, 3000);
            return true;
        }
        return false;
    }

    /**
     * Informs GamaManager on status of salute which then acts accordingly.
     *
     * @param isSaluting
     */
    public static void setIsSaluting(boolean isSaluting) {
        GameManager.isSaluting = isSaluting;
        if (isSaluting) {
            send("bsl");
            beginSalute();
        } else if (saluteFinished) {
            Log.e("beginSalute handler", "Trying to reach PlayActivity");
            Intent intent = new Intent(currentContext, PlayActivity.class);
            currentContext.startActivity(intent);
        } else {
            if(mHandler != null) {
                mHandler.removeCallbacks(null);     // TODO: Might need to be changed to remove specific Callback
            }
            send("esl");
            playSound("");        //Stops media player if it's currently playing
        }
    }

    /**
     * Handles mMediaPlayer to make sounds easy to manage
     *
     * @param sound: the sound to be played. Choose from: "fire", "boom", "splash", "countdown". Any other string will make the
     *               mediaPlayer stop whatever sound it's durrently playing.
     */
    public static void playSound(String sound) {

        if (approachPlayer == null || !approachPlayer.isPlaying()) {
            if (mMediaPlayer != null) {
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
        }
        switch (sound) {
            case "fire":
                mMediaPlayer = MediaPlayer.create(currentContext, R.raw.bottle);
                mMediaPlayer.start();
                break;
            case "boom":
                mMediaPlayer = MediaPlayer.create(currentContext, R.raw.missile_explode);
                mMediaPlayer.start();
                break;
            case "splash":
                mMediaPlayer = MediaPlayer.create(currentContext, R.raw.splash);
                mMediaPlayer.start();
                break;
            case "countdown":
                mMediaPlayer = MediaPlayer.create(currentContext, R.raw.countdown);
                mMediaPlayer.start();
                break;
            case "approaching":
                approachPlayer = MediaPlayer.create(currentContext, R.raw.approach_long);
                approachPlayer.start();
                break;
            default:
                break;
        }
    }

    public static void missileApproaching(final boolean willHit) {
        long flightDuration = (1 + new Random().nextInt(1)) * 1000;
        long approachDuration = 850;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playSound("approaching");
            }
        }, flightDuration);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isDodging || !willHit) { //Player is dodging or missile will miss
                    playSound("splash");
                } else {
                    playSound("boom");
                    loseGame();
                }
            }
        }, flightDuration + approachDuration);
    }

    public static void setTutorial(boolean b) {
        tutorial = b;
    }

    public static boolean isTutorial() {
        return tutorial;
    }

    public static int getMyScore() {
        return myScore;
    }

    public static int getOpponentScore() {
        return opponentScore;
    }

    public static String result() {
        if(iWon) {
            return "YOU WON";
        } else {
            return "YOU LOST";
        }
    }

    public static void setReady(boolean b) {
        ready = b;
        checkIfReady();
    }

    public static void checkIfReady() {
        if (ready && opponentReady) {
            Log.e("Continuing to Salute", "Trying to reach salute from place boat.");
            Intent intent = new Intent(currentContext, SaluteActivity.class);
            currentContext.startActivity(intent);
        }
    }
}