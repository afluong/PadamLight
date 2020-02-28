package com.example.padamlight.ui.search;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.padamlight.enums.MarkerType;
import com.example.padamlight.ui.map.fragment.MapFragment;

import com.example.padamlight.R;
import com.example.padamlight.data.local.Suggestion;
import com.example.padamlight.ui.map.interfaces.MapActionsDelegate;
import com.example.padamlight.ui.propositions.PropositionsActivity;
import com.example.padamlight.utils.Toolbox;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.spinner_from)
    Spinner mSpinnerFrom;
    @Bind(R.id.spinner_to)
    Spinner mSpinnerTo;
    @Bind(R.id.button_search)
    Button mButtonSearch;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.toolbar_main)
    Toolbar toolbar;

    @Bind(R.id.nav_view)
    NavigationView navigationView;


    private ActionBarDrawerToggle toggle;

    private MapActionsDelegate mapDelegate;
    private HashMap<String, Suggestion> mSuggestionsList;

    /*
        Constants FROM lat lng
     */
    static LatLng PADAM = new LatLng(48.8609, 2.349299999999971);
    static LatLng TAO = new LatLng(47.9022, 1.9040499999999838);
    static LatLng FLEXIGO = new LatLng(48.8598, 2.0212400000000343);
    static LatLng LA_NAVETTE = new LatLng(48.8783804, 2.590549);
    static LatLng ILEVIA = new LatLng(50.632, 3.05749000000003);
    static LatLng NIGHT_BUS = new LatLng(45.4077, 11.873399999999947);
    static LatLng FREE2MOVE = new LatLng(33.5951, -7.618780000000015);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Binding UI elements defined below
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        drawerLayout.addDrawerListener(toggle);

        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        initializeTextViews();
        initSpinners();
        initMap();
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void initializeTextViews() {
        mButtonSearch.setText(R.string.button_search);
    }

    private void initMap() {
        /*
            Instanciate MapFragment to get the map on the page
         */
        MapFragment mapFragment = MapFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_map, mapFragment)
                .commitAllowingStateLoss();

        //
        this.mapDelegate = mapFragment;
    }

    /**
     * Initialize spinners from and to
     */
    private void initSpinners() {
        List<String> fromList = Toolbox.formatHashmapToList(initFromHashmap());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fromList);
        mSpinnerFrom.setAdapter(adapter);
        mSpinnerTo.setAdapter(adapter);
    }

    /**
     * Using Hashmap to initialize FROM suggestion list
     */
    private HashMap<String, Suggestion> initFromHashmap() {
        mSuggestionsList = new HashMap<>();
        mSuggestionsList.put("Padam", new Suggestion(PADAM));
        mSuggestionsList.put("Tao Résa'Est", new Suggestion(TAO));
        mSuggestionsList.put("Flexigo", new Suggestion(FLEXIGO));
        mSuggestionsList.put("La Navette", new Suggestion(LA_NAVETTE));
        mSuggestionsList.put("Ilévia", new Suggestion(ILEVIA));
        mSuggestionsList.put("Night Bus", new Suggestion(NIGHT_BUS));
        mSuggestionsList.put("Free2Move", new Suggestion(FREE2MOVE));
        return mSuggestionsList;
    }

    /**
     * Define what to do after the button click interaction
     */
    @OnClick(R.id.button_search)
    void onClickSearch() {

        /*
            Retrieve selection of "From" spinner
         */
        String selectedFrom = String.valueOf(mSpinnerFrom.getSelectedItem());
        String selectedTo = String.valueOf(mSpinnerTo.getSelectedItem());
        mapDelegate.clearMap();
        Suggestion selectedFromSuggestion = mSuggestionsList.get(selectedFrom);
        Suggestion selectedToSuggestion = mSuggestionsList.get(selectedTo);
        if (selectedFromSuggestion != null && selectedToSuggestion != null) {
            mapDelegate.updateMarker(MarkerType.PICKUP, selectedFrom, selectedFromSuggestion.getLatLng());
            mapDelegate.updateMarker(MarkerType.DROPOFF, selectedTo, selectedToSuggestion.getLatLng());
            mapDelegate.updateMap(selectedFromSuggestion.getLatLng(), selectedToSuggestion.getLatLng());

            mapDelegate.drawRoute(selectedFromSuggestion, selectedToSuggestion);
        }


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_item_map:
                Toast.makeText(this, "Tu est déja la :)", Toast.LENGTH_LONG).show();
                drawerLayout.closeDrawer(Gravity.START);
                break;
            case R.id.nav_item_resume:
                Intent intent = new Intent(this, PropositionsActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(Gravity.START);
                break;
        }
        return true;
    }
}


