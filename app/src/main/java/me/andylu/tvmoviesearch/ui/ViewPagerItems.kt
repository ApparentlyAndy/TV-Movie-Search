package me.andylu.tvmoviesearch.ui


import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import me.andylu.tvmoviesearch.R
import me.andylu.tvmoviesearch.controller.ImageLoadAsyncTask
import me.andylu.tvmoviesearch.model.TVMovies
import java.io.ByteArrayOutputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ViewPagerItems : Fragment() {

    fun newInstance(data: TVMovies): ViewPagerItems {
        val fragment = ViewPagerItems()
        val args = Bundle()
        args.putString("title", data.title)
        args.putDouble("vote_average", data.vote_average)
        args.putString("poster_path", data.poster_path)
        args.putString("backdrop_path", data.backdrop_path)
        args.putString("original_lang", data.original_lang)
        args.putInt("vote_count", data.vote_count)
        args.putString("overview", data.overview)
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_pager_items, container, false)

        val posterImageView = view.findViewById<ImageView>(R.id.poster_image_view)
        val backdropImageView = view.findViewById<ImageView>(R.id.backdrop_image_view)
        val backdropBitmap =
            ImageLoadAsyncTask(arrayListOf("backdrop", arguments?.getString("backdrop_path"))).execute().get()
        val posterBitmap =
            ImageLoadAsyncTask(arrayListOf("poster", arguments?.getString("poster_path"))).execute().get()
        backdropImageView.setImageBitmap(backdropBitmap)
        posterImageView.setImageBitmap(posterBitmap)

        view.setOnClickListener {
            val backdropByteArray = compressBitmap(backdropBitmap)
            val posterByteArray = compressBitmap(posterBitmap)
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("title", arguments?.getString("title"))
                .putExtra("backdrop_image", backdropByteArray)
                .putExtra("poster_image", posterByteArray)
                .putExtra("vote_average", arguments?.getDouble("vote_average").toString())
                .putExtra("vote_count", arguments?.getInt("vote_count").toString())
                .putExtra("original_lang", arguments?.getString("original_lang"))
                .putExtra("overview", arguments?.getString("overview"))
            startActivity(intent)
        }
        return view
    }

    private fun compressBitmap(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        return outputStream.toByteArray()
    }
}



