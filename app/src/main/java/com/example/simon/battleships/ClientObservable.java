package com.example.simon.battleships;

import android.util.Log;

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
        Log.e("con", "clientObserver received: " + message);
        setChanged();
        notifyObservers(message);
    }
}
