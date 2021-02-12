package com.loganv.gamepassguide.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.loganv.gamepassguide.apis.GamePassApi
import com.loganv.gamepassguide.apis.GamePassInfoApi
import com.loganv.gamepassguide.models.Availability
import com.loganv.gamepassguide.models.Game
import com.loganv.gamepassguide.models.GamePassInfo
import com.loganv.gamepassguide.models.GamePassResponse
import com.loganv.gamepassguide.utils.ArtworkData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.FileReader
import javax.inject.Inject

@HiltViewModel
open class DashboardViewModel
@Inject constructor() : ViewModel(), SingleObserver<Result<List<Game>>> {

    @Inject
    lateinit var gamePassApi: GamePassApi

    @Inject
    lateinit var gamePassInfoApi: GamePassInfoApi

    val artworkData = ArtworkData()

    var data: MutableLiveData<Result<List<Game>>> = MutableLiveData()
    private var disposable: Disposable? = null

    private var inProgress = false
    private var isSuccess = false
    private var genrefilters = arrayListOf<String>()
    var platformFilter = Platform.ALL

    val csvMapper = CsvMapper().apply {
        registerModule(KotlinModule())
    }

    inline fun <reified T> readCsvFile(reader: String): List<T> {
        return csvMapper
            .readerFor(T::class.java)
            .with(CsvSchema.emptySchema().withHeader())
            .readValues<T>(reader)
            .readAll()
            .toList()
    }

    open fun getGamesPassGames() {
        if (!inProgress && !isSuccess) {
            inProgress = true
            if (cachedGames.isNullOrEmpty()) {
                val call = gamePassApi.getGamePassGameList()

                call.enqueue(object : retrofit2.Callback<List<GamePassResponse>> {
                    override fun onResponse(
                        call: Call<List<GamePassResponse>>,
                        response: Response<List<GamePassResponse>>
                    ) {
                        if (response.isSuccessful) {
                            val games = getGamesList(response.body()!!)
                            cachedGames = games
                            getGamesPassInfo()
                            data.value = Result.success(getFilteredGames())
                            isSuccess = true
                            inProgress = true
                        }
                    }

                    override fun onFailure(call: Call<List<GamePassResponse>>, t: Throwable) {
                        t.message
                    }
                })

            } else {
                data.value = Result.success(getFilteredGames())
            }
        }
    }

    open fun getGamesPassInfo() {
        val call = gamePassInfoApi.getGamePassGameInfo()

        call.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    val info = response.body()!!.string()
                    val infoWithHeaders =
                        "\"game\",\"system\",\"xCloud\",\"status\",\"added\",\"removed\",\"months\",\"release\",\"age\",\"metacritic\",\"completion\",\"genre\"\n$info"
                    val gameInfoList: List<GamePassInfo> = readCsvFile(infoWithHeaders)
                    val activeGames = gameInfoList.filter { it.status.equals("Active") }
                    addInfo(activeGames)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.message
            }
        })
    }

    private fun addInfo(gameInfoList: List<GamePassInfo>) {
        cachedGames!!.forEach { game ->

            game.info = gameInfoList.find {
                val filteredString = game.title.replace(" Standard Edition", "")
                it.game!! == filteredString
            }
        }
    }

    fun getGamesList(response: List<GamePassResponse>) = response.map { game ->
        Game(
            title = game.title,
            gameId = getGameId(game.availability, game.title),
            console = (game.availability.console != null),
            pc = (game.availability.pc != null),
            steam = (game.availability.steam != null)
        )
    }

    fun setCurrentPlatformFilter(platform: Platform) {
        platformFilter = platform
        data.value = Result.success(getFilteredGames())
    }

    fun addFilter(filter: String) {
        genrefilters.add(filter)
        data.value = Result.success(getFilteredGames())
    }

    fun removeFilter(filter: String) {
        genrefilters.remove(filter)
        data.value = Result.success(getFilteredGames())
    }

    fun getSearchResults(title: String): List<Game>{
        if(cachedGames.isNullOrEmpty())return emptyList()
        return cachedGames!!.filter {
            it.title.contains(title)
        }
    }


    private fun getFilteredGames(): List<Game> {
        if (genrefilters.isEmpty()) {
            return when (platformFilter) {
                Platform.ALL -> {
                    cachedGames!!
                }
                Platform.PC -> {
                    cachedGames!!.filter { it.pc }
                }
                Platform.CONSOLE -> {
                    cachedGames!!.filter { it.console }
                }
            }
        }
        val games = cachedGames
        val filteredGames = arrayListOf<Game>()
        games?.forEach games@{ game ->
            if (game.info?.genre != null) {
                genrefilters.forEach {
                    if (game.info!!.genre?.contains(it) == true) {
                        when (platformFilter) {
                            Platform.ALL -> {
                                filteredGames.add(game)
                            }
                            Platform.PC -> {
                                if (game.pc) filteredGames.add(game)
                            }
                            Platform.CONSOLE -> {
                                if (game.console) filteredGames.add(game)
                            }
                        }
                        return@games
                    }
                }
            }
        }
        return filteredGames
    }


    fun generateDashboardCategories(games: List<Game>): List<Pair<String, List<Game>>> {

        val eaGames = games.filter { it.title.contains("EA SPORTS") }
        val firstPartyGames = games.filter {
            it.title.contains("Halo") || it.title.contains("Crackdown") || it.title.contains("Forza") || it.title.contains(
                "Gears of War"
            )
        }
        val recentlyAddedGames = games.filter {
            it.title.contains("Medium") || it.title.contains("Control") || it.title.contains("Way") || it.title.contains(
                "Donut"
            ) || it.title.contains("Yakuza")
        }
        val leavingSoonGames = games.filter {
            it.title.contains("Dirt") || it.title.contains("Dragon") || it.title.contains("Down") || it.title.contains(
                "Comanche"
            ) || it.title.contains("Black")
        }
        val steamGames = games.filter { it.steam }

        return listOf(
            Pair("Recently Added", recentlyAddedGames),
            Pair("Leaving Soon", leavingSoonGames),
            Pair("First Party Exclusives", firstPartyGames),
            Pair("EA Sports", eaGames),
            Pair("Also on Steam", steamGames)
        )
    }


    fun generateCategory(games: List<Game>, category: Platform): List<Game> {
        return when (category) {
            Platform.ALL -> {
                games.subList(0, 50)
            }
            Platform.PC -> {
                games.filter { it.pc }
            }
            Platform.CONSOLE -> {
                games.filter { it.console }
            }
        }
    }

    private fun getGameId(availability: Availability, title: String): String {
        if (availability.pc != null) {
            if (!availability.pc.contains("xbox.com"))
                return availability.pc.toString().split('/').last()
        }
        if (availability.console != null) {
            return if (availability.console.contains("xbox.com")) {
                artworkData.artworkMap[title] ?: ""
            } else {
                availability.console.toString().split('/').last()
            }
        }
        return ""
    }


    override fun onSuccess(t: Result<List<Game>>) {
//        val games = getGamesList(response.body()!!)
//        cachedGames = games
//        getGamesPassInfo()
//        data.value = Result.success(getFilteredGames())
//        isSuccess = true
//        inProgress = true
    }

    override fun onSubscribe(d: Disposable) {
        disposable = d
    }

    override fun onError(e: Throwable) {
        super.onCleared()
        disposable?.dispose()
    }

    enum class Platform {
        ALL,
        PC,
        CONSOLE
    }

    companion object {
        var cachedGames: List<Game>? = null
    }


}