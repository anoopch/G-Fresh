package ch.anoop.g_fresh.view_model.state

/**
 * Sealed class representing state of API's successful or failure
 */
sealed class ApiResponseResult<out T : Any> {
    class LoadingComplete<out T : Any>(val value: T) : ApiResponseResult<T>()
    class LoadingFailed<out T : Any>(val value: T?) : ApiResponseResult<T>()
}