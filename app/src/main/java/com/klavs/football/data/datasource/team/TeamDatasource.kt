package com.klavs.football.data.datasource.team

import com.klavs.football.Resource
import com.klavs.football.data.entity.Team

interface TeamDatasource {
    suspend fun getTeam(id:Int): Resource<Team>

}