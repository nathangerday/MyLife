package com.example.mylife;

import android.os.Bundle;

import com.example.mylife.ui.filter.FilterFragment;
import com.example.mylife.ui.lists.AddListDialog;
import com.example.mylife.ui.lists.ListsFragment;
import com.example.mylife.utils.AppStateManager;
import com.example.mylife.utils.ListsTouchHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AddListDialog.AddListDialogListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AppStateManager.loadData(getFilesDir());

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.nav_lists:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListsFragment()).commit();
                break;
            case R.id.nav_filters:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FilterFragment()).commit();
                break;
            case R.id.nav_backup:
                Snackbar.make(findViewById(R.id.drawer_layout), "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Toast.makeText(this, "HelloWorld", Toast.LENGTH_LONG).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void createList(String name) {
        ListsFragment lf = (ListsFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        lf.createList(name);
    }

    @Override
    protected void onPause() {
        super.onPause();

        AppStateManager.saveData(getFilesDir());
    }
}
