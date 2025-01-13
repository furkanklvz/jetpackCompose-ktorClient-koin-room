package com.klavs.football.data.datasource.team

import android.util.Log
import com.klavs.football.R
import com.klavs.football.Resource
import com.klavs.football.data.entity.Team
import com.klavs.football.retrofit.TeamDao
import java.net.UnknownHostException
import javax.inject.Inject

class TeamDatasourceImpl @Inject constructor(private val teamDao: TeamDao) : TeamDatasource {
    override suspend fun getTeam(id:Int): Resource<Team> {
        return try {
            val response = teamDao.getTeam(id)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                Resource.Success(body.response.first().team)
            } else {
                Log.e("TeamDatasourceImpl", "getTeam: ${response.errorBody()}")
                Resource.Error(message = R.string.something_went_wrong)
            }
        } catch (e: UnknownHostException) {
            Log.e("TeamDatasourceImpl", "getTeam: $e")
            return Resource.Error(message = R.string.server_failed)
        } catch (e: Exception) {
            Log.e("TeamDatasourceImpl", "getTeam: $e")
            return Resource.Error(message = R.string.something_went_wrong)
        }
    }
}