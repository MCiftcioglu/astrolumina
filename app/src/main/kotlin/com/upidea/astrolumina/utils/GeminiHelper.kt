package com.upidea.astrolumina.utils

object GeminiHelper {

    fun createAstrologyPrompt(
        sun: String,
        moon: String,
        rising: String,
        gender: String
    ): String {
        val genderNote = when (gender.lowercase()) {
            "kadın" -> "Yorumlar bir kadın için yazılmış gibi olmalı."
            "erkek" -> "Yorumlar bir erkek için yazılmış gibi olmalı."
            else -> ""
        }

        return """
            Benim doğum haritamda:
            • Güneş burcu: $sun
            • Ay burcu: $moon
            • Yükselen burç: $rising
            
            $genderNote

            Bu burç kombinasyonuna göre kişilik özelliklerimi, eğilimlerimi, güçlü/zayıf yönlerimi ve ilişki tarzımı detaylı ve samimi bir dille analiz eder misin? 
            Astrolojik yorum gibi yazmanı rica ediyorum.
        """.trimIndent()
    }
}
