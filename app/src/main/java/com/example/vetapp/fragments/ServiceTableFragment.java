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
import com.example.vetapp.adapters.ServiceTableAdapter;
import com.example.vetapp.adapters.WorkerTableAdapter;
import com.example.vetapp.database.Client;
import com.example.vetapp.database.Service;
import com.example.vetapp.database.Worker;
import com.example.vetapp.utils.QueueSingleton;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.example.vetapp.utils.Utils.ERROR_REQUEST;
import static com.example.vetapp.utils.Utils.GET_SERVICES;
import static com.example.vetapp.utils.Utils.NETWORK_ERROR;

public class ServiceTableFragment extends Fragment {

    private RecyclerView recyclerView;
    private ServiceTableAdapter adapter;
    private QueueSingleton mRequestQueue;
    private List<Service> sList;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_service_table, container, false);
        context = inflater.getContext();

        recyclerView = v.findViewById(R.id.rw_service);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setHasFixedSize(true);

        sList = new ArrayList<>();

        mRequestQueue = QueueSingleton.getInstance(v.getContext());
        getServices();
        return v;
    }

    private void getServices(){
        JsonArrayRequest request = new JsonArrayRequest(
                GET_SERVICES,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            String name_of_service = response.getJSONObject(i)
                                    .getString("name_of_service");
                            String time_of_service_delivery = response.getJSONObject(i)
                                    .getString("time_of_service_delivery");
                            String amount_of_money = response.getJSONObject(i)
                                    .getString("amount_of_money");
                            sList.add(new Service(name_of_service, time_of_service_delivery,
                                    amount_of_money + " руб."));
                        }
                        adapter = new ServiceTableAdapter(sList, sList.size());
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
