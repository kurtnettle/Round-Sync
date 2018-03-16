package ca.pkay.rcloneexplorer.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import ca.pkay.rcloneexplorer.R;
import ca.pkay.rcloneexplorer.Rclone;
import ca.pkay.rcloneexplorer.RecyclerViewAdapters.RemotesRecyclerViewAdapter;

public class RemotesFragment extends Fragment {

    private static final String ARG_REMOTES = "remotes-array";
    private Rclone rclone;
    private ArrayList<String> remotes;
    private HashMap<String, String> remoteTypes;
    private OnRemoteClickListener clickListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RemotesFragment() {
    }

    @SuppressWarnings("unused")
    public static RemotesFragment newInstance() {
        return new RemotesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        remotes = new ArrayList<>();
        remoteTypes = new HashMap<>();
        rclone = new Rclone((AppCompatActivity) getActivity());
        JSONObject remotesJSON = rclone.getRemotes();
        processJSON(remotesJSON);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remotes_list, container, false);

        // set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new RemotesRecyclerViewAdapter(remotes, remoteTypes, clickListener));
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRemoteClickListener) {
            clickListener = (OnRemoteClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnRemoteClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        clickListener = null;
    }

    public interface OnRemoteClickListener {
        void onRemoteClick(String remote);
    }

    private void processJSON(JSONObject remotesJSON) {
        Iterator<String> keys = remotesJSON.keys();
        while (keys.hasNext()) {
            String type = null;
            String key = keys.next();
            JSONObject values = remotesJSON.optJSONObject(key);
            try {
                type = values.getString("type");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            remotes.add(key);
            remoteTypes.put(key, type);
        }
    }
}
