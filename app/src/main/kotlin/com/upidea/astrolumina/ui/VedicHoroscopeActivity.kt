package com.upidea.astrolumina.ui.vedic

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.upidea.astrolumina.R
import com.upidea.astrolumina.utils.AstroUtils

class VedicHoroscopeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vedic_horoscope)

        // Kullanıcı doğum bilgilerini SharedPreferences'ten alıyoruz
        val sharedPref = getSharedPreferences("AstroPrefs", MODE_PRIVATE)
        val birthDate = sharedPref.getString("birthDate", null)
        val birthTime = sharedPref.getString("birthTime", null)
        val birthPlace = sharedPref.getString("birthPlace", null)

        val textResult = findViewById<TextView>(R.id.textVedicResult)

        if (birthDate != null && birthTime != null && birthPlace != null) {
            val vedicInfo = AstroUtils.calculateVedicHoroscope(birthDate, birthTime, birthPlace)
            textResult.text = vedicInfo
        } else {
            textResult.text = "Doğum bilgileri eksik. Lütfen profilinizden tamamlayın."
        }
    }
}
