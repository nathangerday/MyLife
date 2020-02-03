package com.example.mylife.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.mylife.data.Todo;
import com.example.mylife.data.TodoList;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class BootSetter extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AppStateManager.loadData(context);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);

        for(TodoList tl : AppStateManager.listsOfTodos){
            for(Todo t: tl.todolist){
                if(t.isDeadline() && t.deadline > System.currentTimeMillis()){
                    Intent myIntent = new Intent(context, AlarmReceiver.class);
                    myIntent.putExtra("todo_name", t.name);
                    myIntent.putExtra("todolist_name", t.parentList.name);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1122, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, t.deadline, pendingIntent);
                }
            }
        }
    }
}
