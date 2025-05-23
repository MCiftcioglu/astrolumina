package com.upidea.astrolumina.ui

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.upidea.astrolumina.R
import com.upidea.astrolumina.api.createAstrologyPrompt
import com.upidea.astrolumina.api.GeminiService
import com.upidea.astrolumina.ui.premium.PremiumActivity
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import java.util.*

class ChartFragment : Fragment() {

    private lateinit var textSun: TextView
    private lateinit var textMoon: TextView
    private lateinit var textRising: TextView
    private lateinit var textTitle: TextView
    private lateinit var textDetails: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chart, container, false)

        textTitle = view.findViewById(R.id.textTitle)
        textSun = view.findViewById(R.id.textSunSign)
        textMoon = view.findViewById(R.id.textMoonSign)
        textRising = view.findViewById(R.id.textRisingSign)
        textDetails = view.findViewById(R.id.textChart)

        val sharedPref = requireActivity().getSharedPreferences("AstroPrefs", Context.MODE_PRIVATE)
        val isPremium = sharedPref.getBoolean("isPremium", false)

        if (!isPremium) {
            textTitle.text = "🔒 Premium Özellik"
            textDetails.text = "Doğum haritası analizi sadece premium kullanıcılar içindir."
            return view
        }

        val name = sharedPref.getString("name", "Kullanıcı")
        val birthDate = sharedPref.getString("birthDate", "14/03/1990") ?: "14/03/1990"
        val birthTime = sharedPref.getString("birthTime", "10:30") ?: "10:30"
        val birthPlace = sharedPref.getString("birthPlace", "Ankara") ?: "Ankara"

        val coordinates = getCoordinatesFromPlace(requireContext(), birthPlace)
        if (coordinates == null) {
            textDetails.text = "Doğum yeri çözümlenemedi: $birthPlace"
            return view
        }

        val (latitude, longitude) = coordinates

        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(requireContext()))
        }

        try {
            val py = Python.getInstance()
            val module = py.getModule("astrology_utils")
            val result = module.callAttr("calculate_signs", birthDate, birthTime, latitude, longitude)

            val sunSign = result["sun"]?.toString() ?: "Bilinmiyor"
            val moonSign = result["moon"]?.toString() ?: "Bilinmiyor"
            val risingSign = result["ascendant"]?.toString() ?: "Bilinmiyor"

            // Burç kartlarını güncelle
            textTitle.text = "$name'nin Doğum Haritası"
            textSun.text = "☀ $sunSign"
            textMoon.text = "🌙 $moonSign"
            textRising.text = "⬆ $risingSign"

            // Gemini API'den astrolojik yorum al
            val prompt = createAstrologyPrompt(sunSign, moonSign, risingSign)
            GeminiService.getAstrologyInterpretation(prompt) { response ->
                requireActivity().runOnUiThread {
                    if (response != null) {
                        textDetails.text = response
                    } else {
                        textDetails.text = "Yorum alınamadı. Lütfen daha sonra tekrar deneyin."
                    }
                }
            }

        } catch (e: Exception) {
            textDetails.text = "Hesaplama hatası: ${e.localizedMessage}"
        }

        return view
    }

    private fun getCoordinatesFromPlace(context: Context, placeName: String): Pair<Double, Double>? {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocationName(placeName, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val location = addresses[0]
                Pair(location.latitude, location.longitude)
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
