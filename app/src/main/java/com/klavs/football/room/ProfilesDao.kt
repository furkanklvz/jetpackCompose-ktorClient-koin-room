package com.klavs.football.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.klavs.football.data.entity.Profile
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfilesDao {
    @Query("SELECT name FROM profiles")
    fun getProfiles(): Flow<List<String>>

    @Query("SELECT * FROM profiles WHERE name = :name")
    fun getProfile(name: String): Flow<Profile>

    @Insert
    suspend fun insertProfile(profile: Profile)

    @Query("DELETE FROM profiles WHERE name = :name")
    suspend fun deleteProfile(name:String)

    @Query("UPDATE profiles SET teams = :teams WHERE name = :name")
    suspend fun updateTeams(name: String, teams: String)

    @Query("UPDATE profiles SET name = :newName WHERE name = :oldName")
    suspend fun updateProfileName(oldName: String, newName: String)


}