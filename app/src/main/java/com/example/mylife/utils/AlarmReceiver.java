package com.example.mylife.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mylife.MainActivity;
import com.example.mylife.R;
import com.example.mylife.data.Todo;
import com.example.mylife.ui.totodetail.TodoDetailActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {

    public final static String CHANNEL_ID = "channel_deadline";

    @Override
    public void onReceive(Context context, Intent intent) {

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();

        String todoName = intent.getExtras().getString("todo_name");
        String todolistName = intent.getExtras().getString("todolist_name");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Deadline")
                .setContentText("\""+todoName+"\""+" from \""+todolistName+"\"")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("\""+todoName+"\""+" from \""+todolistName+"\""))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify((int)(System.currentTimeMillis()%Integer.MAX_VALUE), builder.build());

        //Intent i = new Intent(context, RingtonePlayingService.class);
        //context.startService(i);

        //createNotification(context);

//        ComponentName comp = new ComponentName(context.getPackageName(),
//                AlarmService.class.getName());
//        AlarmService.
//        startWakefulService(context, (intent.setComponent(comp)));
//        setResultCode(Activity.RESULT_OK);

    }

}
