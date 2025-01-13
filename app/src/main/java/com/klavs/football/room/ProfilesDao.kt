package com.klavs.football.room

import androidx.room.Dao
import androidx.room.Delete
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

    @Delete
    suspend fun deleteProfile(profile: Profile)

    @Query("UPDATE profiles SET name = :teams WHERE name = :name")
    suspend fun updateTeams(name: String, teams: String)

}