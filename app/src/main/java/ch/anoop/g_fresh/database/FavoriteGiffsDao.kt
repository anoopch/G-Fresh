package ch.anoop.g_fresh.database

import androidx.lifecycle.LiveData
import androidx.room.*
import ch.anoop.g_fresh.api.GiffItem

@Dao
interface FavoriteGiffsDao {

    @Query("SELECT * FROM fav_giffs")
    fun getAllFavoriteGiffs(): LiveData<List<GiffItem>>

    @Query("SELECT id FROM fav_giffs")
    fun getAllFavoriteGiffIds(): LiveData<List<String>>

    @Query("SELECT id FROM fav_giffs WHERE id=:queryId")
    suspend fun checkIfExists(queryId: String): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(newGiffItem: GiffItem)

    @Delete
    suspend fun delete(newGiffItem: GiffItem)

    @Query("DELETE FROM fav_giffs")
    suspend fun delete()
}
