package ch.anoop.g_fresh.view_model.repo

import io.reactivex.rxjava3.core.Single

class Repository(
    private val restfulDataSource: RestfulDataSource,
    private val localDataSource: LocalDataSource
) {

    fun loadTrending(offset: Int) = Single.merge(
        restfulDataSource.loadTrendingGiffs(offset),
        localDataSource.loadTrendingGiffs(offset)
    )

    fun loadSearch(query: String, offset: Int) = Single.merge(
        restfulDataSource.searchGiffs(query, offset),
        localDataSource.searchGiffs(query, offset)
    )
}