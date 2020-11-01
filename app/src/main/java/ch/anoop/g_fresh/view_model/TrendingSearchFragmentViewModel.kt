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

    private val logTag: String = "TREND_FRAG_V_MODEL"
    private var query: String? = null
    private val compositeDisposable = CompositeDisposable()

    private val databaseRepository by lazy {
        val favoriteGiffsDao =
            FavoriteRoomDatabase.getDatabase(application).favoriteGiffsDao()
        FavoriteDatabaseRepository(favoriteGiffsDao)
    }

    private val apiRepository by lazy {
        val api = RetrofitSingleton.getGiphyApiService()
        Repository(RestfulDataSource(api))
    }

    private val _dataEvent by lazy { SingleMutableLiveData<ApiResponseResult<GiphyResponse>>() }
    val dataUpdatedEvent: LiveData<ApiResponseResult<GiphyResponse>> get() = _dataEvent

    private val _viewStateEvent by lazy { SingleMutableLiveData<ViewState>() }
    val viewStateChangeEvent: LiveData<ViewState> get() = _viewStateEvent

    val allFavoriteGiffIdsLiveData: LiveData<List<String>> =
        databaseRepository.getAllFavoriteGiffIds

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private fun Disposable.composeDisposable() = compositeDisposable.add(this)

    private fun onApiRequestComplete(trending: GiphyResponse) {
        if (trending.data.isEmpty()) {
            _viewStateEvent.postValue(ViewState.NoData)
        } else {
            _dataEvent.postValue(ApiResponseResult.LoadingComplete(trending))
        }
    }

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