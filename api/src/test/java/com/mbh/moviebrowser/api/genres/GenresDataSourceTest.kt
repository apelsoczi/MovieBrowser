package com.mbh.moviebrowser.api.genres

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.mbh.moviebrowser.api.common.readJsonFile
import com.mbh.moviebrowser.api.services.genres.GenreApiResponse.GenresErrorDTO
import com.mbh.moviebrowser.api.services.genres.GenreApiResponse.GenresListDTO
import com.mbh.moviebrowser.api.services.genres.GenreDataSource
import com.mbh.moviebrowser.api.services.genres.GenreService
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
class GenresDataSourceTest {

    private val testDispatcher = StandardTestDispatcher()

    private val mockWebServer: MockWebServer = MockWebServer()
    private val httpClient = OkHttpClient.Builder().build()

    private val apiService = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(httpClient)
        .build()
        .create(GenreService::class.java)

    private val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private lateinit var genreDataSource: GenreDataSource

    @Before
    fun setUp() {
        genreDataSource = GenreDataSource(
            genreService = apiService,
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
    fun `request genres, 200 success`() = runTest {
        // given
        enqueueMockResponse(200, "genres-200-success.json")
        // when
        genreDataSource.cached().test {
            // then
            awaitItem().let {
                Truth.assertThat(it).isInstanceOf(GenresListDTO::class.java)
            }
            awaitComplete()
        }
    }

    @Test
    fun `request genres, 401 api key configuration`() = runTest {
        // given
        enqueueMockResponse(401, "genres-401-api-key.json")
        // when
        genreDataSource.cached().test {
            // then
            awaitItem().let {
                Truth.assertThat(it).isInstanceOf(GenresErrorDTO::class.java)
            }
            awaitComplete()
        }
    }

    // region: cache

    @Test
    fun `request genres, returns cache response on multiple invocations, and offline`() = runTest {
        // given
        enqueueMockResponse(200, "genres-200-success.json")
        // when
        genreDataSource.cached().test {
            // then
            awaitItem().let {
                Truth.assertThat(it).isInstanceOf(GenresListDTO::class.java)
            }
            awaitComplete()
        }
        // and when
        enqueueMockResponse(401, "genres-401-api-key.json")
        genreDataSource.cached().test {
            // then
            awaitItem().let {
                Truth.assertThat(it).isInstanceOf(GenresListDTO::class.java)
            }
            awaitComplete()
        }
        // and when
        mockWebServer.shutdown()
        genreDataSource.cached().test {
            // and then
            awaitItem().let {
                Truth.assertThat(it).isInstanceOf(GenresListDTO::class.java)
            }
            awaitComplete()
        }
    }

    // endregion

    @Test
    fun `request genres, server unreachable and all http client errors`() = runTest {
        // given
        val response = MockResponse()
        response.setSocketPolicy(SocketPolicy.DISCONNECT_AT_START)
        mockWebServer.enqueue(response)
        // when
        genreDataSource.cached().test {
            // then
            awaitError().let {
                assert(it is Exception)
            }
        }
        // and when
        mockWebServer.shutdown()
        genreDataSource.cached().test {
            // and then
            awaitError().let {
                assert(it is Exception)
            }
        }
    }

    private fun enqueueMockResponse(responseCode: Int, fileName: String) {
        val response = MockResponse()
            .setResponseCode(responseCode)
            .setBody(readJsonFile("com/mbh/moviebrowser/genres/$fileName"))
        mockWebServer.enqueue(response)
    }

}