package ch.anoop.g_fresh.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ch.anoop.g_fresh.api.GiffItem

/**
 * Class representing the singleton instance of the DB.
 *
 * Database Version, Name and Dao are added here.
 */

@Database(entities = [GiffItem::class], version = 1, exportSchema = false)
abstract class FavoriteRoomDatabase : RoomDatabase() {

    companion object {
        private const val dataBaseName: String = "fav_database.db"

        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: FavoriteRoomDatabase? = null

        fun getDatabase(context: Context): FavoriteRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteRoomDatabase::class.java,
                    dataBaseName
                )
                    .fallbackToDestructiveMigrationOnDowngrade()
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    abstract fun favoriteGiffsDao(): FavoriteGiffsDao
}
