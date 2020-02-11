package com.example.padamlight.views.activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Gravity
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.padamlight.R
import com.google.android.material.navigation.NavigationView
import com.pdfview.PDFView
import kotlinx.android.synthetic.main.activity_proposition.*


class PropositionsActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
	private lateinit var mDrawerLayout: DrawerLayout
	private lateinit var navigationView: NavigationView
	private lateinit var drawerToggle: ActionBarDrawerToggle
	private lateinit var pdfView: PDFView

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_proposition);

		bindViews()
		initDrawer()
		showPdf()
	}

	private fun showPdf() {
		val cvFile = "CV_Cyril_CDI_2020.pdf"
		pdfView.fromAsset(cvFile).show()
	}

	private fun bindViews() {
		mDrawerLayout = drawer_proposition_layout
		navigationView = nav_view
		pdfView= resume_pdfview
	}

	private fun initDrawer() {
		drawerToggle = ActionBarDrawerToggle(this, mDrawerLayout, R.string.open_drawer, R.string.close_drawer)

		mDrawerLayout.addDrawerListener(drawerToggle)
		drawerToggle.syncState()

		supportActionBar!!.setDisplayHomeAsUpEnabled(true)

		navigationView.setNavigationItemSelectedListener(this)

	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		return if (drawerToggle.onOptionsItemSelected(item)) true else super.onOptionsItemSelected(item)
	}

	override fun onNavigationItemSelected(item: MenuItem): Boolean {
		val activity = item.itemId

		when (activity) {
			R.id.resume ->
				if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
					mDrawerLayout.closeDrawer(Gravity.LEFT)
				}
			R.id.search_map -> {
				val intent = Intent(this, SearchActivity::class.java)
				startActivity(intent)
			}
			else -> return true
		}
		return true
	}
}