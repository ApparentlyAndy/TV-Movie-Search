package me.andylu.tvmoviesearch.controller

import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View

class ViewPagerTransformer: ViewPager.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        view.apply {
            when {
                position < 0 -> {
                    alpha = 1f - (Math.abs(position) * .5f)
                    Log.d("rightleft", Math.abs(position).toString())
                }

                position == 0f -> {
                    alpha = 1f
                }

                position > 0 -> {
                    alpha = 1f - (Math.abs(position) * .5f)
                    Log.d("rightright", Math.abs(position).toString())
                }
            }
        }
    }
}