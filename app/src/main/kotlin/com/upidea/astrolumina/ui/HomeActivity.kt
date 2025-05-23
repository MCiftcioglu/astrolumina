package com.upidea.astrolumina.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.upidea.astrolumina.R
import com.upidea.astrolumina.ui.auth.LoginActivity
import com.upidea.astrolumina.ui.HomeFragment




class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Oturum kontrolü
        val sharedPref = getSharedPreferences("AstroPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)
        val email = sharedPref.getString("userEmail", null)

        if (!isLoggedIn || email == null) {
            // Oturum yoksa Login ekranına gönder
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        // activity_home.xml dosyasını yükle
        setContentView(R.layout.activity_home)

        // HomeFragment'i başlat
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment())
            .commit()
    }
}
