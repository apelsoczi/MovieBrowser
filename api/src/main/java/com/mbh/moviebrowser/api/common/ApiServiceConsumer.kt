package com.mbh.moviebrowser.api.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response

/**
 * This class provides functionality for making network requests and processing responses asynchronously.
 * It is designed to be subclassed with concrete implementations defining how responses are mapped to DTO models.
 *
 * @param ioDispatcher The coroutine dispatcher to be used for IO-bound operations.
 * @param R The DTO type parameter representing the response data type.
 */
abstract class ApiServiceConsumer<R>(
    private val ioDispatcher: CoroutineDispatcher
) {

    /**
     * The subclassed mapper to be initialized and used to decode API responses.
     */
    abstract val mapper: DataResponseMapper<R>

    /**
     * Executes a network request and emits the mapped response.
     *
     * @param block The suspending function representing the network request.
     *
     * @return A Flow emitting the mapped response data.
     */
    protected suspend fun request(
        block: suspend () -> Response<ResponseBody>
    ) = channelFlow<R> {
        withContext(ioDispatcher) {
            try {
                val response = block.invoke()
                val dto = mapper.decode(response)
                send(dto)
                close()
            } catch (e: Exception) {
                throw e
            }
        }
    }
}
