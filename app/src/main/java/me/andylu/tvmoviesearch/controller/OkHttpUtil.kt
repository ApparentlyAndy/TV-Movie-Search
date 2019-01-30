package me.andylu.tvmoviesearch.controller

import okhttp3.*
import java.io.IOException

class OkHttpUtil {
    fun callTMDBApi(url: String, callback: Callback) {
         val client = OkHttpClient()
         val request = Request.Builder()
            .url(url)
            .build()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(call,e)
            }

            override fun onResponse(call: Call, response: Response) {
                callback.onResponse(call,response)
            }
        })
    }
}