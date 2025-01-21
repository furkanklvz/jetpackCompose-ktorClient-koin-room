package com.klavs.football.di

import com.klavs.football.data.datasource.profile.ProfileDataSource
import com.klavs.football.data.datasource.profile.ProfileDataSourceImpl
import com.klavs.football.data.datasource.team.TeamDatasource
import com.klavs.football.data.datasource.team.TeamDatasourceImpl
import com.klavs.football.data.repository.profile.ProfileRepository
import com.klavs.football.data.repository.profile.ProfileRepositoryImpl
import com.klavs.football.data.repository.team.TeamRepository
import com.klavs.football.data.repository.team.TeamRepositoryImpl
import com.klavs.football.ktor.KtorService
import com.klavs.football.room.ProfilesDatabase
import com.klavs.football.uix.viewModel.GreetingViewModel
import com.klavs.football.uix.viewModel.MenuViewModel
import com.klavs.football.uix.viewModel.TeamDetailViewModel
import com.klavs.football.uix.viewModel.MyTeamsViewModel
import com.klavs.football.helper.ProfileManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val appModule = module {
    single {
        HttpClient(CIO){
            install(ContentNegotiation){
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }
    single {
        KtorService(get(), get())
    }

    single {
        ProfileManager(get())
    }
    single {
        val db = ProfilesDatabase.getDatabase(get())
        db?.profilesDao()
    }

    singleOf(::TeamDatasourceImpl) { bind<TeamDatasource>() }
    singleOf(::TeamRepositoryImpl) { bind<TeamRepository>() }
    singleOf(::ProfileDataSourceImpl) {bind<ProfileDataSource>()}
    singleOf(::ProfileRepositoryImpl) {bind<ProfileRepository>()}
    viewModelOf(::GreetingViewModel)
    viewModelOf(::MenuViewModel)
    viewModelOf(::TeamDetailViewModel)
    viewModelOf(::MyTeamsViewModel)
}