package ch.anoop.g_fresh.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ch.anoop.g_fresh.R
import ch.anoop.g_fresh.api.GiphyResponse
import ch.anoop.g_fresh.view.adapter.CustomDecorator
import ch.anoop.g_fresh.view.adapter.GiffImageAdapter
import ch.anoop.g_fresh.view_model.TrendingSearchFragmentViewModel
import ch.anoop.g_fresh.view_model.state.ApiResponseResult
import ch.anoop.g_fresh.view_model.state.ViewState
import org.jetbrains.anko.textColorResource

class TrendingSearchFragment : Fragment() {

    private val errorTypeNormal: Int = 1
    private val errorTypeAPI: Int = 2
    private val errorTypeAPIResponseInvalid: Int = 3
    private val errorTypeInfo: Int = 4
    private val errorTypeConnectionTimeOut: Int = 5
    private val errorTypeNetworkFailure: Int = 6
    private val errorTypeUnsupportedApiRequest: Int = 7

    private val trendingAdapter by lazy { GiffImageAdapter() }
    private lateinit var viewModel: TrendingSearchFragmentViewModel

    private lateinit var progressBar: ProgressBar
    private lateinit var giffRecyclerView: RecyclerView
    private lateinit var errorTextView: TextView
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(inflatedView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(inflatedView, savedInstanceState)

        initViewModel()
        initViews(inflatedView)
        initRecyclerView()
        setupSearchView()

        startObservingChangesForUI()
        startObservingChangesForData()

        showProgressBar(true)
        viewModel.loadTrendingGiffs()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(TrendingSearchFragmentViewModel::class.java).apply {
            onFragmentCreated()
        }
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
            addItemDecoration(CustomDecorator())
            adapter = trendingAdapter
        }
    }

    private fun setupSearchView() {
        searchView.visibility = VISIBLE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()) {
                    viewModel.loadTrendingGiffs()
                    searchView.isIconified = true
                } else {
                    viewModel.loadSearchForGiff(query)
                }

                showProgressBar(true)
                trendingAdapter.updateNewTrends(emptyList(), true)
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
        viewModel.viewStateChangeEvent.observe(this, { state ->
            when (state) {
                ViewState.LoadingFresh -> {
                    showProgressBar(true)
                }

                ViewState.LoadingNext -> {
                    showProgressBar(true)
                }

                ViewState.LoadingComplete -> {
                    showProgressBar(false)
                }

                ViewState.NoData -> {
                    showProgressBar(false)
                }

                ViewState.Error -> {
                    showProgressBar(false)
                }
            }
        })
    }

    private fun startObservingChangesForData() {
        viewModel.dataUpdatedEvent.observe(this, { state ->
            showProgressBar(false)
            when (state) {

                is ApiResponseResult.LoadingComplete -> {
                    updateTrendingGiffs(state.value)
                }

                is ApiResponseResult.LoadingFailed -> {
                    showError(errorTypeAPI)
                }

                else -> {
                    showError(errorTypeNormal)
                }
            }
        })
    }

    private fun updateTrendingGiffs(trendingResponse: GiphyResponse) {
        trendingAdapter.updateNewTrends(trendingResponse.data, false)
    }

    private fun showError(errorType: Int) {
        errorTextView.visibility = VISIBLE
        giffRecyclerView.visibility = GONE
        when (errorType) {
            errorTypeAPI, errorTypeAPIResponseInvalid, errorTypeUnsupportedApiRequest -> {
                errorTextView.text = getString(R.string.api_error)
                errorTextView.textColorResource = R.color.error_text
                errorTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error, 0, 0, 0)
            }
            errorTypeInfo -> {
                errorTextView.text = getString(R.string.no_data_error)
                errorTextView.textColorResource = R.color.secondary_text
                errorTextView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_information,
                    0,
                    0,
                    0
                )
            }
            else -> {
                errorTextView.text = getString(R.string.generic_error)
                errorTextView.textColorResource = R.color.error_text
                errorTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error, 0, 0, 0)
            }
        }
    }

    private fun showProgressBar(isVisible: Boolean) {
        progressBar.visibility = if (isVisible) VISIBLE else GONE
        giffRecyclerView.visibility = if (isVisible) GONE else VISIBLE
        errorTextView.visibility = GONE
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}