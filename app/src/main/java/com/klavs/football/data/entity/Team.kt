package com.klavs.football.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class Team(
    val code: String,
    val country: String,
    val founded: Int,
    val id: Int,
    val logo: String,
    val name: String,
    val national: Boolean
)