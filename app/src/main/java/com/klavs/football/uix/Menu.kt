package com.klavs.football.uix

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.klavs.football.R
import com.klavs.football.data.entity.MenuItem
import com.klavs.football.data.routes.Greeting
import com.klavs.football.uix.viewModel.MenuViewModel
import kotlinx.coroutines.launch


@Composable
fun Menu(navController: NavHostController, viewModel: MenuViewModel) {
    val profiles by viewModel.profiles.collectAsStateWithLifecycle()

    MenuContent(
        changeProfile = {
            viewModel.changeProfile()
            navController.navigate(Greeting) {
                popUpTo(0) {
                    inclusive = true
                }
            }
        },
        listenToProfiles = { viewModel.listenToProfiles() },
        stopListeningToProfiles = { viewModel.stopListeningToProfiles() },
        profiles = profiles,
        deleteProfile = { viewModel.deleteProfile(it) },
        addProfile = { viewModel.addProfile(it) },
        changeProfileName = { oldName, newName ->
            viewModel.changeProfileName(oldName, newName)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MenuContent(
    profiles: List<String>,
    changeProfile: () -> Unit,
    deleteProfile: (String) -> Unit,
    addProfile: (String) -> Unit,
    changeProfileName: (oldName: String, newName: String) -> Unit,
    listenToProfiles: () -> Unit,
    stopListeningToProfiles: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    var editProfilesBottomSheetVisible by remember { mutableStateOf(false) }
    var addingProfileDilaogIsVisible by remember { mutableStateOf(false) }
    val editProfileSheetState = rememberModalBottomSheetState()
    val menuItems = listOf(
        MenuItem.EditProfiles(
            onClickAction = {
                editProfilesBottomSheetVisible = true
                listenToProfiles()
            }
        ),
        MenuItem.ChangeProfile(
            onClickAction = changeProfile
        )
    )


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.menu)
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
        ) {
            if (addingProfileDilaogIsVisible) {
                AddingProfileDialog(
                    profileNames = profiles,
                    onDismiss = { addingProfileDilaogIsVisible = false },
                    insertProfile = { addProfile(it) }
                )
            }
            if (editProfilesBottomSheetVisible) {
                var editingProfile by remember { mutableStateOf<String?>(null) }
                ModalBottomSheet(
                    sheetState = editProfileSheetState,
                    onDismissRequest = {
                        scope.launch {
                            editProfileSheetState.hide()
                        }.invokeOnCompletion {
                            if (!editProfileSheetState.isVisible) {
                                editProfilesBottomSheetVisible = false
                                stopListeningToProfiles()
                            }
                        }
                    }
                ) {
                    Column(Modifier.fillMaxWidth()) {
                        profiles.forEach { profileName ->
                            var errorMessage by remember { mutableStateOf<String?>(null) }
                            var textFieldValue by remember { mutableStateOf(TextFieldValue(profileName)) }
                            val editingEnable = editingProfile == profileName
                            val focusRequester = FocusRequester()
                            LaunchedEffect(editingEnable) {
                                if (editingEnable) {
                                    focusRequester.requestFocus()
                                    textFieldValue = textFieldValue.copy(selection = TextRange(textFieldValue.text.length))
                                }
                            }
                            ListItem(
                                colors = ListItemDefaults.colors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    headlineColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                ),
                                modifier = Modifier
                                    .padding(5.dp)
                                    .clip(CircleShape),
                                headlineContent = {
                                    TextField(
                                        isError = errorMessage != null,
                                        supportingText = if (errorMessage != null) {
                                            { Text(errorMessage!!) }
                                        } else null,
                                        modifier = Modifier.focusRequester(focusRequester),
                                        colors = TextFieldDefaults.colors(
                                            unfocusedContainerColor = Color.Transparent,
                                            focusedContainerColor = Color.Transparent,
                                            errorContainerColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                            focusedIndicatorColor = if (!editingEnable) Color.Transparent
                                            else TextFieldDefaults.colors().focusedIndicatorColor,
                                        ),
                                        singleLine = true,
                                        readOnly = !editingEnable,
                                        value = textFieldValue,
                                        onValueChange = {newTextFieldValue->
                                            if (errorMessage != null) {
                                                errorMessage = null
                                            }
                                            textFieldValue = newTextFieldValue
                                        }
                                    )
                                },
                                leadingContent = {
                                    Icon(
                                        imageVector = Icons.Rounded.Person,
                                        contentDescription = "profile",
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier
                                            .background(
                                                MaterialTheme.colorScheme.surfaceContainerLow,
                                                CircleShape
                                            )
                                            .padding(5.dp)
                                    )

                                },
                                trailingContent = {
                                    Row {
                                        if (editingEnable) {
                                            IconButton(
                                                onClick = {
                                                    if (textFieldValue.text != profileName) {
                                                        if (!profiles.contains(textFieldValue.text)) {
                                                            changeProfileName(profileName, textFieldValue.text)
                                                            editingProfile = null
                                                        } else {
                                                            errorMessage =
                                                                context.getString(R.string.this_profile_name_already_exists)
                                                        }
                                                    } else {
                                                        editingProfile = null
                                                    }
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Rounded.Check,
                                                    contentDescription = "confirm",
                                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                                )
                                            }
                                            TextButton(
                                                colors = ButtonDefaults.textButtonColors(
                                                    contentColor = IconButtonDefaults.iconButtonColors().contentColor
                                                ),
                                                onClick = {
                                                    textFieldValue = textFieldValue.copy(text = profileName)
                                                    editingProfile = null
                                                }
                                            ) {
                                                Text(
                                                    stringResource(R.string.cancel),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        } else {
                                            IconButton(
                                                onClick = { editingProfile = profileName }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Rounded.Edit,
                                                    contentDescription = "edit"
                                                )
                                            }
                                            IconButton(
                                                onClick = {
                                                    scope.launch {
                                                        val result = snackBarHostState.showSnackbar(
                                                            message = context.getString(R.string.are_you_sure_you_want_to_delete_this_profile),
                                                            actionLabel = context.getString(R.string.delete),
                                                            withDismissAction = true,
                                                            duration = SnackbarDuration.Long
                                                        )
                                                        if (result == SnackbarResult.ActionPerformed) {
                                                            deleteProfile(profileName)
                                                            snackBarHostState.currentSnackbarData?.dismiss()
                                                        }
                                                    }
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Rounded.DeleteOutline,
                                                    contentDescription = "delete",
                                                    tint = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        }
                                    }
                                }
                            )
                        }
                        if (profiles.size < 6) {
                            IconButton(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .align(Alignment.CenterHorizontally),
                                onClick = {
                                    addingProfileDilaogIsVisible = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Add,
                                    contentDescription = "add profile"
                                )
                            }
                        }
                        SnackbarHost(hostState = snackBarHostState)
                    }
                }
            }
            Column(Modifier.fillMaxSize()) {
                menuItems.forEach { menuItem ->
                    ListItem(
                        modifier = Modifier.clickable { menuItem.onClick() },
                        headlineContent = {
                            Text(stringResource(menuItem.label))
                        },
                        leadingContent = {
                            Icon(
                                imageVector = menuItem.icon,
                                contentDescription = stringResource(menuItem.label)
                            )
                        },
                        trailingContent = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.NavigateNext,
                                contentDescription = "navigate"
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MenuPreview() {
    MenuContent(
        changeProfile = {},
        listenToProfiles = {},
        stopListeningToProfiles = {},
        profiles = listOf("furkan", "cemile"),
        deleteProfile = {},
        addProfile = {},
        changeProfileName = { _, _ -> }
    )
}