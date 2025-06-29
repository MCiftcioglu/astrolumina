package com.upidea.astrolumina.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.chaquo.python.PyException
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.upidea.astrolumina.R
import com.upidea.astrolumina.utils.getSunSign


class ProfileFragment : Fragment() {

    private lateinit var imageCard: CardView
    private lateinit var editName: EditText
    private lateinit var editBirthDate: EditText
    private lateinit var editBirthTime: EditText
    private lateinit var editBirthPlace: EditText
    private lateinit var buttonSave: Button
    private lateinit var imageProfile: ImageView
    private lateinit var buttonSelectPhoto: Button

    private val PREF_KEY_IMAGE_URI = "profileImageUri"

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val imageUri = result.data?.data
            imageProfile.setImageURI(imageUri)

            // URI kaydedilir
            requireActivity().getSharedPreferences("AstroPrefs", Context.MODE_PRIVATE)
                .edit()
                .putString(PREF_KEY_IMAGE_URI, imageUri.toString())
                .apply()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // View tanımlamaları
        imageCard = view.findViewById(R.id.imageCard) // Added imageCard initialization
        editName = view.findViewById(R.id.editTextName)
        editBirthDate = view.findViewById(R.id.editTextBirthDate)
        editBirthTime = view.findViewById(R.id.editTextBirthTime)
        editBirthPlace = view.findViewById(R.id.editTextBirthPlace)
        buttonSave = view.findViewById(R.id.buttonSave)
        imageProfile = view.findViewById(R.id.imageProfile)
        buttonSelectPhoto = view.findViewById(R.id.buttonSelectPhoto)

        val sharedPref = requireActivity().getSharedPreferences("AstroPrefs", Context.MODE_PRIVATE)

        // Kayıtlı verileri doldur
        editName.setText(sharedPref.getString("name", ""))
        editBirthDate.setText(sharedPref.getString("birthDate", ""))
        editBirthTime.setText(sharedPref.getString("birthTime", ""))
        editBirthPlace.setText(sharedPref.getString("birthPlace", ""))

        // Fotoğraf varsa yükle
        val savedUri = sharedPref.getString(PREF_KEY_IMAGE_URI, null)
        savedUri?.let {
            imageProfile.setImageURI(Uri.parse(it))
        }

        // Fotoğraf seçme işlemi
        buttonSelectPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            pickImageLauncher.launch(intent)
        }

        // Kayıt işlemi
        buttonSave.setOnClickListener {
            saveProfileData()
        }

        return view
    }

    private fun saveProfileData() {
        val name = editName.text.toString()
        val birthDate = editBirthDate.text.toString()
        val birthTime = editBirthTime.text.toString()
        val birthPlace = editBirthPlace.text.toString()

        if (name.isBlank() || birthDate.isBlank() || birthTime.isBlank() || birthPlace.isBlank()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Güneş burcunu hesapla (Kotlin tarafında)
        val parts = birthDate.split("/")
        val sunSign = if (parts.size == 3) {
            val day = parts[0].toIntOrNull() ?: 1
            val month = parts[1].toIntOrNull() ?: 1
            getSunSign(day, month)
        } else {
            "Koç" // Varsayılan değer
        }

        // Ay ve Yükselen burcunu hesapla (Python tarafında)
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(requireContext()))
        }
        val py = Python.getInstance()
        val vedicUtils = py.getModule("vedic_utils")

        try {
            val signsResult = vedicUtils.callAttr("calculate_vedic_signs", birthDate, birthTime, birthPlace)

            // Dönen PyObject'ten değerleri doğru şekilde al
            val moonSign = signsResult.get("moon_sign")?.toString() ?: ""
            val risingSign = signsResult.get("rising_sign")?.toString() ?: ""
            val error = signsResult.get("error")?.toString()

            if (error != null) {
                Toast.makeText(requireContext(), "Error calculating signs: $error", Toast.LENGTH_LONG).show()
                return
            }

            // Tüm verileri SharedPreferences'a kaydet
            val sharedPref = requireActivity().getSharedPreferences("AstroPrefs", Context.MODE_PRIVATE)
            sharedPref.edit().apply {
                putString("name", name)
                putString("birthDate", birthDate)
                putString("birthTime", birthTime)
                putString("birthPlace", birthPlace)
                putString("sunSign", sunSign)
                putString("moonSign", moonSign)
                putString("risingSign", risingSign)
                // Cinsiyet bilgisi de gerekiyorsa buradan eklenmeli
                // putString("gender", "...")
                apply()
            }

            Toast.makeText(requireContext(), "Profile saved successfully!", Toast.LENGTH_SHORT).show()

        } catch (e: PyException) {
            Toast.makeText(requireContext(), "Python error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Animasyonu yükle ve başlat
        val neonAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.neon_animation)
        imageCard.startAnimation(neonAnimation)
    }
}