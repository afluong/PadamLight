package com.example.padamlight

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import butterknife.Bind
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.padamlight.utils.Toolboxkt
import com.google.android.gms.maps.model.LatLng
import java.util.*
import kotlinx.android.synthetic.main.activity_main.*
/*
* Kotlin version of SearchFragment whithout butterknife method and kotlin declarations.*/
class SearchFragmentkt : Fragment() {

    private var mapDelegate: MapActionsDelegatekt? = null
    private var mFromList: HashMap<String, Suggestionkt>? = null
    /*Creation of the Tolist*/
    private var mToList: HashMap<String, Suggestionkt>? = null

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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_main, container, false)



        initMap()
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeTextViews()
        initSpinners()
        button_search.setOnClickListener {
           onClickSearch()
        }
    }

    private fun initializeTextViews() {
        button_search!!.setText(R.string.button_search)
        /*changing color for the research button text*/button_search!!.setTextColor(resources.getColor(R.color.colorBackground))
    }

    private fun initMap() { /*
            Instanciate MapFragment to get the map on the page
         */
        val mapFragmentkt = MapFragmentkt.newInstance()
        assert(fragmentManager != null)
        fragmentManager!!.beginTransaction()
                .replace(R.id.fragment_map, mapFragmentkt)
                .commitAllowingStateLoss()
        //
        mapDelegate = mapFragmentkt
    }

    /**
     * Initialize spinners from and to
     */
// TODO : Initialize the "To" spinner with data of your choice
    private fun initSpinners() {
        val fromList = Toolboxkt.formatHashmapToList(initFromHashmap())
        val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, fromList)
        spinner_from!!.adapter = adapter
        /*Set the To spinner*/
        val toList = Toolboxkt.formatHashmapToList(initToHashmap())
        val adapter2 = ArrayAdapter(context, android.R.layout.simple_list_item_1, toList)
        spinner_to!!.adapter = adapter2
    }

    /**
     * Using Hashmap to initialize FROM suggestion list
     */
    private fun initFromHashmap(): HashMap<String, Suggestionkt> {
        mFromList = HashMap()
        mFromList!!["Padam"] = Suggestionkt(PADAM)
        mFromList!!["Tao Résa'Est"] = Suggestionkt(TAO)
        mFromList!!["Flexigo"] = Suggestionkt(FLEXIGO)
        mFromList!!["La Navette"] = Suggestionkt(LA_NAVETTE)
        mFromList!!["Ilévia"] = Suggestionkt(ILEVIA)
        mFromList!!["Night Bus"] = Suggestionkt(NIGHT_BUS)
        mFromList!!["Free2Move"] = Suggestionkt(FREE2MOVE)
        return mFromList as HashMap<String, Suggestionkt>
    }

    /**
     * Using Hashmap to initialize To suggestion list
     */
    private fun initToHashmap(): HashMap<String, Suggestionkt> {
        mToList = HashMap()
        mToList!!["Padam"] = Suggestionkt(PADAM)
        mToList!!["Tao Résa'Est"] = Suggestionkt(TAO)
        mToList!!["Flexigo"] = Suggestionkt(FLEXIGO)
        mToList!!["La Navette"] = Suggestionkt(LA_NAVETTE)
        mToList!!["Ilévia"] = Suggestionkt(ILEVIA)
        mToList!!["Night Bus"] = Suggestionkt(NIGHT_BUS)
        mToList!!["Free2Move"] = Suggestionkt(FREE2MOVE)
        return mToList as HashMap<String, Suggestionkt>
    }

    /**
     * Define what to do after the button click interaction
     */
//TODO : Implement the same thing for "To" spinner

    fun onClickSearch() { /*
            Retrieve selection of "From" spinner
            Retrieve selection of "To" spinner
         */
        val selectedFrom = spinner_from!!.selectedItem.toString()
        val selectedTo = spinner_to!!.selectedItem.toString()
        if (selectedFrom != null && selectedTo!= null ) {
            mapDelegate!!.clearMap()
            val selectedFromSuggestionkt = mFromList!![selectedFrom]
            val selectedToSuggestionkt = mFromList!![selectedTo]
            mapDelegate!!.updateMarker(MarkerTypekt.PICKUP, selectedFrom, selectedFromSuggestionkt?.latLng)
            mapDelegate!!.updateMarker(MarkerTypekt.DROPOFF, selectedTo, selectedToSuggestionkt?.latLng)
            mapDelegate!!.updateMap(selectedFromSuggestionkt?.latLng, selectedToSuggestionkt?.latLng)
        }
    }

    companion object {
        /*
        Constants FROM lat lng
     */
        var PADAM = LatLng(48.8609, 2.349299999999971)
        var TAO = LatLng(47.9022, 1.9040499999999838)
        var FLEXIGO = LatLng(48.8598, 2.0212400000000343)
        var LA_NAVETTE = LatLng(48.8783804, 2.590549)
        var ILEVIA = LatLng(50.632, 3.05749000000003)
        var NIGHT_BUS = LatLng(45.4077, 11.873399999999947)
        var FREE2MOVE = LatLng(33.5951, -7.618780000000015)
    }
}
