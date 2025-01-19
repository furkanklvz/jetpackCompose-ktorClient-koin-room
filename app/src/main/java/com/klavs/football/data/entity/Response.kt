package com.klavs.football.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class Response(
    val team: Team,
    val venue: Venue
)