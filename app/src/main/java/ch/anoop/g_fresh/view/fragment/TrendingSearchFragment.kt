package ch.anoop.g_fresh.view.fragment

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ch.anoop.g_fresh.R
import ch.anoop.g_fresh.api.GiffItem
import ch.anoop.g_fresh.view.adapter.CustomDecorator
import ch.anoop.g_fresh.view.adapter.GiffImageAdapter
import ch.anoop.g_fresh.view.custom.FavoriteClickListener
import ch.anoop.g_fresh.view.custom.PaginationScrollListener
import ch.anoop.g_fresh.view_model.TrendingSearchFragmentViewModel
import ch.anoop.g_fresh.view_model.state.ApiResponseResult
import ch.anoop.g_fresh.view_model.state.ViewState

class TrendingSearchFragment : Fragment(), FavoriteClickListener {

    // Constants for different types of errors
    private val errorTypeNormal: Int = 1
    private val errorTypeAPI: Int = 2
    private val errorTypeNoData: Int = 3
    private val errorTypeNotReachable: Int = 4

    // ViewModel for this Fragment
    private lateinit var viewModel: TrendingSearchFragmentViewModel

    // Views in the Layout
    private lateinit var progressBar: View
    private lateinit var giffRecyclerView: RecyclerView
    private lateinit var errorTextView: TextView
    private lateinit var searchView: SearchView

    // Adapter of the RecyclerView showing the Giffs - Search or Trending
    private val giffImageAdapter by lazy { GiffImageAdapter(this) }

    // Interface implementation for handling click of Fav button
    // Fired from ViewHolder, then Adapter, then Fragment(Here), Then ViewModel
    // Then the view model will pass to the Database Repository for insert/delete
    // This will fire the LiveData observer with new Data
    override fun onFavoriteButtonClicked(clickedGiffImage: GiffItem, adapterPosition: Int) {
        viewModel.updateFavoriteButtonClicked(clickedGiffImage)
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
    // start observing the Favorite LiveData, ViewState, API response State
    // Also setup Search View
    // After all done load a fresh Trending Giffs fetch
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModel()
        initRecyclerView()

        startObservingChangesForFav()
        startObservingChangesForUI()
        startObservingChangesForData()

        setupSearchView()

        viewModel.loadTrendingGiffs(0)
    }

    // Setup the ViewModel
    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(context?.applicationContext as Application)
        ).get(TrendingSearchFragmentViewModel::class.java)
    }

    // Initialise all the views required for the Fragment
    private fun initViews(inflatedView: View) {
        progressBar = inflatedView.findViewById(R.id.giffy_loading_progress_bar)
        errorTextView = inflatedView.findViewById(R.id.giffy_error_info_txt_view)
        giffRecyclerView = inflatedView.findViewById(R.id.giffy_recycler_view)
        searchView = inflatedView.findViewById(R.id.giffy_search_view)
    }

    /**
     * Setup the RecyclerView - LayoutManager, ItemDecorations and adapter are set here
     * Pagination Scroll listener is also added here
     */
    private fun initRecyclerView() {

        val staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE

        giffRecyclerView.apply {
            layoutManager = staggeredGridLayoutManager
            adapter = giffImageAdapter

            //For margins
            addItemDecoration(CustomDecorator())

            //Scroll Listener for pagination
            addOnScrollListener(PaginationScrollListener(viewModel, staggeredGridLayoutManager))
        }
    }

    // Observe the LiveData of current Fav (only Ids)
    private fun startObservingChangesForFav() {
        viewModel.allFavoriteGiffIdsLiveData.observe(
            viewLifecycleOwner, { allFavoriteGiffIdsList ->
                giffImageAdapter.updateFavIds(allFavoriteGiffIdsList)
            }
        )
    }

    /**
     * Setup the search View - Hidden by default
     *
     * Add listener for text change.
     *
     * Empty string - Trending search; Else entered text is searched
     */
    private fun setupSearchView() {
        searchView.visibility = VISIBLE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                showView(progressBar, true)
                showView(errorTextView, false)
                showView(giffRecyclerView, false)

                if (query.isEmpty()) {
                    viewModel.loadTrendingGiffs(0)
                    searchView.isIconified = true
                } else {
                    viewModel.loadSearchForGiff(query, 0)
                }

                giffImageAdapter.updateNewItems(emptyList(), true)
                searchView.clearFocus()
                searchView.hideKeyboard()

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    /**
     * Observe for changes in UI State
     *
     *  No Data
     *  Loading - LoadingFresh, LoadingComplete, LoadingNext
     *  Error - ServerNotReachable, InvalidResponse, GenericError
     */
    private fun startObservingChangesForUI() {
        viewModel.viewStateChangeEvent.observe(viewLifecycleOwner, { state ->
            when (state) {
                ViewState.LoadingNext, ViewState.LoadingFresh -> {
                    showView(progressBar, true)
                    showView(errorTextView, false)
                    showView(giffRecyclerView, true)
                }

                ViewState.LoadingComplete -> {
                    showView(progressBar, false)
                    showView(errorTextView, false)
                    showView(giffRecyclerView, true)
                }

                ViewState.NoData -> {
                    showError(errorTypeNoData)
                }

                ViewState.Error.InvalidResponse -> {
                    showError(errorTypeAPI)
                }

                ViewState.Error.ServerNotReachable -> {
                    showError(errorTypeNotReachable)
                }

                ViewState.Error.GenericError -> {
                    showError(errorTypeNormal)
                }
            }
        })
    }

    /**
     * Observes for changes in Data obtained from Restful API server
     */
    private fun startObservingChangesForData() {
        viewModel.dataUpdatedEvent.observe(viewLifecycleOwner, { state ->
            showView(progressBar, false)
            when (state) {

                is ApiResponseResult.LoadingComplete -> {
                    giffImageAdapter.updateNewItems(state.value.data, false)

                    showView(errorTextView, false)
                    showView(giffRecyclerView, true)
                }

                else -> {
                    Log.e("FRAG_TREND", "startObservingChangesForData: state = $state")
                }
            }
        })
    }

    /**
     * Shows different types of errors - No Data, Server not reachable, API parsing error,
     * Network failure ..etc
     */
    private fun showError(errorType: Int) {

        showView(errorTextView, true)
        showView(giffRecyclerView, false)
        showView(progressBar, false)

        when (errorType) {
            errorTypeNoData -> {
                errorTextView.text = getString(R.string.no_data_error)
                errorTextView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_information,
                    0,
                    0,
                    0
                )
            }
            errorTypeAPI -> {
                errorTextView.text = getString(R.string.api_error)
                errorTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error, 0, 0, 0)
            }
            errorTypeNotReachable -> {
                errorTextView.text = getString(R.string.unable_to_reach_server)
                errorTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error, 0, 0, 0)
            }
            else -> {
                errorTextView.text = getString(R.string.generic_error)
                errorTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error, 0, 0, 0)
            }
        }
    }

    /**
     * Shows or hides the viewToToggleVisibility view based on the isVisible flag
     */
    private fun showView(viewToToggleVisibility: View, isVisible: Boolean) {
        viewToToggleVisibility.visibility = if (isVisible) VISIBLE else INVISIBLE
    }

    /**
     * Hides the keyboard from screen
     */
    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}