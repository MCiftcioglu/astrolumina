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

class HoroscopeActivity : AppCompatActivity() {

    private lateinit var spinnerSun: Spinner
    private lateinit var spinnerMoon: Spinner
    private lateinit var spinnerRising: Spinner
    private lateinit var textViewResult: TextView
    private lateinit var buttonGenerate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horoscope)

        // View'ları bağla
        spinnerSun = findViewById(R.id.spinnerSun)
        spinnerMoon = findViewById(R.id.spinnerMoon)
        spinnerRising = findViewById(R.id.spinnerRising)
        textViewResult = findViewById(R.id.textViewResult)
        buttonGenerate = findViewById(R.id.buttonGenerate)
        bottomNavigation = findViewById(R.id.bottomNavigation)

        bottomNavigation.setOnItemSelectedListener { item ->
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
            val sun = spinnerSun.selectedItem.toString()
            val moon = spinnerMoon.selectedItem.toString()
            val rising = spinnerRising.selectedItem.toString()

            // Kullanıcının kayıtlı cinsiyetini al
            val sharedPref = getSharedPreferences("AstroPrefs", Context.MODE_PRIVATE)
            val gender = sharedPref.getString("gender", "") ?: ""

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
