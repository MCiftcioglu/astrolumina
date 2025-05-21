package com.upidea.astrolumina.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.upidea.astrolumina.R

class HoroscopeFragment : Fragment() {

    private lateinit var spinnerSign: Spinner
    private lateinit var textHoroscope: TextView
    private lateinit var textTitle: TextView

    private val horoscopeMap = mapOf(
        "Koç" to "Bugün enerjin yüksek! Yeni adımlar atmak için cesaretli ol.",
        "Boğa" to "Güven duygusu ön planda. Sevdiklerinle vakit geçir.",
        "İkizler" to "İletişim trafiğin yoğun. Yeni fikirler doğabilir.",
        "Yengeç" to "Duygusal yoğunluk artabilir. İç sesini dinle.",
        "Aslan" to "Kendine güvenin parlıyor! Sahne senin.",
        "Başak" to "Detaylara odaklan, planlarını düzenle.",
        "Terazi" to "Denge arayışın seni sosyal ortamlara taşıyabilir.",
        "Akrep" to "Yoğun düşünceler içinde kalabilirsin. Gözlem gücünü kullan.",
        "Yay" to "Yeni ufuklara açılmak için harika bir gün.",
        "Oğlak" to "Sorumlulukların seni yorsa da kararlılığın yüksek.",
        "Kova" to "Farklı düşüncelerle öne çıkabilirsin. İlham dolu bir gün.",
        "Balık" to "Hayal gücün yüksek, sezgilerine güven."
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_horoscope, container, false)

        spinnerSign = view.findViewById(R.id.spinnerSign)
        textHoroscope = view.findViewById(R.id.textHoroscope)
        textTitle = view.findViewById(R.id.textTitle)

        val sharedPref = requireActivity().getSharedPreferences("AstroPrefs", Context.MODE_PRIVATE)
        val userSunSign = sharedPref.getString("sunSign", "Koç") ?: "Koç"

        val signs = horoscopeMap.keys.toList()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, signs)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSign.adapter = adapter

        // Kullanıcının Güneş burcu varsa spinner başlangıçta onu seçsin
        val defaultIndex = signs.indexOf(userSunSign)
        if (defaultIndex != -1) spinnerSign.setSelection(defaultIndex)

        // Spinner değiştiğinde yorum güncelle
        spinnerSign.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val selectedSign = signs[position]
                textHoroscope.text = horoscopeMap[selectedSign]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        return view
    }
}
