package com.interrapidisimo.pruebaandroid.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.interrapidisimo.pruebaandroid.ui.screen.home.HomeScreen
import com.interrapidisimo.pruebaandroid.ui.screen.localidades.LocalidadesScreen
import com.interrapidisimo.pruebaandroid.ui.screen.login.LoginScreen
import com.interrapidisimo.pruebaandroid.ui.screen.splash.SplashScreen
import com.interrapidisimo.pruebaandroid.ui.screen.tablas.TablasScreen

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val HOME = "home"
    const val TABLAS = "tablas"
    const val LOCALIDADES = "localidades"
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onSplashComplete = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.HOME) {
            HomeScreen(
                onNavigateToTablas = { navController.navigate(Routes.TABLAS) },
                onNavigateToLocalidades = { navController.navigate(Routes.LOCALIDADES) }
            )
        }
        composable(Routes.TABLAS) {
            TablasScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.LOCALIDADES) {
            LocalidadesScreen(onBack = { navController.popBackStack() })
        }
    }
}
