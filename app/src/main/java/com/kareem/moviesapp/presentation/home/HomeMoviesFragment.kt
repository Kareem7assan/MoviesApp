package com.kareem.moviesapp.presentation.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.kareem.moviesapp.R
import com.kareem.moviesapp.data.model.movies_model.Movie
import com.kareem.moviesapp.data.remote.NetWorkState
import com.kareem.moviesapp.databinding.FragmentHomeBinding
import com.kareem.moviesapp.presentation.home.adapter.MovieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.anko.support.v4.toast


@AndroidEntryPoint
class HomeMoviesFragment : Fragment()  {
    private val VISIBLE_THRESHOLD: Int=1
    private val moviesAdapter by lazy { MovieAdapter() }
    private  lateinit var  layManger: GridLayoutManager
    private var binding: FragmentHomeBinding? = null
    private var progressDialog: ACProgressFlower? = null
    private var pageNumber=1

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
        sendRequestMovies()
        observeMovies()
    }

    private fun setupActions() {
        setUpLoadMoreListener()
        moviesAdapter.onClickItem=::onClickItem
        moviesAdapter.onClickFav=::onItemFav
        binding?.root?.setOnRefreshListener {
            binding?.root?.isRefreshing=false
            pageNumber=1
            sendRequestMovies()
        }
    }

    private fun onItemFav(movie: Movie, pos: Int,size:Int) {
        moviesAdapter.updateSelectedItem(pos)
        homeViewModel.changeFavourite(movie)
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

    private fun sendRequestMovies() {
        homeViewModel.showHomeMovies(pageNumber)
    }


    private fun observeMovies() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.moviesFlow.collect { state ->

                    when (state) {
                        is NetWorkState.Loading -> {
                            if (pageNumber==1) showProgressDialog()
                        }
                        is NetWorkState.StopLoading -> {
                            hideProgressDialog()
                        }
                        is NetWorkState.Error -> {
                            toast(getString(R.string.check_your_connection))
                        }
                        is NetWorkState.Success -> {
                            showData(state.data)
                        }
                    }
                }
            }
        }
    }

    private fun setUpLoadMoreListener() {
        binding!!.rvMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layManger.itemCount
                val lastVisibleItem = layManger.findLastVisibleItemPosition()
                if ( dy > 0 && totalItemCount <= lastVisibleItem + VISIBLE_THRESHOLD) {
                    pageNumber++
                    homeViewModel.showHomeMovies(pageNumber)

                }


            }
        })
    }

    private fun setupRec() { 
        layManger = GridLayoutManager(requireContext(),2)
        binding?.rvMovies?.apply {
            layoutManager=layManger
            adapter=moviesAdapter
        }
    }

    private fun showData(data: List<Movie>) {
        moviesAdapter.insertData(data)
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