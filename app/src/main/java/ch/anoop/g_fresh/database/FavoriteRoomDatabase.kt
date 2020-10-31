package ch.anoop.g_fresh.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class FavoriteRoomDatabase : RoomDatabase() {

    abstract fun favoriteGiffsDao(): FavoriteGiffsDao

    companion object {
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
                    "fav_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
