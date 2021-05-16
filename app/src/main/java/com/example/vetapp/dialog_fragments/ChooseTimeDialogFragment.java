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

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import static com.example.vetapp.utils.Utils.*;

public class ChooseTimeDialogFragment extends DialogFragment {

    private QueueSingleton mRequestQueue;
    private Context context;
    private List<String> time = new ArrayList<>();
    private List<String> unavailableTime = new ArrayList<>();
    private int data_position;
    private String date_, specialistId;
    private List<String> ner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.choose_time_dialog_fragment,
                container, false);
        context = inflater.getContext();

        mRequestQueue = QueueSingleton.getInstance(v.getContext());

        String pet = getArguments().getString(PET);
        String petId = getArguments().getString(PET_ID);
        TextView p = v.findViewById(R.id.selected_pet);
        p.setText(pet);

        String specialist = getArguments().getString(SPECIALIST);
        specialistId = getArguments().getString(SPECIALIST_ID);
        TextView s = v.findViewById(R.id.selected_specialist);
        s.setText(specialist);

        date_ = getArguments().getString(DATE);
        TextView d = v.findViewById(R.id.selected_date);
        d.setText(date_);

        getTimes();

        getUnavailableTime(() -> {
            ner = getAvailableTimes(unavailableTime);
            if (ner.isEmpty()) {
                ner.add(FULL_APPOINTMENT);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                    android.R.layout.simple_spinner_item, ner);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            Spinner spinner = v.findViewById(R.id.spinner_time);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    data_position = position;
                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
        });

        Button b = v.findViewById(R.id.add_time);

        b.setOnClickListener(v1 -> {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            EnterReasonDialogFragment chooseFragment = new EnterReasonDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString(PET, pet);
            bundle.putString(PET_ID, petId);
            bundle.putString(SPECIALIST, specialist);
            bundle.putString(SPECIALIST_ID, specialistId);
            bundle.putString(DATE, date_);
            bundle.putString(TIME, ner.get(data_position));
            chooseFragment.setArguments(bundle);
            chooseFragment.setTargetFragment(this, 40);
            chooseFragment.show(fm, TAG);
            dismiss();
        });
        return v;
    }

    private void getTimes() {
        time = Arrays.asList("10:00:00", "11:00:00", "12:00:00", "13:00:00",
                "14:00:00", "15:00:00", "16:00:00", "17:00:00", "18:00:00",
                "19:00:00", "20:00:00", "21:00:00");
        String time_ = getArguments().getString(TIME);
        if (!time_.equals(ANY)) {
            int hour = Integer.parseInt(time_.split(COLON_SPLIT)[0]);
            int index = 0;
            int check = 10;
            while (check < 22) {
                if (hour < check) {
                    break;
                }
                check++;
                index++;
            }
            time = time.subList(index, 12);
        }
    }

    private List<String> getAvailableTimes(List<String> list) {
        List<String> res =  new ArrayList<>();

        time.forEach(it -> {
            if (!list.contains(it)) {
                res.add(it);
            } else
                System.out.println(it);
        });

        return res;
    }

    private void getUnavailableTime(final AddRequestDialogFragment.VolleyCallback callback){

        JsonArrayRequest request = new JsonArrayRequest(
                String.format(GET_UNAVAILABLE_TIME, specialistId, getRequestStringWithData(date_)),
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            String time = response.getJSONObject(i).getString("date_and_time_of_visit");
                            unavailableTime.add(time.split(T_SPLIT)[1]);
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

    private String getRequestStringWithData(String date_) {
        String date = formatDateForDB(date_);
        return String.format("(%sT10:00:00,%sT11:00:00,%sT12:00:00,%sT13:00:00,%sT14:00:00," +
                        "%sT15:00:00,%sT16:00:00,%sT17:00:00,%sT18:00:00,%sT19:00:00,%sT20:00:00,%sT21:00:00)",
                date, date, date, date, date, date, date, date, date, date, date, date);
    }

}