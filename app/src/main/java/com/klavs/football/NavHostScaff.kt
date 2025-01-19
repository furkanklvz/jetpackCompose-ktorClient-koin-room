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
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.klavs.football.data.entity.BottomBarItem
import com.klavs.football.data.routes.Greeting
import com.klavs.football.data.routes.Menu
import com.klavs.football.data.routes.MyTeams
import com.klavs.football.data.routes.TeamDetail
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
            startDestination = Greeting,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            composable<Greeting> {
                LaunchedEffect(Unit) {
                    bottomBarIsVisible = false
                }
                val viewModel = koinViewModel<GreetingViewModel>{ parametersOf(it) }
                Greeting(navController, viewModel)
            }
            composable<MyTeams> {
                LaunchedEffect(Unit) {
                    bottomBarIsVisible = true
                }
                val viewModel = koinViewModel<MyTeamsViewModel>{ parametersOf(it) }
                MyTeams(
                    navController = navController,
                    viewModel = viewModel
                )
            }
            composable<Menu> {
                val viewModel = koinViewModel<MenuViewModel>{ parametersOf(it) }
                Menu(
                navController,
                viewModel
            ) }
            composable<TeamDetail> {
                val teamDetailViewModel = koinViewModel<TeamDetailViewModel>{ parametersOf(it) }
                val teamDetail = it.toRoute<TeamDetail>()
                TeamDetail(
                    navController = navController,
                    teamId = teamDetail.id,
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
                currentDestination?.hierarchy?.any { it.hasRoute(bottomBarItem.route::class) } == true
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