package com.klavs.football.data.repository.team

import com.klavs.football.data.datasource.team.TeamDatasource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TeamRepositoryImpl @Inject constructor(private val ds: TeamDatasource) : TeamRepository {
    override suspend fun getTeam(id:Int) = withContext(Dispatchers.IO) { ds.getTeam(id) }
}