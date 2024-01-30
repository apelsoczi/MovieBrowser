package com.mbh.moviebrowser.api.services.details

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MoviesService {

    @GET("movie/{id}")
    suspend fun detail(
        @Path("id") movieId: String,
    ): Response<ResponseBody>

}