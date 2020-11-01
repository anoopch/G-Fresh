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


class FavoriteFragment : Fragment(), FavoriteClickListener {

    private lateinit var favoriteFragmentViewModel: FavoriteFragmentViewModel

    private lateinit var progressBar: ProgressBar
    private lateinit var giffRecyclerView: RecyclerView
    private lateinit var errorTextView: TextView

    private val giffImageAdapter by lazy { GiffImageAdapter(this) }

    override fun onFavoriteButtonClicked(clickedGiffImage: GiffItem, adapterPosition: Int) {
        favoriteFragmentViewModel.updateFavoriteButtonClicked(clickedGiffImage)
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
        startObservingDatabase()
    }

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

    private fun initViewModel() {
        favoriteFragmentViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(context?.applicationContext as Application)
        ).get(
            FavoriteFragmentViewModel::class.java
        )
    }

    private fun initViews(inflatedView: View) {
        progressBar = inflatedView.findViewById(R.id.giffy_loading_progress_bar)
        errorTextView = inflatedView.findViewById(R.id.giffy_error_info_txt_view)
        giffRecyclerView = inflatedView.findViewById(R.id.giffy_recycler_view)
    }

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

    private fun hideProgressBar() {
        progressBar.visibility = GONE
        giffRecyclerView.visibility = VISIBLE
        errorTextView.visibility = GONE
    }

}