package com.mbh.moviebrowser.features.trending

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.mbh.moviebrowser.api.common.DomainResult.Companion.domainException
import com.mbh.moviebrowser.api.common.DomainResult.Companion.domainFailure
import com.mbh.moviebrowser.api.common.DomainResult.Companion.domainSuccess
import com.mbh.moviebrowser.domain.data.MovieEntity
import com.mbh.moviebrowser.domain.usecase.FavoritesListUseCase
import com.mbh.moviebrowser.domain.usecase.TrendingMoviesTodayUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class TrendingViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val trendingMoviesTodayUseCase = mockk<TrendingMoviesTodayUseCase>()
    private val favoritesListUseCase = mockk<FavoritesListUseCase>()
    private val movie = MovieEntity.SAMPLE_MOVIE

    lateinit var viewModel: TrendingViewModel

    @Before
    fun setup() {
        viewModel = TrendingViewModel(
            trendingMoviesUseCase = trendingMoviesTodayUseCase,
            favoritesListUseCase = favoritesListUseCase,
        )
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    //region: init and load data

    @Test
    fun `init and load data from repository as domain success`() = runTest {
        // given
        val data = listOf(movie)
        coEvery { trendingMoviesTodayUseCase() } returns domainSuccess(data)
        coEvery { favoritesListUseCase.invoke() } returns flowOf(data)
        // when
        viewModel.handle(Actions.Init)
        viewModel.state.test {
            // then
            awaitItem().let {
                assertThat(it.isLoading)
            }
            awaitItem().let {
                assertThat(it.items).isNotEmpty()
                assertThat(it.favorites.isNotEmpty())
                assertThat(it.isLoading).isFalse()
            }
        }
        coVerify(exactly = 1) { trendingMoviesTodayUseCase() }
        coVerify(exactly = 2) { favoritesListUseCase() }
    }

    @Test
    fun `init and load data from repository as domain failure`() = runTest {
        // given
        coEvery { trendingMoviesTodayUseCase() } returns domainFailure("")
        coEvery { favoritesListUseCase.invoke() } returns flowOf(emptyList())
        // when
        viewModel.handle(Actions.Init)
        viewModel.state.test {
            skipItems(1)
            // then
            awaitItem().let {
                assertThat(it.isLoading).isFalse()
                assertThat(it.isError)
            }
        }
        coVerify(exactly = 1) { trendingMoviesTodayUseCase() }
        coVerify(exactly = 1) { favoritesListUseCase() }
    }

    @Test
    fun `init and load data from repository as domain exception`() = runTest {
        // given
        coEvery { trendingMoviesTodayUseCase() } returns domainException(mockk())
        coEvery { favoritesListUseCase.invoke() } returns flowOf(emptyList())
        // when
        viewModel.handle(Actions.Init)
        viewModel.state.test {
            skipItems(1)
            // then
            awaitItem().let {
                assertThat(it.isLoading).isFalse()
                assertThat(it.isError)
            }
        }
        coVerify(exactly = 1) { trendingMoviesTodayUseCase() }
        coVerify(exactly = 1) { favoritesListUseCase() }
    }

    // endregion

    // region: dismiss error

    @Test
    fun `dismiss error loads data`() = runTest {
        // given
        val data = listOf(movie)
        coEvery { trendingMoviesTodayUseCase() } returns domainException(mockk())
        coEvery { favoritesListUseCase.invoke() } returns flowOf(data)
        viewModel.handle(Actions.Init)
        // when
        viewModel.handle(Actions.DismissError)
        // then
        viewModel.state.test {
            awaitItem().let {
                println(it.isLoading)
            }
            skipItems(1)
        }
        coVerify(exactly = 1) { trendingMoviesTodayUseCase() }
        coVerify(exactly = 1) { favoritesListUseCase() }
    }

    // endregion

}