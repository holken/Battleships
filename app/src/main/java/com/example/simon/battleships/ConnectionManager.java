package com.example.simon.battleships;

public class ConnectionManager {
    public boolean haveReceviedCoords;
    public boolean haveReceivedAK;
    private SocketHandler socketHandler;
    ClientWrite sender;
    ClientRead receiver;

    public ConnectionManager(PlayActivity activity) {
        socketHandler = new SocketHandler();
        receiver = new ClientRead (SocketHandler.getClientSocket(), 4001, activity);
        sender = new ClientWrite(SocketHandler.getClientSocket(), 4001, activity);

        receiver.start();
        sender.start();

        int count = 10000;
        while (!haveReceivedAK && count > 0){
        //haveReceivedAK = receiver.haveReceivedAK.get();
        count--;
        sender.sendRandomCoords();
        if (!haveReceviedCoords){
            sender.sendToOpponent("AK");
        }
        //haveReceviedCoords = receiver.haveReceviedCoords.get();
            /*
            try {
                Thread.sleep(50);
            } catch (Exception e){

            }
            */
        }
    }

}
