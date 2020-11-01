package ch.anoop.g_fresh.view.custom

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ch.anoop.g_fresh.view_model.TrendingSearchFragmentViewModel

class PaginationScrollListener(
    private val viewModel: TrendingSearchFragmentViewModel,
    private val staggeredGridLayoutManager: StaggeredGridLayoutManager
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (isLastItemDisplaying(recyclerView)) {

            val totalItemCount = staggeredGridLayoutManager.itemCount
            val visibleItemCount = staggeredGridLayoutManager.childCount
            val lastVisibleItem = getLastVisibleItem(recyclerView)

            viewModel.listScrolled(
                visibleItemCount,
                lastVisibleItem,
                totalItemCount
            )
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