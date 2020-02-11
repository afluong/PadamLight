package com.example.padamlight.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.padamlight.R
import com.example.padamlight.enums.MarkerType
import com.example.padamlight.interfaces.MapActionsDelegate
import com.example.padamlight.models.Suggestion
import com.example.padamlight.utils.Tools
import com.example.padamlight.views.fragments.MapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class SearchActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
	private lateinit var mSpinnerFrom: Spinner
	private lateinit var mSpinnerTo: Spinner
	private lateinit var mButtonSearch: Button
	private lateinit var mDrawerLayout: DrawerLayout
	private lateinit var navigationView: NavigationView
	private lateinit var drawerToggle: ActionBarDrawerToggle

	private lateinit var mapDelegate: MapActionsDelegate
	private lateinit var mFromList: HashMap<String, Suggestion>

	var PADAM = LatLng(48.8609, 2.349299999999971)
	var TAO = LatLng(47.9022, 1.9040499999999838)
	var FLEXIGO = LatLng(48.8598, 2.0212400000000343)
	var LA_NAVETTE = LatLng(48.8783804, 2.590549)
	var ILEVIA = LatLng(50.632, 3.05749000000003)
	var NIGHT_BUS = LatLng(45.4077, 11.873399999999947)
	var FREE2MOVE = LatLng(33.5951, -7.618780000000015)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main);

		initMap()
		bindViews()
		initDrawer()
		bindClicks()
		initializeTextViews()
		initSpinners()
	}

	private fun initDrawer() {
		drawerToggle = ActionBarDrawerToggle(this, mDrawerLayout, R.string.open_drawer, R.string.close_drawer)

		mDrawerLayout.addDrawerListener(drawerToggle)
		drawerToggle.syncState()

		supportActionBar!!.setDisplayHomeAsUpEnabled(true)

		navigationView.setNavigationItemSelectedListener(this)

	}

	private fun bindViews() {
		mSpinnerFrom = spinner_from
		mSpinnerTo = spinner_to
		mButtonSearch = button_search
		mDrawerLayout = drawer_layout
		navigationView = nav_view
	}

	private fun bindClicks() {

		mButtonSearch.setOnClickListener {
			val selectedFrom = mSpinnerFrom.selectedItem.toString()
			val selectedTo = mSpinnerTo.selectedItem.toString()
			mapDelegate.clearMap()
			val selectedFromSuggestion = mFromList[selectedFrom]
			val selectedToSuggestion = mFromList[selectedTo]
			mapDelegate.updateMarker(MarkerType.PICKUP, selectedFrom, selectedFromSuggestion!!.latLng)
			mapDelegate.updateMarker(MarkerType.DROPOFF, selectedTo, selectedToSuggestion!!.latLng)
			mapDelegate.updateMap(selectedFromSuggestion.latLng, selectedToSuggestion.latLng)
			mapDelegate.updateMap(selectedFromSuggestion.latLng, selectedToSuggestion.latLng)
			mapDelegate.buildItinenary(selectedFromSuggestion.latLng, selectedToSuggestion.latLng)
		}
	}

	private fun initializeTextViews() {
		mButtonSearch.setText(R.string.button_search)
	}

	private fun initMap() {
		val mapFragment = MapFragment.newInstance()

		supportFragmentManager.beginTransaction()
				.replace(R.id.fragment_map, mapFragment)
				.commitAllowingStateLoss()
		mapDelegate = mapFragment
	}

	private fun initSpinners() {
		val fromList = Tools.formatHashmapToList(initFromHashmap())
		val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, fromList)
		mSpinnerFrom.adapter = adapter
		mSpinnerTo.adapter = adapter
	}

	private fun initFromHashmap(): HashMap<String, Suggestion> {
		mFromList = HashMap()
		mFromList["Padam"] = Suggestion(PADAM)
		mFromList["Tao Résa'Est"] = Suggestion(TAO)
		mFromList["Flexigo"] = Suggestion(FLEXIGO)
		mFromList["La Navette"] = Suggestion(LA_NAVETTE)
		mFromList["Ilévia"] = Suggestion(ILEVIA)
		mFromList["Night Bus"] = Suggestion(NIGHT_BUS)
		mFromList["Free2Move"] = Suggestion(FREE2MOVE)
		return mFromList
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		return if (drawerToggle.onOptionsItemSelected(item)) true else super.onOptionsItemSelected(item)
	}

	override fun onNavigationItemSelected(item: MenuItem): Boolean {
		val activity = item.itemId

		when (activity) {
			R.id.search_map ->
				if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
					mDrawerLayout.closeDrawer(Gravity.LEFT)
				}
			R.id.resume -> {
				val intent = Intent(this, PropositionsActivity::class.java)
				startActivity(intent)
			}
			else -> return true
		}
		return true
	}


}