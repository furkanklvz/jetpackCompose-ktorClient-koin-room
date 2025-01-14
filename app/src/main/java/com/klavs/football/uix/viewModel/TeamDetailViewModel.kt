package com.klavs.football.uix.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klavs.football.Resource
import com.klavs.football.data.entity.Response
import com.klavs.football.data.repository.team.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamDetailViewModel @Inject constructor(private val repository: TeamRepository) :
    ViewModel() {

    private val _teamResourceFlow = MutableStateFlow<Resource<Response>>(Resource.Idle())
    val teamResourceFlow = _teamResourceFlow.asStateFlow()


    fun getTeam(id:Int) {
        _teamResourceFlow.value = Resource.Loading()
        viewModelScope.launch {
            _teamResourceFlow.value = repository.getTeam(id)
        }
    }
}