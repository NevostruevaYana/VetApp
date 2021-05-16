package com.example.vetapp.dialog_fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.vetapp.R;
import com.example.vetapp.utils.QueueSingleton;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.vetapp.RegisterClientActivity.isDateValid;
import static com.example.vetapp.utils.Utils.*;

public class AddPetDialogFragment extends DialogFragment {

    private final String PET_PARAM_ID = "id_";
    private final String PET_PARAM_ANIMAL = "animal_";
    private final String PET_PARAM_NAME = "name_";
    private final String PET_PARAM_GENDER = "gender_";
    private final String PET_PARAM_DATE_OF_BIRTH = "date_of_birth_";
    private final String PET_PARAM_WEIGHT = "weight_";

    private QueueSingleton mRequestQueue;
    private Context context;
    private EditText regAnimal, regName, regGender, regBirth, regWeight;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_pet_dialog_fragnemt, container, false);
        context = inflater.getContext();

        mRequestQueue = QueueSingleton.getInstance(v.getContext());

        Button registerPet = v.findViewById(R.id.button_register_pet_dialog);

        regAnimal = v.findViewById(R.id.reg_animal_dialog);
        regName = v.findViewById(R.id.reg_pet_name_dialog);
        regGender = v.findViewById(R.id.reg_gender_dialog);
        regBirth = v.findViewById(R.id.reg_date_of_birth_pet_dialog);
        regWeight = v.findViewById(R.id.reg_weight_dialog);


        registerPet.setOnClickListener(v1 -> {
            String animal = regAnimal.getText().toString();
            String name = regName.getText().toString();
            String gender = regGender.getText().toString();
            String dateOfBirth = regBirth.getText().toString();
            String weight = regWeight.getText().toString();

            Pattern pattern2 = Pattern.compile(WEIGHT_PATTERN);
            Matcher matcher2 = pattern2.matcher(weight);

            if (animal.equals(EMPTY) || name.equals(EMPTY) || gender.equals(EMPTY) ||
                    dateOfBirth.equals(EMPTY) || weight.equals(EMPTY)) {
                Toast.makeText(context, EMPTY_WARNING,
                        Toast.LENGTH_LONG).show();
            } else {
                if (!gender.equals(MALE) && !gender.equals(FEMALE)) {
                    Toast.makeText(context, GENDER_WARNING,
                            Toast.LENGTH_LONG).show();
                } else {
                    if (isDateValid(dateOfBirth)) {
                        Toast.makeText(context, DATE_ERROR,
                                Toast.LENGTH_LONG).show();
                    } else {
                        if (!matcher2.find()) {
                            Toast.makeText(context, WEIGHT_WARNING,
                                    Toast.LENGTH_LONG).show();
                        } else {
                            addPet(animal, name, gender, dateOfBirth, weight);
                            dismiss();
                        }
                    }
                }

            }
        });
        return v;
    }


    private void addPet(String animal, String name, String gender,
                        String dateOfBirth, String weight){

        StringRequest request = new StringRequest(
                Request.Method.POST,
                PUT_PET,
                response -> {
                    if (response.substring(1,3).equals(FUNCTION_RETURN)) {
                        Toast.makeText(context, ADD_PET,
                                Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(context, NETWORK_ERROR, Toast.LENGTH_LONG).show()

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(PET_PARAM_ID, mSettings.getString(APP_PREFERENCES_CLIENT, ""));
                params.put(PET_PARAM_ANIMAL, animal);
                params.put(PET_PARAM_NAME, name);
                params.put(PET_PARAM_GENDER, gender);
                params.put(PET_PARAM_DATE_OF_BIRTH, dateOfBirth);
                params.put(PET_PARAM_WEIGHT, weight);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(HEADER_PARAM_1, HEADER_PARAM_2);
                return params;
            }

        } ;
        mRequestQueue.addToRequestQueue(request);
    }
}
