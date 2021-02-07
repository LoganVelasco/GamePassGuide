package com.loganv.gamepassguide.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.loganv.gamepassguide.R
import com.loganv.gamepassguide.adapters.MenuSectionAdapter
import com.loganv.gamepassguide.viewmodels.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    private val navController by lazy { Navigation.findNavController(this, R.id.nav_host_fragment) }
    private val appBarConfiguration by lazy { AppBarConfiguration(navController.graph) }
    private val searchBar by lazy { findViewById<MaterialCardView>(R.id.activity_search_bar) }
    private val searchButton by lazy { findViewById<ImageButton>(R.id.search_button) }
    private val menuButton by lazy { findViewById<ImageButton>(R.id.menu_button) }
    private val dropDownMenu by lazy { findViewById<RecyclerView>(R.id.menu_other_filters) }
    private val motionLayout by lazy { findViewById<MotionLayout>(R.id.dashboard_motion_layout) }
    private val allButton by lazy { findViewById<MaterialButton>(R.id.filter_button_all) }
    private val consoleButton by lazy { findViewById<MaterialButton>(R.id.filter_button_console) }
    private val pcButton by lazy { findViewById<MaterialButton>(R.id.filter_button_pc) }

    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        setupActionBarWithNavController(navController, appBarConfiguration)
        supportActionBar?.hide()
        val layoutManager = LinearLayoutManager(this)
        dropDownMenu.layoutManager = layoutManager
        dropDownMenu.adapter = MenuSectionAdapter(viewModel,
            listOf(
                Pair(
                    "Genre",
                    listOf("RPG", "Sports", "Simulation", "Shooter", "Indie", "Strategy", "Action", "Adventure", "Fighting", "Racing", "Puzzle", "Platformer" )
                ),
                Pair(
                    "Multiplayer Options",
                    listOf("Single Player", "Local Co-Op", "Online Co-Op", "Online Multiplayer", "Cross Play" )
                ),
                Pair(
                    "Capabilities",
                    listOf("Series X/S Enhanced", "One X Enhanced", "Smart Delivery", "xCloud", "Online Multiplayer", "Cross Play" )
                )
            )
        )
        allButton.toggle()
        allButton.setOnClickListener {
            if (allButton.isChecked) {
                allButton.setTextColor(Color.WHITE)
                viewModel.setCurrentPlatformFilter(DashboardViewModel.Platform.ALL)
                if (consoleButton.isChecked) {
                    consoleButton.setTextColor(Color.BLACK)
                    consoleButton.toggle()
                }else if (pcButton.isChecked) {
                    pcButton.setTextColor(Color.BLACK)
                    pcButton.toggle()
                }
            }else{
                allButton.toggle()
            }
        }
        consoleButton.setOnClickListener {
            if(consoleButton.isChecked){
                consoleButton.setTextColor(Color.WHITE)
              if (pcButton.isChecked) {
                    pcButton.setTextColor(Color.BLACK)
                    pcButton.toggle()
                }
                if(allButton.isChecked){
                    allButton.setTextColor(Color.BLACK)
                    allButton.toggle()
                }
                viewModel.setCurrentPlatformFilter(DashboardViewModel.Platform.CONSOLE)
            }else{
                consoleButton.setTextColor(Color.BLACK)
                viewModel.setCurrentPlatformFilter(DashboardViewModel.Platform.ALL)
            }
        }
        pcButton.setOnClickListener {
            if(pcButton.isChecked){
                pcButton.setTextColor(Color.WHITE)
                if (consoleButton.isChecked) {
                    consoleButton.setTextColor(Color.BLACK)
                    consoleButton.toggle()
                }
                if(allButton.isChecked){
                    allButton.setTextColor(Color.BLACK)
                    allButton.toggle()
                }
                viewModel.setCurrentPlatformFilter(DashboardViewModel.Platform.PC)
            }else{
                pcButton.setTextColor(Color.BLACK)
                viewModel.setCurrentPlatformFilter(DashboardViewModel.Platform.ALL)
            }
        }

//        searchButton.setOnClickListener {
//
//        }

        
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