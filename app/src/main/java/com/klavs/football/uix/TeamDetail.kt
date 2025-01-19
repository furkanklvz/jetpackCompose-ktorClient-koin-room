package com.klavs.football.uix

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.klavs.football.R
import com.klavs.football.Resource
import com.klavs.football.data.entity.Response
import com.klavs.football.data.entity.Team
import com.klavs.football.data.entity.Venue
import com.klavs.football.uix.viewModel.TeamDetailViewModel

@Composable
fun TeamDetail(
    navController: NavHostController,
    teamId: Int,
    viewMode: TeamDetailViewModel
) {
    LaunchedEffect(Unit) {
        viewMode.getTeam(teamId)
    }
    val teamResource by viewMode.teamResourceFlow.collectAsStateWithLifecycle()

    TeamDetailContent(
        teamResource = teamResource,
        navBack = { navController.popBackStack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun TeamDetailContent(
    teamResource: Resource<Response>,
    navBack: () -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = navBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
                            contentDescription = "navigate back"
                        )
                    }
                },
                title = {
                    Text("Team Detail")
                }
            )
        }) { innerPadding ->
        Box(
            Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (teamResource) {
                is Resource.Error -> {
                    Text(
                        text = "hata",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                is Resource.Idle -> {}
                is Resource.Loading -> {
                    CircularWavyProgressIndicator()
                }

                is Resource.Success -> {
                    val team = teamResource.data!!.team
                    val venue = teamResource.data.venue
                    Column(
                        modifier = Modifier
                            .padding(15.dp)
                            .fillMaxSize()
                    ) {
                        Text("id: ${team.id}")
                        Text("name: ${team.name}")
                        Text("code: ${team.code}")
                        Text("country: ${team.country}")
                        Text("founded: ${team.founded}")
                        Text("national: ${team.national}")
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(team.logo)
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            error = painterResource(R.drawable.rounded_image_24),
                            contentScale = ContentScale.Fit,
                            placeholder = painterResource(R.drawable.rounded_image_24),
                            modifier = Modifier.size(IconButtonDefaults.largeContainerSize())
                        )
                        Text("Venue")
                        Text("id: ${venue.id}")
                        Text("name: ${venue.name}")
                        Text("address: ${venue.address}")
                        Text("city: ${venue.city}")
                        Text("capacity: ${venue.capacity}")
                        Text("surface: ${venue.surface}")
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(venue.image)
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            error = painterResource(R.drawable.rounded_image_24),
                            contentScale = ContentScale.Fit,
                            placeholder = painterResource(R.drawable.rounded_image_24),
                            modifier = Modifier.size(IconButtonDefaults.largeContainerSize())
                        )
                    }
                }
            }
        }

    }
}

@Preview
@Composable
private fun TeamDetailPreview() {
    val response = Response(
        team = Team(
            code = "",
            country = "",
            founded = 1905,
            id = 0,
            logo = "",
            name = "",
            national = true
        ),
        venue = Venue(
            address = "",
            capacity = 45000,
            city = "",
            id = 1,
            image = "",
            name = "",
            surface = ""
        )
    )
    TeamDetailContent(
        teamResource = Resource.Success(data = response),
        navBack = {}
    )
}