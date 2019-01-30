package me.andylu.tvmoviesearch.model

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import me.andylu.tvmoviesearch.ui.ViewPagerItems

class ViewPagerAdapter(fm: FragmentManager?, private val arrayOfTVMovies:MutableList<TVMovies>) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return ViewPagerItems().newInstance(arrayOfTVMovies[position])
    }

    override fun getCount(): Int {
        return arrayOfTVMovies.size
    }
}