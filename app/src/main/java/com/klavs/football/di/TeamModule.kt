package com.klavs.football.di

import com.klavs.football.data.datasource.team.TeamDatasource
import com.klavs.football.data.datasource.team.TeamDatasourceImpl
import com.klavs.football.data.repository.team.TeamRepository
import com.klavs.football.data.repository.team.TeamRepositoryImpl
import com.klavs.football.retrofit.ApiUtils
import com.klavs.football.retrofit.TeamDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TeamModule {

    @Provides
    @Singleton
    fun provideTeamDao(): TeamDao {
        return ApiUtils.getTeamDao()
    }

    @Provides
    @Singleton
    fun provideTeamDatasource(teamDao: TeamDao): TeamDatasource {
        return TeamDatasourceImpl(teamDao)
    }

    @Provides
    @Singleton
    fun provideTeamRepository(ds: TeamDatasource): TeamRepository {
        return TeamRepositoryImpl(ds)
    }

}