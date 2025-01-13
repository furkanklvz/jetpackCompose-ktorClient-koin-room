package com.klavs.football.data.entity

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.SportsSoccer
import androidx.compose.ui.graphics.vector.ImageVector
import com.klavs.football.R

sealed class BottomBarItem(val route: String, val label: Int, val selectedIcon: ImageVector, val unselectedIcon: ImageVector) {

    data object MyTeams: BottomBarItem("my_teams", R.string.my_teams, selectedIcon = Icons.Rounded.SportsSoccer, unselectedIcon = Icons.Rounded.SportsSoccer)
    data object Menu: BottomBarItem("menu", R.string.menu, selectedIcon = Icons.Rounded.Menu, unselectedIcon = Icons.Rounded.Menu)
}