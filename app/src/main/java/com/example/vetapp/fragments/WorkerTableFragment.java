package com.example.vetapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.toolbox.JsonArrayRequest;
import com.example.vetapp.utils.QueueSingleton;
import com.example.vetapp.R;
import com.example.vetapp.adapters.WorkerTableAdapter;
import com.example.vetapp.database.Worker;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.example.vetapp.utils.Utils.GET_WORKERS;
import static com.example.vetapp.utils.Utils.NETWORK_ERROR;
import static com.example.vetapp.utils.Utils.formatDateForUI;
import static com.example.vetapp.utils.Utils.mSettings;

public class WorkerTableFragment extends Fragment {

    private RecyclerView recyclerView;
    private WorkerTableAdapter adapter;
    private QueueSingleton mRequestQueue;
    private List<Worker> wList;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_worker_table, container, false);
        context = inflater.getContext();

        recyclerView = v.findViewById(R.id.rw_worker);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setHasFixedSize(true);

        wList = new ArrayList<>();

        mRequestQueue = QueueSingleton.getInstance(v.getContext());
        getWorkers();
        return v;
    }

    private void getWorkers(){
        JsonArrayRequest request = new JsonArrayRequest(
                GET_WORKERS,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            String full_name = response.getJSONObject(i).getString("full_name");
                            String position = response.getJSONObject(i).getString("worker_position");
                            String dateOfBirth = response.getJSONObject(i).getString("date_of_birth");
                            String phone = response.getJSONObject(i).getString("phone");
                            wList.add(new Worker(full_name, position, formatDateForUI(dateOfBirth), phone));
                        }
                        adapter = new WorkerTableAdapter(wList, wList.size());
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(context, NETWORK_ERROR, Toast.LENGTH_LONG).show()
        );
        mRequestQueue.addToRequestQueue(request);
    }
}