package com.klavs.football.data.entity

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.SportsSoccer
import androidx.compose.ui.graphics.vector.ImageVector
import com.klavs.football.R

sealed class BottomBarItem(val route: Any, val label: Int, val selectedIcon: ImageVector, val unselectedIcon: ImageVector) {

    data object MyTeams: BottomBarItem(com.klavs.football.data.routes.MyTeams, R.string.my_teams, selectedIcon = Icons.Rounded.SportsSoccer, unselectedIcon = Icons.Rounded.SportsSoccer)
    data object Menu: BottomBarItem(com.klavs.football.data.routes.Menu, R.string.menu, selectedIcon = Icons.Rounded.Menu, unselectedIcon = Icons.Rounded.Menu)
}