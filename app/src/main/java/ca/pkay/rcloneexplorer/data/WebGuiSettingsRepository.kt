package ca.pkay.rcloneexplorer.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import ca.pkay.rcloneexplorer.data.model.DefaultWebGuiSettings
import ca.pkay.rcloneexplorer.data.model.WebGuiSettingsState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class WebGuiSettingsRepository(private val context: Context) {
    companion object {
        private val Context.dataStore by preferencesDataStore(name = "web_settings")
    }

    suspend fun loadSettings(): WebGuiSettingsState {
        val settingsFlow = context.dataStore.data
        return settingsFlow.map { preferences ->
            WebGuiSettingsState(
                user = preferences[WebSettingsPrefKeys.USERNAME] ?: DefaultWebGuiSettings.USERNAME,
                pass = preferences[WebSettingsPrefKeys.PASSWORD] ?: DefaultWebGuiSettings.PASSWORD,
                addr = preferences[WebSettingsPrefKeys.ADDRESS] ?: DefaultWebGuiSettings.ADDRESS,
                isAnonymousLogin = preferences[WebSettingsPrefKeys.ANON_LOGIN] == true,
                guiReleaseUrl = preferences[WebSettingsPrefKeys.GUI_RELEASE_URL]
                    ?: DefaultWebGuiSettings.GUI_RELEASE_URL
            )
        }.first()
    }

    suspend fun saveSettings(settings: WebGuiSettingsState) {
        context.dataStore.edit { prefs ->
            prefs[WebSettingsPrefKeys.USERNAME] = settings.user
            prefs[WebSettingsPrefKeys.PASSWORD] = settings.pass
            prefs[WebSettingsPrefKeys.ADDRESS] = settings.addr
            prefs[WebSettingsPrefKeys.ANON_LOGIN] = settings.isAnonymousLogin
            prefs[WebSettingsPrefKeys.GUI_RELEASE_URL] = settings.guiReleaseUrl
        }
    }
}

private object WebSettingsPrefKeys {
    val ANON_LOGIN = booleanPreferencesKey("web_anon_login")
    val ADDRESS = stringPreferencesKey("web_address")
    val USERNAME = stringPreferencesKey("web_user")
    val PASSWORD = stringPreferencesKey("web_pass")
    val GUI_RELEASE_URL = stringPreferencesKey("web_gui_url")
}