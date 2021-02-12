package com.loganv.gamepassguide.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loganv.gamepassguide.R
import com.loganv.gamepassguide.adapters.DashboardAdapter
import com.loganv.gamepassguide.viewmodels.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private val viewModel: DashboardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val dashboardRecyclerView = view.findViewById<RecyclerView>(R.id.dashboard_rows)
        val layoutManager = LinearLayoutManager(requireContext())
        dashboardRecyclerView.layoutManager = layoutManager

        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)


        viewModel.getGamesPassGames()

        viewModel.data.observe(requireActivity(), { result ->
            result.onSuccess { allGames ->
                val separatedGames = viewModel.generateDashboardCategories(allGames)
                dashboardRecyclerView.adapter = DashboardAdapter(separatedGames, navController)
            }
        })

        return view
    }


}