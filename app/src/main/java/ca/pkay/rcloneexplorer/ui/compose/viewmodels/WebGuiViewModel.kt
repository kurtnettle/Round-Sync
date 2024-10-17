package ca.pkay.rcloneexplorer.ui.compose.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.pkay.rcloneexplorer.Services.WebGuiService
import ca.pkay.rcloneexplorer.data.WebGuiSettingsRepository
import ca.pkay.rcloneexplorer.data.model.WebGuiSettingsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WebGuiViewModel(
    private val webGuiService: WebGuiService,
    private val webGuiSettingsRepository: WebGuiSettingsRepository
) : ViewModel() {
    private val TAG = "WebGuiViewModel"
    private val _settingsState = MutableStateFlow<WebGuiSettingsState?>(null)
    private val settingsState: StateFlow<WebGuiSettingsState?> = _settingsState.asStateFlow()

    private val _addr = MutableStateFlow("")
    val addr: StateFlow<String>
        get() = _addr

    val commandOutput: StateFlow<String>
        get() = webGuiService.commandOutput

    val isRunning: StateFlow<Boolean>
        get() = webGuiService.isRunning

    fun setAddr(newCommand: String) {
        _addr.value = newCommand
    }

    fun start() {
        viewModelScope.launch {
            settingsState.value?.let {
                webGuiService.start(it, addr.value)
            }
        }
    }

    fun clear() {
        webGuiService.clear()
    }

    fun stop() {
        viewModelScope.launch {
            webGuiService.stop()
        }
    }

    init {
        viewModelScope.launch {
            try {
                _settingsState.value = webGuiSettingsRepository.loadSettings()
            } catch (e: Exception) {
                Log.e(TAG, "failed to load settings. reverted to default settings")
                _settingsState.value = WebGuiSettingsState()
            }
        }
    }
}