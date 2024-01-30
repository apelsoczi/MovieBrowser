package com.mbh.moviebrowser.api.common

sealed class DomainResult<out T> {

    data class Success<out T>(val data: T) : DomainResult<T>()

    data class Failure(val error: String) : DomainResult<Nothing>()

    data class Exception(val exception: Throwable) : DomainResult<Nothing>()

    companion object {
        fun <T> domainSuccess(data: T): DomainResult<T> = Success(data)
        fun domainFailure(error: String = ""): DomainResult<Nothing> = Failure(error)
        fun domainException(exception: Throwable): DomainResult<Nothing> = Exception(exception)
    }
}