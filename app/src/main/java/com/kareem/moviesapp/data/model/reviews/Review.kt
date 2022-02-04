package com.kareem.moviesapp.data.model.reviews

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "review")
data class Review(
    var author: String? = null,
    @Ignore
    var author_details: AuthorDetails? = null,
    var content: String? = null,
    @Ignore
    var created_at: String? = null,

    @PrimaryKey(autoGenerate = false)
    var id: String="0",
    @ColumnInfo(name = "created_at")
    var updated_at: String? = null,
    @ColumnInfo(name = "movie_id")
    var movie_id: Int? = 0,
    var url: String? = null
)