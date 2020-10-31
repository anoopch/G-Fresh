package ch.anoop.g_fresh.view_model.state

/**
 * Sealed class representing state if it's successful or failure
 */
sealed class ApiResponseResult<out T : Any> {

    class LoadingComplete<out T : Any>(val value: T) : ApiResponseResult<T>()

    sealed class LoadingError : ApiResponseResult<Nothing>() {
        class NetworkFailure(throwableError: Throwable) : Error()
        object ConnectionTimeoutError : Error()
        object InvalidApiResponseError : Error()
        object ApiError : Error()
        sealed class UnSupportedOperationError : Error() {
            object UnSupportedError : UnSupportedOperationError()
        }
    }
}