package me.andylu.tvmoviesearch.model

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import me.andylu.tvmoviesearch.controller.RecyclerClickItemListener

class RecyclerViewAdapter(private val movieTVDataSet:ArrayList<TVMovies>, private val onResultItemClick:RecyclerClickItemListener): RecyclerView.Adapter<SearchResultViewHolder>(), RecyclerClickItemListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        return SearchResultViewHolder(inflator,parent)
    }

    override fun getItemCount(): Int {
        return movieTVDataSet.size
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val tvMovie: TVMovies = movieTVDataSet[position]
        holder.bind(tvMovie)
        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun onItemClick(position:Int) {
        onResultItemClick.onItemClick(position)
    }
}