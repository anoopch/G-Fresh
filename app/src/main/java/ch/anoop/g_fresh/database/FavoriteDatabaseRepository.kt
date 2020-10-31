package ch.anoop.g_fresh.database

import androidx.lifecycle.LiveData
import ch.anoop.g_fresh.api.GiffItem

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class FavoriteDatabaseRepository(private val favoriteGiffsDao: FavoriteGiffsDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allFavoriteGiffs: LiveData<List<GiffItem>> = favoriteGiffsDao.getAllFavoriteGiffs()

    suspend fun insert(newGiffItem: GiffItem) {
        favoriteGiffsDao.insert(newGiffItem)
    }

    suspend fun checkIfExists(idToCheck: String): String {
        return favoriteGiffsDao.checkIfExists(idToCheck)
    }

    suspend fun delete(newGiffItem: GiffItem) {
        favoriteGiffsDao.delete(newGiffItem)
    }

    suspend fun delete() {
        favoriteGiffsDao.delete()
    }
}
