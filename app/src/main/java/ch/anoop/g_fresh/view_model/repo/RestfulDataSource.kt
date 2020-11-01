package ch.anoop.g_fresh.view_model.repo

import ch.anoop.g_fresh.api.GiphyApiService
import ch.anoop.g_fresh.api.GiphyResponse
import io.reactivex.rxjava3.core.Single

class RestfulDataSource(
    private val apiService: GiphyApiService
) : DataSource {

    override fun loadTrendingGiffs(): Single<GiphyResponse> {
        return apiService.fetchLatestTrending()
    }

    override fun searchGiffs(query: String): Single<GiphyResponse> {
        return apiService.search(query)
    }

}