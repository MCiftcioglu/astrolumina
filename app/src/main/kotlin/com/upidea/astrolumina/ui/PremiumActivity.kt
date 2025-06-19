package com.upidea.astrolumina.ui.premium

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.upidea.astrolumina.R
import com.upidea.astrolumina.ui.HomeActivity

class PremiumActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_premium)

        val infoText = findViewById<TextView>(R.id.textPremiumInfo)
        val activateButton = findViewById<Button>(R.id.buttonActivate)

        infoText.text = """
            Premium ile şunlara erişebilirsiniz:
            • Güneş, Ay ve Yükselen burç analizi
            • Doğum haritası özellikleri
            • Burcunuza uygun insanlarla tanışma fırsatı
            • Mesajlaşırken canlı çeviri özelliği
            • Gelecekte daha fazla özel içerik
        """.trimIndent()

        activateButton.setOnClickListener {
            val sharedPref = getSharedPreferences("AstroPrefs", Context.MODE_PRIVATE)
            sharedPref.edit().putBoolean("isPremium", true).apply()

            Toast.makeText(this, "Premium aktif edildi!", Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}
