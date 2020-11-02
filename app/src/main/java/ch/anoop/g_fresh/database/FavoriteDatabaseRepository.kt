package ch.anoop.g_fresh.database

import androidx.lifecycle.LiveData
import ch.anoop.g_fresh.api.GiffItem

/**
 * Repository class containing all the Database read and write operations
 */
class FavoriteDatabaseRepository(private val favoriteGiffsDao: FavoriteGiffsDao) {

    /**
     * The list of all fav giffs from the DB as a LiveData
     */
    val allFavoriteGiffs: LiveData<List<GiffItem>> = favoriteGiffsDao.getAllFavoriteGiffs()


    /**
     * The list of all fav giff's Ids from the DB as a LiveData
     */
    val getAllFavoriteGiffIds: LiveData<List<String>> = favoriteGiffsDao.getAllFavoriteGiffIds()

    /**
     * checks is Giff image with given id exists in DB
     */
    suspend fun checkIfExists(idToCheck: String): String {
        return favoriteGiffsDao.checkIfExists(idToCheck)
    }

    /**
     * inserts a new Fav Giff image
     */
    suspend fun insert(newGiffItem: GiffItem) {
        favoriteGiffsDao.insert(newGiffItem)
    }

    /**
     * deletes a Giff image from the db
     */
    suspend fun delete(newGiffItem: GiffItem) {
        favoriteGiffsDao.delete(newGiffItem)
    }

}
