package com.example.zcashclienttets

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cash.z.ecc.android.sdk.internal.service.LightWalletGrpcService
import cash.z.ecc.android.sdk.type.ZcashNetwork
import com.example.zcashclienttets.ui.theme.ZcashClientTetsTheme
import com.example.zcashclienttets.ui.theme.screen.InfoScreen
import com.example.zcashclienttets.ui.theme.screen.InfoViewModel
import com.example.zcashclienttets.ui.theme.screen.TransactionsScreen
import com.example.zcashclienttets.ui.theme.screen.TransactionsViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val testSeedPhrase =
    "wish puppy smile loan doll curve hole maze file ginger hair nose key relax knife witness cannon grab despair throw review deal slush frame"

@Module
@InstallIn(SingletonComponent::class)
object LightWalletModule {

    @Singleton
    @Provides
    fun provideLightwallet(@ApplicationContext context: Context): LightWalletGrpcService {
        return LightWalletGrpcService(context, ZcashNetwork.Testnet.defaultHost)
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val screens = listOf(
            Screen.Info,
            Screen.Transactions,
        )

        setContent {
            ZcashClientTetsTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomNavigationBar(screens, navController) }
                ) { innerPadding ->
                    NavHost(navController, startDestination = Screen.Info.route, Modifier.padding(innerPadding)) {
                        composable(Screen.Info.route) {
                            val viewModel = hiltViewModel<InfoViewModel>()
                            InfoScreen(viewModel)
                        }
                        composable(Screen.Transactions.route) {
                            val viewModel = hiltViewModel<TransactionsViewModel>()
                            TransactionsScreen(viewModel)
                        }
                    }
                }
            }
        }
    }

    sealed class Screen(val route: String, val title: String, val imageVector: ImageVector) {
        object Info : Screen("info", "Info", Icons.Filled.Info)
        object Transactions : Screen("transactions", "Transactions", Icons.Filled.List)
    }
}

@Composable
fun BottomNavigationBar(screens: List<MainActivity.Screen>, navController: NavController) {
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        screens.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(screen.imageVector, contentDescription = null) },
                label = { Text(screen.title) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = false
                    }
                }
            )
        }
    }
}


