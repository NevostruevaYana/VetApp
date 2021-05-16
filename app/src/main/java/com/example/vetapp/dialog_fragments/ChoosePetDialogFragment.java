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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.vetapp.R;
import java.util.ArrayList;

import static com.example.vetapp.utils.Utils.*;

public class ChoosePetDialogFragment extends DialogFragment {

    private int pet_position;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.choose_pet_dialog_fragment, container, false);

        Button choosePet = v.findViewById(R.id.add_pet_schedule);

        ArrayList<String> list = getArguments().getStringArrayList(PETS);
        ArrayList<String> listId = getArguments().getStringArrayList(PETS_ID);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(inflater.getContext(),
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = v.findViewById(R.id.spinner_pet);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                pet_position = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        choosePet.setOnClickListener(v1 -> {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                ChooseSpecialistDialogFragment chooseFragment = new ChooseSpecialistDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString(PET, list.get(pet_position));
                bundle.putString(PET_ID, listId.get(pet_position));
                chooseFragment.setArguments(bundle);
                chooseFragment.setTargetFragment(this, 10);
                chooseFragment.show(fm, TAG);
                dismiss();
                }
            );
        return v;
    }

}