package com.upidea.astrolumina.api

import com.google.ai.client.generativeai.GenerativeModel
import com.upidea.astrolumina.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GeminiService {

    // API anahtarını BuildConfig üzerinden al
    private const val API_KEY = BuildConfig.GEMINI_API_KEY

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-pro", // Kullanmak istediğiniz model
        apiKey = API_KEY
    )

    suspend fun getAstrologyInterpretation(prompt: String, callback: (String?) -> Unit) {
        withContext(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(prompt)
                callback(response.text)
            } catch (e: Exception) {
                // Hata durumunda loglama veya kullanıcıya bilgi verme
                e.printStackTrace()
                callback(null)
            }
        }
    }
}
