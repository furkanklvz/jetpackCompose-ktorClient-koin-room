package com.klavs.football.data.datasource.team

import com.klavs.football.Resource
import com.klavs.football.data.entity.Response

interface TeamDatasource {
    /*suspend fun getTeam(id:Int): Resource<Response>*/
    suspend fun getTeamKtor(id:Int): Resource<Response>
}