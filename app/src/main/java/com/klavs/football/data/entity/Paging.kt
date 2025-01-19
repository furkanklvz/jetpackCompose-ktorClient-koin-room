package com.klavs.football.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class Paging(
    val current: Int,
    val total: Int
)