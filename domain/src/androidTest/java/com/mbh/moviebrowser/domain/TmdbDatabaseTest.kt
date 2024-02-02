package com.mbh.moviebrowser.domain

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.mbh.moviebrowser.domain.data.MovieEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class TmdbDatabaseTest {

    private lateinit var database: com.mbh.moviebrowser.domain.data.TmdbDatabase
    private lateinit var dao: com.mbh.moviebrowser.domain.data.FavoritesDao

    val movie1 = MovieEntity(
        id = 1,
        title = "Inception",
        tagline = "Your mind is the scene of the crime.",
        overview = "Cobb, a skilled thief who commits corporate espionage by infiltrating the subconscious of his targets is offered a chance to regain his old life as payment for a task considered to be impossible: \"inception\", the implantation of another person's idea into a target's subconscious.",
        genres = listOf("Action", "Science Fiction", "Adventure"),
        coverUrl = "https://example.com/inception_cover.jpg",
        backdropUrl = "https://example.com/inception_backdrop.jpg",
        rating = 8.8f,
        formattedRating = "8.8/10",
        adult = false,
        isFavorite = true,
        releaseDate = "2010-07-16"
    )

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, com.mbh.moviebrowser.domain.data.TmdbDatabase::class.java)
            .build()
        dao = database.foavoritesDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertFavorite() {
        // given
        val movie = movie1
        // when
        runBlocking { dao.addFavorite(movie) }
//        // then
        runBlocking {
            dao.movie(movie.id).let {
                assertThat(it?.id).isEqualTo(movie.id)
            }
        }
    }

    @Test
    fun deleteFavoriteMovie() {
        // given
        val movie = movie1
        runBlocking { dao.addFavorite(movie) }
        // when
        runBlocking { dao.removeFavorite(movie) }
        // then
        runBlocking {
            dao.getAll().let {
                assertThat(it).isEmpty()
            }
        }
    }

}