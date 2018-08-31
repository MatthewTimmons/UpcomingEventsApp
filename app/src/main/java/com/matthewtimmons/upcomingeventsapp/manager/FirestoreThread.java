package com.matthewtimmons.upcomingeventsapp.manager;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

public class FirestoreThread extends HandlerThread {
    Handler handler;

    public FirestoreThread(String name) {
        super(name);
    }

    @Override
    protected void onLooperPrepared() {

        super.onLooperPrepared();
        handler = new Handler(getLooper()) {
            @Override
            public void handleMessage(Message msg) {

            }
        };
    }
}
