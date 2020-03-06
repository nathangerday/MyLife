package com.example.mylife;

import android.content.Intent;
import android.os.Bundle;

import com.example.mylife.ui.filter.FilterFragment;
import com.example.mylife.ui.filter.FilterMissedFragment;
import com.example.mylife.ui.filter.FilterUrgentFragment;
import com.example.mylife.ui.lists.AddListDialog;
import com.example.mylife.ui.lists.ListsFragment;
import com.example.mylife.ui.tasks.TaskFragment;
import com.example.mylife.utils.AppStateManager;
import com.example.mylife.data.Priority;
import com.google.android.material.snackbar.Snackbar;

import android.os.Environment;
import android.os.FileUtils;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AddListDialog.AddListDialogListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AppStateManager.loadData(this);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListsFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_lists);
        }

    }



    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.nav_tasks:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TaskFragment()).commit();
                break;
            case R.id.nav_lists:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListsFragment()).commit();
                break;
            case R.id.nav_filter_high:
                FilterFragment filter_high =  new FilterFragment();
                Bundle args = new Bundle();
                args.putInt("priority", Priority.HIGH.ordinal());
                filter_high.setArguments(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, filter_high).commit();
                break;
            case R.id.nav_filter_medium:
                FilterFragment filter_medium =  new FilterFragment();
                Bundle args2 = new Bundle();
                args2.putInt("priority", Priority.MEDIUM.ordinal());
                filter_medium.setArguments(args2);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, filter_medium).commit();
                break;
            case R.id.nav_filter_low:
                FilterFragment filter_low =  new FilterFragment();
                Bundle args3 = new Bundle();
                args3.putInt("priority", Priority.LOW.ordinal());
                filter_low.setArguments(args3);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, filter_low).commit();
                break;
            case R.id.nav_filter_urgent:
                FilterUrgentFragment filter_urgent = new FilterUrgentFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, filter_urgent).commit();
                break;
            case R.id.nav_filter_missed:
                FilterMissedFragment filter_missed = new FilterMissedFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, filter_missed).commit();
                break;
            case R.id.nav_backup:
                AppStateManager.backupAndSendMail(this);
                break;
            case R.id.nav_backup_local:
                AppStateManager.backupToStorage(this);
                break;
            case R.id.nav_loadbak:
                AppStateManager.showFileChooser(this);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void createList(String name, int color) {
        ListsFragment lf = (ListsFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        lf.createList(name, color);
    }

    @Override
    protected void onPause() {
        super.onPause();

        AppStateManager.saveData(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // If the user doesn't pick a file just return
        if (requestCode != AppStateManager.RESQUEST_FILE_CHOOSER_BAK || resultCode != RESULT_OK) {
            return;
        }

        File tmp = new File(getFilesDir(), "tmp");

        try {
            InputStream in = getContentResolver().openInputStream(data.getData());
            tmp.deleteOnExit();
            FileOutputStream out = new FileOutputStream(tmp);
            copyStream(in, out);

            AppStateManager.loadFromFile(tmp, this);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        if(AppStateManager.loadFromFile(tmp, this)){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListsFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_lists);
            Toast.makeText(this, "Backup loaded", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "File couldn't be read", Toast.LENGTH_SHORT).show();
        }


    }

    private void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
