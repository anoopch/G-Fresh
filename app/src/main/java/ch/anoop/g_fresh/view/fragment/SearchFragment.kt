package ch.anoop.g_fresh.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ch.anoop.g_fresh.R
import ch.anoop.g_fresh.view_model.SearchFragmentViewModel

class SearchFragment : Fragment() {

    private lateinit var pageViewModel: SearchFragmentViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this).get(SearchFragmentViewModel::class.java).apply {
            onFragmentCreated()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

}