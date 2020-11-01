package ch.anoop.g_fresh.view.fragment

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
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

    private val errorTypeNormal: Int = 1
    private val errorTypeAPI: Int = 2
    private val errorTypeNoData: Int = 3
    private val errorTypeNotReachable: Int = 4

    private val giffImageAdapter by lazy { GiffImageAdapter(this) }
    private lateinit var viewModel: TrendingSearchFragmentViewModel

    private lateinit var progressBar: View
    private lateinit var giffRecyclerView: RecyclerView
    private lateinit var errorTextView: TextView
    private lateinit var searchView: SearchView

    override fun onFavoriteButtonClicked(clickedGiffImage: GiffItem, adapterPosition: Int) {
        viewModel.updateFavoriteButtonClicked(clickedGiffImage)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(inflatedView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(inflatedView, savedInstanceState)
        initViews(inflatedView)
    }

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

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(context?.applicationContext as Application)
        ).get(TrendingSearchFragmentViewModel::class.java)
    }

    private fun initViews(inflatedView: View) {
        progressBar = inflatedView.findViewById(R.id.giffy_loading_progress_bar)
        errorTextView = inflatedView.findViewById(R.id.giffy_error_info_txt_view)
        giffRecyclerView = inflatedView.findViewById(R.id.giffy_recycler_view)
        searchView = inflatedView.findViewById(R.id.giffy_search_view)
    }

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

    private fun startObservingChangesForFav() {
        viewModel.allFavoriteGiffIdsLiveData.observe(
            viewLifecycleOwner, { allFavoriteGiffIdsList ->
                giffImageAdapter.updateFavIds(allFavoriteGiffIdsList)
            }
        )
    }

    private fun setupSearchView() {
        searchView.visibility = VISIBLE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                showProgressBar(true)

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

    private fun startObservingChangesForUI() {
        viewModel.viewStateChangeEvent.observe(viewLifecycleOwner, { state ->
            when (state) {
                ViewState.LoadingNext, ViewState.LoadingFresh -> {
                    showProgressBar(true)
                }

                ViewState.LoadingComplete -> {
                    showProgressBar(false)
                }

                ViewState.NoData -> {
                    showProgressBar(false)
                    showError(errorTypeNoData)
                }

                ViewState.Error.InvalidResponse -> {
                    showProgressBar(false)
                    showError(errorTypeAPI)
                }

                ViewState.Error.ServerNotReachable -> {
                    showProgressBar(false)
                    showError(errorTypeNotReachable)
                }

                ViewState.Error.GenericError -> {
                    showProgressBar(false)
                    showError(errorTypeNormal)
                }
            }
        })
    }

    private fun startObservingChangesForData() {
        viewModel.dataUpdatedEvent.observe(viewLifecycleOwner, { state ->
            showProgressBar(false)
            when (state) {

                is ApiResponseResult.LoadingComplete -> {
                    giffImageAdapter.updateNewItems(state.value.data, false)
                }

                else -> {
                    Log.e("FRAG_TREND", "startObservingChangesForData: state = $state")
                }
            }
        })
    }

    private fun showError(errorType: Int) {
        errorTextView.visibility = VISIBLE
        giffRecyclerView.visibility = GONE
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

    private fun showProgressBar(isVisible: Boolean) {
        progressBar.visibility = if (isVisible) VISIBLE else INVISIBLE
        giffRecyclerView.visibility = if (isVisible) GONE else VISIBLE
        errorTextView.visibility = GONE
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}