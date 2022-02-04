package com.kareem.moviesapp.data.model.movies_model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey(autoGenerate = false)
    var id: Int?=0,
    var adult: Boolean? = null,
    var backdrop_path: String? = null,
    @Ignore
    var genre_ids: List<Int>? = null,
    var original_language: String? = null,
    var original_title: String? = null,
    var overview: String? = null,
    var popularity: Double? = null,
    var poster_path: String? = null,
    var release_date: String? = null,
    var title: String? = null,
    @Ignore
    var video: Boolean? = null,
    var vote_average: Float? = null,
    var vote_count: Int? = null,
    var hasFav: Boolean? = false
)