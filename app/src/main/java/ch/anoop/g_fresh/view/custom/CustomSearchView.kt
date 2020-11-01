package ch.anoop.g_fresh.view.custom

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.SearchView
import ch.anoop.g_fresh.R

class CustomSearchView : SearchView {

    var mSearchSrcTextView: SearchAutoComplete? = null
    var listener: OnQueryTextListener? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun setOnQueryTextListener(listener: OnQueryTextListener?) {
        this.listener = listener
        mSearchSrcTextView = this.findViewById(R.id.search_src_text)
        mSearchSrcTextView!!.setOnEditorActionListener { _, _, _ ->
            listener?.onQueryTextSubmit(query.toString())
            true
        }
        super.setOnQueryTextListener(listener)
    }
}