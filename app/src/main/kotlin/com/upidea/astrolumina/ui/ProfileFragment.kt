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
import androidx.fragment.app.Fragment
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

    private val PICK_IMAGE_REQUEST = 101
    private val PREF_KEY_IMAGE_URI = "profileImageUri"

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
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Kayıt işlemi
        buttonSave.setOnClickListener {
            val birthDate = editBirthDate.text.toString()
            val parts = birthDate.split("/")
            val sunSign = if (parts.size == 3) {
                val day = parts[0].toIntOrNull() ?: 1
                val month = parts[1].toIntOrNull() ?: 1
                getSunSign(day, month)
            } else {
                "Koç"
            }

            sharedPref.edit().apply {
                putString("name", editName.text.toString())
                putString("birthDate", birthDate)
                putString("birthTime", editBirthTime.text.toString())
                putString("birthPlace", editBirthPlace.text.toString())
                putString("sunSign", sunSign)
                apply()
            }

            Toast.makeText(requireContext(), "Bilgiler kaydedildi", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Animasyonu yükle ve başlat
        val neonAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.neon_animation)
        imageCard.startAnimation(neonAnimation)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri = data.data
            imageProfile.setImageURI(imageUri)

            // URI kaydedilir
            requireActivity().getSharedPreferences("AstroPrefs", Context.MODE_PRIVATE)
                .edit()
                .putString(PREF_KEY_IMAGE_URI, imageUri.toString())
                .apply()
        }
    }
}
