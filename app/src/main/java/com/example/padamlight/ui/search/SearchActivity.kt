package com.example.padamlight.ui.search

import android.content.Intent
import android.content.res.Configuration
import android.os.PersistableBundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast


import com.example.padamlight.enums.MarkerType
import com.example.padamlight.ui.map.fragment.MapFragment

import com.example.padamlight.R
import com.example.padamlight.data.local.Suggestion
import com.example.padamlight.ui.map.interfaces.MapActionsDelegate
import com.example.padamlight.ui.propositions.PropositionsActivity
import com.example.padamlight.utils.ToPrettyList
import com.google.android.gms.maps.model.LatLng

import java.util.HashMap

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class SearchActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    companion object {

        /*
        Constants FROM lat lng
     */
        internal var PADAM = LatLng(48.8609, 2.349299999999971)
        internal var TAO = LatLng(47.9022, 1.9040499999999838)
        internal var FLEXIGO = LatLng(48.8598, 2.0212400000000343)
        internal var LA_NAVETTE = LatLng(48.8783804, 2.590549)
        internal var ILEVIA = LatLng(50.632, 3.05749000000003)
        internal var NIGHT_BUS = LatLng(45.4077, 11.873399999999947)
        internal var FREE2MOVE = LatLng(33.5951, -7.618780000000015)
    }


    private lateinit var mSpinnerFrom: Spinner

    private lateinit var mSpinnerTo: Spinner

    private lateinit var mButtonSearch: Button


    private lateinit var drawerLayout: DrawerLayout

    private lateinit var toolbar: Toolbar

    private lateinit var navigationView: NavigationView


    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var mapDelegate: MapActionsDelegate

    private lateinit var mSuggestionsList: HashMap<String, Suggestion>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Binding UI elements defined below
        bindViews()

        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.syncState()

        drawerLayout.addDrawerListener(toggle)

        navigationView.setNavigationItemSelectedListener(this)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        bindClickListener()
        initializeTextViews()
        initSpinners()
        initMap()
    }

    private fun bindViews(){

        mSpinnerFrom = spinner_from

        mSpinnerTo = spinner_to

        mButtonSearch = button_search

        drawerLayout = drawer_layout

        toolbar = toolbar_main

        navigationView = nav_view
    }

    private fun bindClickListener(){

        /**
         * Define what to do after the button click interaction
         */
        mButtonSearch.setOnClickListener{

            /*
                Retrieve selection of "From" spinner
             */
            val selectedFrom = mSpinnerFrom.selectedItem.toString()
            val selectedTo = mSpinnerTo.selectedItem.toString()
            mapDelegate.clearMap()
            val selectedFromSuggestion = mSuggestionsList[selectedFrom]
            val selectedToSuggestion = mSuggestionsList[selectedTo]
            if (selectedFromSuggestion != null && selectedToSuggestion != null) {
                mapDelegate.updateMarker(MarkerType.PICKUP, selectedFrom, selectedFromSuggestion.latLng)
                mapDelegate.updateMarker(MarkerType.DROPOFF, selectedTo, selectedToSuggestion.latLng)
                mapDelegate.updateMap(selectedFromSuggestion.latLng, selectedToSuggestion.latLng)

                mapDelegate.drawRoute(selectedFromSuggestion, selectedToSuggestion)
            }

        }
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

    private fun initializeTextViews() {
        mButtonSearch.setText(R.string.button_search)
    }

    private fun initMap() {
        /*
            Instanciate MapFragment to get the map on the page
         */
        val mapFragment = MapFragment.newInstance()

        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_map, mapFragment)
                .commitAllowingStateLoss()

        //
        this.mapDelegate = mapFragment
    }

    /**
     * Initialize spinners from and to
     */
    private fun initSpinners() {
        val fromList = initFromHashmap().ToPrettyList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, fromList)
        mSpinnerFrom.adapter = adapter
        mSpinnerTo.adapter = adapter
    }

    /**
     * Using Hashmap to initialize FROM suggestion list
     */
    private fun initFromHashmap(): HashMap<String, Suggestion> {
        mSuggestionsList = HashMap()
        mSuggestionsList["Padam"] = Suggestion(PADAM)
        mSuggestionsList["Tao Résa'Est"] = Suggestion(TAO)
        mSuggestionsList["Flexigo"] = Suggestion(FLEXIGO)
        mSuggestionsList["La Navette"] = Suggestion(LA_NAVETTE)
        mSuggestionsList["Ilévia"] = Suggestion(ILEVIA)
        mSuggestionsList["Night Bus"] = Suggestion(NIGHT_BUS)
        mSuggestionsList["Free2Move"] = Suggestion(FREE2MOVE)
        return mSuggestionsList
    }



    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_item_map -> {
                Toast.makeText(this, R.string.already_here, Toast.LENGTH_LONG).show()
                drawerLayout.closeDrawer(Gravity.START)
            }
            R.id.nav_item_resume -> {
                val intent = Intent(this, PropositionsActivity::class.java)
                startActivity(intent)
                drawerLayout.closeDrawer(Gravity.START)
            }
        }
        return true
    }

}


