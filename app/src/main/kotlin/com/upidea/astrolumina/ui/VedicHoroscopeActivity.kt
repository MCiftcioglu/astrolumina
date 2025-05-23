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

        val sharedPref = getSharedPreferences("AstroPrefs", MODE_PRIVATE)
        val birthDate = sharedPref.getString("birthDate", null)
        val birthTime = sharedPref.getString("birthTime", null)
        val birthPlace = sharedPref.getString("birthPlace", null)
        val gender = sharedPref.getString("gender", null)

        val textResult = findViewById<TextView>(R.id.textVedicResult)

        if (!birthDate.isNullOrEmpty() && !birthTime.isNullOrEmpty() && !birthPlace.isNullOrEmpty() && !gender.isNullOrEmpty()) {
            val vedicInfo = AstroUtils.calculateVedicHoroscopeViaPython(
                this,
                birthDate,
                birthTime,
                birthPlace,
                gender
            )

            textResult.text = vedicInfo
        } else {
            textResult.text = "Doğum bilgileri eksik. Lütfen profilinizden tamamlayın."
        }
    }
}
