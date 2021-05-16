package com.example.vetapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.JsonArrayRequest;
import com.example.vetapp.R;
import com.example.vetapp.adapters.ClientTableAdapter;
import com.example.vetapp.adapters.WorkerTableAdapter;
import com.example.vetapp.database.Client;
import com.example.vetapp.database.Worker;
import com.example.vetapp.utils.QueueSingleton;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.example.vetapp.utils.Utils.*;

public class ClientTableFragment extends Fragment {

    private RecyclerView recyclerView;
    private ClientTableAdapter adapter;
    private QueueSingleton mRequestQueue;
    private List<Client> clList;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_client_table, container, false);
        context = inflater.getContext();

        recyclerView = v.findViewById(R.id.rw_client);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setHasFixedSize(true);

        clList = new ArrayList<>();

        mRequestQueue = QueueSingleton.getInstance(v.getContext());
        getClients();
        return v;
    }

    private void getClients(){
        JsonArrayRequest request = new JsonArrayRequest(
                GET_CLIENTS,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            String full_name = response.getJSONObject(i).getString("full_name");
                            String address = response.getJSONObject(i).getString("address");
                            String phone = response.getJSONObject(i).getString("phone");
                            String regular = response.getJSONObject(i).getString("regular");
                            clList.add(new Client(full_name, address, phone, regular));
                        }
                        adapter = new ClientTableAdapter(clList, clList.size());
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        Toast.makeText(context, ERROR_REQUEST, Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(context, NETWORK_ERROR, Toast.LENGTH_LONG).show()
        );
        mRequestQueue.addToRequestQueue(request);
    }
}
