package com.example.padamlight.ui.propositions

import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import com.example.padamlight.R
import com.github.barteksc.pdfviewer.PDFView
import kotlinx.android.synthetic.main.activity_proposition.*
import kotlinx.android.synthetic.main.app_bar_main.*

class PropositionsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout

    private lateinit var toolbar: Toolbar

    private lateinit var navigationView: NavigationView

    private lateinit var pdfView: PDFView

    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proposition)

        // Binding UI elements defined below
        bindViews()

        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        drawerLayout.addDrawerListener(toggle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = getString(R.string.cv_title)


        navigationView.setNavigationItemSelectedListener(this)

        val resumeName = getString(R.string.CV)

        pdfView.fromAsset(resumeName).load()


    }

    private fun bindViews(){

        drawerLayout = drawer_layout

        toolbar = toolbar_main

        navigationView = nav_view

        pdfView = pdf_view
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_item_map -> {
                finish()
                drawerLayout.closeDrawer(Gravity.START)
            }
            R.id.nav_item_resume -> {
                Toast.makeText(this, R.string.already_here, Toast.LENGTH_LONG).show()
                drawerLayout.closeDrawer(Gravity.START)
            }
        }
        return true
    }
}
