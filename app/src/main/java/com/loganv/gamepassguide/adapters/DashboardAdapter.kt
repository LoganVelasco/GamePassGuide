package com.loganv.gamepassguide.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loganv.gamepassguide.R
import com.loganv.gamepassguide.models.Game
import com.loganv.gamepassguide.ui.CategoryFragmentDirections
import com.loganv.gamepassguide.ui.DashboardFragmentDirections

internal class DashboardAdapter(
    private val gameLists: List<Pair<String, List<Game>>>,
    private val navController: NavController
): RecyclerView.Adapter<DashboardAdapter.MyViewHolder>()  {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rowTitle: TextView = view.findViewById(R.id.row_title)
        val rowGames: RecyclerView = view.findViewById(R.id.row_games)
        val rowRecyclerView: RecyclerView = view.findViewById(R.id.row_games)
        val divider = DividerItemDecoration(view.context, DividerItemDecoration.HORIZONTAL)
        val dividerDrawable = view.resources.getDrawable(R.drawable.horizontal_divider)
        val layoutManager = LinearLayoutManager(view.context)
        val category: TextView = view.findViewById(R.id.row_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return gameLists.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val title = gameLists[position].first
        val games = gameLists[position].second
        holder.apply {
            rowTitle.text = title
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            rowRecyclerView.layoutManager = layoutManager
            divider.setDrawable(dividerDrawable)
            rowRecyclerView.addItemDecoration(divider)
            rowGames.adapter = RowAdapter(games, navController)
            category.setOnClickListener {
                val actionWithArgs = DashboardFragmentDirections.actionDashboardFragmentToCategoryFragment(title)
                navController.navigate(actionWithArgs)
            }

        }
    }

}