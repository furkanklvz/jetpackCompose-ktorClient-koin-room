package com.klavs.football.data.datasource.profile

import com.klavs.football.data.entity.Profile
import com.klavs.football.room.ProfilesDao
import javax.inject.Inject

class ProfileDataSourceImpl @Inject constructor(private val profilesDao: ProfilesDao) : ProfileDataSource {
    override fun getProfiles() = profilesDao.getProfiles()
    override fun getProfile(name: String)= profilesDao.getProfile(name)
    override suspend fun insertProfile(profile: Profile) = profilesDao.insertProfile(profile)
    override suspend fun deleteProfile(name: String) = profilesDao.deleteProfile(name)
    override suspend fun updateTeams(name: String, teams: String) = profilesDao.updateTeams(name,teams)
    override suspend fun updateProfileName(oldName: String, newName: String) = profilesDao.updateProfileName(oldName, newName)
}