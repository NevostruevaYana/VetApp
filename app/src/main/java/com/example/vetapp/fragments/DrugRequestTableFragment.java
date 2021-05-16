package com.example.vetapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.toolbox.JsonArrayRequest;
import com.example.vetapp.R;
import com.example.vetapp.adapters.DrugRequestTableAdapter;
import com.example.vetapp.database.DrugRequest;
import com.example.vetapp.dialog_fragments.AddRequestDialogFragment;
import com.example.vetapp.dialog_fragments.ChangePhoneDialogFragment;
import com.example.vetapp.utils.QueueSingleton;

import org.json.JSONException;

import java.util.LinkedList;
import java.util.List;

import static com.example.vetapp.dialog_fragments.AddRequestDialogFragment.KEY1;
import static com.example.vetapp.dialog_fragments.AddRequestDialogFragment.KEY2;
import static com.example.vetapp.dialog_fragments.AddRequestDialogFragment.KEY3;
import static com.example.vetapp.dialog_fragments.AddRequestDialogFragment.KEY4;
import static com.example.vetapp.dialog_fragments.AddRequestDialogFragment.KEY5;
import static com.example.vetapp.utils.Utils.ERROR_REQUEST;
import static com.example.vetapp.utils.Utils.GET_DRUG_REQUESTS;
import static com.example.vetapp.utils.Utils.NETWORK_ERROR;
import static com.example.vetapp.utils.Utils.TAG;

public class DrugRequestTableFragment extends Fragment {

    private RecyclerView recyclerView;
    private DrugRequestTableAdapter adapter;
    private QueueSingleton mRequestQueue;
    private List<DrugRequest> drList;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_drug_request_table, container, false);
        context = inflater.getContext();

        recyclerView = v.findViewById(R.id.rw_drug_request);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setHasFixedSize(true);

        drList = new LinkedList<>();

        mRequestQueue = QueueSingleton.getInstance(v.getContext());
        getRequests();
        Button add = v.findViewById(R.id.add_request);

        add.setOnClickListener(clickAdd -> {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            AddRequestDialogFragment addFragment = new AddRequestDialogFragment();
            addFragment.setTargetFragment(this, 34);
            addFragment.show(fm, TAG);
        });
        return v;
    }


    private void getRequests(){
        JsonArrayRequest request = new JsonArrayRequest(
                GET_DRUG_REQUESTS,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            String requesting_worker_id = response.getJSONObject(i)
                                    .getString("requesting_worker_id");
                            String drug_id = response.getJSONObject(i).getString("drug_id");
                            String amount = response.getJSONObject(i).getString("amount");
                            String date_of_request = response.getJSONObject(i)
                                    .getString("date_of_request");
                            String date_of_supply = response.getJSONObject(i)
                                    .getString("date_of_supply");
                            drList.add(new DrugRequest(requesting_worker_id, drug_id,
                                    amount, date_of_request, date_of_supply));
                        }
                        adapter = new DrugRequestTableAdapter(drList, drList.size());
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        Toast.makeText(context, ERROR_REQUEST, Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(context, NETWORK_ERROR, Toast.LENGTH_LONG).show()
        );
        mRequestQueue.addToRequestQueue(request);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 34) {
            String requesting_worker_id = data.getStringExtra(KEY1);
            String drug_id = data.getStringExtra(KEY2);
            String amount = data.getStringExtra(KEY3);
            String date_of_request = data.getStringExtra(KEY4);
            String date_of_supply = data.getStringExtra(KEY5);
            drList.add(new DrugRequest(requesting_worker_id, drug_id,
                    amount, date_of_request, date_of_supply));
            adapter = new DrugRequestTableAdapter(drList, drList.size());
            recyclerView.setAdapter(adapter);
        }
    }

}
