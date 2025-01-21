package com.klavs.football.uix

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.SportsSoccer
import androidx.compose.material.icons.rounded.Stadium
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
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
                    val tabData = listOf(
                        "Team" to Icons.Rounded.SportsSoccer,
                        "Venue" to Icons.Rounded.Stadium
                    )
                    var tabState by remember { mutableIntStateOf(0) }
                    Column(modifier = Modifier.matchParentSize()) {
                        TabRow(
                            selectedTabIndex = tabState
                        ) {
                            tabData.forEachIndexed { index, tabInfo ->
                                Tab(
                                    selected = tabState == index,
                                    onClick = { tabState = index },
                                    text = { Text(tabInfo.first) },
                                    icon = {
                                        Icon(
                                            imageVector = tabInfo.second,
                                            contentDescription = tabInfo.first
                                        )
                                    }
                                )
                            }
                        }
                        when (tabState) {
                            0 -> {
                                val team = teamResource.data!!.team
                                Column(
                                    modifier = Modifier
                                        .padding(15.dp)
                                        .fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    Text(team.name, style = MaterialTheme.typography.headlineMedium)
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
                                    listOf(
                                        "Code" to team.code,
                                        "Country" to team.country,
                                        "Founded" to team.founded.toString(),
                                        "National" to team.national.toString()
                                    ).forEach { teamUtil ->
                                        Card(
                                            modifier = Modifier
                                                .padding(10.dp)
                                                .fillMaxWidth()
                                                .align(Alignment.Start)
                                        ) {
                                            Row(modifier = Modifier.fillMaxWidth()) {
                                                Text(
                                                    teamUtil.first + ":",
                                                    style = MaterialTheme.typography.titleSmall,
                                                    modifier = Modifier.padding(10.dp)
                                                )
                                                Text(
                                                    teamUtil.second,
                                                    modifier = Modifier
                                                        .padding(10.dp)
                                                        .weight(1f)
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            1 -> {
                                val venue = teamResource.data!!.venue
                                Column(
                                    modifier = Modifier
                                        .padding(15.dp)
                                        .fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    Text(
                                        venue.name, style = MaterialTheme.typography.headlineSmall,
                                        textAlign = TextAlign.Center
                                    )
                                    AsyncImage(
                                        model = ImageRequest.Builder(context)
                                            .data(venue.image)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = null,
                                        error = painterResource(R.drawable.rounded_image_24),
                                        contentScale = ContentScale.Fit,
                                        placeholder = painterResource(R.drawable.rounded_image_24),
                                        modifier = Modifier.size(IconButtonDefaults.xLargeContainerSize())
                                    )
                                    listOf(
                                        "Address" to venue.address,
                                        "City" to venue.city,
                                        "Capacity" to venue.capacity.toString(),
                                        "Surface" to venue.surface
                                    ).forEach { venueUtil ->
                                        Card(
                                            modifier = Modifier
                                                .padding(10.dp)
                                                .fillMaxWidth()
                                                .align(Alignment.Start)
                                        ) {
                                            Row(modifier = Modifier.fillMaxWidth()) {
                                                Text(
                                                    venueUtil.first + ":",
                                                    style = MaterialTheme.typography.titleSmall,
                                                    modifier = Modifier.padding(10.dp)
                                                )
                                                Text(
                                                    venueUtil.second,
                                                    modifier = Modifier
                                                        .padding(10.dp)
                                                        .weight(1f)
                                                )
                                            }
                                        }
                                    }
                                }

                            }
                        }
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
            code = "fb",
            country = "turkiye",
            founded = 1907,
            id = 0,
            logo = "",
            name = "fenerbahce",
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