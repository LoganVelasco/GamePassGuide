package com.loganv.gamepassguide.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class GamePassResponse(@JsonProperty("name") val title: String,
                       @JsonProperty("availability") val availability: Availability,
                       @JsonProperty("steam") val steamInfo: Int?)

@JsonIgnoreProperties(ignoreUnknown = true)
class Availability(@JsonProperty("console") val console: String?,
                   @JsonProperty("pc") val pc :String?,
                   @JsonProperty("steam") val steam: String?)

//@JsonIgnoreProperties(ignoreUnknown = true)
//class SteamInfo(@JsonProperty("reviews") val reviews: Review?)
//
//@JsonIgnoreProperties(ignoreUnknown = true)
//class Review(@JsonProperty("reviewScore") val reviewScore: Int?)