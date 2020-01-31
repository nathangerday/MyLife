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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mylife.R;
import com.example.mylife.data.Todo;
import com.example.mylife.data.TodoList;
import com.example.mylife.utils.AlarmReceiver;
import com.example.mylife.utils.AppStateManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class TodoDetailActivity extends AppCompatActivity {

    private Todo todo;
    private TodoList parent = null;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private TextView alarmtext;
    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_detail);

        createNotificationChannel();

        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");


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
        alarmtext = findViewById(R.id.deadline_display);
        if(todo.isDeadline()){
            alarmtext.setText(sdf.format(todo.getDeadline().getTime()));
        }


        todotitle.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                todo.name = todotitle.getText().toString();
                todotitle.clearFocus();
            }
            return false;
        });
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

                    TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                            (view2, hourOfDay, minute) -> {
                                if(this.todo.isDeadline()){
                                    cancelAlarm();
                                }

                                Intent myIntent = new Intent(this, AlarmReceiver.class);
                                myIntent.putExtra("todo_name", this.todo.name);
                                myIntent.putExtra("todolist_name", this.parent.name);
                                pendingIntent = PendingIntent.getBroadcast(this, 1122, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                                Calendar alarm = Calendar.getInstance();
                                alarm.set(year, monthOfYear, dayOfMonth, hourOfDay, minute, 0);
                                long timeDifference = alarm.getTimeInMillis() - System.currentTimeMillis();
                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(), pendingIntent);
                                Toast.makeText(this, "Alarm set in " + String.format("%02d hours, %02d min",
                                        TimeUnit.MILLISECONDS.toHours(timeDifference),
                                        TimeUnit.MILLISECONDS.toMinutes(timeDifference) % 60
                                ), Toast.LENGTH_LONG).show();


                                alarmtext.setText(sdf.format(alarm.getTime()));


                                this.todo.setDeadline(alarm);
                            }, mHour, mMinute, false);
                    timePickerDialog.show();
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();



    }

    public void takePicture(View view){

    }

    public void removeDeadline(View view){
        cancelAlarm();
        alarmtext.setText(R.string.no_alarm);
    }




    @Override
    public void onBackPressed(){
        setResult(RESULT_OK);
        finish();
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

    private void cancelAlarm(){
        Intent myIntent = new Intent(this, AlarmReceiver.class);
        myIntent.putExtra("todo_name", this.todo.name);
        myIntent.putExtra("todolist_name", this.parent.name);
        pendingIntent = PendingIntent.getBroadcast(this, 1122, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        todo.removeDeadline();
    }
}
