package ch.anoop.g_fresh.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ch.anoop.g_fresh.api.GiffItem
import ch.anoop.g_fresh.api.GiphyResponse
import ch.anoop.g_fresh.api.RetrofitSingleton
import ch.anoop.g_fresh.database.FavoriteDatabaseRepository
import ch.anoop.g_fresh.database.FavoriteRoomDatabase
import ch.anoop.g_fresh.view_model.repo.Repository
import ch.anoop.g_fresh.view_model.repo.RestfulDataSource
import ch.anoop.g_fresh.view_model.state.ApiResponseResult
import ch.anoop.g_fresh.view_model.state.SingleMutableLiveData
import ch.anoop.g_fresh.view_model.state.ViewState
import com.google.gson.JsonParseException
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class TrendingSearchFragmentViewModel(application: Application) : AndroidViewModel(application) {

    // LOG TAG
    private val logTag: String = "TREND_FRAG_V_MODEL"

    // The last submitted query, maintained for pagination. null means Trending
    private var query: String? = null

    // Disposable Container used for background tasks
    private val compositeDisposable = CompositeDisposable()

    // The instance of Database Repository Singleton
    private val databaseRepository by lazy {
        val favoriteGiffsDao =
            FavoriteRoomDatabase.getDatabase(application).favoriteGiffsDao()
        FavoriteDatabaseRepository(favoriteGiffsDao)
    }

    // The instance of Restful API Repository
    private val apiRepository by lazy {
        val api = RetrofitSingleton.getGiphyApiService()
        Repository(RestfulDataSource(api))
    }

    // API response monitoring
    private val _dataEvent by lazy { SingleMutableLiveData<ApiResponseResult<GiphyResponse>>() }
    val dataUpdatedEvent: LiveData<ApiResponseResult<GiphyResponse>> get() = _dataEvent

    // View State monitoring
    private val _viewStateEvent by lazy { SingleMutableLiveData<ViewState>() }
    val viewStateChangeEvent: LiveData<ViewState> get() = _viewStateEvent

    // Fav List monitoring
    val allFavoriteGiffIdsLiveData: LiveData<List<String>> =
        databaseRepository.getAllFavoriteGiffIds

    // Clear the Disposable Container when the Fragment is destroyed and the ViewModel is cleared
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    // Add to add tasks to the Disposable
    private fun Disposable.composeDisposable() = compositeDisposable.add(this)

    // Triggered when new Search or Trending API request completed successfully
    // Empty will fire NoData, Success will transfer data to Fragment and later to the Adapter
    private fun onApiRequestComplete(trending: GiphyResponse) {
        if (trending.data.isEmpty()) {
            _viewStateEvent.postValue(ViewState.NoData)
        } else {
            _dataEvent.postValue(ApiResponseResult.LoadingComplete(trending))
        }
    }

    // Triggered when new Search or Trending API request failed,
    // View's state will be updated accordingly
    private fun onError(error: Throwable) {
        when (error) {
            is UnknownHostException -> {
                _viewStateEvent.postValue(ViewState.Error.ServerNotReachable)
            }
            is SocketTimeoutException -> {
                _viewStateEvent.postValue(ViewState.Error.ServerNotReachable)
            }
            is IOException -> {
                _viewStateEvent.postValue(ViewState.Error.ServerNotReachable)
            }
            is JsonParseException -> {
                _viewStateEvent.postValue(ViewState.Error.InvalidResponse)
            }
            else -> {
                _viewStateEvent.postValue(ViewState.Error.GenericError)
            }
        }
        Log.e(logTag, "onError: Failed to load giffs", error)
    }


    // Fires a search API request
    fun loadSearchForGiff(newQuery: String, offset: Int) {
        query = newQuery
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.loadSearch(newQuery, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::onApiRequestComplete, ::onError)
                .composeDisposable()
        }
    }

    // Fires a Trending API request
    fun loadTrendingGiffs(offset: Int) {
        query = null
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.loadTrending(offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::onApiRequestComplete, ::onError)
                .composeDisposable()
        }
    }

    // Handles FavButton click -- checks of the currently clicked item is in Database
    // if present in database it will be removed
    // if not present in database it will be added newly
    // Both above triggers LiveData updates
    fun updateFavoriteButtonClicked(clickedGiffImage: GiffItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingDbId: String? = databaseRepository.checkIfExists(clickedGiffImage.id)
            if (existingDbId.isNullOrEmpty()) {
                databaseRepository.insert(clickedGiffImage)
            } else {
                databaseRepository.delete(clickedGiffImage)
            }
        }
    }

    // Pagination Scroll -- updates UI's state to loading and fires a new API call
    // If query is null or blank, Trending API is fired with offset as current data count
    // If query is not null and has one or more characters, then
    //          Search API is fired with last query along with offset as current data count
    fun listScrolled(totalItemCount: Int) {
        viewModelScope.launch(Dispatchers.IO) {

            _viewStateEvent.postValue(ViewState.LoadingNext)

            if (query.isNullOrEmpty())
                loadTrendingGiffs(totalItemCount)
            else
                loadSearchForGiff(query!!, totalItemCount)
        }
    }
}