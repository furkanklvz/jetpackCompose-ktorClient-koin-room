package com.klavs.football.data.datasource.team

import android.util.Log
import com.klavs.football.R
import com.klavs.football.Resource
import com.klavs.football.data.entity.Response
import com.klavs.football.data.entity.TeamResponse
import com.klavs.football.ktor.KtorService
import io.ktor.client.call.body
import io.ktor.http.isSuccess

class TeamDatasourceImpl(
    private val ktorService: KtorService
) : TeamDatasource {

    override suspend fun getTeamKtor(id: Int): Resource<Response> {
        return try {
            val response = ktorService.getTeam(id)
            if (response.status.isSuccess()) {
                Resource.Success(data = response.body<TeamResponse>().response.first())
            } else {
                Resource.Error(
                    message = R.string.something_went_wrong
                )
            }
        } catch (e: Exception) {
            Log.e("TeamDatasourceImpl", "getTeamKtor: $e")
            Resource.Error(message = R.string.something_went_wrong)
        }
    }
}