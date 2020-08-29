package com.scalx.locationly.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;

import com.scalx.locationly.R;
import com.scalx.locationly.activities.MapsActivity;

public class NotificationManagementSystem extends FragmentActivity {
    Context context;
    NotificationCompat.Builder builder;
    NotificationManagerCompat notificationManager;
    static final int NOTIFICA1 = 1;
    static final int NOTIFICA2 = 2;

    public NotificationManagementSystem(Context context) {
        this.context = context;
        notificationManager = NotificationManagerCompat.from(context);
    }
    public void mostra(int Id){
        Intent resultIntent = new Intent(context, MapsActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        switch (Id){
            case NOTIFICA1:
                builder = new NotificationCompat.Builder(context, "1")
                        .setSmallIcon(R.mipmap.ic_launcher_loca)
                        .setWhen(System.currentTimeMillis())
                        .setContentTitle("Bulunduğunuz konumda yeni mesajlar var")
                        .setContentText("Deneme içeriği")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(resultPendingIntent)
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS )
                        .setAutoCancel(true);
                notificationManager.notify(1, builder.build());

            case NOTIFICA2:
                builder = new NotificationCompat.Builder(context, "2")
                        .setSmallIcon(R.mipmap.ic_launcher_loca)
                        .setWhen(System.currentTimeMillis())
                        .setContentTitle("Yeni yazılacak")
                        .setContentText("Yeni yazılacak")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(resultPendingIntent)
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS )
                        .setAutoCancel(true);
                notificationManager.notify(1, builder.build());
        }
    }

    public void sendCustomNotification(String title,String text){
        Intent resultIntent = new Intent(context, MapsActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder = new NotificationCompat.Builder(context, "2")
                .setSmallIcon(R.mipmap.ic_launcher_loca)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(resultPendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS )
                .setAutoCancel(true);
        notificationManager.notify(1, builder.build());
    }


}
