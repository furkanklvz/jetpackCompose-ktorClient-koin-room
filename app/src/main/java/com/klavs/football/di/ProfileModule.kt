package com.klavs.football.di

import android.content.Context
import com.klavs.football.data.datasource.profile.ProfileDataSource
import com.klavs.football.data.datasource.profile.ProfileDataSourceImpl
import com.klavs.football.data.repository.profile.ProfileRepository
import com.klavs.football.data.repository.profile.ProfileRepositoryImpl
import com.klavs.football.room.ProfilesDao
import com.klavs.football.room.ProfilesDatabase
import com.klavs.football.utils.ProfileManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProfileModule {

    @Provides
    @Singleton
    fun provideProfileDao(@ApplicationContext context: Context): ProfilesDao {
        val db = ProfilesDatabase.getDatabase(context)
        return db!!.profilesDao()
    }

    @Provides
    @Singleton
    fun provideProfileDataSource(profilesDao: ProfilesDao): ProfileDataSource {
        return ProfileDataSourceImpl(profilesDao)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(ds: ProfileDataSource): ProfileRepository {
        return ProfileRepositoryImpl(ds)
    }

    @Provides
    @Singleton
    fun provideProfileManager(profileRepo: ProfileRepository): ProfileManager {
        return ProfileManager(profileRepo)
    }

}