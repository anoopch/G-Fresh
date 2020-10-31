package ch.anoop.g_fresh.view_model.repo

import ch.anoop.g_fresh.api.GiphyResponse
import io.reactivex.rxjava3.core.Single

class LocalDataSource : DataSource {
    override fun loadData(): Single<GiphyResponse> =
        Single.fromCallable { GiphyResponse((emptyList())) }

}