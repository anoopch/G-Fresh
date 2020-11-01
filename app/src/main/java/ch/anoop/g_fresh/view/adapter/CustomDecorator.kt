package ch.anoop.g_fresh.view.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ch.anoop.g_fresh.R

class CustomDecorator : RecyclerView.ItemDecoration() {

    private var offset: Int? = null

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (offset == null) {
            offset = parent.context.resources.getDimensionPixelSize(R.dimen.giff_item_margin)
        }

        val lp = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        val spanIndex = lp.spanIndex

        outRect.left = offset as Int
        outRect.right = if (spanIndex == 0) 0 else (offset!! * 2)

        outRect.bottom = offset as Int
        outRect.top = offset as Int
    }
}