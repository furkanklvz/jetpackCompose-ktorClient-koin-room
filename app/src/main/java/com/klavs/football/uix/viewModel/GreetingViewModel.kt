package com.klavs.football.uix.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klavs.football.data.entity.Profile
import com.klavs.football.data.repository.profile.ProfileRepository
import com.klavs.football.utils.ProfileManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GreetingViewModel (
    private val profileRepo: ProfileRepository,
    private val profileManager: ProfileManager
) : ViewModel() {

    private val _profileNamesFlow = MutableStateFlow<List<String>>(emptyList())
    val profileNamesFlow = _profileNamesFlow.asStateFlow()

    val currentProfile = profileManager.currentProfile

    fun signIn(name: String) {
        profileManager.signIn(name)
    }

    init {
        getProfiles()
    }

    private fun getProfiles() {
        viewModelScope.launch(Dispatchers.Main) {
            profileRepo.getProfiles().collect { profileName ->
                _profileNamesFlow.value = profileName
            }
        }
    }

    fun insertProfile(name: String) {
        viewModelScope.launch(Dispatchers.Main) {
            profileRepo.insertProfile(
                profile = Profile(
                    name = name,
                    teams = emptyList(),
                )
            )
        }
    }
}