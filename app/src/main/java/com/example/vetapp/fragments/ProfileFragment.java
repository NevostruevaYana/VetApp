package com.example.vetapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonArrayRequest;
import com.example.vetapp.dialog_fragments.ChangePhoneDialogFragment;
import com.example.vetapp.utils.QueueSingleton;
import com.example.vetapp.R;

import org.json.JSONException;

import static com.example.vetapp.utils.Utils.EMPTY;
import static com.example.vetapp.utils.Utils.ERROR_REQUEST;
import static com.example.vetapp.utils.Utils.GET_WORKER_BY_LOGIN;
import static com.example.vetapp.utils.Utils.HYPHEN;
import static com.example.vetapp.utils.Utils.NETWORK_ERROR;
import static com.example.vetapp.utils.Utils.TAG;
import static com.example.vetapp.utils.Utils.formatDateForUI;
import static com.example.vetapp.utils.Utils.mSettings;
import static com.example.vetapp.utils.Utils.APP_PREFERENCES_USERNAME;

public class ProfileFragment extends Fragment {

    private TextView full_name, position, dateOfBirth, phone;
    private Context context;
    private View v;
    private ChangePhoneDialogFragment cpdFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile, container, false);
        context = inflater.getContext();

        full_name = v.findViewById(R.id.full_name);
        position = v.findViewById(R.id.position);
        dateOfBirth = v.findViewById(R.id.date_of_birth);
        phone = v.findViewById(R.id.phone);

        getInformation(v);
        Button change_password_button = v.findViewById(R.id.change_phone);

        change_password_button.setOnClickListener(v1 -> {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            cpdFragment = new ChangePhoneDialogFragment();
            cpdFragment.setTargetFragment(this, 55);
            cpdFragment.show(fm, TAG);
        });

        return v;
    }

    private View getInformation(View v) {
        JsonArrayRequest request = new JsonArrayRequest(
                String.format(GET_WORKER_BY_LOGIN,
                        mSettings.getString(APP_PREFERENCES_USERNAME, EMPTY)),
                response -> {
                    try {
                        String full_name_ = response.getJSONObject(0).getString("full_name");
                        String position_ = response.getJSONObject(0).getString("worker_position");
                        String dateOfBirth_ = response.getJSONObject(0).getString("date_of_birth");
                        String phone_ = response.getJSONObject(0).getString("phone");
                        full_name.setText(full_name_);
                        position.setText(position_);
                        dateOfBirth.setText(formatDateForUI(dateOfBirth_));
                        phone.setText(phone_);
                    } catch (JSONException e) {
                        Toast.makeText(context, ERROR_REQUEST, Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(context, NETWORK_ERROR, Toast.LENGTH_LONG).show()
        );
        QueueSingleton.getInstance(context).addToRequestQueue(request);

        return v;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 55) {
            String editTextString = data.getStringExtra(
                    "key");
            phone.setText(editTextString);
        }
    }

}