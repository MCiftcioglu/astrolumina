package com.upidea.astrolumina.ui.horoscope

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
        val sun = sharedPref.getString("sunSign", "") ?: ""
        val moon = sharedPref.getString("moonSign", "") ?: ""
        val rising = sharedPref.getString("risingSign", "") ?: ""
        val gender = sharedPref.getString("gender", "") ?: ""

        textSun.text = sun
        textMoon.text = moon
        textRising.text = rising

        Log.d("Horoscope", "Loaded signs: Sun=$sun, Moon=$moon, Rising=$rising, Gender=$gender")

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

        buttonGenerate.setOnClickListener {
            if (sun.isBlank() || moon.isBlank() || rising.isBlank()) {
                Toast.makeText(this, "Burç bilgileriniz eksik. Lütfen doğum bilgilerinizi kontrol edin.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val prompt = GeminiHelper.createAstrologyPrompt(sun, moon, rising, gender)
            Log.d("Horoscope", "Generated prompt: $prompt")

            GeminiService.getAstrologyInterpretation(prompt) { result ->
                runOnUiThread {
                    if (!result.isNullOrBlank()) {
                        textViewResult.text = result
                    } else {
                        textViewResult.text = "Yorum alınamadı."
                        Toast.makeText(this, "Yorum alınamadı. Lütfen daha sonra tekrar deneyin.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
