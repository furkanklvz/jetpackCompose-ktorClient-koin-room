package com.klavs.football.uix.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klavs.football.data.entity.Profile
import com.klavs.football.data.repository.profile.ProfileRepository
import com.klavs.football.utils.ProfileManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val profileManager: ProfileManager,
    private val profileRepo: ProfileRepository,
) : ViewModel() {
    private val _profiles = MutableStateFlow<List<String>>(emptyList())
    val profiles = _profiles.asStateFlow()

    private var profilesJob: Job? = null


    fun listenToProfiles() {
        profilesJob?.cancel()
        profilesJob = viewModelScope.launch(Dispatchers.Main) {
            profileRepo.getProfiles().collect {
                _profiles.value = it
            }
        }
    }

    fun stopListeningToProfiles() {
        profilesJob?.cancel()
        _profiles.value = emptyList()
    }


    fun changeProfile() {
        profileManager.signOut()
    }

    fun deleteProfile(profileName: String) {
        viewModelScope.launch(Dispatchers.Main) {
            profileRepo.deleteProfile(
                name = profileName
            )
        }
    }

    fun addProfile(name: String) {
        viewModelScope.launch(Dispatchers.Main) {
            profileRepo.insertProfile(
                profile = Profile(
                    name = name,
                    teams = emptyList(),
                )
            )
        }
    }

    fun changeProfileName(oldName: String, newName: String) {
        viewModelScope.launch(Dispatchers.Main) {
            profileManager.signIn(newName)
            profileRepo.updateProfileName(
                oldName = oldName,
                newName = newName
            )
        }
    }

}