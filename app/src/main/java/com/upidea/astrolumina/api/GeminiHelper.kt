package com.upidea.astrolumina.api

fun createAstrologyPrompt(sun: String, moon: String, rising: String): String {
    return """
        Benim doğum haritamda:
        • Güneş burcu: $sun
        • Ay burcu: $moon
        • Yükselen burç: $rising

        Bu burç kombinasyonuna göre kişilik özelliklerimi, eğilimlerimi, güçlü/zayıf yönlerimi ve ilişki tarzımı detaylı ve samimi bir dille analiz eder misin? 
        Astrolojik yorum gibi yazmanı rica ediyorum.
    """.trimIndent()
}
