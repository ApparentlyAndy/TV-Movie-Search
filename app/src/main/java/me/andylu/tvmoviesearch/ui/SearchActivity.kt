package me.andylu.tvmoviesearch.ui

import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.*
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import me.andylu.tvmoviesearch.R
import me.andylu.tvmoviesearch.controller.ImageLoadAsyncTask
import me.andylu.tvmoviesearch.controller.OkHttpUtil
import me.andylu.tvmoviesearch.controller.RecyclerClickItemListener
import me.andylu.tvmoviesearch.model.PrepareData
import me.andylu.tvmoviesearch.model.RecyclerViewAdapter
import me.andylu.tvmoviesearch.model.TVMovies
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException

class SearchActivity : AppCompatActivity(), Callback, RecyclerClickItemListener {
    private lateinit var searchView: SearchView
    private var filterMovies: Boolean = true
    private var filterTV: Boolean = true

    private var tvShows: ArrayList<TVMovies> = arrayListOf()
    private var movies: ArrayList<TVMovies> = arrayListOf()

    private var arrayOfTVMovies: ArrayList<TVMovies> = arrayListOf()
    private lateinit var adapter: RecyclerViewAdapter
    var currentSearchQuery: String? = null

    // Menu Items
    private lateinit var showMoviesMenuItem:MenuItem
    private lateinit var showTVShowsMenuItem:MenuItem
    // Pages tracker
    private var currentTVPage: Int = 0
    private var maxTVPageNumber: Int? = null

    private var currentMoviePage: Int = 0
    private var maxMoviePageNumber: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setUpToolbar()
        setUpSearchView()

        val recyclerView: RecyclerView = findViewById(R.id.search_result_recyclerview)
        recyclerView.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        adapter = RecyclerViewAdapter(arrayOfTVMovies, this)
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollHorizontally(1)) {
                    if (currentTVPage != maxTVPageNumber) {
                        currentTVPage++
                        getTVData(currentSearchQuery)
                    }

                    if (currentMoviePage != maxMoviePageNumber) {
                        currentMoviePage++
                        getMovieData(currentSearchQuery)
                    }
                }
            }
        })
    }

    override fun onRestart() {
        searchView.clearFocus()
        super.onRestart()
    }

    override fun onItemClick(position: Int) {
        startDetailActivity(position)
    }

    private fun startDetailActivity(position: Int) {
        val backdropBitmap: Bitmap
        val posterBitmap: Bitmap
        val backdropByteArray: ByteArray
        val posterByteArray: ByteArray

        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("title", arrayOfTVMovies[position].title)
            .putExtra("vote_average", arrayOfTVMovies[position].vote_average.toString())
            .putExtra("vote_count", arrayOfTVMovies[position].vote_count.toString())
            .putExtra("original_lang", arrayOfTVMovies[position].original_lang)
            .putExtra("overview", arrayOfTVMovies[position].overview)

        if (arrayOfTVMovies[position].backdrop_path != "null") {
            backdropBitmap =
                ImageLoadAsyncTask(arrayListOf("backdrop", arrayOfTVMovies[position].backdrop_path)).execute().get()
            backdropByteArray = compressBitmap(backdropBitmap)
            intent.putExtra("backdrop_image", backdropByteArray)
        }
        if (arrayOfTVMovies[position].poster_path != "null") {
            posterBitmap =
                ImageLoadAsyncTask(arrayListOf("poster", arrayOfTVMovies[position].poster_path)).execute().get()
            posterByteArray = compressBitmap(posterBitmap)
            intent.putExtra("poster_image", posterByteArray)
        }
        startActivity(intent)
    }

    private fun compressBitmap(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        return outputStream.toByteArray()
    }


    private fun setUpToolbar() {
        val mToolbar: Toolbar = findViewById(R.id.toolbar_search)
        mToolbar.title = ""
        this.setSupportActionBar(mToolbar)

        val actionBar = supportActionBar
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_navigate_before_black)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.filter_movies -> {
                item.isChecked = !item.isChecked
                filterMovies = item.isChecked
                showTVMoviesOption(false, arrayListOf())
                return true
            }
            R.id.filter_tv -> {
                item.isChecked = !item.isChecked
                filterTV = item.isChecked
                showTVMoviesOption(false, arrayListOf())
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showTVMoviesOption(addOn:Boolean, arrayOfTVMovie: ArrayList<TVMovies>) {
        if (addOn) {
            if ((!filterTV).and(!filterMovies)) {
                Log.d("LENGTH", "LENGTH is ${arrayOfTVMovies.size}")
                return
            } else {
                arrayOfTVMovies.addAll(arrayOfTVMovie)
                Log.d("LENGTH", "LENGTH is ${arrayOfTVMovies.size}")
                adapter.notifyDataSetChanged()
            }

        } else {

            if ((filterTV).and(filterMovies)) {
                arrayOfTVMovies.clear()
                arrayOfTVMovies.addAll(movies)
                arrayOfTVMovies.addAll(tvShows)
                Log.d("LENGTH", "LENGTH is ${arrayOfTVMovies.size}")
                adapter.notifyDataSetChanged()
            }

            if (filterMovies.and(!filterTV)) {
                arrayOfTVMovies.clear()
                arrayOfTVMovies.addAll(movies)
                Log.d("LENGTH", "LENGTH is ${arrayOfTVMovies.size}")
                adapter.notifyDataSetChanged()
            }

            if (filterTV.and(!filterMovies)) {
                Log.d("showwhat", "Movies is $filterMovies")
                Log.d("showwhat", "TV is $filterTV")
                arrayOfTVMovies.clear()
                arrayOfTVMovies.addAll(tvShows)
                Log.d("LENGTH", "LENGTH is ${arrayOfTVMovies.size}")
                adapter.notifyDataSetChanged()
            }

            if ((!filterTV).and(!filterMovies)) {
                Log.d("showwhat", "Movies is $filterMovies")
                Log.d("showwhat", "TV is $filterTV")
                arrayOfTVMovies.clear()
                Log.d("LENGTH", "LENGTH is ${arrayOfTVMovies.size}")
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_search_filter, menu)
        if (menu != null) {
            showMoviesMenuItem = menu.findItem(R.id.filter_movies)
            showTVShowsMenuItem = menu.findItem(R.id.filter_tv)
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun setUpSearchView() {
        searchView = findViewById(R.id.search_view)
        searchView.isFocusable = true
        searchView.isIconified = false
        searchView.requestFocusFromTouch()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(searchQuery: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(searchQuery: String?): Boolean {
                searchView.clearFocus()
                tvShows.clear()
                movies.clear()
                arrayOfTVMovies.clear()
                resetCurrentMaxPages()
                currentSearchQuery = searchQuery
                showTVShowsMenuItem.isChecked = true
                showMoviesMenuItem.isChecked = true
                filterMovies = true
                filterTV = true
                getMovieData(searchQuery)
                getTVData(searchQuery)
                return true
            }

        })
    }

    private fun getMovieData(searchQuery: String?) {
        val url =
            "https://api.themoviedb.org/3/search/movie?api_key=c8d78a2d240032191c36263e9ea00d3a&language=en-US&query=$searchQuery&page=$currentMoviePage&include_adult=false"
        OkHttpUtil().callTMDBApi(url, this)
    }

    private fun getTVData(searchQuery: String?) {
        val url =
            "https://api.themoviedb.org/3/search/tv?api_key=c8d78a2d240032191c36263e9ea00d3a&language=en-US&query=$searchQuery&page=$currentTVPage"
        OkHttpUtil().callTMDBApi(url, this)
    }

    override fun onFailure(call: Call, e: IOException) {
        val toast = Toast.makeText(applicationContext, "Could not fetch from TMDB api.", Toast.LENGTH_LONG)
        toast.show()
    }

    override fun onResponse(call: Call, response: Response) {
        val data = response.body()?.string()
        setCurrentMaxPages(response.request().url().toString(), data)
        val arrayOfTVMovie = PrepareData(data).getData()
        setTVMovieArrayList(arrayOfTVMovie, response.request().toString())
    }

    private fun setCurrentMaxPages(url: String, data: String?) {
        val movieURL = "https://api.themoviedb.org/3/search/movie"

        if (url.contains(movieURL)) {
            if (movies.size == 0) {
                currentMoviePage = JSONObject(data).getInt("page")
            }
            maxMoviePageNumber = JSONObject(data).getInt("total_pages")
        } else {
            if (tvShows.size == 0) {
                currentMoviePage = JSONObject(data).getInt("page")
            }
            maxTVPageNumber = JSONObject(data).getInt("total_pages")
        }
    }

    private fun setTVMovieArrayList(arrayOfTVMovie: ArrayList<TVMovies>, url: String) {
        val movieURL = "https://api.themoviedb.org/3/search/movie"

        runOnUiThread {
            if (url.contains(movieURL)) {
                movies.addAll(arrayOfTVMovie)
                showTVMoviesOption(true, arrayOfTVMovie)
            } else {
                tvShows.addAll(arrayOfTVMovie)
                showTVMoviesOption(true, arrayOfTVMovie)
            }
        }
    }

    private fun resetCurrentMaxPages() {
        currentTVPage = 1
        currentMoviePage = 1
        maxTVPageNumber = null
        maxMoviePageNumber = null
    }

}
