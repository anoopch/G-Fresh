package ch.anoop.g_fresh.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ch.anoop.g_fresh.api.GiffItem
import ch.anoop.g_fresh.database.FavoriteDatabaseRepository
import ch.anoop.g_fresh.database.FavoriteRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel for the Favorites fragment
 *      -- Holds the Favorite items LiveData
 *      -- Adds, removes items to fav table of the DB based on button click and isFavorite flag
 */
class FavoriteFragmentViewModel(application: Application) : AndroidViewModel(application) {

    // Reference to the Database repository
    private val databaseRepository by lazy {
        val favoriteGiffsDao =
            FavoriteRoomDatabase.getDatabase(application).favoriteGiffsDao()
        FavoriteDatabaseRepository(favoriteGiffsDao)
    }

    // LiveData list of all current Giff Images
    val favGiffItemListLiveData: LiveData<List<GiffItem>> = databaseRepository.allFavoriteGiffs

    // Fired from Fragment when fav button is clicked
    fun updateFavoriteButtonClicked(clickedGiffImage: GiffItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingDbId: String? = databaseRepository.checkIfExists(clickedGiffImage.id)
            if (clickedGiffImage.isFavorite && existingDbId.isNullOrEmpty()) {
                databaseRepository.insert(clickedGiffImage)
            } else {
                databaseRepository.delete(clickedGiffImage)
            }
        }
    }
}