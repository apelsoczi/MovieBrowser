package com.mbh.moviebrowser.api.trending

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.mbh.moviebrowser.api.common.readJsonFile
import com.mbh.moviebrowser.api.services.trending.TrendingApiResponse.TrendingErrorDTO
import com.mbh.moviebrowser.api.services.trending.TrendingApiResponse.TrendingMoviesDTO
import com.mbh.moviebrowser.api.services.trending.TrendingDataSource
import com.mbh.moviebrowser.api.services.trending.TrendingService
import com.mbh.moviebrowser.api.services.trending.TrendingTimeWindow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

@OptIn(ExperimentalCoroutinesApi::class)
class TrendingDataSourceTest {

    private val testDispatcher = StandardTestDispatcher()

    private val mockWebServer: MockWebServer = MockWebServer()
    private val httpClient = OkHttpClient.Builder().build()

    private val apiService = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(httpClient)
        .build()
        .create(TrendingService::class.java)

    private val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private lateinit var trendingDataSource: TrendingDataSource

    @Before
    fun setUp() {
        trendingDataSource = TrendingDataSource(
            trendingService = apiService,
            json = json,
            ioDispatcher = testDispatcher,
        )
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        Dispatchers.resetMain()
    }

    @Test
    fun `request trending movies, 200 success`() = runTest {
        // given
        enqueueMockResponse(200, "trending-200-success.json")
        // when
        trendingDataSource.request(TrendingTimeWindow.DAY).test {
            // then
            awaitItem().let {
                Truth.assertThat(it).isInstanceOf(TrendingMoviesDTO::class.java)
            }
            awaitComplete()
        }
    }

    @Test
    fun `request trending movies, 400 invalid params`() = runTest {
        // given
        enqueueMockResponse(400, "trending-400-invalid-params.json")
        // when
        trendingDataSource.request(TrendingTimeWindow.DAY).test {
            // then
            awaitItem().let {
                Truth.assertThat(it).isInstanceOf(TrendingErrorDTO::class.java)
            }
            awaitComplete()
        }
    }

    @Test
    fun `request trending movies, 401 api key configuration`() = runTest {
        // given
        enqueueMockResponse(401, "trending-401-api-key.json")
        // when
        trendingDataSource.request(TrendingTimeWindow.DAY).test {
            // then
            awaitItem().let {
                Truth.assertThat(it).isInstanceOf(TrendingErrorDTO::class.java)
            }
            awaitComplete()
        }
    }

    @Test
    fun `request trending movies, 404 resource not found`() = runTest {
        // given
        enqueueMockResponse(404, "trending-404-resource.json")
        // when
        trendingDataSource.request(TrendingTimeWindow.DAY).test {
            // then
            awaitItem().let {
                Truth.assertThat(it).isInstanceOf(TrendingErrorDTO::class.java)
            }
            awaitComplete()
        }
    }

    @Test
    fun `request trending movies, server unreachable and all http client errors`() = runTest {
        // given
        val response = MockResponse()
        response.setSocketPolicy(SocketPolicy.DISCONNECT_AT_START)
        mockWebServer.enqueue(response)
        // when
        trendingDataSource.request(TrendingTimeWindow.DAY).test {
            // then
            awaitError().let {
                assert(it is Exception)
            }
        }
        // and when
        mockWebServer.shutdown()
        trendingDataSource.request(TrendingTimeWindow.DAY).test {
            // and then
            awaitError().let {
                assert(it is Exception)
            }
        }
    }

    private fun enqueueMockResponse(responseCode: Int, fileName: String) {
        val response = MockResponse()
            .setResponseCode(responseCode)
            .setBody(readJsonFile("com/mbh/moviebrowser/trending/$fileName"))
        mockWebServer.enqueue(response)
    }

}