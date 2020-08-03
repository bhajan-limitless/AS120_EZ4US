package com.ez4us.shieldapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.ez4us.shieldapp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static Uri alarmingsound;
    private final long[] pattern ={100, 300, 300 ,300};

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        Map<String, String> extraData = remoteMessage.getData();

        String UniqueId = extraData.get("UniqueId");
        String category = extraData.get("category");
        String smslink = extraData.get("link");

        alarmingsound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, "TAC")
                        .setContentTitle(title)
                        .setContentText(body)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setSound(alarmingsound)
                        .setVibrate(pattern);


        Intent intent;
        if (category.equals("shoes")) {
            intent = new Intent(this, var_get.class);

        } else {
            intent = new Intent(this, var_get.class);

        }
        intent.putExtra("UniqueId", UniqueId);
        intent.putExtra("link",smslink);
        intent.putExtra("category", category);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(pendingIntent);


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        int id =  (int) System.currentTimeMillis();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("TAC","demo",NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(id,notificationBuilder.build());

    }

}

