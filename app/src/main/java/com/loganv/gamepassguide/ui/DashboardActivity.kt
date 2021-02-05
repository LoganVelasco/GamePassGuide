package com.loganv.gamepassguide.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.loganv.gamepassguide.R
import com.loganv.gamepassguide.adapters.MenuSectionAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    private val navController by lazy { Navigation.findNavController(this, R.id.nav_host_fragment) }
    private val appBarConfiguration by lazy { AppBarConfiguration(navController.graph) }
    private val searchBar by lazy { findViewById<MaterialCardView>(R.id.activity_search_bar) }
    private val searchButton by lazy { findViewById<ImageButton>(R.id.search_button) }
    private val menuButton by lazy { findViewById<ImageButton>(R.id.menu_button) }
    private val dropDownMenu by lazy { findViewById<RecyclerView>(R.id.menu_drop_down) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        setupActionBarWithNavController(navController, appBarConfiguration)
        supportActionBar?.hide()
        val layoutManager = LinearLayoutManager(this)
        dropDownMenu.layoutManager = layoutManager
        dropDownMenu.adapter = MenuSectionAdapter(listOf(Pair("Platform", listOf("ALL", "CONSOLE", "PC")),
            Pair("Genre", listOf("RPG", "Sports", "Simulation", "Shooter", "Indie", "Strategy", "Action & Adventure", "Fighting", "Racing", "Puzzle & Trivia", "Platformer" ))))

        searchButton.setOnClickListener {

        }
        menuButton.setOnClickListener {
            if(dropDownMenu.visibility == View.GONE) {
                menuButton.scaleY = -1f
                dropDownMenu.visibility = View.VISIBLE
            }
            else if(dropDownMenu.visibility == View.VISIBLE) {
                menuButton.scaleY = 1f
                dropDownMenu.visibility = View.GONE
            }
        }
        
    }


  override fun onBackPressed() {
      // fix
        navController.popBackStack()
        showSearchBar()
    }

    fun hideSearchBar(){
        searchBar.visibility = View.GONE
    }
    fun showSearchBar(){
        searchBar.visibility = View.VISIBLE
    }
}