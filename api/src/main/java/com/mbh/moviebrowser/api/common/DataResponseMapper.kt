package com.mbh.moviebrowser.api.common

import okhttp3.ResponseBody
import retrofit2.Response

/**
 * This class defines methods for decoding successful and error responses from the API.
 *
 * @param R The type parameter representing the domain model of the response.
 */
abstract class DataResponseMapper<R> {

    /**
     * Abstract method to decode a successful response body into a domain model.
     *
     * @param bodyString The string representation of the response body.
     *
     * @return The decoded DTO representing the successful response.
     */
    abstract fun decodeSuccess(bodyString: String): R

    /**
     * Abstract method to decode an error response body into a domain model.
     *
     * @param bodyString The string representation of the error response body.
     *
     * @return The decoded DTO representing the error response.
     */
    abstract fun decodeError(bodyString: String): R

    /**
     * This method handles decoding of both successful and error responses returned by the API.
     * It parses the response body and delegates the decoding process to specific methods based on
     * the response status.
     *
     * @param response The response object received from the API.
     *
     * @return The decoded domain model representing the response.
     * @throws Exception if any error occurs during the decoding process.
     */
    fun decode(response: Response<ResponseBody>): R {
        val dto = try {
            if (response.isSuccessful) {
                val successJson = response.body()?.string() ?: ""
                decodeSuccess(successJson)
            } else {
                val errorJson = response.errorBody()?.string() ?: ""
                decodeError(errorJson)
            }
        } catch (e: Exception) {
            throw e
        }
        return dto
    }
}