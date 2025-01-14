package com.klavs.football.uix

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material.icons.rounded.SportsSoccer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.klavs.football.R
import com.klavs.football.data.entity.BottomBarItem
import com.klavs.football.uix.viewModel.GreetingViewModel

@Composable
fun Greeting(navController: NavHostController, greetingViewModel: GreetingViewModel) {

    val profileNames by greetingViewModel.profileNamesFlow.collectAsStateWithLifecycle()
    val currentProfile by greetingViewModel.currentProfile.collectAsStateWithLifecycle()

    LaunchedEffect(currentProfile) {
        if (currentProfile != null) {
            navController.navigate(BottomBarItem.MyTeams.route) {
                popUpTo(0) {
                    inclusive = true
                }
            }
        }
    }

    GreetingContent(
        insertProfile = {
            greetingViewModel.insertProfile(it)
        },
        profileNames = profileNames,
        signIn = { name ->
            greetingViewModel.signIn(name)
        }
    )
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalLayoutApi::class
)
@Composable
private fun GreetingContent(
    insertProfile: (String) -> Unit,
    profileNames: List<String>,
    signIn: (String) -> Unit
) {
    var addingProfileDilaog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.SportsSoccer,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 5.dp)
                                .size(IconButtonDefaults.xLargeIconSize)
                        )
                        Text(
                            stringResource(R.string.app_name)
                        )
                    }

                }
            )
        })
    { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            if (addingProfileDilaog) {
                AddingProfileDialog(
                    profileNames = profileNames,
                    onDismiss = { addingProfileDilaog = false },
                    insertProfile = { insertProfile(it) }
                )
            }
            Column(
                Modifier.fillMaxHeight(0.7f),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(R.string.select_a_profile),
                    style = MaterialTheme.typography.titleMedium
                )
                FlowRow(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(25.dp)
                ) {
                    profileNames.forEach { profileName ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            FilledIconButton(
                                colors = IconButtonDefaults.filledIconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                ),
                                onClick = { signIn(profileName) },
                                modifier = Modifier.size(IconButtonDefaults.largeContainerSize())
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Person,
                                    contentDescription = profileName,
                                    modifier = Modifier.size(IconButtonDefaults.mediumContainerSize())
                                )
                            }
                            Text(
                                profileName,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    if (profileNames.size < 6) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            IconButton(
                                modifier = Modifier
                                    .size(IconButtonDefaults.largeContainerSize()),
                                onClick = { addingProfileDilaog = true }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Add,
                                    contentDescription = "add",
                                    modifier = Modifier.size(IconButtonDefaults.largeIconSize)
                                )
                            }
                        }
                    }
                }

            }

        }
    }
}

@Composable
fun AddingProfileDialog(
    profileNames: List<String>,
    onDismiss: () -> Unit,
    insertProfile: (String) -> Unit
) {
    val context = LocalContext.current
    var profileNameInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Rounded.PersonAdd,
                contentDescription = "add profile"
            )
        },
        title = {
            Text(stringResource(R.string.add_profile))
        },
        text = {
            TextField(
                isError = errorMessage != null,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent
                ),
                supportingText = if (errorMessage != null) {
                    {
                        Text(errorMessage!!)
                    }
                } else null,
                label = { Text(stringResource(R.string.profile_name)) },
                value = profileNameInput,
                onValueChange = {
                    profileNameInput = it
                    errorMessage =
                        null
                }
            )
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(stringResource(R.string.cancel))
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (!profileNames.contains(profileNameInput)) {
                        if (profileNameInput.isNotBlank()) {
                            insertProfile(profileNameInput)
                            onDismiss()
                        } else {
                            errorMessage =
                                context.getString(R.string.profile_name_cannot_be_empty)
                        }
                    } else {
                        errorMessage =
                            context.getString(R.string.this_profile_name_already_exists)
                    }
                }
            ) {
                Text(stringResource(R.string.add))
            }
        },
        onDismissRequest = onDismiss
    )
}

@Preview(locale = "tr")
@Composable
private fun GreetingPreview() {
    GreetingContent(
        insertProfile = {},
        profileNames = listOf("Furkan", "cemile", "berkan", "sema", "erkan"),
        signIn = {}
    )
}