package com.upidea.astrolumina.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.upidea.astrolumina.R
import com.upidea.astrolumina.ui.chart.ChartActivity
import com.upidea.astrolumina.ui.HoroscopeActivity
import com.upidea.astrolumina.ui.match.MatchActivity
import com.upidea.astrolumina.ui.premium.PremiumActivity
import com.upidea.astrolumina.ui.vedic.VedicHoroscopeActivity
import com.upidea.astrolumina.ui.auth.LoginActivity

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val sharedPref = requireActivity().getSharedPreferences("AstroPrefs", Context.MODE_PRIVATE)
        val name = sharedPref.getString("name", null)

        val welcomeText = view.findViewById<TextView>(R.id.textWelcome)

        val dailyCard = view.findViewById<CardView>(R.id.cardDaily)
        val chartCard = view.findViewById<CardView>(R.id.cardChart)
        val matchCard = view.findViewById<CardView>(R.id.cardMatch)
        val vedicCard = view.findViewById<CardView>(R.id.cardVedicAstro)
        val dailyImage = view.findViewById<ImageView>(R.id.imageDaily)

        welcomeText.text = getString(R.string.welcome_message, name ?: "Kullanıcı")

        // Güneş burcuna göre ikon görselini ayarla
        val sunSignTr = sharedPref.getString("sunSign", "Koç") ?: "Koç"
        val sunSignEn = mapToEnglish(sunSignTr)
        val resId = resources.getIdentifier("${sunSignEn}_icon", "drawable", requireContext().packageName)
        if (resId != 0) {
            dailyImage.setImageResource(resId)
        }



        dailyCard.setOnClickListener {
            startActivity(Intent(requireContext(), HoroscopeActivity::class.java))
        }

        chartCard.setOnClickListener {
            startActivity(Intent(requireContext(), ChartActivity::class.java))
        }

        matchCard.setOnClickListener {
            val target = if (sharedPref.getBoolean("isPremium", false)) {
                MatchActivity::class.java
            } else {
                PremiumActivity::class.java
            }
            startActivity(Intent(requireContext(), target))
        }

        vedicCard.setOnClickListener {
            val target = if (sharedPref.getBoolean("isPremium", false)) {
                VedicHoroscopeActivity::class.java
            } else {
                PremiumActivity::class.java
            }
            startActivity(Intent(requireContext(), target))
        }

        return view
    }

    private fun mapToEnglish(sign: String): String {
        return when (sign) {
            "Koç" -> "aries"
            "Boğa" -> "taurus"
            "İkizler" -> "gemini"
            "Yengeç" -> "cancer"
            "Aslan" -> "leo"
            "Başak" -> "virgo"
            "Terazi" -> "libra"
            "Akrep" -> "scorpio"
            "Yay" -> "sagittarius"
            "Oğlak" -> "capricorn"
            "Kova" -> "aquarius"
            "Balık" -> "pisces"
            else -> "aries"
        }
    }
}
