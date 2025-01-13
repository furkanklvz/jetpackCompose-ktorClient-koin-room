package com.klavs.football.uix

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Login
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.klavs.football.data.entity.Profile
import com.klavs.football.uix.viewModel.GreetingViewModel
import kotlinx.coroutines.launch

@Composable
fun Greeting(navController: NavHostController, greetingViewModel: GreetingViewModel) {

    val profileNames by greetingViewModel.profileNamesFlow.collectAsStateWithLifecycle()
    val currentProfile by greetingViewModel.currentProfile.collectAsStateWithLifecycle()

    GreetingContent(
        insertProfile = {
            greetingViewModel.insertProfile(it)
        },
        profileNames = profileNames,
        currentProfile = currentProfile,
        signIn = { name ->
            greetingViewModel.signIn(name)
        }
    )
}

@Composable
private fun GreetingContent(
    insertProfile: (String) -> Unit,
    profileNames: List<String>,
    signIn: (String) -> Unit,
    currentProfile: Profile?
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var profileNameInput by remember { mutableStateOf("") }
    LaunchedEffect(currentProfile) {
        if (currentProfile != null) {
            scope.launch {
                snackbarHostState.showSnackbar("Signed in as ${currentProfile.name}")
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        })
    { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column {
                    profileNames.forEach { profileName ->
                        ListItem(
                            headlineContent = {
                                Text(profileName)
                            },
                            trailingContent = {
                                FilledTonalIconButton(
                                    onClick = { signIn(profileName) }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.Login,
                                        contentDescription = "sign in"
                                    )
                                }
                            }
                        )
                    }
                }
                TextField(
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                insertProfile(profileNameInput)
                                profileNameInput = ""
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.PersonAdd,
                                contentDescription = "add"
                            )
                        }
                    },
                    value = profileNameInput,
                    onValueChange = { profileNameInput = it }
                )

            }

        }
    }
}

@Preview(locale = "tr")
@Composable
private fun GreetingPreview() {
    GreetingContent(
        insertProfile = {},
        profileNames = listOf("Furkan", "cemile"),
        currentProfile = null,
        signIn = {}
    )
}