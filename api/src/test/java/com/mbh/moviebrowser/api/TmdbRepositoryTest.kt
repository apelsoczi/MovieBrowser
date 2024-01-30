package com.mbh.moviebrowser.api

import com.google.common.truth.Truth.assertThat
import com.mbh.moviebrowser.api.common.DomainResult
import com.mbh.moviebrowser.api.services.details.DetailsApiResponse
import com.mbh.moviebrowser.api.services.details.DetailsDataSource
import com.mbh.moviebrowser.api.services.trending.TrendingApiResponse.TrendingErrorDTO
import com.mbh.moviebrowser.api.services.trending.TrendingApiResponse.TrendingMoviesDTO
import com.mbh.moviebrowser.api.services.trending.TrendingDataSource
import com.mbh.moviebrowser.api.services.trending.TrendingTimeWindow
import com.mbh.moviebrowser.api.services.trending.model.MovieDTO
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class TmdbRepositoryTest {

    private val trendingDataSource = mockk<TrendingDataSource>()
    private val detailsDataSource = mockk<DetailsDataSource>()

    private lateinit var repository: TmdbRepository

    @Before
    fun setup() {
        repository = TmdbRepository(
            trendingDataSource = trendingDataSource,
            detailsDataSource = detailsDataSource,
        )
    }

    // region: trending

    @Test
    fun `trending api mapped to success domain model`() = runTest {
        // given
        val dto = mockk<TrendingMoviesDTO>() {
            coEvery { results } returns listOf(mockk<MovieDTO>())
        }
        coEvery { trendingDataSource.request(any()) } returns flowOf(dto)
        // when
        val data = repository.trendingMovies(TrendingTimeWindow.DAY)
        // then
        assertThat(data).isInstanceOf(DomainResult.Success::class.java)
        coVerify(exactly = 1) { trendingDataSource.request(TrendingTimeWindow.DAY) }
    }

    @Test
    fun `trending api mapped to domain failure`() = runTest {
        // given
        val dto = mockk<TrendingErrorDTO>()
        coEvery { trendingDataSource.request(any()) } returns flowOf(dto)
        // when
        val data = repository.trendingMovies(TrendingTimeWindow.DAY)
        // then
        assertThat(data).isInstanceOf(DomainResult.Failure::class.java)
        coVerify(exactly = 1) { trendingDataSource.request(TrendingTimeWindow.DAY) }
    }

    @Test
    fun `trending api mapped to domain exception`() = runTest {
        // given
        coEvery { trendingDataSource.request(any()) } throws Exception()
        // when
        val data = repository.trendingMovies(TrendingTimeWindow.DAY)
        // then
        assertThat(data).isInstanceOf(DomainResult.Exception::class.java)
        coVerify(exactly = 1) { trendingDataSource.request(TrendingTimeWindow.DAY) }
    }

    // endregion

    // region: details

    @Test
    fun `details api mapped to success domain model`() = runTest {
        // given
        val dto = mockk<DetailsApiResponse.MovieDetailDTO>()
        coEvery { detailsDataSource.request(any()) } returns flowOf(dto)
        // when
        val data = repository.movieDetail(0)
        // then
        assertThat(data).isInstanceOf(DomainResult.Success::class.java)
        coVerify(exactly = 1) { detailsDataSource.request(0) }
    }

    @Test
    fun `details api mapped to domain failure`() = runTest {
        // given
        val dto = mockk<DetailsApiResponse.DetailsErrorDTO>()
        coEvery { detailsDataSource.request(any()) } returns flowOf(dto)
        // when
        val data = repository.movieDetail(0)
        // then
        assertThat(data).isInstanceOf(DomainResult.Failure::class.java)
        coVerify(exactly = 1) { detailsDataSource.request(0) }
    }

    @Test
    fun `details api mapped to domain exception`() = runTest {
        // given
        coEvery { detailsDataSource.request(any()) } throws Exception()
        // when
        val data = repository.movieDetail(0)
        // then
        assertThat(data).isInstanceOf(DomainResult.Exception::class.java)
        coVerify(exactly = 1) { detailsDataSource.request(0) }
    }

    // endregion

}