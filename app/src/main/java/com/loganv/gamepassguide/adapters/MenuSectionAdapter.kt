package com.loganv.gamepassguide.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.loganv.gamepassguide.R

internal class MenuSectionAdapter(
    private val sectionLists: List<Pair<String, List<String>>>,
): RecyclerView.Adapter<MenuSectionAdapter.MyViewHolder>()  {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sectionTitle: TextView = view.findViewById(R.id.menu_section_title)
        val filters: RecyclerView = view.findViewById(R.id.filter_buttons)
//        val layoutManager = GridLayoutManager(view.context,3)
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
//            val layoutManager = Grid(requireContext())
            sectionTitle.text = list.first
            filters.adapter = MenuFiltersAdapter(list.second)
        }
    }

}