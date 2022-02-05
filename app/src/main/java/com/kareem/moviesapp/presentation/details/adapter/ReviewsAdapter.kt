package com.kareem.moviesapp.presentation.details.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kareem.moviesapp.R
import com.kareem.moviesapp.data.model.reviews.Review
import com.kareem.moviesapp.databinding.ItemReviewBinding
import java.util.*

class ReviewsAdapter : RecyclerView.Adapter<ReviewsAdapter.ReviewsVH>() {

    private var data: MutableList<Review> = ArrayList()

    var onClickItem: ((Review, Int) -> Unit)? = null

    var selectedItemPosition = -1

    fun updateSelectedItem(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsVH {
        return ReviewsVH(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_review, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ReviewsVH, position: Int) = holder.bind(data[position])

    fun swapData(data: List<Review>) {
        this.data = data as MutableList<Review>
        notifyDataSetChanged()
    }
    fun insertData(data: List<Review>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun removeWithIndex(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

    class ReviewsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item:Review) = with(ItemReviewBinding.bind(itemView)) {
            tvName.text=item.author ?: ""
            tvReview.text=item.content ?: ""
        }
    }
}