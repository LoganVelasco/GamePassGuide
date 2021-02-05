package com.loganv.gamepassguide.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.loganv.gamepassguide.R
import com.loganv.gamepassguide.models.Game
import com.loganv.gamepassguide.models.GameFilter
import com.loganv.gamepassguide.ui.DashboardFragmentDirections
import com.loganv.gamepassguide.utils.ArtworkData
import com.squareup.picasso.Picasso
import java.lang.StringBuilder

internal class MenuFiltersAdapter(private val filters: List<String>): RecyclerView.Adapter<MenuFiltersAdapter.MyViewHolder>()  {
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
    }

}