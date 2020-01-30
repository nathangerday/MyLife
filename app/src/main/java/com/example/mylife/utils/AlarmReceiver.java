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

        //this will sound the alarm tone
        //this will sound the alarm once, if you wish to
        //raise alarm in loop continuously then use MediaPlayer and setLooping(true)
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
                .setContentText("\""+todoName+"\""+"from \""+todolistName+"\"")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("\""+todoName+"\""+"from \""+todolistName+"\""))
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



    public void createNotification(Context context) {


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_deadline")
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("It is prayer time")
                .setContentText("Prayer")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSubText("Tab to cancel the ringtone")
                .setPriority(NotificationCompat.PRIORITY_HIGH);


        //To add a dismiss button
        Intent dismissIntent = new Intent(context, RingtonePlayingService.class);
        dismissIntent.setAction(RingtonePlayingService.ACTION_DISMISS);

        PendingIntent pendingIntent = PendingIntent.getService(context,
                123, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action action = new NotificationCompat.Action
                (android.R.drawable.ic_lock_idle_alarm, "DISMISS", pendingIntent);
        builder.addAction(action);
        // end of setting action button to notification


        Intent intent1 = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 123, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pIntent);


        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("channel_deadline", "My Notifications", NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(123, notification);


    }


}
