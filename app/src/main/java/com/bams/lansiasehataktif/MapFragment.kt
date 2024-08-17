package com.bams.lansiasehataktif

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton
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
    private lateinit var expandableButton: MaterialButton
    private lateinit var listPlace: RecyclerView
    private var isExpanded = false

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
            .replace(R.id.mapView, mapFragment)
            .commit()
        mapFragment.getMapAsync(this)

        // Initialize RecyclerView
        placeRecyclerView = view.findViewById(R.id.list_place)
        placeRecyclerView.layoutManager = LinearLayoutManager(context)

        expandableButton = view.findViewById(R.id.expandable_button)
        listPlace = view.findViewById(R.id.list_place)

        expandableButton.setOnClickListener {
            toggleRecyclerViewVisibility()
        }

        remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("MapFragment", "Remote Config fetch succeeded")
                    fetchGmap()
                } else {
                    Log.e("MapFragment", "Remote Config fetch failed")
                }
            }
        return view
    }

    private fun fetchGmap() {
        val apiKey = remoteConfig.getString("google_api_key")
        val resources = resources
        resources.getString(R.string.google_maps_key).replace("YOUR_API_KEY", apiKey)
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

    private fun toggleRecyclerViewVisibility() {
        val expandAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.expand)
        val collapseAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.collapse)

        if (isExpanded) {
            placeRecyclerView.startAnimation(collapseAnim)
            placeRecyclerView.visibility = View.GONE
            expandableButton.text = "Show Places"
            expandableButton.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_expand_more)
        } else {
            placeRecyclerView.visibility = View.VISIBLE
            placeRecyclerView.startAnimation(expandAnim)
            expandableButton.text = "Show Less"
            expandableButton.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_expand_less)
        }
        isExpanded = !isExpanded
    }

}
