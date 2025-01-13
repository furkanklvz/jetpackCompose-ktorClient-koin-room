package com.klavs.football.uix

import android.util.Log
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.klavs.football.R
import com.klavs.football.Resource
import com.klavs.football.data.entity.Team
import com.klavs.football.uix.viewModel.TeamDetailViewModel
import com.skydoves.landscapist.glide.GlideImage

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
    teamResource: Resource<Team>,
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
                        text = stringResource(teamResource.message!!),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                is Resource.Idle -> {}
                is Resource.Loading -> {
                    CircularWavyProgressIndicator()
                }

                is Resource.Success -> {
                    val team = teamResource.data!!
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
                        Text("coil")
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(team.logo)
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            error = painterResource(R.drawable.rounded_image_24),
                            contentScale = ContentScale.Crop,
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
    val team = Team(
        id = 0,
        code = "fb",
        country = "Turkey",
        founded = 1907,
        national = true,
        logo = "",
        name = "Fenerbahçe"
    )
    TeamDetailContent(
        teamResource = Resource.Success(data = team),
        navBack = {}
    )
}