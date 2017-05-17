package com.example.simon.battleships;

import java.util.Observable;

/**
 * Created by simon on 2017-05-17.
 */

public class ClientObservable extends Observable {

    private String message;

    public String getMessage() {
        return message;
    }

    public void changeMessage(String message) {
        this.message = message;
        setChanged();
        notifyObservers(message);
    }
}
