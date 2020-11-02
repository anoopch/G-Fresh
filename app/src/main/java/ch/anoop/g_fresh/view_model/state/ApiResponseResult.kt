package ch.anoop.g_fresh.view_model.state

/**
 * Sealed Generics class representing state of API's successful or failure
 */
sealed class ApiResponseResult<out T : Any> {
    // API response as expected
    class LoadingComplete<out T : Any>(val value: T) : ApiResponseResult<T>()

    // API response failed -- Not used
    class LoadingFailed<out T : Any>(val value: T?) : ApiResponseResult<T>()
}