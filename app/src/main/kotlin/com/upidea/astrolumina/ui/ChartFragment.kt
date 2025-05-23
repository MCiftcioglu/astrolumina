package com.upidea.astrolumina.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.upidea.astrolumina.R
import com.upidea.astrolumina.utils.GeminiHelper

class ChartFragment : Fragment() {

    private lateinit var spinnerSun: Spinner
    private lateinit var spinnerMoon: Spinner
    private lateinit var spinnerRising: Spinner
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

        spinnerSun = view.findViewById(R.id.spinnerSun)
        spinnerMoon = view.findViewById(R.id.spinnerMoon)
        spinnerRising = view.findViewById(R.id.spinnerRising)
        textViewResult = view.findViewById(R.id.textViewResult)
        buttonGenerate = view.findViewById(R.id.buttonGenerate)

        buttonGenerate.setOnClickListener {
            val sun = spinnerSun.selectedItem.toString()
            val moon = spinnerMoon.selectedItem.toString()
            val rising = spinnerRising.selectedItem.toString()

            // Cinsiyet bilgisini al
            val sharedPref = requireActivity().getSharedPreferences("AstroPrefs", Context.MODE_PRIVATE)
            val gender = sharedPref.getString("gender", "") ?: ""

            val prompt = GeminiHelper.createAstrologyPrompt(sun, moon, rising, gender)

            // Chaquopy Python başlatma
            if (!Python.isStarted()) {
                Python.start(AndroidPlatform(requireContext()))
            }

            try {
                val py = Python.getInstance()
                val module = py.getModule("local_model")
                val result = module.callAttr("generate_astrology_comment", prompt)
                textViewResult.text = result.toString()
            } catch (e: Exception) {
                textViewResult.text = "Yorum alınamadı: ${e.localizedMessage}"
            }
        }
    }
}
