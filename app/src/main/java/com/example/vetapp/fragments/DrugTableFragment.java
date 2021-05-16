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
import com.example.vetapp.adapters.DrugTableAdapter;
import com.example.vetapp.adapters.WorkerTableAdapter;
import com.example.vetapp.database.Client;
import com.example.vetapp.database.Drug;
import com.example.vetapp.database.Worker;
import com.example.vetapp.utils.QueueSingleton;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.example.vetapp.utils.Utils.ERROR_REQUEST;
import static com.example.vetapp.utils.Utils.GET_DRUGS;
import static com.example.vetapp.utils.Utils.NETWORK_ERROR;

public class DrugTableFragment extends Fragment {

    private RecyclerView recyclerView;
    private DrugTableAdapter adapter;
    private QueueSingleton mRequestQueue;
    private List<Drug> dList;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_drug_table, container, false);
        context = inflater.getContext();

        recyclerView = v.findViewById(R.id.rw_drug);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setHasFixedSize(true);

        dList = new ArrayList<>();

        mRequestQueue = QueueSingleton.getInstance(v.getContext());
        getDrugs();
        return v;
    }

    private void getDrugs(){
        JsonArrayRequest request = new JsonArrayRequest(
                GET_DRUGS,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            String drug_name = response.getJSONObject(i).getString("drug_name");
                            String amount = response.getJSONObject(i).getString("amount");
                            dList.add(new Drug(drug_name, amount));
                        }
                        adapter = new DrugTableAdapter(dList, dList.size());
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
