package com.upidea.astrolumina.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.astroapp.R

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

        setContentView(R.layout.activity_home)

        val welcomeText = findViewById<TextView>(R.id.textWelcome)
        val logoutButton = findViewById<Button>(R.id.buttonLogout)

        welcomeText.text = "Hoş geldin, $email"

        logoutButton.setOnClickListener {
            // Oturumu kapat
            sharedPref.edit().clear().apply()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
