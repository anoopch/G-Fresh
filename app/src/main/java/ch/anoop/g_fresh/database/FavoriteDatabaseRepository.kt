package ch.anoop.g_fresh.database

import androidx.lifecycle.LiveData
import ch.anoop.g_fresh.api.GiffItem

class FavoriteDatabaseRepository(private val favoriteGiffsDao: FavoriteGiffsDao) {

    val allFavoriteGiffs: LiveData<List<GiffItem>> = favoriteGiffsDao.getAllFavoriteGiffs()

    val getAllFavoriteGiffIds: LiveData<List<String>> = favoriteGiffsDao.getAllFavoriteGiffIds()

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
