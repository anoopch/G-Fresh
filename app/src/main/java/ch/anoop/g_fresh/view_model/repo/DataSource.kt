package ch.anoop.g_fresh.view_model.repo

import ch.anoop.g_fresh.api.GiphyResponse
import io.reactivex.rxjava3.core.Single

interface DataSource {
    fun loadTrendingGiffs(): Single<GiphyResponse>
    fun searchGiffs(query: String): Single<GiphyResponse>
}