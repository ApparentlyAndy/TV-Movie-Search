package me.andylu.tvmoviesearch.controller

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import java.lang.Exception
import java.net.URL

class ImageLoadAsyncTask(private val urlArray: ArrayList<String?>) : AsyncTask<Void, Void, Bitmap>() {


    override fun doInBackground(vararg params: Void?): Bitmap? {
        var currentUrl = ""

        when {
            urlArray[0] == "backdrop" -> currentUrl = "https://image.tmdb.org/t/p/w300${urlArray[1]}"
            urlArray[0] == "poster" -> currentUrl = "https://image.tmdb.org/t/p/w500${urlArray[1]}"
            urlArray[0] == "lowRes" -> currentUrl = "https://image.tmdb.org/t/p/w45${urlArray[1]}"
        }

        try {
            val urlConnection = URL(currentUrl)
            val connection = urlConnection.openConnection()
            connection.doInput = true
            connection.connect()
            val input = connection.getInputStream()
            return BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}