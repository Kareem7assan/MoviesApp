package com.kareem.moviesapp.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.kareem.moviesapp.data.*
import com.kareem.moviesapp.data.cache.relations.MovieWithReviews
import com.kareem.moviesapp.data.model.movies_model.MoviesModel
import com.kareem.moviesapp.data.model.reviews.ReviewsModel
import com.kareem.moviesapp.data.repository.MoviesRepository
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
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
class DetailsUseCasesTest{

    private lateinit var useCase: DetailsUseCases

    @Mock
    lateinit var fakeMoviesRepository: MoviesRepository

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        useCase = DetailsUseCases(repository = fakeMoviesRepository)
    }


    @ExperimentalCoroutinesApi
    @Test
    fun `getReviews() when page has valid data then return reviews`() {
        runBlockingTest {
            //arrange
            Mockito.`when`(fakeMoviesRepository.getReviews(1,1)).thenReturn(flow { emit(Response.success(reviewsMockResponse)) })
            //assert
            Assert.assertEquals(useCase.getReviews(1,1).first(), reviewsMockedList)
        }
    }
    @ExperimentalCoroutinesApi
    @Test
    fun `getReviews() when page has valid data then onEach() invoked`() {
        runBlockingTest {
            //arrange
            Mockito.`when`(fakeMoviesRepository.getReviews(1,1)).thenReturn(flow { emit(Response.success(reviewsMockResponse)) })
            //act
            useCase.getReviews(1,1).first()

            //assert
            Mockito.verify(fakeMoviesRepository, times(1)).saveReviews(reviewsMockResponse.results!!)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getReviews() when page has valid data then noThingElse invoked(emitted)`() {
        runBlockingTest {
            //arrange
            Mockito.`when`(fakeMoviesRepository.getReviews(1,1)).thenReturn(flow { emit(Response.success(reviewsMockResponse)) })
            //assert
            Assert.assertEquals(useCase.getReviews(1,1).count(), 1)

        }
    }


    @ExperimentalCoroutinesApi
    @Test
    fun `getReviews() when page has error return then never emit value `() {
        runBlockingTest {
            //arrange
            Mockito.`when`(fakeMoviesRepository.getReviews(1,1)).thenReturn(flow { emit(Response.error<ReviewsModel>(401, errorResponseBody())) })
            Mockito.`when`(fakeMoviesRepository.getReviewsCache(1)).thenReturn(emptyList())

            //assert
            assertEquals(useCase.getReviews(1,1).firstOrNull(), null)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getReviews() when page has error return then getReviews() invoked once `() {
        runBlockingTest {
            //arrange
            Mockito.`when`(fakeMoviesRepository.getReviews(1,1)).thenReturn(flow { emit(Response.error<ReviewsModel>(401, errorResponseBody())) })
            Mockito.`when`(fakeMoviesRepository.getReviewsCache(1)).thenReturn(reviewsMockedList)
            //act
            useCase.getReviews(1,1).first()
            //assert
            Mockito.verify(fakeMoviesRepository, Mockito.times(2)).getReviewsCache(1)
        }
    }




}