package me.andylu.tvmoviesearch.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import me.andylu.tvmoviesearch.R
import me.andylu.tvmoviesearch.controller.OkHttpUtil
import me.andylu.tvmoviesearch.controller.ViewPagerTransformer
import me.andylu.tvmoviesearch.model.TVMovies
import me.andylu.tvmoviesearch.model.PrepareData
import me.andylu.tvmoviesearch.model.URL
import me.andylu.tvmoviesearch.model.ViewPagerAdapter
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity(), Callback {

    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mToolbar: Toolbar
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var viewpager:ViewPager
    private lateinit var navigationView: NavigationView
    private lateinit var menuItem: MenuItem
    private var arrayOfTVMovies:MutableList<TVMovies> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewpager = findViewById(R.id.main_viewpager)
        viewpager.setPageTransformer(true, ViewPagerTransformer())

        setUpNavigationView()
        setUpToolbar()
        getTMDBData(URL.discoverMoviesURL)
    }

    private fun displayTVMovie(arrayOfTVMovie:MutableList<TVMovies>) {
        runOnUiThread {
            arrayOfTVMovies = arrayOfTVMovie
            viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, arrayOfTVMovies)
            viewpager.adapter = viewPagerAdapter
        }
    }

    private fun getTMDBData(url:String) {
        OkHttpUtil().callTMDBApi(url, this)
    }

    override fun onFailure(call: Call, e: IOException) {
        val toast = Toast.makeText(applicationContext, "Could not fetch from TMDB api.", Toast.LENGTH_LONG)
        toast.show()
    }

    override fun onResponse(call: Call, response: Response) {
        val data = response.body()?.string()
        val arrayOfTVMovie = PrepareData(data).getData()
        displayTVMovie(arrayOfTVMovie)
    }

    private fun setUpNavigationView() {
        mDrawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        menuItem = navigationView.menu.findItem(R.id.discover_movies)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // set item as selected to persist highlight
            menuItem.isChecked = true
            // close drawer when item is tapped
            mDrawerLayout.closeDrawers()

            when (menuItem.itemId) {
                // Movies
                R.id.discover_movies -> {
                    mToolbar.title = "Discover - Movies"
                    getTMDBData(URL.discoverMoviesURL)
                }
                R.id.onAir_movies -> {
                    mToolbar.title = "Now Playing - Movies"
                    menuItem.isChecked = false
                    getTMDBData(URL.onAirMoviesURL)
                }
                R.id.popular_movies -> {
                    mToolbar.title = "Popular - Movies"
                    menuItem.isChecked = false
                    getTMDBData(URL.popularMoviesURL)
                }
                R.id.topRated_movies -> {
                    mToolbar.title = "Top Rated - Movies"
                    menuItem.isChecked = false
                    getTMDBData(URL.topRatedMoviesURL)
                }
                // TV Shows
                R.id.discover_tv -> {
                    mToolbar.title = "Discover - TV Shows"
                    menuItem.isChecked = false
                    getTMDBData(URL.discoverTVShowsURL)
                }
                R.id.onAir_tv -> {
                    mToolbar.title = "On Air - TV Shows"
                    menuItem.isChecked = false
                    getTMDBData(URL.onAirTVShowsURL)
                }
                R.id.popular_tv -> {
                    mToolbar.title = "Popular - TV Shows"
                    menuItem.isChecked = false
                    getTMDBData(URL.popularTVShowsURL)
                }
                R.id.topRated_tv -> {
                    mToolbar.title = "Top Rated - TV Shows"
                    menuItem.isChecked = false
                    getTMDBData(URL.topRatedTVShowsURL)
                }
            }
            true
        }
    }

    private fun setUpToolbar() {
        mToolbar = findViewById(R.id.toolbar_main)
        mToolbar.title = "Discover - Movies"
        this.setSupportActionBar(mToolbar)

        val actionBar = supportActionBar
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_black)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                mDrawerLayout.openDrawer(GravityCompat.START)
                true
            }
            R.id.action_search -> {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_main, menu)
        return true
    }
}
