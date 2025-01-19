package com.klavs.football.uix

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.SportsSoccer
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.klavs.football.R
import com.klavs.football.data.entity.BottomBarItem
import com.klavs.football.data.entity.ListedTeamInfos
import com.klavs.football.data.routes.TeamDetail
import com.klavs.football.uix.viewModel.MyTeamsViewModel
import kotlinx.coroutines.launch


@Composable
fun MyTeams(navController: NavHostController, viewModel: MyTeamsViewModel) {

    val myProfile by viewModel.currentProfile.collectAsStateWithLifecycle()

    if (myProfile != null) {
        MyTeamsContent(
            onTeamClick = { navController.navigate(TeamDetail(it)) },
            teams = myProfile!!.teams.map { it.toInt() },
            addTeam = { viewModel.addTeam(it) },
            removeTeam = { viewModel.removeTeam(it) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun MyTeamsContent(
    onTeamClick: (Int) -> Unit,
    teams: List<Int>?,
    addTeam: (Int) -> Unit,
    removeTeam: (Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    var showAddingTeamBottomSheet by remember { mutableStateOf(false) }
    val addingTeamBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val context = LocalContext.current
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { scope.launch { showAddingTeamBottomSheet = true } },
                content = {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "add team"
                    )
                }
            )
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(BottomBarItem.MyTeams.label))
                }
            )
        }) { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (showAddingTeamBottomSheet) {
                AddingTeamBottomSheet(
                    sheetState = addingTeamBottomSheetState,
                    onDismiss = {
                        scope.launch {
                            addingTeamBottomSheetState.hide()
                        }.invokeOnCompletion {
                            if (!addingTeamBottomSheetState.isVisible) {
                                showAddingTeamBottomSheet = false
                            }
                        }
                    },
                    teamIds = teams!!,
                    onTeamAdded = { addTeam(it) },
                    onTeamRemoved = { removeTeam(it) }
                )
            }
            Column(
                Modifier.matchParentSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                teams!!.forEach { teamId ->
                    val listedTeamInfo = ListedTeamInfos.teams.find { it.id == teamId }!!
                    ListItem(
                        colors = ListItemDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            headlineColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            trailingIconColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        modifier = Modifier
                            .padding(5.dp)
                            .clickable { onTeamClick(teamId) }
                            .clip(CircleShape),
                        headlineContent = { Text(listedTeamInfo.name) },
                        leadingContent = {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(listedTeamInfo.logoUrl)
                                    .crossfade(true)
                                    .build(),
                                error = rememberVectorPainter(image = Icons.Rounded.SportsSoccer),
                                contentDescription = listedTeamInfo.name,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.size(IconButtonDefaults.mediumContainerSize())
                            )
                        },
                        trailingContent = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.NavigateNext,
                                contentDescription = "navigate team detail"
                            )
                        }
                    )
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun AddingTeamBottomSheet(
    sheetState: SheetState,
    teamIds: List<Int>,
    onTeamAdded: (Int) -> Unit,
    onTeamRemoved: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val teams = teamIds.map {
        ListedTeamInfos.teams.find { team -> team.id == it }!!
    }
    ModalBottomSheet(
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color.Transparent) },
        onDismissRequest = onDismiss,
        shape = RectangleShape
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(Modifier.fillMaxWidth()) {
                IconButton(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(IconButtonDefaults.mediumContainerSize()),
                    onClick = onDismiss
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "close dialog"
                    )
                }
                Text(
                    stringResource(R.string.add_team),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.Center)
                )
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                ListedTeamInfos.teams.forEach { team ->
                    val itContains = teams.contains(team)
                    ListItem(
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        headlineContent = {
                            Text(team.name)
                        },
                        leadingContent = {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(team.logoUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = team.name,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.size(IconButtonDefaults.mediumContainerSize())
                            )
                        },
                        trailingContent = {
                            IconToggleButton(
                                checked = itContains,
                                onCheckedChange = {
                                    if (it) {
                                        onTeamAdded(team.id)
                                    } else {
                                        onTeamRemoved(team.id)
                                    }
                                }
                            ) {
                                Crossfade(
                                    targetState = itContains
                                ) {
                                    if (it) {
                                        Icon(
                                            imageVector = Icons.Rounded.Check,
                                            contentDescription = "remove team",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Rounded.Add,
                                            contentDescription = "add team"
                                        )
                                    }
                                }
                            }
                        }
                    )

                }
            }

        }

    }
}

@Preview
@Composable
private fun MyTeamPreview() {
    MyTeamsContent(
        onTeamClick = {},
        teams = listOf(
            ListedTeamInfos.teams[0].id,
            ListedTeamInfos.teams[1].id,
            ListedTeamInfos.teams[2].id
        ),
        addTeam = {},
        removeTeam = {}
    )
}