package com.example.vetapp.dialog_fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.vetapp.R;
import com.example.vetapp.utils.QueueSingleton;
import java.util.HashMap;
import java.util.Map;

import static com.example.vetapp.utils.Utils.*;

public class AddVisitDialogFragment extends DialogFragment {

    private final String PARAM_PET_ID = "pet_id_";
    private final String PARAM_CLIENT_ID = "client_id_";
    private final String PARAM_WORKER_ID = "worker_id_";
    private final String PARAM_SERVICE_ID = "service_id_";
    private final String PARAM_REASON = "reason_";
    private final String PARAM_VISIT = "date_and_time_of_visit_";

    private QueueSingleton mRequestQueue;
    private Context context;
    private String date_, pet, specialistId, time, reason, petId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_visit_dialog_fragment, container, false);
        context = inflater.getContext();

        mRequestQueue = QueueSingleton.getInstance(v.getContext());

        pet = getArguments().getString(PET);
        petId = getArguments().getString(PET_ID);
        TextView p = v.findViewById(R.id.selected_pet);
        p.setText(pet);

        String specialist = getArguments().getString(SPECIALIST);
        specialistId = getArguments().getString(SPECIALIST_ID);
        TextView s = v.findViewById(R.id.selected_specialist);
        s.setText(specialist);

        date_ = getArguments().getString(DATE);
        TextView d = v.findViewById(R.id.selected_date);
        d.setText(date_);

        time = getArguments().getString(TIME);
        TextView t = v.findViewById(R.id.selected_time);
        t.setText(time);

        reason = getArguments().getString(REASON);
        TextView r = v.findViewById(R.id.my_reason);
        r.setText(reason);

        Button b = v.findViewById(R.id.add_visit);

        b.setOnClickListener(v1 -> {
            addVisit();
            dismiss();
        });
        return v;
    }

    private void addVisit(){

        StringRequest request = new StringRequest(
                Request.Method.POST,
                PUT_VISIT,
                response -> {
                    System.out.println(response);
                    if (response.substring(1,3).equals(FUNCTION_RETURN)) {
                        Toast.makeText(context, ADD_VISIT,
                                Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(context, NETWORK_ERROR, Toast.LENGTH_LONG).show()

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(PARAM_PET_ID, petId);
                params.put(PARAM_CLIENT_ID, mSettings.getString(APP_PREFERENCES_CLIENT, EMPTY));
                params.put(PARAM_WORKER_ID, specialistId);
                params.put(PARAM_SERVICE_ID, "5");
                String r = reason;
                if (reason.equals(EMPTY)) {
                    r = DEFAULT_PARAM;
                }
                params.put(PARAM_REASON, r);
                params.put(PARAM_VISIT, date_ + SPACE + time);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(HEADER_PARAM_1, HEADER_PARAM_2);
                return params;
            }

        } ;
        mRequestQueue.addToRequestQueue(request);
    }

}