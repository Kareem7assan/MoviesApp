package com.kareem.moviesapp.presentation.home.adapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kareem.moviesapp.R
import com.kareem.moviesapp.app.App.Companion.Img_Suffix
import com.kareem.moviesapp.data.model.movies_model.Movie
import com.kareem.moviesapp.databinding.ItemMovieBinding
import java.util.*

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieVH>() {

    private var data: MutableList<Movie> = ArrayList()

    var onClickItem: ((Movie, Int) -> Unit)? = null
    var onClickFav: ((Movie, Int,Int) -> Unit)? = null

    var selectedItemPosition = -1

    fun updateSelectedItem(position: Int) {
        selectedItemPosition = position
        data[position] = data[position].copy(hasFav = !data[position].hasFav!!)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVH {
        return MovieVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MovieVH, position: Int) {
        holder.bind(data[position])
    }

    fun swapData(data: List<Movie>) {
        this.data = data as MutableList<Movie>
        notifyDataSetChanged()
    }

    fun insertData(data: List<Movie>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun removeWithIndex(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

    inner class MovieVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item:Movie) = with(ItemMovieBinding.bind(itemView)) {
            if (item.hasFav == true){
                ivLike.setImageResource(R.drawable.ic_baseline_favorite_24)
            }
            else{
                ivLike.setImageResource(R.drawable.ic_baseline_not_favorite_24)
            }
            tvMovie.text=item.title
            Glide.with(itemView.context)
                    .load(Img_Suffix+""+item.poster_path)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(ivMovie)

            ivLike.setOnClickListener {
                onClickFav?.invoke(item,adapterPosition,data.size)
            }

            ivMovie.setOnClickListener {
                onClickItem?.invoke(item,adapterPosition)
            }
        }
    }
}