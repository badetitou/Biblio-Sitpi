package fr.badetitou.bibliositpi

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import fr.badetitou.bibliositpi.model.api.Resource


@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = NavigationItem.Home.route,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavigationItem.Home.route) {
            HomeScreen(navController)
        }
        composable(NavigationItem.Login.route) {
            LoginScreen(navController)
        }
        composable<Resource> {
             backStackEntry ->
                val resource: Resource = backStackEntry.toRoute()
                ResourceDetailScreen(navController, resource = resource)
        }
    }
}
