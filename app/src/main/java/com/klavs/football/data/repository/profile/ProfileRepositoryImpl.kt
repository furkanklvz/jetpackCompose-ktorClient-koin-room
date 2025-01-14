package com.klavs.football.data.repository.profile

import com.klavs.football.data.datasource.profile.ProfileDataSource
import com.klavs.football.data.entity.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(private val ds: ProfileDataSource) : ProfileRepository {
    override suspend fun getProfiles() = ds.getProfiles().flowOn(Dispatchers.IO)
    override suspend fun getProfile(name: String)= ds.getProfile(name).flowOn(Dispatchers.IO)
    override suspend fun insertProfile(profile: Profile) = withContext(Dispatchers.IO){ds.insertProfile(profile)}
    override suspend fun deleteProfile(name: String) = withContext(Dispatchers.IO){ds.deleteProfile(name)}
    override suspend fun updateTeams(name: String, teams: String) = withContext(Dispatchers.IO){ds.updateTeams(name,teams)}
    override suspend fun updateProfileName(oldName: String, newName: String) = withContext(Dispatchers.IO){ds.updateProfileName(oldName, newName)}
}