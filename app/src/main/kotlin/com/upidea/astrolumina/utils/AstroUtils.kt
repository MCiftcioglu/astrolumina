package com.upidea.astrolumina.utils

import android.content.Context
import android.location.Geocoder
import java.util.Locale
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform

fun getSunSign(day: Int, month: Int): String {
    return when (month) {
        1 -> if (day < 20) "Oğlak" else "Kova"
        2 -> if (day < 19) "Kova" else "Balık"
        3 -> if (day < 21) "Balık" else "Koç"
        4 -> if (day < 20) "Koç" else "Boğa"
        5 -> if (day < 21) "Boğa" else "İkizler"
        6 -> if (day < 21) "İkizler" else "Yengeç"
        7 -> if (day < 23) "Yengeç" else "Aslan"
        8 -> if (day < 23) "Aslan" else "Başak"
        9 -> if (day < 23) "Başak" else "Terazi"
        10 -> if (day < 23) "Terazi" else "Akrep"
        11 -> if (day < 22) "Akrep" else "Yay"
        12 -> if (day < 22) "Yay" else "Oğlak"
        else -> "Bilinmiyor"
    }
}

object AstroUtils {

    fun calculateVedicHoroscopeViaPython(context: Context, date: String, time: String, place: String, gender: String): String {
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(context))
        }

        return try {
            val py = Python.getInstance()
            val module = py.getModule("vedic_utils")  // Python dosyasının adı
            val result = module.callAttr("calculate_vedic_horoscope", date, time, place, gender)
            result.toString()
        } catch (e: Exception) {
            "Hesaplama sırasında hata oluştu: ${e.localizedMessage}"
        }
    }

    fun calculateSignsViaPython(context: Context, date: String, time: String, place: String): Triple<String, String, String> {
        // Konumu geocode ile koordinata çevir
        val geocoder = Geocoder(context, Locale.getDefault())
        val address = try {
            geocoder.getFromLocationName(place, 1)?.firstOrNull()
        } catch (e: Exception) {
            null
        }
        val lat = address?.latitude ?: 0.0
        val lon = address?.longitude ?: 0.0

        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(context))
        }

        return try {
            val py = Python.getInstance()
            val module = py.getModule("astrology_utils")
            val result = module.callAttr("calculate_signs", date, time, lat, lon).toString()
            val parts = result.split(",")
            val sun = parts.getOrNull(0) ?: "Bilinmiyor"
            val moon = parts.getOrNull(1) ?: "Bilinmiyor"
            val asc = parts.getOrNull(2) ?: "Bilinmiyor"
            Triple(sun, moon, asc)
        } catch (e: Exception) {
            Triple("Bilinmiyor", "Bilinmiyor", "Bilinmiyor")
        }
    }


    private val vedicSigns = listOf(
        "Koç", "Boğa", "İkizler", "Yengeç", "Aslan", "Başak",
        "Terazi", "Akrep", "Yay", "Oğlak", "Kova", "Balık"
    )

    private const val LAHIRI_AYANAMSA = 23.856

    fun getSiderealZodiacSign(eclipticLongitude: Double): String {
        val correctedLongitude = (eclipticLongitude - LAHIRI_AYANAMSA + 360) % 360
        val signIndex = (correctedLongitude / 30).toInt()
        return vedicSigns[signIndex]
    }

    fun calculateVedicHoroscope(birthDate: String, birthTime: String, birthPlace: String, gender: String): String {
        return """
            📍 Doğum Yeri: $birthPlace
            📅 Tarih: $birthDate
            ⏰ Saat: $birthTime
            👤 Cinsiyet: $gender

            🪐 Vedik Astroloji Yorumu:
            Vedik haritan sana sabırlı olman gereken bir dönemde olduğunu söylüyor.
            Duygularını bastırmak yerine anlamaya çalış, sezgilerini takip et.
        """.trimIndent()
    }
}
