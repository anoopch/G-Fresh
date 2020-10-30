package ch.anoop.g_fresh.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ch.anoop.g_fresh.R
import ch.anoop.g_fresh.ui.view_model.FavFragmentViewModel


class FavFragment : Fragment() {

    private lateinit var favFragmentViewModel: FavFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favFragmentViewModel = ViewModelProvider(this).get(FavFragmentViewModel::class.java).apply {
            onLoad()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

}