package com.example.vetapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.vetapp.dialog_fragments.AddRequestDialogFragment;
import com.example.vetapp.utils.QueueSingleton;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.vetapp.utils.Utils.ALPHABET;
import static com.example.vetapp.utils.Utils.APP_PREFERENCES_CLIENT;
import static com.example.vetapp.utils.Utils.CHECK_CLIENT_EXISTS;
import static com.example.vetapp.utils.Utils.CL;
import static com.example.vetapp.utils.Utils.DATE_ERROR;
import static com.example.vetapp.utils.Utils.DATE_FORMAT;
import static com.example.vetapp.utils.Utils.DOT_SPLIT;
import static com.example.vetapp.utils.Utils.EMPTY;
import static com.example.vetapp.utils.Utils.EMPTY_WARNING;
import static com.example.vetapp.utils.Utils.ERROR_REQUEST;
import static com.example.vetapp.utils.Utils.FEMALE;
import static com.example.vetapp.utils.Utils.GENDER_WARNING;
import static com.example.vetapp.utils.Utils.HEADER_PARAM_1;
import static com.example.vetapp.utils.Utils.HEADER_PARAM_2;
import static com.example.vetapp.utils.Utils.LOGIN_CLIENT_ERROR;
import static com.example.vetapp.utils.Utils.MALE;
import static com.example.vetapp.utils.Utils.NETWORK_ERROR;
import static com.example.vetapp.utils.Utils.PHONE_PATTERN;
import static com.example.vetapp.utils.Utils.PUT_REQUEST_CLIENT_PET;
import static com.example.vetapp.utils.Utils.UNCORRECTED_PHONE;
import static com.example.vetapp.utils.Utils.WEIGHT_PATTERN;
import static com.example.vetapp.utils.Utils.WEIGHT_WARNING;
import static com.example.vetapp.utils.Utils.formatDateForDB;
import static com.example.vetapp.utils.Utils.generatePersonalCode;
import static com.example.vetapp.utils.Utils.mSettings;

public class RegisterClientActivity extends AppCompatActivity {

    private EditText clName, clAddress, clPhone, petAnimal,
            petName, petGender, petDateOfBirth, petWeight;
    private QueueSingleton mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);

        mRequestQueue = QueueSingleton.getInstance(this);

        Button register = findViewById(R.id.button_register_client_2);
        clName = findViewById(R.id.reg_full_name);
        clAddress = findViewById(R.id.reg_address);
        clPhone = findViewById(R.id.reg_phone);
        petAnimal = findViewById(R.id.reg_animal);
        petName = findViewById(R.id.reg_pet_name);
        petGender = findViewById(R.id.reg_gender);
        petDateOfBirth = findViewById(R.id.reg_date_of_birth_pet);
        petWeight = findViewById(R.id.reg_weight);

        register.setOnClickListener(v -> {
            String fullName = clName.getText().toString();
            String address = clAddress.getText().toString();
            String phone = clPhone.getText().toString();
            String animal = petAnimal.getText().toString();
            String name = petName.getText().toString();
            String gender = petGender.getText().toString();
            String dateOfBirth = petDateOfBirth.getText().toString();
            String weight = petWeight.getText().toString();;

            Pattern pattern = Pattern.compile(PHONE_PATTERN);
            Matcher matcher = pattern.matcher(phone);

            Pattern pattern2 = Pattern.compile(WEIGHT_PATTERN);
            Matcher matcher2 = pattern2.matcher(weight);

            String code = CL + generatePersonalCode(8, ALPHABET);

            if (fullName.equals(EMPTY) || address.equals(EMPTY) || phone.equals(EMPTY)||
                    animal.equals(EMPTY) || name.equals(EMPTY) || gender.equals(EMPTY) ||
                   dateOfBirth.equals(EMPTY) || weight.equals(EMPTY)) {
                Toast.makeText(this, EMPTY_WARNING,
                        Toast.LENGTH_LONG).show();
            } else {
                if (!matcher.find()) {
                    Toast.makeText(this, UNCORRECTED_PHONE,
                            Toast.LENGTH_LONG).show();
                } else {
                    if (!gender.equals(MALE) && !gender.equals(FEMALE)) {
                        Toast.makeText(this, GENDER_WARNING,
                                Toast.LENGTH_LONG).show();
                    } else {
                        if (isDateValid(dateOfBirth)) {
                            Toast.makeText(this, DATE_ERROR,
                                    Toast.LENGTH_LONG).show();
                        } else {
                            if (!matcher2.find()) {
                                Toast.makeText(this, WEIGHT_WARNING,
                                        Toast.LENGTH_LONG).show();
                            } else {
                                addRequest(fullName, address, phone, animal, name, gender,
                                        formatDateForDB(dateOfBirth), weight, code,
                                        () -> {
                                            findId(code, () -> {
                                                Intent intent = new Intent(this,
                                                        PersonalClientAccountActivity.class);
                                                startActivity(intent);
                                            });
                                        });
                            }
                        }
                    }

                }
            }
        });
    }

    public static boolean isDateValid(String date) {
        SimpleDateFormat myFormat = new SimpleDateFormat(DATE_FORMAT);
        myFormat.setLenient(false);
        try {
            myFormat.parse(date);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    private void addRequest(String fullName, String address, String phone, String animal,
                            String name, String gender, String dateOfBirth, String weight,
                            String code, final AddRequestDialogFragment.VolleyCallback callback){

        StringRequest request = new StringRequest(
                Request.Method.POST,
                PUT_REQUEST_CLIENT_PET,
                response -> {
                    callback.onSuccess();
                },
                error -> Toast.makeText(this, NETWORK_ERROR, Toast.LENGTH_LONG).show()

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("full_name_", fullName);
                params.put("address_", address);
                params.put("phone_", phone);
                params.put("personal_code_", code);
                params.put("animal_", animal);
                params.put("name_", name);
                params.put("gender_", gender);
                params.put("date_of_birth_", dateOfBirth);
                params.put("weight_", weight);
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

    private void findId(String code, final AddRequestDialogFragment.VolleyCallback callback) {
        JsonArrayRequest request = new JsonArrayRequest(
                String.format(CHECK_CLIENT_EXISTS, code),
                response -> {
                    try {
                        String id = response.getJSONObject(0).getString("id");
                        editSharedPreferences(APP_PREFERENCES_CLIENT, id);
                        callback.onSuccess();
                    } catch (JSONException e) {
                        Toast.makeText(this, LOGIN_CLIENT_ERROR,
                                Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, NETWORK_ERROR, Toast.LENGTH_LONG).show()
        );
        QueueSingleton.getInstance(this).addToRequestQueue(request);
    }

    private void editSharedPreferences(String preferencesName, String put) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(preferencesName, put);
        editor.apply();
    }
}