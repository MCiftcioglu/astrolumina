package com.upidea.astrolumina.api

import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GeminiService {

    // TODO: API anahtarını güvenli bir yerden al (örn: BuildConfig)
    private const val API_KEY = "AIzaSyAOoiC3GN5x-dxKWVwfvioYrqS8oUJpN5Q" // BURAYA GERÇEK API ANAHTARINIZI GİRİN

    private val generativeModel = GenerativeModel(
        modelName = "gemini-2,5", // Kullanmak istediğiniz model
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
