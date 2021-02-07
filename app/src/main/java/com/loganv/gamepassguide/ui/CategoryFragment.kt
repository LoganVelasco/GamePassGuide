package com.loganv.gamepassguide.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.loganv.gamepassguide.R
import com.loganv.gamepassguide.adapters.RowAdapter
import com.loganv.gamepassguide.viewmodels.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private val viewModel: DashboardViewModel by activityViewModels()
    private val args: CategoryFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_category, container, false)

        val categoryRecyclerView = view.findViewById<RecyclerView>(R.id.category_grid_list)

        // fix this
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        val title = view.findViewById<TextView>(R.id.category_title)

        title.text = args.title
1
        val divider = DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL)
        val dividerDrawable = view.resources.getDrawable(R.drawable.vertical_divider)
        divider.setDrawable(dividerDrawable)
        categoryRecyclerView.addItemDecoration(divider)

        val viewModel: DashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        viewModel.getGamesPassGames()

        viewModel.data.observe(requireActivity(), { result ->
            result.onSuccess { allGames ->
                val games = viewModel.generateCategory(allGames, DashboardViewModel.Platform.PC)
                categoryRecyclerView.adapter = RowAdapter(games, navController)
            }
        })

        return view
    }


}