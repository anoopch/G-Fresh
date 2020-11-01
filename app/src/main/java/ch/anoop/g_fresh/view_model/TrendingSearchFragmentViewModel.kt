package ch.anoop.g_fresh.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ch.anoop.g_fresh.api.GiffItem
import ch.anoop.g_fresh.api.GiphyResponse
import ch.anoop.g_fresh.api.RetrofitSingleton
import ch.anoop.g_fresh.database.FavoriteDatabaseRepository
import ch.anoop.g_fresh.database.FavoriteRoomDatabase
import ch.anoop.g_fresh.view_model.repo.LocalDataSource
import ch.anoop.g_fresh.view_model.repo.Repository
import ch.anoop.g_fresh.view_model.repo.RestfulDataSource
import ch.anoop.g_fresh.view_model.state.ApiResponseResult
import ch.anoop.g_fresh.view_model.state.SingleMutableLiveData
import ch.anoop.g_fresh.view_model.state.ViewState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class TrendingSearchFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private var query: String? = null
    private val compositeDisposable = CompositeDisposable()
    private val visibleItemThreshold = 4

    private val databaseRepository by lazy {
        val favoriteGiffsDao =
            FavoriteRoomDatabase.getDatabase(application).favoriteGiffsDao()
        FavoriteDatabaseRepository(favoriteGiffsDao)
    }

    private val repository by lazy {
        val api = RetrofitSingleton.getGiphyApiService()
        Repository(RestfulDataSource(api), LocalDataSource())
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
        if (error is SocketTimeoutException) {
            _viewStateEvent.postValue(ViewState.Error)
            _dataEvent.postValue(ApiResponseResult.LoadingFailed(null))
        }
        println("Error - unable to load")
        error.printStackTrace()
    }


    fun loadSearchForGiff(newQuery: String, offset: Int) {
        println("Fetching Search")
        query = newQuery
        repository.loadSearch(newQuery, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onApiRequestComplete, ::onError)
            .composeDisposable()
    }

    fun loadTrendingGiffs(offset: Int) {
        println("Fetching Trending")
        query = null
        repository.loadTrending(offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onApiRequestComplete, ::onError)
            .composeDisposable()
    }

    fun updateFavoriteButtonClicked(clickedGiffImage: GiffItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingDbId: String? = databaseRepository.checkIfExists(clickedGiffImage.id)
            if (clickedGiffImage.isFavorite && existingDbId.isNullOrEmpty()) {
                databaseRepository.insert(clickedGiffImage)
            } else {
                databaseRepository.delete(clickedGiffImage)
            }
        }
    }

    fun listScrolled(visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + visibleItemThreshold >= totalItemCount) {
            if (query.isNullOrEmpty())
                loadTrendingGiffs(totalItemCount)
            else
                loadSearchForGiff(query!!, totalItemCount)
        }
    }
}