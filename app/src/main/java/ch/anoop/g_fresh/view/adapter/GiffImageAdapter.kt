package ch.anoop.g_fresh.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ch.anoop.g_fresh.R
import ch.anoop.g_fresh.api.GiffItem
import ch.anoop.g_fresh.view.adapter.view_holder.GiffImageViewHolder
import ch.anoop.g_fresh.view.custom.FavoriteClickListener
import okhttp3.internal.toImmutableList

/**
 * Common adapter for showing the Giffs in Search/Trending Fragment and Fav Fragment
 */

class GiffImageAdapter(private val favoriteClickListener: FavoriteClickListener) :
    RecyclerView.Adapter<GiffImageViewHolder>(), FavoriteClickListener {

    // displayedGiffList -- Contains the giffs currently presented to the user
    private val displayedGiffList = mutableListOf<GiffItem>()

    // favoriteGiffIdsList -- Ids of all the Giffs currently in the Fav table of the database
    // Will be null for the Favorite Fragment
    private val favoriteGiffIdsList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiffImageViewHolder {
        return GiffImageViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.recycler_item_gif_image, parent, false
                )
        )
    }

    override fun getItemCount() = displayedGiffList.size

    override fun onBindViewHolder(holder: GiffImageViewHolder, position: Int) {

        // When newly adding an item, if the favoriteGiffIdsList is not empty,
        // check if the current Giff is already in Fav list, if so
        // --> Set isFavorite flag to true and view holder will update UI accordingly
        if (favoriteGiffIdsList.isNotEmpty()) {
            displayedGiffList[position].isFavorite =
                favoriteGiffIdsList.contains(displayedGiffList[position].id)
        }

        holder.bind(displayedGiffList[position], this)
    }

    // Call from ViewHolder lands here
    // necessary changes to data set is done and updated to the DB
    // Note - No UI update here
    override fun onFavoriteButtonClicked(clickedGiffImage: GiffItem, adapterPosition: Int) {
        clickedGiffImage.isFavorite = !clickedGiffImage.isFavorite
        favoriteClickListener.onFavoriteButtonClicked(clickedGiffImage, adapterPosition)
    }

    // Call from Fragment to update latest items to be displayed in the RecyclerView
    // boolean flag denotes append to existing List or Fresh load
    fun updateNewItems(newTrends: List<GiffItem>, clearExistingList: Boolean) {

        if (clearExistingList) {
            displayedGiffList.clear()
            notifyDataSetChanged()
        }
        displayedGiffList.addAll(newTrends)

        // Remove duplicates -- Some times the new API response and existing data has duplicate
        // Compare with id and remove
        val set = displayedGiffList.toImmutableList()
        displayedGiffList.clear()

        displayedGiffList.addAll(
            set.distinctBy { it.id }
        )

        //After filtering and add only the unique items  notify the updated ones
        notifyItemRangeInserted(displayedGiffList.size + 1, newTrends.size)
    }

    /**
     * To update the current fav items, this is used to show favorite Giffs in red and
     *remove red from non-favorite Giffs when the action is performed in Favorite fragment.
     */
    fun updateFavIds(allFavoriteGiffIdsList: List<String>) {
        // Contains items that are removed from favorites
        val deletedElements = this.favoriteGiffIdsList.minus(allFavoriteGiffIdsList)

        // Contains items that are added to favorites
        val addedElements = allFavoriteGiffIdsList.minus(this.favoriteGiffIdsList)

        // Clear existing Fav list and update with the latest list
        this.favoriteGiffIdsList.clear()
        this.favoriteGiffIdsList.addAll(allFavoriteGiffIdsList)

        // To update UI for removed items - update isFavorite flag to false and refresh that Giff
        // Repeat for all such deleted items
        for (eachDeletedItem in deletedElements) {
            val index =
                displayedGiffList.indexOfFirst { it.id == eachDeletedItem } // -1 if not found
            if (index in 0 until itemCount) {
                displayedGiffList[index].isFavorite = false
                notifyItemChanged(index)
            }
        }

        // To update UI for new added items - update isFavorite flag to true and refresh that Giff
        // Repeat for all such newly added items
        for (eachAddedItem in addedElements) {
            val index = displayedGiffList.indexOfFirst { it.id == eachAddedItem } // -1 if not found
            if (index in 0 until itemCount) {
                displayedGiffList[index].isFavorite = true
                notifyItemChanged(index)
            }
        }
    }
}