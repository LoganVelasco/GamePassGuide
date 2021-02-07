package com.loganv.gamepassguide.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loganv.gamepassguide.R
import com.loganv.gamepassguide.viewmodels.DashboardViewModel

internal class MenuSectionAdapter(
    private val viewModel: DashboardViewModel,
    private val sectionLists: List<Pair<String, List<String>>>,
): RecyclerView.Adapter<MenuSectionAdapter.MyViewHolder>()  {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sectionTitle: TextView = view.findViewById(R.id.menu_section_title)
        val filters: RecyclerView = view.findViewById(R.id.filter_buttons)
        val layoutManager = GridLayoutManager(view.context,3)
        val filterDropDown: ImageButton = view.findViewById(R.id.expand_filters_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuSectionAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.menu_section, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return sectionLists.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val list = sectionLists[position]
        holder.apply {
            sectionTitle.text = list.first
            filters.layoutManager = layoutManager
            filters.adapter = MenuFiltersAdapter(viewModel, list.second)

            filterDropDown.setOnClickListener {
            if(filterDropDown.scaleY == 1f) {
                filterDropDown.scaleY = -1f
                filters.visibility = View.VISIBLE
            }
            else if(filterDropDown.scaleY == -1f) {
                filterDropDown.scaleY = 1f
                filters.visibility = View.GONE
            }
        }

        }
    }

}