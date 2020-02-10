package com.example.padamlight;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;

/*
* Set up of an activity that will contain the drawer navigation and allow to contain fragments.
 These will be the Searchactivity become Searchfragment and Myprofilefragment containing my resume.*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar, R.string.drawer_open,R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragments_content, new SearchFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_itinerary);
        }
    }
/*This function allows us to detect the selected item and display the corresponding fragment in the container.*/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
       switch (menuItem.getItemId()){
           case R.id.nav_itinerary:
               getSupportFragmentManager().beginTransaction().replace(R.id.fragments_content,new SearchFragment()).commit();
               //Toast.makeText(this, "Oups", Toast.LENGTH_SHORT).show();
               break;
           case R.id.nav_profile:
               getSupportFragmentManager().beginTransaction().replace(R.id.fragments_content, new MyProfileFragment()).commit();
               break;
       }
       drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed(){
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
}
