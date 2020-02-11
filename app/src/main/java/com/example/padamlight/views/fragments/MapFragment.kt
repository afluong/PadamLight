package com.example.padamlight.views.fragments

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.example.padamlight.R
import com.example.padamlight.enums.MarkerType
import com.example.padamlight.interfaces.MapActionsDelegate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.alert_dialog_itinerary.view.*
import java.net.URL
import kotlin.concurrent.thread

class MapFragment : Fragment(), OnMapReadyCallback, MapActionsDelegate {
	private var mMap: GoogleMap? = null
	private var mBuilder = LatLngBounds.Builder()

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_map, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		// Instanciate map fragment
		val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment?
		mapFragment?.getMapAsync(this)
	}

	override fun onMapReady(googleMap: GoogleMap) {
		mMap = googleMap
		mMap.let {
			mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(47.902964, 1.9092510000000402), 16f))
		}
	}

	override fun updateMap(vararg latLngs: LatLng) {
		mMap.let {
			var builder = LatLngBounds.Builder()
			for (latLng in latLngs) {
				builder.include(latLng)
			}
			mBuilder = builder
			val bounds = builder.build()
			animateMapCamera(bounds)
		}
	}

	override fun buildItinenary(from: LatLng, to: LatLng) {
		val url = getURL(from, to)
		var result: String? = ""

		val options = PolylineOptions()
		options.color(Color.RED)
		options.width(5f)

		thread {
			result = httpGet(url).toString()
		}.join()

			val parser = Parser.default()
			val stringBuilder: StringBuilder = StringBuilder(result)
			val json: JsonObject = parser.parse(stringBuilder) as JsonObject

			val routes = json.array<JsonObject>("routes")
			val points = routes!!["legs"]["steps"][0] as JsonArray<JsonObject>

			val travelTime = routes["legs"]["duration"]["text"].value.toString()

			val polypts = points.flatMap { decodePoly(it.obj("polyline")?.string("points")!!) }
			options.add(from)
			for (point in polypts) options.add(point)
			options.add(to)
			mMap!!.addPolyline(options)

			mMap!!.moveCamera(CameraUpdateFactory.newLatLngBounds(mBuilder.build(), 100))


			displayManualPickingWarning(from, to, travelTime)
	}

	private fun displayManualPickingWarning(from: LatLng, to: LatLng, travelTime: String) {
		val view = layoutInflater.inflate(R.layout.alert_dialog_itinerary, null)
		val dialog = AlertDialog.Builder(ContextThemeWrapper(view.context, R.style.AppTheme))
				.setView(view)
				.setCancelable(true)
				.create()

		view.alert_late_delivery_title.text = String.format(getString(R.string.itinerary_dialog), travelTime)
		view.alert_return.setOnClickListener {
			dialog.dismiss()
		}

		view.alert_validate.setOnClickListener {
			openInMaps(from, to)
			dialog.dismiss()
		}
		dialog.show()
	}

	private fun openInMaps(from: LatLng, to: LatLng) {
		val origin = "saddr=" + from.latitude + "," + from.longitude
		val dest = "daddr=" + to.latitude + "," + to.longitude
		val params = "$origin&$dest"

		val intent = Intent(Intent.ACTION_VIEW,
				Uri.parse("http://maps.google.com/maps?$params"))
		startActivity(intent)
	}

	private fun httpGet(myURL: String?): String? = URL(myURL).readText()

	override fun updateMarker(type: MarkerType, markerName: String, markerLatLng: LatLng) {
		if (mMap != null) {
			val marker = MarkerOptions()
					.position(markerLatLng)
					.title(markerName)
					.icon(getMarkerIcon(type))
			mMap!!.addMarker(marker)
		}
	}

	override fun clearMap() {
		if (mMap != null) {
			mMap!!.clear()
		}
	}

	private fun animateMapCamera(bounds: LatLngBounds) {
		if (mMap != null) {
			mMap!!.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
		}
	}

	private fun getURL(from: LatLng, to: LatLng): String {
		val origin = "origin=" + from.latitude + "," + from.longitude
		val dest = "destination=" + to.latitude + "," + to.longitude
		val sensor = "sensor=false"
		val key = "key=" + getString(R.string.google_maps_key)
		val params = "$origin&$dest&$sensor&$key"
		return "https://maps.googleapis.com/maps/api/directions/json?$params"
	}

	private fun getMarkerIcon(type: MarkerType): BitmapDescriptor {

		val icon = when (type) {
			MarkerType.PICKUP -> {
				R.drawable.ic_pickup
			}
			MarkerType.DROPOFF -> {
				R.drawable.ic_dropoff
			}
		}
		return BitmapDescriptorFactory.fromResource(icon)
	}

	private fun decodePoly(encoded: String): List<LatLng> {
		val poly = ArrayList<LatLng>()
		var index = 0
		val len = encoded.length
		var lat = 0
		var lng = 0

		while (index < len) {
			var b: Int
			var shift = 0
			var result = 0
			do {
				b = encoded[index++].toInt() - 63
				result = result or (b and 0x1f shl shift)
				shift += 5
			} while (b >= 0x20)
			val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
			lat += dlat

			shift = 0
			result = 0
			do {
				b = encoded[index++].toInt() - 63
				result = result or (b and 0x1f shl shift)
				shift += 5
			} while (b >= 0x20)
			val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
			lng += dlng

			val p = LatLng(lat.toDouble() / 1E5,
					lng.toDouble() / 1E5)
			poly.add(p)
		}

		return poly
	}

	companion object {
		fun newInstance(): MapFragment {
			return MapFragment()
		}
	}
}