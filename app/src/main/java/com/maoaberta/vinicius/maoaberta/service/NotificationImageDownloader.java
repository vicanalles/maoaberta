package com.maoaberta.vinicius.maoaberta.service;

import android.app.NotificationManager;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

/**
 * Created by vinicius on 28/12/17.
 */

public class NotificationImageDownloader extends AsyncTask<Void, Void, Void>{

    private NotificationManager mNotificationManager;
    private final NotificationCompat.Builder builder;


    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }
}
