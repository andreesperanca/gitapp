package hopeapps.dedev.gitapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hopeapps.dedev.core.network.observer.ConnectivityObserver
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    private val connectivityObserver: ConnectivityObserver
): ViewModel() {

    val isConnected = connectivityObserver
        .isConnected
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            false
        )
}