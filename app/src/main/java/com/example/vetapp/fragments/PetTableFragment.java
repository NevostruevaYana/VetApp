package com.example.vetapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.toolbox.JsonArrayRequest;
import com.example.vetapp.R;
import com.example.vetapp.adapters.PetTableAdapter;
import com.example.vetapp.adapters.WorkerTableAdapter;
import com.example.vetapp.database.Pet;
import com.example.vetapp.database.Worker;
import com.example.vetapp.utils.QueueSingleton;

import org.json.JSONException;

import java.util.LinkedList;
import java.util.List;

import static com.example.vetapp.utils.Utils.ERROR_REQUEST;
import static com.example.vetapp.utils.Utils.GET_PETS;
import static com.example.vetapp.utils.Utils.NETWORK_ERROR;

public class PetTableFragment extends Fragment {

    private RecyclerView recyclerView;
    private PetTableAdapter adapter;
    private QueueSingleton mRequestQueue;
    private List<Pet> pList;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pet_table, container, false);
        context = inflater.getContext();

        recyclerView = v.findViewById(R.id.rw_pet);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setHasFixedSize(true);

        pList = new LinkedList<>();

        mRequestQueue = QueueSingleton.getInstance(v.getContext());
        getPets();
        return v;
    }

    private void getPets(){
        JsonArrayRequest request = new JsonArrayRequest(
                GET_PETS,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            String client_id = response.getJSONObject(i).getString("client_id");
                            String animal = response.getJSONObject(i).getString("animal");
                            String pet_name = response.getJSONObject(i).getString("pet_name");
                            String pet_gender = response.getJSONObject(i).getString("pet_gender");
                            String date_of_birth = response.getJSONObject(i).getString("date_of_birth");
                            String weigh = response.getJSONObject(i).getString("weigh");
                            pList.add(new Pet(client_id, animal, pet_name, pet_gender, date_of_birth, weigh));
                        }
                        adapter = new PetTableAdapter(pList, pList.size());
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
