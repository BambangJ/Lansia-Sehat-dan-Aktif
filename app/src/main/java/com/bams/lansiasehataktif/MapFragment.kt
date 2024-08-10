package com.bams.lansiasehataktif

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val db = FirebaseFirestore.getInstance()
    private lateinit var placeRecyclerView: RecyclerView
    private lateinit var placeAdapter: PlaceAdapter
    private lateinit var remoteConfig: FirebaseRemoteConfig
    private lateinit var apiKey: String

    companion object {
        fun newInstance(apiKey: String): MapFragment {
            val fragment = MapFragment()
            fragment.apiKey = apiKey
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        // Initialize Map
        val mapFragment = SupportMapFragment.newInstance()
        childFragmentManager.beginTransaction()
            .replace(R.id.map_container, mapFragment)
            .commit()
        mapFragment.getMapAsync(this)

        // Initialize RecyclerView
        placeRecyclerView = view.findViewById(R.id.place_list)
        placeRecyclerView.layoutManager = LinearLayoutManager(context)

        remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600) // 1 hour
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)


        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("NewsFragment", "Remote Config fetch succeeded")
                    fetchGmap()
                } else {
                    Log.e("NewsFragment", "Remote Config fetch failed")
                }
            }
        return view
    }
    private fun fetchGmap() {
        val apiKey = remoteConfig.getString("google_api_key")
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Fetch places from Firestore
        db.collection("places").get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    Log.d("MapFragment", "Documents fetched successfully")
                    val places = documents.toObjects(Place::class.java)
                    // Setup RecyclerView
                    placeAdapter = PlaceAdapter(places) { place ->
                        val latLng = LatLng(place.geopoint.latitude, place.geopoint.longitude)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    }
                    placeRecyclerView.adapter = placeAdapter

                    // Add markers to the map
                    for (place in places) {
                        val latLng = LatLng(place.geopoint.latitude, place.geopoint.longitude)
                        mMap.addMarker(MarkerOptions().position(latLng).title(place.name))
                    }
                    val firstPlace = places.first()
                    val firstLatLng = LatLng(firstPlace.geopoint.latitude, firstPlace.geopoint.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLatLng, 10f))
                } else {
                    Log.d("MapFragment", "No documents found")
                }
            }
            .addOnFailureListener { e ->
                Log.e("MapFragment", "Failed to fetch documents", e)
            }
    }
}
