package com.maoaberta.vinicius.maoaberta.service;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Notificacao;
import com.maoaberta.vinicius.maoaberta.presentation.ui.activity.ApresentacaoActivity;
import com.maoaberta.vinicius.maoaberta.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vinicius on 28/12/17.
 */

public class NotificationUtil {

    private static MyFirebaseMessagingService myFirebaseMessagingService;

    public static void showNotification(Notificacao notificacao, MyFirebaseMessagingService myFirebaseMessagingService){
        NotificationUtil.myFirebaseMessagingService = myFirebaseMessagingService;

        List<String> allNotifications = saveNotificationOnShared(notificacao, myFirebaseMessagingService);

        //Build Label
        String replyLabel = myFirebaseMessagingService.getString(R.string.notif_action_reply);
        RemoteInput remoteInput = new RemoteInput.Builder(Constants.REPLY_KEY)
                .setLabel(replyLabel)
                .build();

        // 2. Build action
        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
                R.drawable.ic_reply, replyLabel, getReplyPendingIntent(myFirebaseMessagingService, notificacao))
                .addRemoteInput(remoteInput)
                .setAllowGeneratedReplies(true)
                .build();

        // 3. Build notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(myFirebaseMessagingService);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_mao_aberta);
        mBuilder.setAutoCancel(true);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);
        long[] vibrate = {500, 500};
        mBuilder.setVibrate(vibrate);
        mBuilder.setContentIntent(getNormalPendingIntent(myFirebaseMessagingService, notificacao));
        mBuilder.setContentTitle(notificacao.getTitle());
        mBuilder.setShowWhen(true);

        for (String n : allNotifications) {
            inboxStyle.addLine(n);
        }

        mBuilder.setStyle(inboxStyle);
        mBuilder.addAction(replyAction);


    }

    private static List<String> saveNotificationOnShared(Notificacao notificacao, MyFirebaseMessagingService myFirebaseMessagingService){
        SharedPreferences sharedPreferences = myFirebaseMessagingService.getApplicationContext().getSharedPreferences(Constants.SHARED_PREFERENCES_NOTIFICATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor shEdit = sharedPreferences.edit();

        HashMap<String, List<String>> notificationHash = getNotificationFromShared(myFirebaseMessagingService);

        if(notificationHash == null){
            notificationHash = new HashMap<>();
        }

        List<String> strings = notificationHash.get(notificacao.getNotificationId());
        if(strings == null)
            strings = new ArrayList<>();

        strings.add(notificacao.getTitle() + ": " + notificacao.getBody());
        notificationHash.put(notificacao.getNotificationId(), strings);
        Gson gson = new Gson();
        String hashJson = gson.toJson(notificationHash);
        shEdit.putString(Constants.SHARED_PREFERENCES_NOTIFICATION, hashJson);
        shEdit.apply();

        return strings;
    }

    private static HashMap<String, List<String>> getNotificationFromShared(MyFirebaseMessagingService myFirebaseMessagingService){
        SharedPreferences sharedPreferences = myFirebaseMessagingService.getApplicationContext().getSharedPreferences(Constants.SHARED_PREFERENCES_NOTIFICATION, Context.MODE_PRIVATE);
        String notificationString = sharedPreferences.getString(Constants.SHARED_PREFERENCES_NOTIFICATION, null);

        if (notificationString != null) {
            Gson gson = new Gson();
            java.lang.reflect.Type type = new TypeToken<HashMap<String, List<String>>>() {
            }.getType();
            return gson.fromJson(notificationString, type);
        }
        return null;
    }

    private static PendingIntent getNormalPendingIntent(MyFirebaseMessagingService myFirebaseMessagingService, Notificacao notification) {
        Intent intent = new Intent(myFirebaseMessagingService, ApresentacaoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.OPEN_FROM_NOTIFICATION, notification);
        return PendingIntent.getActivity(myFirebaseMessagingService, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent getReplyPendingIntent(MyFirebaseMessagingService myFirebaseMessagingService, Notificacao notification) {

        return getNormalPendingIntent(myFirebaseMessagingService, notification);

    }
}
