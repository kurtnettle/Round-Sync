package ca.pkay.rcloneexplorer.ui.compose.settings.webguisettings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.pkay.rcloneexplorer.R
import ca.pkay.rcloneexplorer.data.model.WebGuiSettingsState


@Composable
fun SettingsCategory(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.surfaceTint,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun OutlinedTextInputBox(
    label: String,
    supportingText: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        label = { Text(text = label) },
        singleLine = true,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        supportingText = { Text(supportingText) }
    )
}

@Composable
fun SwitchItem(
    value: Boolean,
    label: String,
    description: String? = null,
    onValueChange: (Boolean) -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.weight(1f, fill = true),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
        Switch(checked = value, onCheckedChange = onValueChange)
    }
}

@Composable
fun SectionDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
    )
}

@Composable
fun AuthenticationSection(
    settingsState: WebGuiSettingsState,
    webGuiSettingsViewModel: WebGuiSettingsViewModel
) {
    SettingsCategory(stringResource(R.string.web_gui_setting_category_authentication))
    SwitchItem(
        settingsState.isAnonymousLogin,
        stringResource(R.string.web_gui_setting_anon_login_title),
        stringResource(R.string.web_gui_setting_anon_login_desc),
        { webGuiSettingsViewModel.setIsAnonymousLogin(it) }
    )

    OutlinedTextInputBox(
        stringResource(R.string.web_gui_setting_user_title),
        stringResource(R.string.web_gui_setting_user_desc),
        settingsState.user,
        onValueChange = { webGuiSettingsViewModel.setUser(it) }
    )

    OutlinedTextInputBox(
        stringResource(R.string.web_gui_setting_pass_title),
        stringResource(R.string.web_gui_setting_pass_desc),
        settingsState.pass,
        onValueChange = { webGuiSettingsViewModel.setPassword(it) }
    )
}

@Composable
fun ServerConfigurationSection(
    settingsState: WebGuiSettingsState,
    webGuiSettingsViewModel: WebGuiSettingsViewModel
) {
    SettingsCategory(stringResource(R.string.web_gui_setting_category_server_conf))
    OutlinedTextInputBox(
        stringResource(R.string.web_gui_setting_server_title),
        stringResource(R.string.web_gui_setting_server_desc),
        settingsState.addr,
        onValueChange = { webGuiSettingsViewModel.setAddress(it) }
    )
}

@Composable
fun WebInterfaceSection(
    settingsState: WebGuiSettingsState,
    webGuiSettingsViewModel: WebGuiSettingsViewModel
) {
    SettingsCategory(stringResource(R.string.web_gui_setting_category_web_interface))
    OutlinedTextInputBox(
        stringResource(R.string.web_gui_setting_gui_url_title),
        stringResource(R.string.web_gui_setting_gui_url_desc),
        settingsState.guiReleaseUrl,
        onValueChange = { webGuiSettingsViewModel.setGuiReleaseUrl(it) }
    )
}


@Composable
fun WebSettingsScreen(webGuiSettingsViewModel: WebGuiSettingsViewModel = viewModel()) {
    val context = LocalContext.current
    val settingsState by webGuiSettingsViewModel.settingsState.collectAsState()
    val colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()

    MaterialTheme(colorScheme = colorScheme) {
        LaunchedEffect(Unit) { webGuiSettingsViewModel.loadSettings() }
        Scaffold(
            containerColor = Color.Transparent,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        webGuiSettingsViewModel.saveSettings(settingsState)
                        Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Save settings",
                    )
                }
            }
        ) {
            paddingValues->
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier =
                Modifier
                    .background(Color.Transparent)
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                AuthenticationSection(settingsState, webGuiSettingsViewModel)
                SectionDivider()
                ServerConfigurationSection(settingsState, webGuiSettingsViewModel)
                SectionDivider()
                WebInterfaceSection(settingsState, webGuiSettingsViewModel)
            }
        }
    }
}

// TODO: Add support for multiple gui usage as currently cache clear is need to download the new gui.
//  As of now, the two other forks is not usable in the mobile.
// https://api.github.com/repos/yuudi/rclone-webui-angular/releases/latest
// https://api.github.com/repos/rclone/rclone-webui-react/releases/latest
// https://api.github.com/repos/retifrav/rclone-rc-web-gui/releases/latest
