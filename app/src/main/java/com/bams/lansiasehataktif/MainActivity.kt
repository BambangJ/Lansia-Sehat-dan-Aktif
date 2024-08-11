package com.bams.lansiasehataktif

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

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
                R.id.nav_profile -> {
                    showProfileMenu()
                    true
                }
                else -> false
            }
        }
    }

    private fun showProfileMenu() {
        val popupMenu = PopupMenu(this, findViewById<View>(R.id.nav_profile))
        popupMenu.menuInflater.inflate(R.menu.profile_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_logout -> {
                    logout()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun logout() {
        // Implementasi logout sesuai dengan kebutuhan aplikasi Anda
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
