package com.loganv.gamepassguide.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.loganv.gamepassguide.R
import com.loganv.gamepassguide.models.Game
import com.loganv.gamepassguide.ui.DashboardFragmentDirections
import com.loganv.gamepassguide.utils.ArtworkData
import com.squareup.picasso.Picasso
import java.lang.StringBuilder

internal class CategoryAdapter(private val games: List<Game>, private val navController: NavController): RecyclerView.Adapter<CategoryAdapter.MyViewHolder>()  {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.game_title)
        val artwork: ImageView = view.findViewById(R.id.game_art)
//        val genres: TextView = view.findViewById(R.id.game_genres)
        val gameCardView = view.findViewById<MaterialCardView>(R.id.game_card_view)

    }


    val artworkData = ArtworkData()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return games.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val map = HashMap<String, String>()
        val game = games[position]
        val gameId = game.gameId

        val availability = StringBuilder()
        if(game.console)availability.append("Console ")
        if(game.pc)availability.append("PC")

//        holder.genres.text = availability
        holder.title.text = game.title

        holder.gameCardView.setOnClickListener {
            val actionWithArgs = DashboardFragmentDirections.actionDashboardFragmentToGameDetailFragment(game.title, game.gameId, pc = game.pc, console = game.console  )
            navController.navigate(actionWithArgs)
        }


        if(gameId.isNotEmpty()) {
            Picasso.get().load("https://gamepassport.net/images/boxart/$gameId.webp")
                .resize(500, 500)
                .into(holder.artwork)
        }else{
            Picasso.get().load("https://gamepassport.net/images/boxart/9NBLGGH43KZB.webp")
                .resize(500, 500)
                .into(holder.artwork)
        }

    }

}