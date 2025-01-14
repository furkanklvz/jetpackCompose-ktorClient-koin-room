package com.klavs.football.data.entity

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChangeCircle
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.ui.graphics.vector.ImageVector
import com.klavs.football.R

sealed class MenuItem( val label: Int, val icon: ImageVector, val onClick: () -> Unit) {
    data class EditProfiles(val onClickAction: () -> Unit) : MenuItem(
        R.string.edit_profiles,
        Icons.Rounded.Groups,
        onClickAction
    )

    data class ChangeProfile(val onClickAction: () -> Unit) : MenuItem(
        label = R.string.change_profile,
        icon = Icons.Outlined.ChangeCircle,
        onClickAction
    )
}