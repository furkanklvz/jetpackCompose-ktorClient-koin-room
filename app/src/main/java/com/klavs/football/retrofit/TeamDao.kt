package com.klavs.football.retrofit

import com.klavs.football.data.entity.TeamResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface TeamDao {

    @Headers("x-rapidapi-key:e382ba6e39mshadac71ca13943adp139e95jsn9573ccc014cf")
    @GET("teams")
    suspend fun getTeam(@Query("id") id:Int):Response<TeamResponse>
}