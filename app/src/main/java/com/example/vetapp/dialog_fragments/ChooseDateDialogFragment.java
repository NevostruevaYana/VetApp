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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.vetapp.R;
import com.example.vetapp.utils.QueueSingleton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ArrayList;
import java.util.List;

import static com.example.vetapp.utils.Utils.*;

public class ChooseDateDialogFragment extends DialogFragment {

    private List<String> data;
    private int data_position;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.choose_date_dialog_fragment, container, false);

        data = new ArrayList<>();

        String pet = getArguments().getString(PET);
        String petId = getArguments().getString(PET_ID);
        TextView p = v.findViewById(R.id.selected_pet);
        p.setText(pet);

        String specialist = getArguments().getString(SPECIALIST);
        String specialistId = getArguments().getString(SPECIALIST_ID);
        TextView s = v.findViewById(R.id.selected_specialist);
        s.setText(specialist);

        addAvailableDates();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(inflater.getContext(),
                android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = v.findViewById(R.id.spinner_data);
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

        Button b = v.findViewById(R.id.add_date);

        b.setOnClickListener(v1 -> {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            ChooseTimeDialogFragment chooseFragment = new ChooseTimeDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString(PET, pet);
            bundle.putString(PET_ID, petId);
            bundle.putString(SPECIALIST, specialist);
            bundle.putString(SPECIALIST_ID, specialistId);
            bundle.putString(DATE, data.get(data_position));
            bundle.putString(TIME, ANY);
            if (data_position == 0) {
                bundle.putString(TIME, getCurrentTime());
            }
            chooseFragment.setArguments(bundle);
            chooseFragment.setTargetFragment(this, 30);
            chooseFragment.show(fm, TAG);
            dismiss();
            });
        return v;
    }

    private void addAvailableDates() {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

        Calendar instance = Calendar.getInstance();
        instance.setTime(currentDate);
        Date newDate = instance.getTime();
        String dateText = dateFormat.format(newDate);

        data.add(dateText);

        for (int i = 0; i < 13; i++) {
            instance.add(Calendar.DAY_OF_MONTH, 1);
            Date newDate2 = instance.getTime();
            data.add(dateFormat.format(newDate2));
        }
    }

    private String getCurrentTime() {
        Date currentDate = new Date();
        DateFormat timeFormat = new SimpleDateFormat(HOUR_FORMAT, Locale.getDefault());
        return timeFormat.format(currentDate);
    }
}