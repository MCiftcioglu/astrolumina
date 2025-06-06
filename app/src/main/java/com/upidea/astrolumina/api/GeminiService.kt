package com.upidea.astrolumina.api

import android.util.Log
import com.upidea.astrolumina.BuildConfig
import com.upidea.astrolumina.utils.GeminiHelper
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

object GeminiService {

    private const val TAG = "GeminiService"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent"
    private val JSON = "application/json; charset=utf-8".toMediaType()

    fun getAstrologyInterpretation(prompt: String, callback: (String?) -> Unit) {
        val apiKey = BuildConfig.GEMINI_API_KEY

        val requestBody = JSONObject().apply {
            put("contents", listOf(
                mapOf("parts" to listOf(mapOf("text" to prompt)))
            ))
        }.toString().toRequestBody(JSON)

        val request = Request.Builder()
            .url("$BASE_URL?key=$apiKey")
            .post(requestBody)
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "API call failed", e)
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        Log.e(TAG, "Unexpected response: ${response.code}")
                        callback(null)
                    } else {
                        val json = JSONObject(it.body?.string() ?: "")
                        val candidates = json.optJSONArray("candidates")
                        val content = candidates?.optJSONObject(0)
                            ?.optJSONObject("content")
                            ?.optJSONArray("parts")
                            ?.optJSONObject(0)
                            ?.optString("text")

                        callback(content)
                    }
                }
            }
        })
    }
}
