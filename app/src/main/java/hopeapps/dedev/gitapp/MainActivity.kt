package hopeapps.dedev.gitapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import hopeapps.dedev.core.network.observer.AndroidConnectivityObserver
import hopeapps.dedev.core.presentation.designsystem.LocalSpacing
import hopeapps.dedev.core.presentation.designsystem.theme.GitappTheme
import hopeapps.dedev.gitapp.navigation.SetupNavGraph


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = MainViewModel(
            connectivityObserver = AndroidConnectivityObserver(
                context = applicationContext
            )
        )

        enableEdgeToEdge()
        setContent {
            GitappTheme {

                val navController = rememberNavController()
                val isConnected = viewModel.isConnected.collectAsStateWithLifecycle()

                Box(modifier = Modifier.fillMaxSize()) {
                    SetupNavGraph(navController = navController)
                    if (!isConnected.value) {
                        OfflineBanner(
                            modifier = Modifier
                                .padding(top = LocalSpacing.current.extraLarge)
                                .align(Alignment.TopCenter)
                        )
                    }
                }
            }
        }
    }
}

