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

class FavoriteFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val databaseRepository by lazy {
        val favoriteGiffsDao =
            FavoriteRoomDatabase.getDatabase(application).favoriteGiffsDao()
        FavoriteDatabaseRepository(favoriteGiffsDao)
    }
    val favGiffItemListLiveData: LiveData<List<GiffItem>> = databaseRepository.allFavoriteGiffs

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