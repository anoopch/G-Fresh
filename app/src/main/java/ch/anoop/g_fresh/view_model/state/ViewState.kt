package ch.anoop.g_fresh.view_model.state

/**
 * Represents UI states - No Data, Error and Loading (LoadingFresh, LoadingComplete, LoadingNext)
 */

sealed class ViewState {
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