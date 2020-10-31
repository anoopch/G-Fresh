package ch.anoop.g_fresh.view_model.repo

import io.reactivex.rxjava3.core.Single

class Repository(
    private val restfulDataSource: RestfulDataSource,
    private val localDataSource: LocalDataSource
) {

    fun loadTrending() = Single.merge(
        restfulDataSource.loadData(),
        localDataSource.loadData()
    )
}