package com.interrapidisimo.pruebaandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.interrapidisimo.pruebaandroid.ui.navigation.NavGraph
import com.interrapidisimo.pruebaandroid.ui.theme.PruebaTecnicaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PruebaTecnicaTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}
