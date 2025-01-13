package com.klavs.football.retrofit

class ApiUtils {
    companion object{
        val BASE_URL = "https://api-football-v1.p.rapidapi.com/v3/"
        fun getTeamDao():TeamDao{
            return RetrofitClient.getClient(BASE_URL).create(TeamDao::class.java)
        }
    }
}