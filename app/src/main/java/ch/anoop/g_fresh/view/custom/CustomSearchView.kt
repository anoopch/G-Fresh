package ch.anoop.g_fresh.view.custom

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.SearchView
import ch.anoop.g_fresh.R

/**
 * Android SearchView contains a bug where empty search string is not accepted
 * and no callback will be made to the normal listener.
 *
 * Existing android/library code is checking if the entered query is Empty.
 *
 * To overcome shortfall this Custom View is used.
 *
 * -- Intercepts on setting the listener and saves it
 * -- When submitting the query, same is also intercepted and passed to the OnQueryTextListener
 * -- Intercept is done at the EditText OnEditorActionListener
 */

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