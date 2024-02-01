package com.mbh.moviebrowser.api.services.genres

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface GenreService {

    @GET("genre/movie/list")
    suspend fun movie(): Response<ResponseBody>

}