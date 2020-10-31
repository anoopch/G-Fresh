package ch.anoop.g_fresh.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ch.anoop.g_fresh.api.GiphyResponse
import ch.anoop.g_fresh.api.RetrofitSingleton
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

class TrendingSearchFragmentViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _dataEvent by lazy { SingleMutableLiveData<ApiResponseResult<GiphyResponse>>() }
    val dataUpdatedEvent: LiveData<ApiResponseResult<GiphyResponse>> get() = _dataEvent

    private val _viewStateEvent by lazy { SingleMutableLiveData<ViewState>() }
    val viewStateChangeEvent: LiveData<ViewState> get() = _viewStateEvent

    private val repository by lazy {
        val api = RetrofitSingleton.getGiphyApiService()
        Repository(RestfulDataSource(api), LocalDataSource())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun onFragmentCreated() {
        println("Search/Trending Fragment created")
    }

    fun loadTrendingGiffs() {
        println("Fetching Trending")
        repository.loadTrending()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onApiRequestComplete, ::onError)
            .composeDisposable()
    }

    private fun onApiRequestComplete(trending: GiphyResponse) {
        if (trending.data.isEmpty()) {
            println("Empty test result")
        } else {
            _dataEvent.postValue(ApiResponseResult.LoadingComplete(trending))
        }
    }

    private fun onError(error: Throwable) {
        println("Error - unable to load")
        error.printStackTrace()
    }

    private fun Disposable.composeDisposable() = compositeDisposable.add(this)

    fun loadSearchForGiff(query: String) {
        println("Fetching Trending")
        repository.loadSearch(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onApiRequestComplete, ::onError)
            .composeDisposable()
    }

}