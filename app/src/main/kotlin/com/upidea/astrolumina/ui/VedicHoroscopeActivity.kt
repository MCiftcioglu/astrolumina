package com.upidea.astrolumina.ui

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.upidea.astrolumina.R
import com.upidea.astrolumina.api.GeminiService
import com.upidea.astrolumina.utils.GeminiHelper
import kotlinx.coroutines.launch

class VedicHoroscopeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vedic_horoscope)

        // View'ları bağla
        val textUserName: TextView = findViewById(R.id.textUserName)
        val textUserBirth: TextView = findViewById(R.id.textUserBirth)
        val textUserPlace: TextView = findViewById(R.id.textUserPlace)
        val textSunSign: TextView = findViewById(R.id.textSunSign)
        val textMoonSign: TextView = findViewById(R.id.textMoonSign)
        val textRisingSign: TextView = findViewById(R.id.textRisingSign)
        val textVedicResult: TextView = findViewById(R.id.textVedicResult)
        val btnPremium: Button = findViewById(R.id.btnPremium)

        // SharedPreferences'tan verileri oku
        val sharedPref = getSharedPreferences("AstroPrefs", Context.MODE_PRIVATE)
        val name = sharedPref.getString("name", "N/A")
        val birthDate = sharedPref.getString("birthDate", "N/A")
        val birthTime = sharedPref.getString("birthTime", "N/A")
        val birthPlace = sharedPref.getString("birthPlace", "N/A")
        val sunSign = sharedPref.getString("sunSign", "N/A")
        val moonSign = sharedPref.getString("moonSign", "N/A")
        val risingSign = sharedPref.getString("risingSign", "N/A")
        val gender = sharedPref.getString("gender", "") ?: ""

        // Bilgilerin eksik olup olmadığını kontrol et
        if (name == "N/A" || sunSign == "N/A" || moonSign == "N/A" || risingSign == "N/A") {
            textVedicResult.text = "Vedic interpretation requires complete profile information. Please go to your profile and save your birth details first."
            Toast.makeText(this, "Incomplete profile data.", Toast.LENGTH_LONG).show()
            return
        }

        // Ekranı dinamik verilerle doldur
        textUserName.text = "Ad: $name"
        textUserBirth.text = "Doğum: $birthDate · $birthTime"
        textUserPlace.text = "Yer: $birthPlace"
        textSunSign.text = "☀️ Güneş: $sunSign"
        textMoonSign.text = "🌕 Ay Burcu: $moonSign"
        textRisingSign.text = "🪐 Yükselen: $risingSign"

        // Gemini'den Vedik yorumu al
        textVedicResult.text = "Your Vedic chart is being analyzed based on the ancient system..."

        lifecycleScope.launch {
            try {
                val prompt = GeminiHelper.createVedicAstrologyPrompt(sunSign, moonSign, risingSign, gender)
                val result = GeminiService.getAstrologyInterpretation(prompt)
                if (!result.isNullOrBlank()) {
                    textVedicResult.text = result
                } else {
                    textVedicResult.text = "Failed to get Vedic interpretation. Please try again later."
                }
            } catch (e: Exception) {
                textVedicResult.text = "An error occurred: ${e.message}"
                Toast.makeText(this@VedicHoroscopeActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        // Premium butonuna tıklama olayı (isteğe bağlı)
        btnPremium.setOnClickListener {
            // PremiumActivity'yi başlat
            // val intent = Intent(this, PremiumActivity::class.java)
            // startActivity(intent)
            Toast.makeText(this, "Premium feature coming soon!", Toast.LENGTH_SHORT).show()
        }
    }
}