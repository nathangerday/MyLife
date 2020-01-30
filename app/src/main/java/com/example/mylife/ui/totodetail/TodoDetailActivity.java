package com.example.mylife.ui.totodetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mylife.R;
import com.example.mylife.data.Todo;
import com.example.mylife.data.TodoList;
import com.example.mylife.utils.AlarmReceiver;
import com.example.mylife.utils.AppStateManager;

import java.util.Calendar;

public class TodoDetailActivity extends AppCompatActivity {

    private Todo todo;
    private TodoList parent = null;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_detail);

        createNotificationChannel();

        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String todolist_name = extras.getString(("todolist_name"));
            for (TodoList t : AppStateManager.listsOfTodos){
                if(t.name.equals(todolist_name)){
                    parent = t;
                    break;
                }
            }
            todo = parent.todolist.get(extras.getInt("todo_index"));
        }else{
            Log.v("TodosActivity", "Error, no TodoList associated");
            NavUtils.navigateUpFromSameTask(this);
        }

        EditText todotitle = findViewById(R.id.todo_title);
        todotitle.setText(todo.name);
    }

    public void changeDeadline(View view){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view1, year, monthOfYear, dayOfMonth) -> {
                    //TODO Store date

                    TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                            (view2, hourOfDay, minute) -> {
                                Intent myIntent = new Intent(this, AlarmReceiver.class);
                                myIntent.putExtra("todo_name", this.todo.name);
                                myIntent.putExtra("todolist_name", this.parent.name);
                                pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                //TODO May need to create another Calendar instance to stora selected date and pass it to alarm manager
                                // alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, pendingIntent);
                                Toast.makeText(this, "Alarm set in " + 5 + " seconds", Toast.LENGTH_LONG).show();

                            }, mHour, mMinute, false);
                    timePickerDialog.show();
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();



    }

    public void takePicture(View view){

    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(AlarmReceiver.CHANNEL_ID, "Deadline Channel", importance);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
