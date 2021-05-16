package com.example.vetapp.dialog_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import com.example.vetapp.R;
import static com.example.vetapp.utils.Utils.*;

public class EnterReasonDialogFragment extends DialogFragment {

    private String date_, specialistId, time;
    private EditText reason;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.enter_reason_dialog_fragment, container, false);

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

        time = getArguments().getString(TIME);
        TextView t = v.findViewById(R.id.selected_time);
        t.setText(time);

        reason = v.findViewById(R.id.reason_et);

        Button b = v.findViewById(R.id.go_to_parameters);

        b.setOnClickListener(v1 -> {
            String gettingReason = reason.getText().toString();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            AddVisitDialogFragment chooseFragment = new AddVisitDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString(PET, pet);
            bundle.putString(PET_ID, petId);
            bundle.putString(SPECIALIST, specialist);
            bundle.putString(SPECIALIST_ID, specialistId);
            bundle.putString(DATE, date_);
            bundle.putString(TIME, time);
            bundle.putString(REASON, gettingReason);
            chooseFragment.setArguments(bundle);
            chooseFragment.setTargetFragment(this, 30);
            chooseFragment.show(fm, TAG);
            dismiss();
        });
        return v;
    }

}