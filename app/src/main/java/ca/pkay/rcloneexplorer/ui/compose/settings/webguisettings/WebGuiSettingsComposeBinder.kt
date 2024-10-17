package ca.pkay.rcloneexplorer.ui.compose.settings.webguisettings

import androidx.compose.ui.platform.ComposeView

class WebGuiSettingsComposeBinder {
    companion object {
        fun setupComposeView(composeView: ComposeView, viewModel: WebGuiSettingsViewModel) {
            composeView.setContent {
                WebSettingsScreen(webGuiSettingsViewModel = viewModel)
            }
        }
    }
}