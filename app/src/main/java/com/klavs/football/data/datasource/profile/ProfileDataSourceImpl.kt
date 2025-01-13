package com.klavs.football.data.datasource.profile

import com.klavs.football.data.entity.Profile
import com.klavs.football.room.ProfilesDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileDataSourceImpl @Inject constructor(private val profilesDao: ProfilesDao) : ProfileDataSource {
    override fun getProfiles() = profilesDao.getProfiles()
    override fun getProfile(name: String)= profilesDao.getProfile(name)
    override suspend fun insertProfile(profile: Profile) = profilesDao.insertProfile(profile)
    override suspend fun deleteProfile(profile: Profile) = profilesDao.deleteProfile(profile)
    override suspend fun updateTeams(name: String, teams: String) = profilesDao.updateTeams(name,teams)
}