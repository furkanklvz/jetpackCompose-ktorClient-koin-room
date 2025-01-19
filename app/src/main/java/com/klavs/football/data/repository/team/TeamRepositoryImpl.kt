package com.klavs.football.data.repository.team


import com.klavs.football.data.datasource.team.TeamDatasource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TeamRepositoryImpl (private val ds: TeamDatasource) : TeamRepository {
    override suspend fun getTeamKtor(id: Int) = withContext(Dispatchers.IO){ds.getTeamKtor(id)}
}