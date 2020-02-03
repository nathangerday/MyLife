package com.example.mylife.ui.totodetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.content.FileProvider;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mylife.R;
import com.example.mylife.data.Priority;
import com.example.mylife.data.Todo;
import com.example.mylife.data.TodoList;
import com.example.mylife.utils.AlarmReceiver;
import com.example.mylife.utils.AppStateManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TodoDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    static final int REQUEST_IMAGE_CAPTURE = 10;

    private Todo todo;
    private TodoList parent = null;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private EditText todotitle;
    private TextView alarmtext;
    private SimpleDateFormat sdf;
    private ImageView pictureViewer;
    private EditText description;
    private Spinner priority;
    private View view;

    private String previousPicturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        createNotificationChannel();

        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        view = findViewById(R.id.todo_detail_layout);
        todotitle = findViewById(R.id.todo_title);
        alarmtext = findViewById(R.id.deadline_display);
        pictureViewer = findViewById(R.id.picture_viewer);
        description = findViewById(R.id.todo_description);
        priority = findViewById(R.id.priority_spinner);



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
            NavUtils.navigateUpFromSameTask(this);
        }

        if(todo.isDeadline()){
            alarmtext.setText(sdf.format(todo.getDeadline().getTime()));
        }
        setTitle(todo.name);
        todotitle.setText(todo.name);
        description.setText(todo.description);
        priority.setOnItemSelectedListener(this);
        switch (this.todo.priority){
            case NONE:
                priority.setSelection(0);
                break;
            case HIGH:
                priority.setSelection(1);
                break;
            case MEDIUM:
                priority.setSelection(2);
                break;
            case LOW:
                priority.setSelection(3);
                break;
        }
        updatePicture();

        setAllListener();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                this.todo.priority = Priority.NONE;
                break;
            case 1:
                this.todo.priority = Priority.HIGH;
                break;
            case 2:
                this.todo.priority = Priority.MEDIUM;
                break;
            case 3:
                this.todo.priority = Priority.LOW;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home){
            setResult(RESULT_OK);
            finish();
        }
        return true;
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

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                finish();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(resultCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if(resultCode == RESULT_OK){
                updatePicture();
            }else if(resultCode == RESULT_CANCELED){
                this.todo.photoPath = previousPicturePath;
            }
        }
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


    @Override
    protected void onPause() {
        super.onPause();

        AppStateManager.saveData(this);
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

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        this.previousPicturePath = this.todo.photoPath;
        this.todo.photoPath = image.getAbsolutePath();
        return image;
    }

    private void updatePicture(){
        if(this.todo.isPicture()){
            File imgFile = new  File(this.todo.photoPath);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                pictureViewer.setImageBitmap(myBitmap);
            }else{
                this.todo.photoPath=null;
            }
        }else{
            pictureViewer.setImageDrawable(null);
        }
    }

    private void setAllListener(){
        todotitle.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                todo.name = todotitle.getText().toString();
                setTitle(todo.name);
                todotitle.clearFocus();
            }
            return false;
        });

        pictureViewer.setOnClickListener(v -> {
            if(this.todo.isPicture()){
                File file = new File(this.todo.photoPath);
                final Intent intent = new Intent(Intent.ACTION_VIEW)//
                        .setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                                        FileProvider.getUriForFile(this,"com.example.android.fileprovider", file) : Uri.fromFile(file),
                                "image/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            }
        });

        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                todo.description = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        view.setOnTouchListener((v, event) -> {
            if(description.hasFocus()){
                description.clearFocus();
            }else if(todotitle.hasFocus()){
                todotitle.clearFocus();
            }
            return false;
        });
    }
}
