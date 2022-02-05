package com.kareem.moviesapp.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kareem.moviesapp.data.*
import com.kareem.moviesapp.data.model.movies_model.Movie
import com.kareem.moviesapp.data.model.movies_model.MoviesModel
import com.kareem.moviesapp.data.repository.MoviesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.times
import retrofit2.Response


@RunWith(MockitoJUnitRunner::class)
class MoviesUseCasesTest {

    private lateinit var useCase: MoviesUseCases

    @Mock
    lateinit var fakeMoviesRepository: MoviesRepository

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        useCase = MoviesUseCases(repository = fakeMoviesRepository)
    }

    //showAllMovies() scenarios

    @ExperimentalCoroutinesApi
    @Test
    fun `showAllMovies() when page has valid data then return Movies`() {
        runBlockingTest {
            //arrange
            Mockito.`when`(fakeMoviesRepository.getMovies(1)).thenReturn(flow { emit(Response.success(moviesMockResponse)) })
            Mockito.`when`(fakeMoviesRepository.getMoviesCache()).thenReturn(moviesDbMockedList)
            //assert
            assertEquals(useCase.showAllMovies(1).first(), moviesMockedList)

        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `showAllMovies() when page has valid data then onEach() invoked`() {
        runBlockingTest {
            //arrange
            Mockito.`when`(fakeMoviesRepository.getMovies(1)).thenReturn(flow { emit(Response.success(moviesMockResponse)) })
            Mockito.`when`(fakeMoviesRepository.getMoviesCache()).thenReturn(moviesDbMockedList)

            //act
            useCase.showAllMovies(1).first()

            //assert
            Mockito.verify(fakeMoviesRepository, times(1)).saveMovies(moviesMockResponse.results!!)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `handleWithFavsItems() when valid data from api and item existed in favs cache then returned expected modified`() = runBlockingTest{
        //arrange
        Mockito.`when`(fakeMoviesRepository.getMoviesCache()).thenReturn(favsMockedList)

        val expected=listOf(
                Movie(id = 1, title = "spider man", hasFav = false),
                Movie(id = 2, title = "matrix", hasFav = false),
                Movie(id = 3, title = "avatar", hasFav = true),
                Movie(id = 4, title = "hulk", hasFav = false),
        )

        assertEquals(useCase.handleWithFavsItems(moviesMockedList), expected)
    }



    @ExperimentalCoroutinesApi
    @Test
    fun `isItemExistInFavDB() when movie exist`() = runBlockingTest{
        //arrange
        Mockito.`when`(fakeMoviesRepository.getMoviesCache()).thenReturn(favsMockedList)
        //assert
        assert(useCase.isItemExistInFavDB(moviesMockedList[2]))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `isItemExistInFavDB() when movie not exist`() = runBlockingTest{
        //arrange
        Mockito.`when`(fakeMoviesRepository.getMoviesCache()).thenReturn(favsMockedList)
        //assert
        assert(useCase.isItemExistInFavDB(moviesMockedList[0]).not())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `showAllMovies() when page has valid data then noThingElse invoked`() {
        runBlockingTest {
            //arrange
            Mockito.`when`(fakeMoviesRepository.getMovies(1)).thenReturn(flow { emit(Response.success(moviesMockResponse)) })
            //assert
            assertEquals(useCase.showAllMovies(1).count(),1)

        }
    }


    @ExperimentalCoroutinesApi
    @Test
    fun `showAllMovies() when page has error return then never emit value`() {
        runBlockingTest {
            //arrange
            Mockito.`when`(fakeMoviesRepository.getMovies(1)).thenReturn(flow { emit(Response.error<MoviesModel>(401, errorResponseBody())) })
            //assert
            assertEquals(useCase.showAllMovies(1).firstOrNull(), null)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `showAllMovies() when page has error response return then getMovies() invoked once`() {
        runBlockingTest {
            //arrange
            Mockito.`when`(fakeMoviesRepository.getMovies(1)).thenReturn(flow { emit(Response.error<MoviesModel>(401, errorResponseBody())) })
            //act
            useCase.showAllMovies(1).first()
            //assert
            Mockito.verify(fakeMoviesRepository, Mockito.times(1)).getMoviesCache()
        }
    }
    @ExperimentalCoroutinesApi
    @Test
    fun `showAllMovies() when page empty then onEmpty() then getCache `() {
        runBlockingTest {
            //arrange
            Mockito.`when`(fakeMoviesRepository.getMovies(1)).thenReturn(flow { emit(Response.success(emptyMoviesMockResponse)) })
            Mockito.`when`(fakeMoviesRepository.getMoviesCache()).thenReturn(moviesDbMockedList)
            //assert
            assertEquals(useCase.showAllMovies(1).first(),moviesDbMockedList)
        }
    }

    //showFavouriteMovies() scenarios

    @ExperimentalCoroutinesApi
    @Test
    fun `showFavouriteMovies() when there is data stored then return Movies`() {
        runBlockingTest {
            //arrange
            Mockito.`when`(fakeMoviesRepository.getMoviesCache()).thenReturn(moviesDbMockedList)

            //act
            val result=useCase.showFavouriteMovies().first()

            //assert
            assertEquals(result, favsMockedList)


        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `showFavouriteMovies() when there is empty data stored then return Movies`() {
        runBlockingTest {
            //arrange
            Mockito.`when`(fakeMoviesRepository.getMoviesCache()).thenReturn(listOf())

            //act
            val result=useCase.showFavouriteMovies().first()

            //assert
            assertEquals(result, listOf<Movie>())


        }
    }

}
