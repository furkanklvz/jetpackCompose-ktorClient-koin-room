package com.klavs.football.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class TeamResponse(
    val errors: List<String>,
    val get: String,
    val paging: Paging,
    val parameters: Parameters,
    val response: List<Response>,
    val results: Int
)