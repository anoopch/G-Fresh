package ch.anoop.g_fresh.view.fragment

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
import ch.anoop.g_fresh.api.GiphyResponse
import ch.anoop.g_fresh.view.adapter.TrendingAdapter
import ch.anoop.g_fresh.view_model.SearchFragmentViewModel
import ch.anoop.g_fresh.view_model.state.ApiResponseResult
import ch.anoop.g_fresh.view_model.state.ViewState
import org.jetbrains.anko.textColorResource

class SearchFragment : Fragment() {

    private val errorTypeNormal: Int = 1
    private val errorTypeAPI: Int = 2
    private val errorTypeAPIResponseInvalid: Int = 3
    private val errorTypeInfo: Int = 4
    private val errorTypeConnectionTimeOut: Int = 5
    private val errorTypeNetworkFailure: Int = 6
    private val errorTypeUnsupportedApiRequest: Int = 7

    private val trendingAdapter by lazy { TrendingAdapter() }
    private lateinit var viewModel: SearchFragmentViewModel

    private lateinit var progressBar: ProgressBar
    private lateinit var giffRecyclerView: RecyclerView
    private lateinit var errorTextView: TextView

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
        initRecyclerView(inflatedView)

        startObservingChangesForUI()
        startObservingChangesForData()

        viewModel.loadTrendingGiffs()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(SearchFragmentViewModel::class.java).apply {
            onFragmentCreated()
        }
    }

    private fun initViews(inflatedView: View) {
        progressBar = inflatedView.findViewById(R.id.giffy_loading_progress_bar)
        errorTextView = inflatedView.findViewById(R.id.giffy_error_info_txt_view)
    }

    private fun initRecyclerView(inflatedView: View) {
        giffRecyclerView = inflatedView.findViewById(R.id.giffy_recycler_view)
        giffRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = trendingAdapter
        }
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

                ApiResponseResult.LoadingComplete(state) -> {
//                    updateTrendingGiffs(state)
                    println("Loading completed")
                }

//                ApiResponseResult.LoadingError.ApiError -> {
//                    showError(errorTypeAPI)
//                }
//
//                ApiResponseResult.LoadingError.InvalidApiResponseError -> {
//                    showError(errorTypeAPIResponseInvalid)
//                }
//
//                ApiResponseResult.LoadingError.ConnectionTimeoutError -> {
//                    showError(errorTypeConnectionTimeOut)
//                }
//
//                ApiResponseResult.LoadingError.NetworkFailure -> {
//                    showError(errorTypeNetworkFailure)
//                }
//
//                ApiResponseResult.LoadingError.UnSupportedOperationError.UnSupportedError -> {
//                    showError(errorTypeUnsupportedApiRequest)
//                }

                else -> {
                    showError(errorTypeNormal)
                }
            }
        })
    }

    private fun updateTrendingGiffs(trendingResponse: GiphyResponse) {
        trendingAdapter.updateNewTrends(trendingResponse.data)
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
}