package ch.anoop.g_fresh.view_model.repo

import ch.anoop.g_fresh.api.GiphyApiService

class RestfulDataSource(
    private val apiService: GiphyApiService
) : DataSource {
    override fun loadData() = apiService.fetchLatestTrending()

}