package com.example.vetapp.dialog_fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.toolbox.JsonArrayRequest;
import com.example.vetapp.R;
import com.example.vetapp.utils.QueueSingleton;

import org.json.JSONException;

import java.util.ArrayList;

import static com.example.vetapp.utils.Utils.*;

public class ChooseSpecialistDialogFragment extends DialogFragment {

    private QueueSingleton mRequestQueue;
    private Context context;
    private int count;
    private ArrayList<String> data, dataId;
    private int specialist_position;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.choose_specialist_dialog_fragment,
                container, false);
        context = inflater.getContext();

        mRequestQueue = QueueSingleton.getInstance(v.getContext());

        String pet = getArguments().getString(PET);
        String petId = getArguments().getString(PET_ID);
        TextView p = v.findViewById(R.id.pet);
        p.setText(pet);

        data = new ArrayList<>();
        dataId = new ArrayList<>();

        getData(() -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, data);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            Spinner spinner = v.findViewById(R.id.spinner_specialist);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    specialist_position = position;
                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });

            Button b = v.findViewById(R.id.add_specialist);

            b.setOnClickListener(v1 -> {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                ChooseDateDialogFragment chooseFragment = new ChooseDateDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString(PET, pet);
                bundle.putString(PET_ID, petId);
                bundle.putString(SPECIALIST, data.get(specialist_position));
                bundle.putString(SPECIALIST_ID, dataId.get(specialist_position));
                chooseFragment.setArguments(bundle);
                chooseFragment.setTargetFragment(this, 20);
                chooseFragment.show(fm, TAG);
                dismiss();
            });

        });
        return v;
    }


    public void getData(final AddRequestDialogFragment.VolleyCallback callback) {
        JsonArrayRequest request = new JsonArrayRequest(
                GET_WORKERS,
                response -> {
                    try {
                        count = response.length();
                        for (int i = 0; i < count; i++) {
                            String id = response.getJSONObject(i).getString("id");
                            String worker_name = response.getJSONObject(i).getString("full_name");
                            String worker_position = response.getJSONObject(i).getString("worker_position");
                            data.add(worker_position + SPACE + worker_name);
                            dataId.add(id);
                        }
                        callback.onSuccess();
                    } catch (JSONException e) {
                        Toast.makeText(context, ERROR_REQUEST, Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(context, NETWORK_ERROR, Toast.LENGTH_LONG).show()
        );
        mRequestQueue.addToRequestQueue(request);
    }
}