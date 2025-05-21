package com.upidea.astrolumina.utils

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

    private val vedicSigns = listOf(
        "Koç", "Boğa", "İkizler", "Yengeç", "Aslan", "Başak",
        "Terazi", "Akrep", "Yay", "Oğlak", "Kova", "Balık"
    )

    private const val LAHIRI_AYANAMSA = 23.856  // Derece cinsinden yaklaşık değer

    fun getSiderealZodiacSign(eclipticLongitude: Double): String {
        val correctedLongitude = (eclipticLongitude - LAHIRI_AYANAMSA + 360) % 360
        val signIndex = (correctedLongitude / 30).toInt()
        return vedicSigns[signIndex]
    }
}
