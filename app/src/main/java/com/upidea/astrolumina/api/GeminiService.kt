package com.upidea.astrolumina.api

import android.util.Log
import com.upidea.astrolumina.BuildConfig
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

object GeminiService {
    private val client = OkHttpClient()
    private const val ENDPOINT =
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent"

    fun getAstrologyInterpretation(
        prompt: String,
        callback: (String?) -> Unit
    ) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty()) {
            callback("API anahtarı bulunamadı.")
            return
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()

        val requestBody = """
            {
              "contents": [
                {
                  "parts": [
                    { "text": "$prompt" }
                  ]
                }
              ]
            }
        """.trimIndent().toRequestBody(mediaType)

        val request = Request.Builder()
            .url("$ENDPOINT?key=$apiKey")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("GeminiService", "İstek başarısız: ${e.message}")
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                if (response.isSuccessful && responseBody != null) {
                    try {
                        val json = JSONObject(responseBody)
                        val content = json.getJSONArray("candidates")
                            .getJSONObject(0)
                            .getJSONObject("content")
                            .getJSONArray("parts")
                            .getJSONObject(0)
                            .getString("text")
                        callback(content)
                    } catch (e: Exception) {
                        Log.e("GeminiService", "Yanıt çözümleme hatası: ${e.message}")
                        callback("Yorum çözümlenemedi.")
                    }
                } else {
                    Log.e("GeminiService", "API hatası: $responseBody")
                    callback("Yorum alınamadı.")
                }
            }
        })
    }
}


