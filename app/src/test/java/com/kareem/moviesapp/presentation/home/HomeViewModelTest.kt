package com.kareem.moviesapp.presentation.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.kareem.moviesapp.data.model.movies_model.Movie
import com.kareem.moviesapp.data.moviesMockedList
import com.kareem.moviesapp.data.remote.NetWorkMovieState
import com.kareem.moviesapp.data.remote.RoomMoviesState
import com.kareem.moviesapp.domain.MoviesUseCases
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
class HomeViewModelTest {

    private lateinit var coroutineDispatcher: TestCoroutineDispatcher
    private lateinit var viewModel: HomeViewModel

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()


    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()



    @Mock
    private lateinit var fakeUseCases: MoviesUseCases



    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        coroutineDispatcher = TestCoroutineDispatcher()
        viewModel = HomeViewModel(fakeUseCases)

    }



    
    @Test
    fun `showHomeMovies()  when getDataFlow then  LOADING starts then SUCCESS finally LOADING`() {

        runBlockingTest {
            Mockito.`when`(fakeUseCases.showAllMovies(1))
                .thenReturn(flow { emit(moviesMockedList) })

            viewModel.moviesFlow.test {
                    viewModel.showHomeMovies(1)
                    
                    assert(awaitItem() is NetWorkMovieState.Loading)
                    assert(awaitItem() is NetWorkMovieState.Success)
                    assert(awaitItem() is NetWorkMovieState.StopLoading)
            }



        }

    }

    
    @Test
    fun `showHomeMovies(page)  when errorFlow then  no data in cache `() {

        runBlockingTest {
            Mockito.`when`(fakeUseCases.showAllMovies(1))
                .thenReturn(null)

            viewModel.moviesFlow.test {
                    viewModel.showHomeMovies(1)
                    
                    assert(awaitItem() is NetWorkMovieState.Loading)
                    assert(awaitItem() is NetWorkMovieState.StopLoading)
                    assert(awaitItem() is NetWorkMovieState.Error)
            }


        }

    }

    
    @Test
    fun `showHomeMovies(page)  when emptyFlow then data from cache `() {
        runBlockingTest {
            Mockito.`when`(fakeUseCases.showAllMovies(1))
                .thenReturn(null)

            Mockito.`when`(fakeUseCases.showAllMovies(1))
                .thenReturn(flow { emit(listOf<Movie>()) })

            viewModel.moviesFlow.test {
                    viewModel.showHomeMovies(1)
                
                assert(awaitItem() is NetWorkMovieState.Loading)
                assert(awaitItem() is NetWorkMovieState.Success)
                assert(awaitItem() is NetWorkMovieState.StopLoading)
            }

        }

    }

    
    @Test
    fun `showMyFavouriteMovies()  when there is data then LOADING,SUCCESS,STOP LOADING `() {
        runBlockingTest {
            Mockito.`when`(fakeUseCases.showFavouriteMovies())
                .thenReturn(flow { emit(moviesMockedList) })


            viewModel.favFlow.test {
                    viewModel.showMyFavouriteMovies()

                assert(awaitItem() is RoomMoviesState.Loading)
                assert(awaitItem() is RoomMoviesState.Success)
                assert(awaitItem() is RoomMoviesState.StopLoading)
            }

        }

    }
    @Test
    fun `showMyFavouriteMovies()  when there is no data or Exception then LOADING,SUCCESS,STOP LOADING,EMPTY `() {
        runBlockingTest {
            Mockito.`when`(fakeUseCases.showFavouriteMovies())
                .thenReturn(flow { emit(listOf<Movie>()) })


            viewModel.favFlow.test {
                    viewModel.showMyFavouriteMovies()

                assert(awaitItem() is RoomMoviesState.Loading)
                assert(awaitItem() is RoomMoviesState.StopLoading)
                assert(awaitItem() is RoomMoviesState.Empty)
            }

        }
    }


    

}





