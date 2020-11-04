package ch.anoop.g_fresh.view_model.state

/**
 * Represents UI states
 *
 *  No Data
 *  Loading - LoadingFresh, LoadingComplete, LoadingNext
 *  Error - ServerNotReachable, InvalidResponse, GenericError
 */

sealed class ViewState {
    companion object {
        const val ERROR_TYPE_NORMAL = 1
        const val ERROR_TYPE_API = 2
        const val ERROR_TYPE_NO_DATA = 3
        const val ERROR_TYPE_SERVER_NOT_REACHABLE = 4
    }

    object NoData : ViewState()
    object LoadingFresh : ViewState()
    object LoadingComplete : ViewState()
    object LoadingNext : ViewState()

    object Error {
        object ServerNotReachable : ViewState()
        object InvalidResponse : ViewState()
        object GenericError : ViewState()
    }
}