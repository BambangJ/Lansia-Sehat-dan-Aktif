package com.bams.lansiasehataktif

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.android.gms.maps.MapsInitializer
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, NewsFragment<Any>())
                .commit()
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_news -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, NewsFragment<Any>())
                        .commit()
                    true
                }
                R.id.nav_map -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, MapFragment())
                        .commit()
                    true
                }
                R.id.navigation_rekomendasi -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, RekomendasiFragment())
                        .commit()
                    true
                }
                R.id.nav_reminder -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ReminderFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}

