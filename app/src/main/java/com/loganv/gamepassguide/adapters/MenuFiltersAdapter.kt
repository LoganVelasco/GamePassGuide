package com.loganv.gamepassguide.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.loganv.gamepassguide.R
import com.loganv.gamepassguide.viewmodels.DashboardViewModel

internal class MenuFiltersAdapter(private val viewModel: DashboardViewModel, private val filters: List<String>): RecyclerView.Adapter<MenuFiltersAdapter.MyViewHolder>()  {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: MaterialButton= view.findViewById(R.id.filter_button)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.filter_button, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return filters.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = filters[position]
        holder.title.setOnClickListener {
            if(holder.title.isChecked){
                viewModel.addFilter(holder.title.text.toString())
                holder.title.setTextColor(Color.WHITE)
            }else{
                holder.title.setTextColor(Color.BLACK)
                viewModel.removeFilter(holder.title.text.toString())
            }
        }
    }

}