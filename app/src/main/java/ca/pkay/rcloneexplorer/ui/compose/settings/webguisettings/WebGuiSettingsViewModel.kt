package ca.pkay.rcloneexplorer.ui.compose.settings.webguisettings

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ca.pkay.rcloneexplorer.data.WebGuiSettingsRepository
import ca.pkay.rcloneexplorer.data.model.WebGuiSettingsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WebGuiSettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "WebGuiSettingsViewModel"

    private val webGuiSettingsRepository = WebGuiSettingsRepository(application)
    private val _settingsState = MutableStateFlow(WebGuiSettingsState())
    val settingsState: StateFlow<WebGuiSettingsState> = _settingsState

    fun loadSettings() {
        viewModelScope.launch {
            try {
                val settings = webGuiSettingsRepository.loadSettings()
                _settingsState.value = settings
            } catch (e: Exception) {
                Log.e(TAG,"failed to load the settings. $e")
            }
        }
    }

    fun saveSettings(settings: WebGuiSettingsState) {
        viewModelScope.launch {
            try {
                webGuiSettingsRepository.saveSettings(settings)
            } catch (e: Exception) {
                Log.e(TAG,"failed to save the settings. $e")
            }
        }
    }

    fun setIsAnonymousLogin(isAnonymous: Boolean) {
        _settingsState.update { it.copy(isAnonymousLogin = isAnonymous) }
    }

    fun setUser(user: String) {
        _settingsState.update { it.copy(user = user) }
    }

    fun setPassword(pass: String) {
        _settingsState.update { it.copy(pass = pass) }
    }

    fun setAddress(addr: String) {
        _settingsState.update { it.copy(addr = addr) }
    }

    fun setGuiReleaseUrl(guiReleaseUrl: String) {
        _settingsState.update { it.copy(guiReleaseUrl = guiReleaseUrl) }
    }
}