package com.example.ToDoListAndReminder;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.ToDoListAndReminder.Models.NumberOfUncompletedTodos;

public class Notify extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManagerCompat notificationManager =  NotificationManagerCompat.from(context);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID);
        NumberOfUncompletedTodos num = new NumberOfUncompletedTodos();

        Intent contentIntent = new Intent(context,MainActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentTitle("Todos Notification");
        notification.setContentText("You have "+ NumberOfUncompletedTodos.getNum()+" uncompleted todos");
        notification.setSmallIcon(R.drawable.ic_baseline_notifications_24);
        notification.setPriority(NotificationCompat.PRIORITY_HIGH);
        notification.setDefaults(NotificationCompat.DEFAULT_ALL);
        // za da ne se restartiraat promenite napraveni od strana na korisnikot , setContentIntent e komentirano
       // notification.setContentIntent(pendingIntent);
        // imam lokalno azuriranje na podatoci , Http Put request ne gi azurira podatocite
        // zatoa pri klikanje na notifikacijata , povtorno se vcituvaat istite podatoci
        notification.setAutoCancel(true);
        notificationManager.notify(NOTIFICATION_ID,notification.build());


    }

}