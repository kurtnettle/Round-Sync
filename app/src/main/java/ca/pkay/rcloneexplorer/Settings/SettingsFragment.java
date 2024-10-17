package ca.pkay.rcloneexplorer.Settings;

import static ca.pkay.rcloneexplorer.Activities.MainActivity.MAIN_ACTIVITY_START_EXPORT;
import static ca.pkay.rcloneexplorer.Activities.MainActivity.MAIN_ACTIVITY_START_IMPORT;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ca.pkay.rcloneexplorer.Activities.MainActivity;
import ca.pkay.rcloneexplorer.R;

public class SettingsFragment extends Fragment {

    public final static int GENERAL_SETTINGS = 1;
    public final static int FILE_ACCESS_SETTINGS = 2;
    public final static int LOOK_AND_FEEL_SETTINGS = 3;
    public final static int LOGGING_SETTINGS = 4;
    public final static int NOTIFICATION_SETTINGS = 5;
    private OnSettingCategorySelectedListener clickListener;

    public interface OnSettingCategorySelectedListener {
        void onSettingCategoryClicked(int category);
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SettingsFragment() {
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        setClickListeners(view);

        if (getActivity() != null) {
            getActivity().setTitle(getString(R.string.settings));
        }

        return view;
    }

    private void openWebSettingsScreen() {
        WebGuiSettingsFragment webGuiSettingsFragment = WebGuiSettingsFragment.newInstance();

        if (getActivity() != null) {
            getActivity().getSupportFragmentManager();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flFragment, webGuiSettingsFragment)
                    .commit();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSettingCategorySelectedListener) {
            clickListener = (OnSettingCategorySelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement listener");
        }
    }

    private void setClickListeners(View view) {

        view.findViewById(R.id.general_settings).setOnClickListener(v -> clickListener.onSettingCategoryClicked(GENERAL_SETTINGS));

        view.findViewById(R.id.logging_settings).setOnClickListener(v -> clickListener.onSettingCategoryClicked(LOGGING_SETTINGS));

        view.findViewById(R.id.logging_settings).setOnClickListener(v -> clickListener.onSettingCategoryClicked(LOGGING_SETTINGS));

        view.findViewById(R.id.look_and_feel_settings).setOnClickListener(v -> clickListener.onSettingCategoryClicked(LOOK_AND_FEEL_SETTINGS));

        view.findViewById(R.id.notification_settings).setOnClickListener(v -> clickListener.onSettingCategoryClicked(NOTIFICATION_SETTINGS));

        view.findViewById(R.id.file_access_settings).setOnClickListener(v -> clickListener.onSettingCategoryClicked(FILE_ACCESS_SETTINGS));

        view.findViewById(R.id.importSettings).setOnClickListener(v -> startActivity(getImportIntent()));
        view.findViewById(R.id.exportSettings).setOnClickListener(v -> startActivity(getExportIntent()));

        view.findViewById(R.id.web_settings).setOnClickListener(v -> openWebSettingsScreen());
    }

    private Intent getImportIntent() {
        Intent i = new Intent(this.getContext(), MainActivity.class);
        i.setAction(MAIN_ACTIVITY_START_IMPORT);
        return i;
    }

    private Intent getExportIntent() {
        Intent i = new Intent(this.getContext(), MainActivity.class);
        i.setAction(MAIN_ACTIVITY_START_EXPORT);
        return i;
    }
}
