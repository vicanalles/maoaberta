package com.maoaberta.vinicius.maoaberta.service;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.maoaberta.vinicius.maoaberta.domain.models.Notificacao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vinicius on 28/12/17.
 */

public class NotificatorSingleton {

    private List<NotificationListener> notificationListenerList = new ArrayList<>();
    private static NotificatorSingleton instance;

    protected NotificatorSingleton(){

    }

    public static NotificatorSingleton getInstance(){
        if(instance == null){
            instance = new NotificatorSingleton();
        }

        return instance;
    }

    public List<NotificationListener> getNotificationListenerList(){
        return notificationListenerList;
    }

    public void setNotificationListener(NotificationListener notificationListener){
        if(notificationListener != null && !this.notificationListenerList.contains(notificationListener)){
            this.notificationListenerList.add(notificationListener);
            Log.d("NotificatorSingleton", "notification listener attached");
            Log.d("NotificatorSingleton", "attached listeners: " + this.notificationListenerList.size());
        }
    }

    public void cancelNotificationListener(NotificationListener notificationListener){
        this.notificationListenerList.remove(notificationListener);
        Log.d("NotificatorSingleton", "notification listener detached");
        Log.d("NotificatorSingleton", "attached listeners: " + this.notificationListenerList.size());
    }

    public void checkMessageToNotify(RemoteMessage remoteMessage, MyFirebaseMessagingService myFirebaseMessagingService, String id){
        if(notificationListenerList != null && notificationListenerList.size() > 0){
            for (NotificationListener notificationListener : notificationListenerList){
                notificationListener.onNotificationReceived(id);
            }
            Log.d("NotificatorSingleton", "notificatiors: " + notificationListenerList.size());
        }else if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Log.d("NotificatorSingleton", "notificating");

            Gson gson = new Gson();

            String jsonNotification = gson.toJson(remoteMessage.getData());
            Notificacao notificacao = gson.fromJson(jsonNotification, Notificacao.class);


        }
    }

    public interface NotificationListener{
        void onNotificationReceived(String id);
    }
}
