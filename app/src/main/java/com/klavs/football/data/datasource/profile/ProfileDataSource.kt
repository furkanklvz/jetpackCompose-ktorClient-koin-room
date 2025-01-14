package com.klavs.football.data.datasource.profile

import com.klavs.football.data.entity.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileDataSource {
    fun getProfiles(): Flow<List<String>>
    fun getProfile(name: String): Flow<Profile>
    suspend fun insertProfile(profile: Profile)
    suspend fun deleteProfile(name: String)
    suspend fun updateTeams(name: String, teams: String)
    suspend fun updateProfileName(oldName: String, newName: String)
}