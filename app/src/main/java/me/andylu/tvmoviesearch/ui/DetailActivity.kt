package me.andylu.tvmoviesearch.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import me.andylu.tvmoviesearch.R

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setUpToolbar()

        val backdropImageView: ImageView = findViewById(R.id.backdrop_image_view)
        val posterImageView: ImageView = findViewById(R.id.poster_image_view)


        val backdropByteArray = intent?.extras?.getByteArray("backdrop_image")
        val posterByteArray = intent?.extras?.getByteArray("poster_image")

        if (backdropByteArray != null) {
            backdropImageView.setImageBitmap(getBitmap(backdropByteArray))
        }

        if (posterByteArray != null) {
            posterImageView.setImageBitmap(getBitmap(posterByteArray))
        } else {
            posterImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.no_poster))
        }

        val title: TextView = findViewById(R.id.tv_movie_title)
        val voteCount: TextView = findViewById(R.id.vote_count_text)
        val voteAverage: TextView = findViewById(R.id.vote_average_text)
        val originalLang: TextView = findViewById(R.id.original_lang_text)
        val overview: TextView = findViewById(R.id.overview_text)

        title.text = intent.getStringExtra("title")
        voteCount.text = intent.getStringExtra("vote_count")
        voteAverage.text = intent.getStringExtra("vote_average")
        originalLang.text = intent.getStringExtra("original_lang").toUpperCase()
        overview.text = intent.getStringExtra("overview")
        overview.movementMethod = ScrollingMovementMethod()

        setVoteStars()
    }

    private fun setVoteStars() {
        val voteAverage = intent.getStringExtra("vote_average").toDouble() / 2
        val starCount = Math.round(voteAverage * 2) / 2.0
        val ratingBar:RatingBar = findViewById(R.id.vote_count_rating)
        ratingBar.rating = starCount.toFloat()
    }

    private fun setUpToolbar() {
        val mToolbar: Toolbar = findViewById(R.id.toolbar_main)
        mToolbar.title = ""
        this.setSupportActionBar(mToolbar)

        val actionBar = supportActionBar
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_navigate_before_black)
        }
    }

    private fun getBitmap(byteArray: ByteArray?): Bitmap? {
        return if (byteArray?.size != null) {
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        } else {
            null
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
