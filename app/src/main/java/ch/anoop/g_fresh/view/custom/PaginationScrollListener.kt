package ch.anoop.g_fresh.view.custom

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ch.anoop.g_fresh.view_model.TrendingSearchFragmentViewModel

/**
 * Scroll listener of the RecyclerView
 *  - Custom built for the Pagination Use case
 *  - fires an event to the TrendingSearchFragmentViewModel when last item is visible
 */
class PaginationScrollListener(
    private val viewModel: TrendingSearchFragmentViewModel,
    private val staggeredGridLayoutManager: StaggeredGridLayoutManager
) : RecyclerView.OnScrollListener() {

    /**
     * Threshold for denoting when the next page fetch trigger should happen
     *    - until 4 or less items are yet to be seen by the user, pagination will not be triggered
     */
    private val visibleItemThreshold = 4

    private var visibleItemCount: Int? = null
    private var lastVisibleItemPosition: Int? = null
    private var totalItemCount: Int? = null

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (isLastItemDisplaying(recyclerView)) {

            visibleItemCount = staggeredGridLayoutManager.childCount
            lastVisibleItemPosition = getLastVisibleItem(recyclerView)
            totalItemCount = staggeredGridLayoutManager.itemCount

            if (visibleItemCount!! + lastVisibleItemPosition!! + visibleItemThreshold >= totalItemCount!!) {
                viewModel.listScrolled(totalItemCount!!)
            }

        }
    }

    //return true if it's last item visited
    private fun isLastItemDisplaying(recyclerView: RecyclerView): Boolean {
        if (recyclerView.adapter!!.itemCount != 0) {
            // get maximum element within the list
            val lastVisibleItemPosition = getLastVisibleItem(recyclerView)
            return lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.adapter!!
                .itemCount - 1
        }
        return false
    }

    //get last item
    private fun getLastVisibleItem(recyclerView: RecyclerView): Int {
        val lastVisibleItemPositions =
            (recyclerView.layoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(
                null
            )

        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }
}