package com.loganv.gamepassguide.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.loganv.gamepassguide.R
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameDetailFragment : Fragment() {

    private val args: GameDetailFragmentArgs by navArgs()


    protected val navController: NavController?
        get() {
            return try {
                Navigation.findNavController(requireView())
            } catch (e: Exception) {
                null
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_game_detail, container, false)

        val art = view.findViewById<ImageView>(R.id.game_detail_art)
        (requireActivity() as DashboardActivity).hideSearchBar()

        val gameId = args.gameId

        val title = view.findViewById<TextView>(R.id.game_detail_title)
        title.text = args.title

        if (gameId.isNotEmpty()) {
            Picasso.get().load("https://gamepassport.net/images/boxart/$gameId.webp")
                .resize(1000, 1000)
                .into(art)
        }

        return view
    }


}