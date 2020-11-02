package ch.anoop.g_fresh.view_model.repo

import ch.anoop.g_fresh.api.GiphyApiService
import ch.anoop.g_fresh.api.GiphyResponse
import io.reactivex.rxjava3.core.Single

/**
 * A Data source representing the RestFul API server.
 *
 * Fetch data using GiphyApiService and Retrofit
 *
 */
class RestfulDataSource(
    private val apiService: GiphyApiService
) : DataSource {

    /**
     *  Loads currently trending Giffs
     *  Offset denotes how many items to skip
     */
    override fun loadTrendingGiffs(offset: Int): Single<GiphyResponse> {
        return apiService.fetchLatestTrending(offset)
    }

    /**
     *  Loads Giffs matching the current search query
     *  Offset denotes how many items to skip
     */
    override fun searchGiffs(query: String, offset: Int): Single<GiphyResponse> {
        return apiService.search(query, offset)
    }

}