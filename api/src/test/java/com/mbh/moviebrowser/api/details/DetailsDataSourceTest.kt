package com.mbh.moviebrowser.api.details

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.mbh.moviebrowser.api.common.readJsonFile
import com.mbh.moviebrowser.api.services.details.DetailsApiResponse
import com.mbh.moviebrowser.api.services.details.DetailsDataSource
import com.mbh.moviebrowser.api.services.details.MoviesService
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
class DetailsDataSourceTest {

    private val testDispatcher = StandardTestDispatcher()

    private val mockWebServer: MockWebServer = MockWebServer()
    private val httpClient = OkHttpClient.Builder().build()

    private val moviesService = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(httpClient)
        .build()
        .create(MoviesService::class.java)

    private val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private lateinit var detailsDataSource: DetailsDataSource

    @Before
    fun setUp() {
        detailsDataSource = DetailsDataSource(
            moviesService = moviesService,
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
    fun `request movie details, 200 success`() = runTest {
        // given
        enqueueMockResponse(200, "movie-200-success.json")
        // when
        detailsDataSource.request(1234).test {
            // then
            awaitItem().let {
                Truth.assertThat(it).isInstanceOf(DetailsApiResponse.MovieDetailDTO::class.java)
            }
            awaitComplete()
        }
    }

    @Test
    fun `request movie details, 401 api key invalid`() = runTest {
        // given
        enqueueMockResponse(401, "movie-401-api-key.json")
        // when
        detailsDataSource.request(1234).test {
            // then
            awaitItem().let {
                Truth.assertThat(it).isInstanceOf(DetailsApiResponse.DetailsErrorDTO::class.java)
            }
            awaitComplete()
        }
    }

    @Test
    fun `request movie details, 404 resource not valid`() = runTest {
        // given
        enqueueMockResponse(404, "movie-404-resource.json")
        // when
        detailsDataSource.request(1234).test {
            // then
            awaitItem().let {
                Truth.assertThat(it).isInstanceOf(DetailsApiResponse.DetailsErrorDTO::class.java)
            }
            awaitComplete()
        }
    }

    @Test
    fun `request movie details, server unreachable and all http client errors`() = runTest {
        // given
        val response = MockResponse()
        response.setSocketPolicy(SocketPolicy.DISCONNECT_AT_START)
        mockWebServer.enqueue(response)
        // when
        detailsDataSource.request(1234).test {
            // then
            awaitError().let {
                assert(it is Exception)
            }
        }
        // and when
        mockWebServer.shutdown()
        detailsDataSource.request(1234).test {
            // and then
            awaitError().let {
                assert(it is Exception)
            }
        }
    }

    private fun enqueueMockResponse(responseCode: Int, fileName: String) {
        val response = MockResponse()
            .setResponseCode(responseCode)
            .setBody(readJsonFile("com/mbh/moviebrowser/details/$fileName"))
        mockWebServer.enqueue(response)
    }

}