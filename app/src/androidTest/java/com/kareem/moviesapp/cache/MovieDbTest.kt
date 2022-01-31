package com.kareem.moviesapp.cache

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.google.common.truth.Truth.assertThat
import com.kareem.moviesapp.data.cache.MovieDB
import com.kareem.moviesapp.data.cache.MovieDao
import com.kareem.moviesapp.data.model.movies_model.Movie
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4ClassRunner::class)
class MovieDbTest {
    private lateinit var movieDao: MovieDao
    private lateinit var db: MovieDB

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
                context, MovieDB::class.java).build()
        movieDao = db.movieDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }


    @Test
    fun addMoviesWithCollectionMoviesAssertEquals(){
        runBlocking {
            val expected =
                listOf(
                    Movie(id = 1, title = "spider man", hasFav = false),
                    Movie(id = 2, title = "matrix", hasFav = false),
                    Movie(id = 3, title = "avatar", hasFav = true),
                    Movie(id = 4, title = "hulk", hasFav = false),

                )
            movieDao.addMovies(expected)
            val result = movieDao.getAllMovies()

            assertThat(expected).isEqualTo(result)

        }
    }

    @Test
    fun markAsFavouriteAssertItemFav(){
        runBlocking {
            val expected =
                listOf(
                    Movie(id = 1, title = "spider man", hasFav = false),
                    Movie(id = 2, title = "matrix", hasFav = false),
                    Movie(id = 3, title = "avatar", hasFav = false),
                    Movie(id = 4, title = "hulk", hasFav = false),

                )
            movieDao.addMovies(expected)
            val favMovie=Movie(id = 1, title = "spider man", hasFav = true)

            movieDao.markAsFavourite(favMovie)
            //ensure that the marked movie has been added to the movie list
            assert(movieDao.getAllMovies().find { it.id== favMovie.id}?.hasFav==true)

        }
    }

    @Test
    fun markAsUnFavouriteAssertItemNotFav(){
        runBlocking {
            val expected =
                listOf(
                    Movie(id = 1, title = "spider man", hasFav = true),
                    Movie(id = 2, title = "matrix", hasFav = false),
                    Movie(id = 3, title = "avatar", hasFav = false),
                    Movie(id = 4, title = "hulk", hasFav = false),
                )
            movieDao.addMovies(expected)
            val favMovie=Movie(id = 1, title = "spider man", hasFav = false)

            movieDao.markAsUnFavourite(favMovie)
            //ensure that the marked movie has been updated within the movie list
            assert(movieDao.getAllMovies().find { it.id== favMovie.id}?.hasFav==false)

        }
    }


}