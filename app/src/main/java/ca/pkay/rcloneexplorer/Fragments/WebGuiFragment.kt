package ca.pkay.rcloneexplorer.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import ca.pkay.rcloneexplorer.data.WebGuiSettingsRepository
import ca.pkay.rcloneexplorer.Services.WebGuiService
import ca.pkay.rcloneexplorer.ui.compose.WebGuiScreen
import ca.pkay.rcloneexplorer.ui.compose.viewmodels.WebGuiViewModel

class WebGuiFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val webGuiSettingsRepository = WebGuiSettingsRepository(requireContext())
        val webGuiService = WebGuiService.getInstance(requireContext())
        val webGuiViewModel = WebGuiViewModel(webGuiService,webGuiSettingsRepository)
        return ComposeView(requireContext()).apply {
            setContent {
                WebGuiScreen(webGuiViewModel)
            }
        }
    }
}