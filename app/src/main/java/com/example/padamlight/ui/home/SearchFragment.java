package com.example.padamlight.ui.home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.padamlight.R;
import com.example.padamlight.Suggestion;
import com.example.padamlight.utils.Toolbox;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    @Bind(R.id.spinner_from)
    Spinner mSpinnerFrom;
    @Bind(R.id.spinner_to)
    Spinner mSpinnerTo;
    @Bind(R.id.button_search)
    Button mButtonSearch;

    private MapActionsDelegate mapDelegate;
    private HashMap<String, Suggestion> mFromList;
    private HashMap<String, Suggestion> mToList;

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

    /*
        Constants TO lat lng
     */
    static LatLng LESHALLES = new LatLng(48.8621, 2.3467812538146973);
    static LatLng TROCADERO = new LatLng(48.8630487, 2.2871436);
    static LatLng PLACEDITALIE = new LatLng(48.8319, 2.3554980754852295);
    static LatLng OPERA = new LatLng(48.8706446, 2.33233);
    static LatLng VINCENNES = new LatLng(48.8474508, 2.4396714);
    static LatLng SAINTLAZARE = new LatLng(48.8753662109375, 2.325467586517334);
    static LatLng OLYMPIADES = new LatLng(48.827003479003906, 2.3662283420562744);

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        initializeTextViews();
        initSpinners();
        initMap();
        return view;
    }

    private void initializeTextViews() {
        mButtonSearch.setText(R.string.button_search);
    }

    private void initMap() {
        /*
            Instanciate MapFragment to get the map on the page
         */
        MapFragment mapFragment = MapFragment.newInstance();

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_map, mapFragment)
                .commitAllowingStateLoss();

        //
        this.mapDelegate = mapFragment;
    }

    /**
     * Initialize spinners from and to
     */
    // TODO : Initialize the "To" spinner with data of your choice
    private void initSpinners() {
        List<String> fromList = Toolbox.formatHashmapToList(initFromHashmap());
        List<String> toList = Toolbox.formatHashmapToList(initToHashmap());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, fromList);
        ArrayAdapter<String> toAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, toList);
        mSpinnerFrom.setAdapter(adapter);
        mSpinnerTo.setAdapter(toAdapter);
    }

    /**
     * Using Hashmap to initialize FROM suggestion list
     */
    private HashMap<String, Suggestion> initFromHashmap() {
        mFromList = new HashMap<>();
        mFromList.put("Padam", new Suggestion(PADAM));
        mFromList.put("Tao Résa'Est", new Suggestion(TAO));
        mFromList.put("Flexigo", new Suggestion(FLEXIGO));
        mFromList.put("La Navette", new Suggestion(LA_NAVETTE));
        mFromList.put("Ilévia", new Suggestion(ILEVIA));
        mFromList.put("Night Bus", new Suggestion(NIGHT_BUS));
        mFromList.put("Free2Move", new Suggestion(FREE2MOVE));
        return mFromList;
    }

    /**
     * Using Hashmap to initialize TO suggestion list
     */
    private HashMap<String, Suggestion> initToHashmap() {
        mToList = new HashMap<>();
        mToList.put("Les Halles", new Suggestion(LESHALLES));
        mToList.put("Trocadéro", new Suggestion(TROCADERO));
        mToList.put("Place d'italie", new Suggestion(PLACEDITALIE));
        mToList.put("Opéra", new Suggestion(OPERA));
        mToList.put("Vincennes", new Suggestion(VINCENNES));
        mToList.put("Saint-Lazare", new Suggestion(SAINTLAZARE));
        mToList.put("Olymipades", new Suggestion(OLYMPIADES));
        return mToList;
    }

    /**
     * Define what to do after the button click interaction
     */
    //TODO : Implement the same thing for "To" spinner
    @OnClick(R.id.button_search)
    void onClickSearch() {

        /*
            Retrieve selection of "From" and "To" spinner
         */
        String selectedFrom = String.valueOf(mSpinnerFrom.getSelectedItem());
        String selectedTo = String.valueOf(mSpinnerTo.getSelectedItem());
        if (selectedFrom != null || !selectedFrom.isEmpty() && selectedTo != null || !selectedTo.isEmpty()) {
            mapDelegate.clearMap();
            Suggestion selectedFromSuggestion = mFromList.get(selectedFrom);
            Suggestion selectedToSuggestion = mToList.get(selectedTo);

            ArrayList<MarkerType> markerTypes = new ArrayList<>();
            markerTypes.add(MarkerType.PICKUP);
            markerTypes.add(MarkerType.DROPOFF);

            ArrayList<String> titles = new ArrayList<>();
            titles.add(selectedFrom);
            titles.add(selectedTo);

            ArrayList<LatLng> addresses = new ArrayList<>();
            addresses.add(selectedFromSuggestion.getLatLng());
            addresses.add(selectedToSuggestion.getLatLng());

            mapDelegate.updateMarker(markerTypes, titles, addresses);
            mapDelegate.updateMap(addresses);
            mapDelegate.displayItinerary(addresses);
        }

    }
}
