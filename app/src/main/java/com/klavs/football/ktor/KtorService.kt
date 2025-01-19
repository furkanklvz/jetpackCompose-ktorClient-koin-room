package com.klavs.football.ktor

import android.content.Context
import com.klavs.football.R
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.URLProtocol
import io.ktor.http.path

class KtorService (private val client: HttpClient, private val context: Context) {


    suspend fun getTeam(id: Int): HttpResponse {
        val BASE_URL = context.getString(R.string.football_api_base_url)
        val response = client.get{
            url {
                protocol = URLProtocol.HTTPS
                host = BASE_URL
                path("v3", "teams")
                parameter("id", id)
            }
            headers {
                header("x-rapidapi-key", context.getString(R.string.x_rapidapi_key))
                header("x-rapidapi-host", BASE_URL)
            }
        }
        return response
    }

}