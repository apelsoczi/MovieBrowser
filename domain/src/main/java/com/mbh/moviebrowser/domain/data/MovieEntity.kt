package com.mbh.moviebrowser.domain.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "movie_details")
data class MovieEntity(
    @PrimaryKey @ColumnInfo val id: Int,
    @ColumnInfo val title: String,
    @ColumnInfo val tagline: String,
    @ColumnInfo val overview: String,
    @ColumnInfo val genres: List<String>,
    @ColumnInfo val coverUrl: String,
    @ColumnInfo val backdropUrl: String,
    @ColumnInfo val rating: Float,
    @ColumnInfo val formattedRating: String,
    @ColumnInfo val adult: Boolean,
    @ColumnInfo val releaseDate: String,
) {
    companion object {
        val SAMPLE_MOVIE = MovieEntity(
            id = 572802,
            title = "Aquaman and the Lost Kingdom",
            tagline = "The tide is turning.",
            overview = "Black Manta, still driven by the need to avenge his father's death and wielding the power of the mythic Black Trident, will stop at nothing to take Aquaman down once and for all. To defeat him, Aquaman must turn to his imprisoned brother Orm, the former King of Atlantis, to forge an unlikely alliance in order to save the world from irreversible destruction.",
            genres = listOf("Action", "Adventure", "Comedy"),
            coverUrl = "/7lTnXOy0iNtBAdRP3TZvaKJ77F6.jpg",
            backdropUrl = "/4gV6FOT4mEF4JaOmurO1kQSQ0Zl.jpg",
            rating = 6.995f,
            formattedRating = "6.9",
            adult = true,
            releaseDate = "2023-12-20",
        )
    }
}

