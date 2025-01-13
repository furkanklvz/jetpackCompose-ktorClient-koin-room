package com.klavs.football.data.repository.team

import com.klavs.football.Resource
import com.klavs.football.data.entity.Team

interface TeamRepository {
    suspend fun getTeam(id:Int): Resource<Team>
}