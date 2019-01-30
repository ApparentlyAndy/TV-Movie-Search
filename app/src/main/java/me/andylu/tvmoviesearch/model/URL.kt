package me.andylu.tvmoviesearch.model

class URL {
    companion object {
        
        val discoverMoviesURL: String =
            "https://api.themoviedb.org/3/discover/movie?api_key=${ApiKey.apiKey}" +
                    "&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false"

        val onAirMoviesURL: String = "https://api.themoviedb.org/3/movie/now_playing?api_key=${ApiKey.apiKey}&language=en-US&page=1"

        val popularMoviesURL: String = "https://api.themoviedb.org/3/movie/popular?api_key=${ApiKey.apiKey}&language=en-US&page=1"

        val topRatedMoviesURL: String = "https://api.themoviedb.org/3/movie/top_rated?api_key=${ApiKey.apiKey}&language=en-US&page=1"


        val discoverTVShowsURL: String =
            "https://api.themoviedb.org/3/discover/tv?api_key=${ApiKey.apiKey}" +
                    "&language=en-US&sort_by=popularity.desc&page=1&timezone=America%2FNew_York&include_null_first_air_dates=false"

        val onAirTVShowsURL: String = "https://api.themoviedb.org/3/tv/on_the_air?api_key=${ApiKey.apiKey}&language=en-US&page=1"

        val popularTVShowsURL: String = "https://api.themoviedb.org/3/tv/popular?api_key=${ApiKey.apiKey}&language=en-US&page=1"

        val topRatedTVShowsURL: String = "https://api.themoviedb.org/3/tv/top_rated?api_key=${ApiKey.apiKey}&language=en-US&page=1"
    }
}
