package me.andylu.tvmoviesearch.model

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.squareup.picasso.Picasso
import me.andylu.tvmoviesearch.R

class SearchResultViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.fragment_search_result_items, parent, false)) {
    private var title: TextView? = null
    private var poster_image_view:ImageView? = null
    private var ratingBar:RatingBar? = null

    init {
        poster_image_view = itemView.findViewById(R.id.poster_image_view)
        title = itemView.findViewById(R.id.search_result_title)
        ratingBar = itemView.findViewById(R.id.vote_count_rating)
    }

    fun bind(data: TVMovies) {
        if (data.poster_path == "null") {
            poster_image_view?.scaleType = ImageView.ScaleType.FIT_CENTER
            poster_image_view?.setImageDrawable(itemView.resources.getDrawable(R.drawable.no_poster))

        } else {
            poster_image_view?.scaleType = ImageView.ScaleType.CENTER_CROP
            Picasso.get().load("https://image.tmdb.org/t/p/w780${data.poster_path}").into(poster_image_view)
        }
        title?.text = data.title
        setVoteStars(data.vote_count)
    }

    private fun setVoteStars(voteCount:Int) {
        val voteAverage = voteCount.toDouble() / 2
        val starCount = Math.round(voteAverage * 2) / 2.0
        ratingBar?.rating = starCount.toFloat()
    }

}