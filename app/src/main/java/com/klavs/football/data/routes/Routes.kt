package com.klavs.football.data.routes

import kotlinx.serialization.Serializable

@Serializable
object Greeting

@Serializable
object MyTeams

@Serializable
object Menu

@Serializable
data class TeamDetail(val id: Int)