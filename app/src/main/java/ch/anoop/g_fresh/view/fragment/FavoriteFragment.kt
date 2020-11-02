package ch.anoop.g_fresh.view.fragment

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ch.anoop.g_fresh.R
import ch.anoop.g_fresh.api.GiffItem
import ch.anoop.g_fresh.view.adapter.CustomDecorator
import ch.anoop.g_fresh.view.adapter.GiffImageAdapter
import ch.anoop.g_fresh.view.custom.FavoriteClickListener
import ch.anoop.g_fresh.view_model.FavoriteFragmentViewModel

/**
 * Fragment showing the list of all the Favorite Giffs added by the User
 */

class FavoriteFragment : Fragment(), FavoriteClickListener {

    // ViewModel for this Fragment
    private lateinit var favoriteFragmentViewModel: FavoriteFragmentViewModel

    // Views in the Layout
    private lateinit var progressBar: ProgressBar
    private lateinit var giffRecyclerView: RecyclerView
    private lateinit var errorTextView: TextView

    // Adapter of the RecyclerView showing the user's Favorite Giffs
    private val giffImageAdapter by lazy { GiffImageAdapter(this) }

    // Interface implementation for handling click of Fav button
    // Fired from ViewHolder, then Adapter, then Fragment(Here), Then ViewModel
    // Then the view model will pass to the Database Repository for insert/delete
    override fun onFavoriteButtonClicked(clickedGiffImage: GiffItem, adapterPosition: Int) {
        favoriteFragmentViewModel.updateFavoriteButtonClicked(clickedGiffImage)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_trending_fav, container, false)
    }

    // After inflating the layout above, callback comes here. Just initialise the views here.
    override fun onViewCreated(inflatedView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(inflatedView, savedInstanceState)
        initViews(inflatedView)
    }

    // After the callback above, init the ViewModel, RecyclerView and
    // start observing the Favorite LiveData of the items in favorite table in database
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModel()
        initRecyclerView()
        startObservingDatabase()
    }

    // Initialise all the views required for the Fragment
    private fun initViews(inflatedView: View) {
        progressBar = inflatedView.findViewById(R.id.giffy_loading_progress_bar)
        errorTextView = inflatedView.findViewById(R.id.giffy_error_info_txt_view)
        giffRecyclerView = inflatedView.findViewById(R.id.giffy_recycler_view)
    }

    // Setup the ViewModel
    private fun initViewModel() {
        favoriteFragmentViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(context?.applicationContext as Application)
        ).get(
            FavoriteFragmentViewModel::class.java
        )
    }

    // Setup the RecyclerView - LayoutManager, ItemDecorations and adapter are set here
    private fun initRecyclerView() {

        val staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE

        giffRecyclerView.apply {
            layoutManager = staggeredGridLayoutManager
            addItemDecoration(CustomDecorator())
            adapter = giffImageAdapter
        }
    }

    // The items in favorite table in the database is observed for changes via the ViewModel
    // using LiveData
    private fun startObservingDatabase() {
        favoriteFragmentViewModel.favGiffItemListLiveData.observe(
            viewLifecycleOwner,
            { favoriteItemsList ->
                giffImageAdapter.updateNewItems(favoriteItemsList, true)
                hideProgressBar()
                if (favoriteItemsList.isNullOrEmpty()) {
                    showNoDataError()
                }
            }
        )
    }

    // Shows no data yet in favs error
    private fun showNoDataError() {
        progressBar.visibility = GONE
        giffRecyclerView.visibility = GONE
        errorTextView.visibility = VISIBLE

        errorTextView.text = getString(R.string.no_fav_prompt)
        errorTextView.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_information,
            0,
            0,
            0
        )
    }

    // Hides progress bar after loading from db,
    // -- ideally will not be visible to user; Hidden quickly
    private fun hideProgressBar() {
        progressBar.visibility = GONE
        giffRecyclerView.visibility = VISIBLE
        errorTextView.visibility = GONE
    }

}