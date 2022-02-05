package com.kareem.moviesapp.presentation.details

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kareem.moviesapp.R
import com.kareem.moviesapp.app.App
import com.kareem.moviesapp.data.model.movies_model.Movie
import com.kareem.moviesapp.data.model.reviews.Review
import com.kareem.moviesapp.data.remote.NetWorkReviewsState
import com.kareem.moviesapp.data.remote.RoomMovieState
import com.kareem.moviesapp.databinding.FragmentDetailsBinding
import com.kareem.moviesapp.presentation.details.adapter.ReviewsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.jetbrains.anko.support.v4.toast


@AndroidEntryPoint
class MovieDetailsFragment : Fragment()  {
    private var movieId: Int = 0
    private val reviewsAdapter by lazy { ReviewsAdapter() }
    private  lateinit var  layManger: LinearLayoutManager
    private var binding: FragmentDetailsBinding? = null
    private var progressDialog: ACProgressFlower? = null
    private var pageNumber=1

    private val detailsViewModel: DetailsViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentDetailsBinding.bind(view)
        movieId=arguments?.getInt("movie") ?: 0
        setupRec()
        setupActions()
        sendRequestReviews(movieId)
        getMovieDetails(movieId)
        observeReviews()

    }

    private fun sendRequestReviews(movieId: Int) {
        detailsViewModel.showMovieReviews(movieId, pageNumber)
    }

    private fun getMovieDetails(movieId: Int) {
        detailsViewModel.showMovieDetails(movieId)
    }

    private fun setupActions() {
        setUpLoadMoreListener()

    }

    private fun setUpLoadMoreListener() {
        binding?.root?.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (v.getChildAt(v.getChildCount() - 1) != null) {

                if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight &&
                        scrollY > oldScrollY) {
                            pageNumber++
                            detailsViewModel.showMovieReviews(movieId, pageNumber)
                }

            }
        }

    }
    private fun setupRec() {
        layManger = LinearLayoutManager(requireContext())
        binding?.rvReviews?.apply {
            layoutManager=layManger
            adapter=reviewsAdapter
        }
    }

    private fun observeReviews() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailsViewModel.reviewsFlow.collect { state ->

                    when (state) {
                        is NetWorkReviewsState.Loading -> {
                            if (pageNumber == 1) showProgressDialog()
                        }
                        is NetWorkReviewsState.StopLoading -> {
                            hideProgressDialog()
                        }
                        is NetWorkReviewsState.Error -> {
                            toast(getString(R.string.check_your_connection))
                        }
                        is NetWorkReviewsState.Empty->{
                            binding?.tvReviews?.isVisible=false
                        }
                        is NetWorkReviewsState.Success -> {
                            showData(state.data)
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailsViewModel.detailsFlow.collect { state ->

                    when (state) {
                        is RoomMovieState.Error -> {
                            toast(getString(R.string.check_your_connection))
                        }
                        is RoomMovieState.Success -> {
                            showDetails(state.data)
                        }
                        else -> {}
                    }
                }
            }
        }


    }

    private fun showDetails(movie: Movie) {
        with(binding!!){
            changeFaveMovieDisplay(movie.hasFav ?: false)
            tvTitle.text=movie.title
            Glide.with(requireContext())
                    .load(App.Img_Suffix + "" + movie.poster_path)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(ivMovie)

            if (movie.vote_average?:0f > 0f) {
                ratingBar.rating = ((movie.vote_average ?: 0f) / 2f)
                tvAvgRating.text = movie.vote_average.toString()
            }
            if (movie.vote_count ?: 0 > 0) tvVotesAvg.text=movie.vote_count.toString()+" "+getString(R.string.vote)
            if (movie.popularity ?: 0.0 > 0) tvPopularity.text=(movie.popularity ?: 0.0).toString()
            tvDescription.text=movie.overview ?: ""
            tvReleaseDate.text=movie.release_date ?: ""
            binding?.ivLike?.setOnClickListener {
                detailsViewModel.changeFavourite(movie).also { changeFaveMovieDisplay(!movie.hasFav!!) }
            }
        }
    }

    private fun changeFaveMovieDisplay(hasFav: Boolean) {
        if (hasFav) {
            binding?.ivLike?.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            binding?.ivLike?.setImageResource(R.drawable.ic_baseline_not_favorite_24)
        }
    }

    private fun showData(data: List<Review>) {
        reviewsAdapter.insertData(data)
    }

    private fun showProgressDialog() {
        progressDialog = ACProgressFlower.Builder(requireContext())
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.GRAY)
                .fadeColor(Color.WHITE)
                .build()
        progressDialog?.show()
    }

    private fun hideProgressDialog() {
        progressDialog?.run {
            dismiss()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        progressDialog?.hide()
        binding=null
    }
}