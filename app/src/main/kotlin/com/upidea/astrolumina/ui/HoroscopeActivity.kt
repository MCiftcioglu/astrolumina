package com.upidea.astrolumina.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.upidea.astrolumina.R
import com.upidea.astrolumina.api.GeminiService
import com.upidea.astrolumina.utils.GeminiHelper
import com.upidea.astrolumina.ui.ProfileActivity
import com.upidea.astrolumina.ui.HomeActivity
import com.upidea.astrolumina.ui.ChatActivity
import com.upidea.astrolumina.ui.chart.ChartActivity
import kotlinx.coroutines.launch
import android.view.MenuItem
import android.widget.ImageView

class HoroscopeActivity : AppCompatActivity() {

    private lateinit var textSun: TextView
    private lateinit var textMoon: TextView
    private lateinit var textRising: TextView
    private lateinit var imageSun: ImageView
    private lateinit var imageMoon: ImageView
    private lateinit var imageRising: ImageView
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
        imageSun = findViewById(R.id.imageSun)
        imageMoon = findViewById(R.id.imageMoon)
        imageRising = findViewById(R.id.imageRising)
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

        // Sun Sign için ikon seçimi
        val sunDrawableId = when (sun.lowercase()) {
            "koç" -> R.drawable.aries_icon
            "boğa" -> R.drawable.taurus_icon
            "ikizler" -> R.drawable.gemini_icon
            "yengeç" -> R.drawable.cancer_icon
            "aslan" -> R.drawable.leo_icon
            "başak" -> R.drawable.virgo_icon
            "terazi" -> R.drawable.libra_icon
            "akrep" -> R.drawable.scorpio_icon
            "yay" -> R.drawable.sagittarius_icon
            "oğlak" -> R.drawable.capricorn_icon
            "kova" -> R.drawable.aquarius_icon
            "balık" -> R.drawable.pisces_icon
            else -> R.drawable.ic_sun_placeholder
        }
        imageSun.setImageResource(sunDrawableId)

        // Diğer ikonlar sabit
        imageMoon.setImageResource(R.drawable.ic_vedic3)
        imageRising.setImageResource(R.drawable.ic_rising)

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

            textViewResult.text = "Günlük yorumunuz oluşturuluyor..."

            lifecycleScope.launch {
                GeminiService.getAstrologyInterpretation(prompt) { result ->
                    runOnUiThread {
                        if (!result.isNullOrBlank()) {
                            textViewResult.text = result
                        } else {
                            textViewResult.text = "Yorum alınamadı."
                            Toast.makeText(this@HoroscopeActivity, "Yorum alınamadı. Lütfen daha sonra tekrar deneyin.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}
