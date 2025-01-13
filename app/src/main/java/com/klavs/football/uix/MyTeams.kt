package com.klavs.football.uix

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Remove
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.klavs.football.R
import com.klavs.football.data.entity.BottomBarItem
import com.klavs.football.data.entity.ListedTeamInfos
import kotlinx.coroutines.launch


@Composable
fun MyTeams(navController: NavHostController) {
    MyTeamsContent(
        onTeamClick = { navController.navigate("team_detail/$it") }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyTeamsContent(onTeamClick: (Int) -> Unit) {
    val scope = rememberCoroutineScope()
    var showAddingTeamBottomSheet by remember { mutableStateOf(false) }
    val addingTeamBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val myTeams = remember { mutableStateListOf<ListedTeamInfos>() }
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
                    myTeams = myTeams,
                    onTeamAdded = { myTeams.add(it) },
                    onTeamRemoved = { myTeams.remove(it) }
                )
            }
            Column(
                Modifier.matchParentSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                myTeams.forEach { team ->
                    ListItem(
                        modifier = Modifier.clickable { onTeamClick(team.id) },
                        headlineContent = { Text(team.name) },
                        leadingContent = {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(team.logoUrl)
                                    .crossfade(true)
                                    .build(),
                                error = painterResource(R.drawable.rounded_image_24),
                                contentDescription = team.name,
                                contentScale = ContentScale.Crop
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
    myTeams: List<ListedTeamInfos>,
    onTeamAdded: (ListedTeamInfos) -> Unit,
    onTeamRemoved: (ListedTeamInfos) -> Unit,
    onDismiss: () -> Unit
) {
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
            Box(Modifier.fillMaxWidth()){
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
                    modifier = Modifier.padding(10.dp).align(Alignment.Center)
                )
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                ListedTeamInfos.teams.forEach { team ->
                    val itContains = myTeams.contains(team)
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
                                        onTeamAdded(team)
                                    } else {
                                        onTeamRemoved(team)
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
        onTeamClick = {}
    )
}