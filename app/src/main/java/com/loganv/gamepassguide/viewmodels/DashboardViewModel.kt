package com.loganv.gamepassguide.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loganv.gamepassguide.apis.GamePassApi
import com.loganv.gamepassguide.models.Availability
import com.loganv.gamepassguide.models.Game
import com.loganv.gamepassguide.models.GamePassResponse
import com.loganv.gamepassguide.utils.ArtworkData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
open class DashboardViewModel
    @Inject constructor(): ViewModel(), SingleObserver<Result<List<Game>>> {

    @Inject lateinit var gamePassApi: GamePassApi

    val artworkData = ArtworkData()

    var data: MutableLiveData<Result<List<Game>>> = MutableLiveData()
    private var disposable: Disposable? = null

    private var inProgress = false
    private var isSuccess = false

    open fun getGamesPassGames(){
        if(!inProgress && !isSuccess){
            inProgress = true
            val call = gamePassApi.getGamePassGameList()


            call.enqueue(object :retrofit2.Callback<List<GamePassResponse>>{
                override fun onResponse(call: Call<List<GamePassResponse>>, response: Response<List<GamePassResponse>>) {
                    if (response.isSuccessful){
                        data.value= Result.success(getGamesList(response.body()!!))
                        isSuccess = true
                        inProgress = true
                    }
                }
                override fun onFailure(call: Call<List<GamePassResponse>>, t: Throwable) {
                    t.message
                }
            })

        }
    }

    fun getGamesList(response: List<GamePassResponse>) = response.map { game ->
        Game(
            title = game.title,
            gameId = getGameId(game.availability, game.title),
            console = (game.availability.console !=null),
            pc = (game.availability.pc != null),
            steam = (game.availability.steam != null)
        )
    }

    fun generateDashboardCategories(games: List<Game>): List<Pair<String, List<Game>>> {

        val eaGames = games.filter { it.title.contains("EA SPORTS") }
        val firstPartyGames = games.filter { it.title.contains("Halo") || it.title.contains("Crackdown") || it.title.contains("Forza") || it.title.contains("Gears of War")}
        val recentlyAddedGames = games.filter { it.title.contains("Medium") || it.title.contains("Control") || it.title.contains("Injustice") || it.title.contains("Donut") || it.title.contains("Yakuza")}
        val leavingSoonGames = games.filter { it.title.contains("Dirt") || it.title.contains("Dragon") || it.title.contains("Down") || it.title.contains("Comanche") || it.title.contains("Black")}
        val steamGames = games.filter { it.steam }.subList(0,20)

        return listOf(
            Pair("Recently Added", recentlyAddedGames),
            Pair("Leaving Soon", leavingSoonGames),
            Pair("First Party Exclusives", firstPartyGames),
            Pair("EA Sports", eaGames),
            Pair("Also on Steam", steamGames))
    }



    fun generateCategory(games: List<Game>, category: Categories): List<Game>{
        return when(category){
            Categories.ALL ->{
                games.filter { it.pc || it.console }.subList(0, 50)
            }
            Categories.PC ->{
                games.filter { it.pc }.subList(0, 50)
            }
            Categories.CONSOLE ->{
                games.filter { it.console }.subList(0, 50)
            }
        }
    }

    private fun getGameId(availability: Availability, title: String): String {
        if(availability.pc != null){
            if(!availability.pc.contains("xbox.com"))
                return availability.pc.toString().split('/').last()
        }
        if(availability.console != null){
            return if(availability.console.contains("xbox.com")){
                artworkData.artworkMap[title]?: ""
            }else {
                availability.console.toString().split('/').last()
            }
        }
        return ""
    }


    override fun onSuccess( t:  Result<List<Game>>) {
        data.value = t
        isSuccess = true
        inProgress = true
    }

    override fun onSubscribe(d: Disposable) {
        disposable = d
    }

    override fun onError(e: Throwable) {
        super.onCleared()
        disposable?.dispose()
    }

    enum class Categories{
        ALL,
        PC,
        CONSOLE
    }


}