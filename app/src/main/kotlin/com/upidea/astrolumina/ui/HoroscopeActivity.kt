package com.upidea.astrolumina.ui.horoscope

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.upidea.astrolumina.R
import com.upidea.astrolumina.api.GeminiService
import com.upidea.astrolumina.utils.GeminiHelper
import com.upidea.astrolumina.ui.ProfileActivity
import com.upidea.astrolumina.ui.HomeActivity
import com.upidea.astrolumina.ui.ChatActivity
import com.upidea.astrolumina.ui.chart.ChartActivity
import android.view.MenuItem


class HoroscopeActivity : AppCompatActivity() {

    private lateinit var textSun: TextView
    private lateinit var textMoon: TextView
    private lateinit var textRising: TextView
    private lateinit var textViewResult: TextView
    private lateinit var buttonGenerate: Button
    private lateinit var bottomNavigation: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horoscope)

        // View'ları bağla
        textSun = findViewById(R.id.textSun)
        textMoon = findViewById(R.id.textMoon)
        textRising = findViewById(R.id.textRising)
        textViewResult = findViewById(R.id.textViewResult)
        buttonGenerate = findViewById(R.id.buttonGenerate)
        bottomNavigation = findViewById(R.id.bottomNavigation)

        val sharedPref = getSharedPreferences("AstroPrefs", Context.MODE_PRIVATE)
        textSun.text = sharedPref.getString("sunSign", "")
        textMoon.text = sharedPref.getString("moonSign", "")
        textRising.text = sharedPref.getString("risingSign", "")

        bottomNavigation.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.navigation_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                R.id.navigation_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.navigation_messages -> {
                    startActivity(Intent(this, ChatActivity::class.java))
                    true
                }
                R.id.navigation_chart -> {
                    startActivity(Intent(this, ChartActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Tıklama ile yorum oluştur
        buttonGenerate.setOnClickListener {
            val prefs = getSharedPreferences("AstroPrefs", Context.MODE_PRIVATE)
            val sun = prefs.getString("sunSign", "") ?: ""
            val moon = prefs.getString("moonSign", "") ?: ""
            val rising = prefs.getString("risingSign", "") ?: ""

            // Kullanıcının kayıtlı cinsiyetini al
            val gender = prefs.getString("gender", "") ?: ""

            // AI için prompt oluştur
            val prompt = GeminiHelper.createAstrologyPrompt(sun, moon, rising, gender)

            // Gemini API ile yorum al
            GeminiService.getAstrologyInterpretation(prompt) { result ->
                runOnUiThread {
                    if (result != null) {
                        textViewResult.text = result
                    } else {
                        Toast.makeText(this, "Yorum alınamadı.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
