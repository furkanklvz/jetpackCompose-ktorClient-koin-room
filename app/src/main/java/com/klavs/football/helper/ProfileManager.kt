package com.klavs.football.helper

import com.klavs.football.data.entity.Profile
import com.klavs.football.data.repository.profile.ProfileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class ProfileManager (private val profileRepo: ProfileRepository) {
    private val _currentProfile = MutableStateFlow<Profile?>(null)
    val currentProfile = _currentProfile.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.Main)
    private var currentProfileJob: Job? = null

    fun signIn(name: String) {
        currentProfileJob?.cancel()
        currentProfileJob = scope.launch {
            profileRepo.getProfile(name).collect{profile->
                _currentProfile.value = profile
            }
        }
    }

    fun signOut() {
        _currentProfile.value = null
        currentProfileJob?.cancel()
    }

}