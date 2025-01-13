package com.klavs.football.utils

import com.klavs.football.data.entity.Profile
import com.klavs.football.data.repository.profile.ProfileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileManager @Inject constructor(private val profileRepo: ProfileRepository) {
    private val _currentProfile = MutableStateFlow<Profile?>(null)
    val currentProfile = _currentProfile.asStateFlow()

    private var currentProfileJob: Job? = null

    fun signIn(name: String) {
        val scope = CoroutineScope(Dispatchers.Main)
        currentProfileJob?.cancel()
        currentProfileJob = scope.launch {
            profileRepo.getProfile(name).collect{profile->
                _currentProfile.value = profile
            }
        }
    }

    fun signOut() {
        currentProfileJob?.cancel()
        _currentProfile.value = null
    }

}