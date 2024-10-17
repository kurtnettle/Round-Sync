package ca.pkay.rcloneexplorer.Settings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.compose.ui.platform.ComposeView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ca.pkay.rcloneexplorer.R;
import ca.pkay.rcloneexplorer.ui.compose.settings.webguisettings.WebGuiSettingsComposeBinder;
import ca.pkay.rcloneexplorer.ui.compose.settings.webguisettings.WebGuiSettingsViewModel;

public class WebGuiSettingsFragment extends Fragment {

    public WebGuiSettingsFragment() {
        // Required empty public constructor
    }

    public static WebGuiSettingsFragment newInstance() {
        return new WebGuiSettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.compose_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WebGuiSettingsViewModel webGuiSettingsViewModel = new ViewModelProvider(this).get(WebGuiSettingsViewModel.class);
        ComposeView composeView = view.findViewById(R.id.compose_view);
        if (composeView != null) {
            WebGuiSettingsComposeBinder.Companion.setupComposeView(composeView, webGuiSettingsViewModel);
        } else {
            Log.e("WebGuiSettingsFragment", "ComposeView is null!");
        }
    }
}