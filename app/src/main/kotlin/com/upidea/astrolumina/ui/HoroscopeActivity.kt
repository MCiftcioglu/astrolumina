package com.upidea.astrolumina.ui.horoscope

import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.upidea.astrolumina.R
import com.upidea.astrolumina.api.GeminiService
import com.upidea.astrolumina.utils.GeminiHelper

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
