package com.mbh.moviebrowser.api.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOf
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
     * Represents the cache for the fetched data, initialized as DomainException - NullPointerException
     * when the cache is empty.
     */
    private var cache: DomainResult<R> = DomainResult.domainException(NullPointerException())

    /**
     * Executes a request to fetch data.
     * If the cache contains a successful result, emits the cached data wrapped in a Flow.
     * If the cache does not contain a successful result, initiates a request by executing the given block.
     *
     * @param block A suspend function block that returns a Retrofit Response containing a ResponseBody.
     * @return A Flow emitting a successful result or an exception from the executed block.
     */
    protected suspend fun cached(
        block: suspend () -> Response<ResponseBody>,
    ): Flow<R> = withContext(ioDispatcher) {
        if (cache is DomainResult.Success<R>) {
            flowOf((cache as DomainResult.Success<R>).data)
        } else {
            cache = DomainResult.domainException(NullPointerException())
            request(block)
        }
    }

    /**
     *
     */
    internal fun addToCache(dto: DomainResult<R>) {
        cache = dto
    }

    /**
     * Executes a network request and emits the mapped response.
     *
     * @param block The suspending function representing the network request.
     *
     * @return A Flow emitting the mapped response data.
     */
    protected suspend fun request(
        block: suspend () -> Response<ResponseBody>
    ): Flow<R> = channelFlow<R> {
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
