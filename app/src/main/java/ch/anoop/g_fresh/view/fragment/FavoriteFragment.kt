package ch.anoop.g_fresh.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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

    }

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
    }

    private fun initViewModel() {
        favoriteFragmentViewModel =
            ViewModelProvider(this).get(FavoriteFragmentViewModel::class.java)
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

}