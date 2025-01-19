package com.klavs.football

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShortNavigationBar
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.klavs.football.data.entity.BottomBarItem
import com.klavs.football.uix.Greeting
import com.klavs.football.uix.Menu
import com.klavs.football.uix.MyTeams
import com.klavs.football.uix.TeamDetail
import com.klavs.football.uix.viewModel.GreetingViewModel
import com.klavs.football.uix.viewModel.MenuViewModel
import com.klavs.football.uix.viewModel.MyTeamsViewModel
import com.klavs.football.uix.viewModel.TeamDetailViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun NavHostScaff() {
    val navController = rememberNavController()
    NavHostContent(navController)
}

@Composable
private fun NavHostContent(navController: NavHostController) {
    var bottomBarIsVisible by remember { mutableStateOf(false) }
    Scaffold(
        bottomBar = {
            if (bottomBarIsVisible) {
                BottomBar(
                    navController = navController
                )
            }
        }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "greeting",
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            composable("greeting") {
                LaunchedEffect(Unit) {
                    bottomBarIsVisible = false
                }
                val viewModel = koinViewModel<GreetingViewModel>{ parametersOf(it) }
                Greeting(navController, viewModel)
            }
            composable(BottomBarItem.MyTeams.route) {
                LaunchedEffect(Unit) {
                    bottomBarIsVisible = true
                }
                val viewModel = koinViewModel<MyTeamsViewModel>{ parametersOf(it) }
                MyTeams(
                    navController = navController,
                    viewModel = viewModel
                )
            }
            composable(BottomBarItem.Menu.route) {
                val viewModel = koinViewModel<MenuViewModel>{ parametersOf(it) }
                Menu(
                navController,
                viewModel
            ) }
            composable("team_detail/{id}",
                arguments = listOf(
                    navArgument("id") {
                        type = NavType.IntType
                    }
                )) {
                val teamDetailViewModel = koinViewModel<TeamDetailViewModel>{ parametersOf(it) }
                TeamDetail(
                    navController = navController,
                    teamId = it.arguments?.getInt("id") ?: 0,
                    viewMode = teamDetailViewModel
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun BottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val bottomBarItems = listOf(
        BottomBarItem.MyTeams,
        BottomBarItem.Menu
    )
    ShortNavigationBar {
        bottomBarItems.forEach { bottomBarItem ->
            val selected =
                currentDestination?.hierarchy?.any { it.route == bottomBarItem.route } == true
            ShortNavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(bottomBarItem.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selected) bottomBarItem.selectedIcon
                        else bottomBarItem.unselectedIcon,
                        contentDescription = stringResource(bottomBarItem.label)
                    )
                },
                label = { Text(stringResource(bottomBarItem.label)) }
            )
        }
    }
}

@Preview(locale = "tr")
@Composable
private fun MainScaffPreview() {
    Scaffold(bottomBar = {
        BottomBar(
            navController = rememberNavController()
        )
    }) {
        Box(modifier = Modifier.padding(it))
    }
}