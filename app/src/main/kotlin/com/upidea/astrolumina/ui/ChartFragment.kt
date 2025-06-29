package com.upidea.astrolumina.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.upidea.astrolumina.R
import com.upidea.astrolumina.api.GeminiService
import com.upidea.astrolumina.utils.GeminiHelper
import kotlinx.coroutines.launch

class ChartFragment : Fragment() {

    private lateinit var imageSunSign: ImageView
    private lateinit var imageMoonSign: ImageView
    private lateinit var imageRisingSign: ImageView
    private lateinit var textSunSign: TextView
    private lateinit var textMoonSign: TextView
    private lateinit var textRisingSign: TextView
    private lateinit var textViewResult: TextView
    private lateinit var buttonGenerate: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // View'ları bağla
        imageSunSign = view.findViewById(R.id.imageSunSign)
        imageMoonSign = view.findViewById(R.id.imageMoonSign)
        imageRisingSign = view.findViewById(R.id.imageRisingSign)
        textSunSign = view.findViewById(R.id.textSunSign)
        textMoonSign = view.findViewById(R.id.textMoonSign)
        textRisingSign = view.findViewById(R.id.textRisingSign)
        textViewResult = view.findViewById(R.id.textViewResult)
        buttonGenerate = view.findViewById(R.id.buttonGenerate)

        // 🔮 Kaydedilmiş burçları oku ve göster
        val sharedPref = requireActivity().getSharedPreferences("AstroPrefs", Context.MODE_PRIVATE)
        val sun = sharedPref.getString("sunSign", "") ?: ""
        val moon = sharedPref.getString("moonSign", "") ?: ""
        val rising = sharedPref.getString("risingSign", "") ?: ""

        if (sun.isBlank() || moon.isBlank() || rising.isBlank()) {
            // Eğer bilgiler eksikse kullanıcıyı bilgilendir
            textSunSign.text = "N/A"
            textMoonSign.text = "N/A"
            textRisingSign.text = "N/A"
            textViewResult.text = "Your birth chart information is incomplete. Please go to your profile and save your birth details first."
            buttonGenerate.isEnabled = false // Butonu devre dışı bırak
            return
        }

        // Burç isimlerini ve ikonlarını ayarla
        textSunSign.text = sun
        textMoonSign.text = moon
        textRisingSign.text = rising

        imageSunSign.setImageResource(getZodiacDrawable(sun))
        imageMoonSign.setImageResource(getZodiacDrawable(moon))
        imageRisingSign.setImageResource(getZodiacDrawable(rising))

        // İlk bilgilendirici mesaj
        textViewResult.text = "Press the button to get your birth chart interpretation."

        buttonGenerate.setOnClickListener {
            val gender = sharedPref.getString("gender", "") ?: ""

            // Doğum haritası için özel prompt oluştur
            val prompt = GeminiHelper.createBirthChartPrompt(sun, moon, rising, gender)

            textViewResult.text = "Interpreting your birth chart..."
            buttonGenerate.isEnabled = false // Tekrar tıklamayı önle

            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val result = GeminiService.getAstrologyInterpretation(prompt)
                    if (!result.isNullOrBlank()) {
                        textViewResult.text = result
                    } else {
                        textViewResult.text = "Failed to get interpretation. Please try again later."
                    }
                } catch (e: Exception) {
                    textViewResult.text = "An error occurred: ${e.message}"
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
                buttonGenerate.isEnabled = true // Butonu tekrar aktif et
            }
        }
    }

    private fun getZodiacDrawable(sign: String): Int {
        return when (sign.split(" ")[0].lowercase()) {
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
    }
}
