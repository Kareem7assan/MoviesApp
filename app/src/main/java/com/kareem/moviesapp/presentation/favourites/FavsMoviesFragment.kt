package com.kareem.moviesapp.presentation.favourites

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.google.android.material.snackbar.Snackbar
import com.kareem.moviesapp.R
import com.kareem.moviesapp.data.model.movies_model.Movie
import com.kareem.moviesapp.data.remote.NetWorkState
import com.kareem.moviesapp.data.remote.RoomState
import com.kareem.moviesapp.databinding.FragmentHomeBinding
import com.kareem.moviesapp.presentation.home.HomeViewModel
import com.kareem.moviesapp.presentation.home.adapter.MovieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.jetbrains.anko.support.v4.toast


@AndroidEntryPoint
class FavsMoviesFragment : Fragment()  {
    private val moviesAdapter by lazy { MovieAdapter() }
    private  lateinit var  layManger: GridLayoutManager
    private var binding: FragmentHomeBinding? = null
    private var progressDialog: ACProgressFlower? = null
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentHomeBinding.bind(view)
        setupRec()
        setupActions()
        getFavMovies()
        observeMovies()
    }

    private fun getFavMovies() {
        homeViewModel.showMyFavouriteMovies()
    }

    private fun setupActions() {
        moviesAdapter.onClickItem=::onClickItem
        moviesAdapter.onClickFav=::onItemFav
        binding?.root?.setOnRefreshListener {
            binding?.root?.isRefreshing=false
            getFavMovies()
        }
    }

    private fun onItemFav(movie: Movie, pos: Int,size:Int) {
        moviesAdapter.removeWithIndex(pos)
        homeViewModel.changeFavourite(movie)
        if (size==1) showEmpty()
    }

    private fun observeMovies() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.favFlow.collect { state ->

                    when (state) {
                        is RoomState.Loading -> {
                            hideEmpty()
                            showProgressDialog()
                        }
                        is RoomState.StopLoading -> {
                            hideProgressDialog()
                        }
                        is RoomState.Empty->{
                            showEmpty()
                        }
                        is RoomState.Success -> {
                            hideEmpty()
                            showData(state.data)
                        }
                    }
                }
            }
        }
    }

    private fun showEmpty() {
        binding?.emptyLottie?.isVisible = true
        binding?.rvMovies?.isVisible = false
    }

    private fun hideEmpty() {
        binding?.emptyLottie?.isVisible = false
        binding?.rvMovies?.isVisible = true
    }

    private fun showData(data: List<Movie>) {
        moviesAdapter.swapData(data)
    }

    private fun onClickItem(movie: Movie, i: Int) {
        findNavController().navigate(R.id.action_moviesFragment_to_detailsFragment,
                bundleOf(
                        "movie"
                                to
                                movie
                )
        )
    }

    private fun setupRec() {
        layManger = GridLayoutManager(requireContext(),2)
        binding?.rvMovies?.apply {
            layoutManager=layManger
            adapter=moviesAdapter
        }
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