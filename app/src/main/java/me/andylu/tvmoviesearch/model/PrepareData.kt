package me.andylu.tvmoviesearch.model

import org.json.JSONArray
import org.json.JSONObject

class PrepareData(private val data:String?) {
    fun getData():ArrayList<TVMovies> {
        val jsonObject = JSONObject(data)
        val results = jsonObject.get("results") as JSONArray
        val arrayOfTVMovie:ArrayList<TVMovies> = arrayListOf()
        for(i in 0 until results.length()) {
            val o = JSONObject(results[i].toString())

            var title:String = if (o.has("title")) {
                o.getString("title")
            } else {
                o.getString("name")
            }

            val voteAverage = o.getDouble("vote_average")
            val posterPath = o.getString("poster_path")
            val backdropPath = o.getString("backdrop_path")
            val originalLang = o.getString("original_language")
            val voteCount = o.getInt("vote_count")
            val overview = o.getString("overview")
            arrayOfTVMovie.add(TVMovies(title,voteAverage,posterPath,backdropPath,originalLang,voteCount, overview))
        }
        return arrayOfTVMovie
    }
}