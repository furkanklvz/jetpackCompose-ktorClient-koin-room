package com.klavs.football.uix.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klavs.football.data.repository.profile.ProfileRepository
import com.klavs.football.room.Converters
import com.klavs.football.utils.ProfileManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyTeamsViewModel (
    private val profileManager: ProfileManager,
    private val profileRepo: ProfileRepository
) :
    ViewModel() {

    private val _currentProfile = MutableStateFlow(profileManager.currentProfile.value)
    val currentProfile = _currentProfile.asStateFlow()

    init {
        getCurrentProfile()
    }

    private fun getCurrentProfile() {
        viewModelScope.launch(Dispatchers.Main) {
            profileManager.currentProfile.collect {
                _currentProfile.value = it
            }
        }
    }

    fun addTeam(teamId: Int) {
        val teamList = _currentProfile.value?.teams?.toMutableList()
        if (teamList != null) {
            teamList.add(teamId.toString())
            viewModelScope.launch(Dispatchers.Main) {
                profileRepo.updateTeams(
                    name = _currentProfile.value!!.name,
                    teams = Converters().fromList(teamList)
                )
            }
        }
    }

    fun removeTeam(teamId: Int) {
        val teamList = _currentProfile.value?.teams?.toMutableList()
        if (teamList != null) {
            teamList.remove(teamId.toString())
            viewModelScope.launch(Dispatchers.Main) {
                profileRepo.updateTeams(
                    name = _currentProfile.value!!.name,
                    teams = Converters().fromList(teamList)
                )
            }
        }
    }
}