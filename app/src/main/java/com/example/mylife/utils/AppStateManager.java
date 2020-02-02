package com.example.mylife.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.mylife.data.Todo;
import com.example.mylife.data.TodoList;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class AppStateManager {

    public static List<TodoList> listsOfTodos = new ArrayList<>();

    private static String filename = "datafile";
    public final static int RESQUEST_FILE_CHOOSER_BAK = 333;

    public static void saveData(Context context){
        try {
            File path = context.getFilesDir();
            ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(new File(path, filename)));
            o.writeObject(listsOfTodos);
            o.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadData(Context context){
        try{
            File path = context.getFilesDir();
            ObjectInputStream o = new ObjectInputStream((new FileInputStream(new File(path, filename))));
            listsOfTodos = (List<TodoList>)o.readObject();
            o.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void backupAndSendMail(Context context){
        try{
            File path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File savefile = new File(path, "Backup_MyLife_"+timeStamp+".bak");
            ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(savefile));
            o.writeObject(listsOfTodos);
            o.close();

            Intent mailIntent = new Intent(Intent.ACTION_SEND);
            mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Backup Mylife "+timeStamp);
            mailIntent.setType("text/plain");
            mailIntent.putExtra(Intent.EXTRA_STREAM, Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                    FileProvider.getUriForFile(context,"com.example.android.fileprovider", savefile) : Uri.fromFile(savefile));
            context.startActivity(Intent.createChooser(mailIntent, "Pick an email app"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void backupToStorage(Context context){
        try{
            File path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File savefile = new File(path, "Backup_MyLife_"+timeStamp+".bak");
            ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(savefile));
            o.writeObject(listsOfTodos);
            o.close();
            Toast.makeText(context, "Data backed up to " + savefile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showFileChooser(Context context) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        // Update with mime types
        intent.setType("*/*");

        //intent.addCategory(Intent.CATEGORY_OPENABLE);
        //intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

        ((Activity) context).startActivityForResult(intent, RESQUEST_FILE_CHOOSER_BAK);


    }

    public static boolean loadFromFile(File savefile, Context context){
        try{

            ObjectInputStream o = new ObjectInputStream(new FileInputStream(savefile));
            listsOfTodos = (List<TodoList>)o.readObject();
            o.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
