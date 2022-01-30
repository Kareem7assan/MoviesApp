package com.kareem.moviesapp.data.model.movies_model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity(tableName = "movie")
data class Movie(
    @ColumnInfo(name = "adult")
    var adult: Boolean? = null,
    @ColumnInfo(name = "backdrop_path")
    var backdrop_path: String? = null,
    @Ignore
    var genre_ids: List<Int>? = null,
    @PrimaryKey(autoGenerate = false)
    var id: Int? = null,
    @ColumnInfo(name = "original_language")
    var original_language: String? = null,
    @ColumnInfo(name = "original_title")
    var original_title: String? = null,
    @ColumnInfo(name = "overview")
    var overview: String? = null,
    @ColumnInfo(name = "popularity")
    var popularity: Double? = null,
    @ColumnInfo(name = "poster_path")
    var poster_path: String? = null,
    @ColumnInfo(name = "release_date")
    var release_date: String? = null,
    @ColumnInfo(name = "title")
    var title: String? = null,
    @Ignore
    var video: Boolean? = null,
    @ColumnInfo(name = "vote_average")
    var vote_average: Float? = null,
    @ColumnInfo(name = "vote_count")
    var vote_count: Int? = null,
    @ColumnInfo(name = "hasFav")
    var hasFav: Boolean? = false
)