package com.kareem.moviesapp.data.model.reviews

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class Review(
    var author: String? = null,
    @Ignore
    var author_details: AuthorDetails? = null,
    @ColumnInfo(name = "content")
    var content: String? = null,
    @Ignore
    var created_at: String? = null,
    @PrimaryKey(autoGenerate = false)
    var id: String? = null,
    @ColumnInfo(name = "created_at")
    var updated_at: String? = null,
    @ColumnInfo(name = "movie_id")
    var movie_id: String? = null,
    @ColumnInfo(name = "url")
    var url: String? = null
)