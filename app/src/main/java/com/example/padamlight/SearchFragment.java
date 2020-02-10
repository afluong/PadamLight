package com.example.padamlight;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.padamlight.utils.Toolbox;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
/*
* SearchActivity was become SearchFragment to use a navigationdrawer*/
public class SearchFragment extends Fragment {

    @Bind(R.id.spinner_from)
    Spinner mSpinnerFrom;
    @Bind(R.id.spinner_to)
    Spinner mSpinnerTo;
    @Bind(R.id.button_search)
    Button mButtonSearch;

    private MapActionsDelegate mapDelegate;
    private HashMap<String, Suggestion> mFromList;
    /*Creation of the Tolist*/
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

   /* @Override
    protected void onCreateView(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Binding UI elements defined below
        ButterKnife.bind(this);

        initializeTextViews();
        initSpinners();
        initMap();
    }*/
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.activity_main, container, false);
       // Binding UI elements defined below
       ButterKnife.bind(this,view);

       initializeTextViews();
       initSpinners();
       initMap();
       return view;
   }

    private void initializeTextViews() {
        mButtonSearch.setText(R.string.button_search);
        /*changing color for the research button text*/
        mButtonSearch.setTextColor(getResources().getColor(R.color.colorBackground));
    }

    private void initMap() {
        /*
            Instanciate MapFragment to get the map on the page
         */
        MapFragment mapFragment = MapFragment.newInstance();

        assert getFragmentManager() != null;
        getFragmentManager().beginTransaction()
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, fromList);
        mSpinnerFrom.setAdapter(adapter);
        /*Set the To spinner*/
        List<String> toList = Toolbox.formatHashmapToList(initToHashmap());
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, toList);
        mSpinnerTo.setAdapter(adapter2);
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
     * Using Hashmap to initialize To suggestion list
     */
    private HashMap<String, Suggestion> initToHashmap() {
        mToList = new HashMap<>();
        mToList.put("Padam", new Suggestion(PADAM));
        mToList.put("Tao Résa'Est", new Suggestion(TAO));
        mToList.put("Flexigo", new Suggestion(FLEXIGO));
        mToList.put("La Navette", new Suggestion(LA_NAVETTE));
        mToList.put("Ilévia", new Suggestion(ILEVIA));
        mToList.put("Night Bus", new Suggestion(NIGHT_BUS));
        mToList.put("Free2Move", new Suggestion(FREE2MOVE));
        return mToList;
    }
    /**
     * Define what to do after the button click interaction
     */
    //TODO : Implement the same thing for "To" spinner
    @OnClick(R.id.button_search)
    void onClickSearch() {

        /*
            Retrieve selection of "From" spinner
            Retrieve selection of "To" spinner
         */
        String selectedFrom = String.valueOf(mSpinnerFrom.getSelectedItem());
        String selectedTo = String.valueOf(mSpinnerTo.getSelectedItem());
        if (selectedFrom != null || !selectedFrom.isEmpty()) {
            mapDelegate.clearMap();
            Suggestion selectedFromSuggestion = mFromList.get(selectedFrom);
            Suggestion selectedToSuggestion = mFromList.get(selectedTo);
            mapDelegate.updateMarker(MarkerType.PICKUP, selectedFrom, selectedFromSuggestion.getLatLng());
            mapDelegate.updateMarker(MarkerType.DROPOFF, selectedTo, selectedToSuggestion.getLatLng());
            mapDelegate.updateMap(selectedFromSuggestion.getLatLng(),selectedToSuggestion.getLatLng());
        }


    }
}


