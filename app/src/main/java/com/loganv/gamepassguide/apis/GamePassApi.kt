package com.loganv.gamepassguide.apis

import com.loganv.gamepassguide.models.GamePassResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface GamePassApi {

    @GET("/Zhouzi/included-with-xbox-game-pass/master/packages/xgp.community/static/games.json")
    fun getGamePassGameList(): Call<List<GamePassResponse>>
}