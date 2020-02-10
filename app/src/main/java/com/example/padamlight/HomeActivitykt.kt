package com.example.padamlight

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Toast
/* kotlin version of HomeActivity*/
class HomeActivitykt : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar,  R.string.drawer_open,R.string.drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.fragments_content, SearchFragmentkt()).commit()
            navView.setCheckedItem(R.id.nav_itinerary )
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_itinerary -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragments_content,  SearchFragmentkt()).commit()
                Toast.makeText(this, "itinerary clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_profile -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragments_content,  MyProfileFragmentkt()).commit()
                Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
            }


        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}