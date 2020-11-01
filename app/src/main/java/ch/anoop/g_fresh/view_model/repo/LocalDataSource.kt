package ch.anoop.g_fresh.view_model.repo

import ch.anoop.g_fresh.api.GiphyResponse
import io.reactivex.rxjava3.core.Single

class LocalDataSource : DataSource {

    override fun loadTrendingGiffs(offset: Int): Single<GiphyResponse> {
        return Single.fromCallable { GiphyResponse((emptyList())) }
    }

    override fun searchGiffs(query: String, offset: Int): Single<GiphyResponse> {
        return Single.fromCallable { GiphyResponse((emptyList())) }
    }

}