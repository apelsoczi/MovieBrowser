package com.mbh.moviebrowser.api.services.trending

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TrendingService {

    @GET("trending/movie/{time_window}")
    suspend fun movies(
        @Path("time_window") period: TrendingTimeWindow = TrendingTimeWindow.DAY,
    ): Response<ResponseBody>

}

enum class TrendingTimeWindow {
    DAY,
    WEEK;

    override fun toString() = name.lowercase()
}