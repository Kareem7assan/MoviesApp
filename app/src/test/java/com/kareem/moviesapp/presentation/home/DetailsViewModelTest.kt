package com.kareem.moviesapp.presentation.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.kareem.moviesapp.data.model.movies_model.Movie
import com.kareem.moviesapp.data.model.reviews.Review
import com.kareem.moviesapp.data.moviesMockedList
import com.kareem.moviesapp.data.remote.NetWorkMovieState
import com.kareem.moviesapp.data.remote.NetWorkReviewsState
import com.kareem.moviesapp.data.remote.RoomMovieState
import com.kareem.moviesapp.data.remote.RoomMoviesState
import com.kareem.moviesapp.data.reviewsMockedList
import com.kareem.moviesapp.domain.DetailsUseCases
import com.kareem.moviesapp.domain.MoviesUseCases
import com.kareem.moviesapp.presentation.details.DetailsViewModel
import com.kareem.moviesapp.presentation.rules.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DetailsViewModelTest {

    private lateinit var coroutineDispatcher: TestCoroutineDispatcher
    private lateinit var viewModel: DetailsViewModel

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()


    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()



    @Mock
    private lateinit var fakeUseCases: DetailsUseCases



    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        coroutineDispatcher = TestCoroutineDispatcher()
        viewModel = DetailsViewModel(fakeUseCases)

    }



    
    @Test
    fun `showMovieReviews()  when getDataFlow then  LOADING starts then SUCCESS finally LOADING`() {

        runBlockingTest {
            Mockito.`when`(fakeUseCases.getReviews(1,1))
                .thenReturn(flow { emit(reviewsMockedList) })

            viewModel.reviewsFlow.test {
                    viewModel.showMovieReviews(1,1)
                    assert(awaitItem() is NetWorkReviewsState.Loading)
                    assert(awaitItem() is NetWorkReviewsState.Success)
                    assert(awaitItem() is NetWorkReviewsState.StopLoading)
            }

        }

    }
    @Test
    fun `showMovieReviews() when errorFlow then no data in cache `() {

        runBlockingTest {
            Mockito.`when`(fakeUseCases.getReviews(1,1))
                    .thenReturn(null)

            viewModel.reviewsFlow.test {
                viewModel.showMovieReviews(1,1)

                assert(awaitItem() is NetWorkReviewsState.Loading)
                assert(awaitItem() is NetWorkReviewsState.StopLoading)
                assert(awaitItem() is NetWorkReviewsState.Error)
            }


        }

    }

    @Test
    fun `showMovieReviews()  when errorFlow then there is data in cache `() {

        runBlockingTest {
            Mockito.`when`(fakeUseCases.getReviews(1,1))
                    .thenReturn(null)

            Mockito.`when`(fakeUseCases.getReviews(1,1))
                    .thenReturn(flow { emit(listOf<Review>()) })

            viewModel.reviewsFlow.test {
                viewModel.showMovieReviews(1,1)

                assert(awaitItem() is NetWorkReviewsState.Loading)
                assert(awaitItem() is NetWorkReviewsState.Success)
                assert(awaitItem() is NetWorkReviewsState.StopLoading)
            }


        }

    }

    @Test
    fun `showMovieDetails()  when there is data then LOADING,SUCCESS,STOP LOADING `() {
        runBlockingTest {
            Mockito.`when`(fakeUseCases.getMovieDetails(1))
                    .thenReturn(flow { emit(moviesMockedList.first()) })


            viewModel.detailsFlow.test {
                viewModel.showMovieDetails(1)
                assert(awaitItem() is RoomMovieState.Loading)
                assert(awaitItem() is RoomMovieState.Success)
                assert(awaitItem() is RoomMovieState.StopLoading)
            }

        }

    }

}





