package com.example.vetapp.dialog_fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.vetapp.R;
import com.example.vetapp.utils.QueueSingleton;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.vetapp.utils.Utils.*;

public class ChangePhoneDialogFragment extends DialogFragment {

    private final String KEY = "key";
    private final String PARAM_USERNAME = "username";
    private final String PARAM_PHONE = "phone_";

    private QueueSingleton mRequestQueue;
    private Context context;
    private String received_phone;
    private List<String> phList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.change_phone_dialog_fragment,
                container, false);
        context = inflater.getContext();

        mRequestQueue = QueueSingleton.getInstance(v.getContext());

        Button enter_phone = v.findViewById(R.id.enter_phone);
        EditText new_phone = v.findViewById(R.id.edit_phone);

        phList = new ArrayList<>();

        enter_phone.setOnClickListener(v1 -> {
            received_phone = new_phone.getText().toString();
            Pattern pattern = Pattern.compile(PHONE_PATTERN);
            Matcher matcher = pattern.matcher(received_phone);
            if (matcher.find()) {
                getPhones(() -> {
                    if (phList.contains(received_phone)) {
                        Toast.makeText(context, ENTER_PHONE_ERROR, Toast.LENGTH_LONG).show();
                    } else {
                        changePhone();
                        sendResult();
                        dismiss();
                        Toast.makeText(context, CHANGE_PHONE, Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(context, UNCORRECTED_PHONE, Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }

    private void getPhones(final AddRequestDialogFragment.VolleyCallback callback){
        JsonArrayRequest request = new JsonArrayRequest(
                GET_WORKERS_PHONES,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            phList.add(response.getJSONObject(i).getString("phone"));
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

    private void changePhone(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                PUT_PHONE,
                response -> {
                },
                error -> Toast.makeText(context, NETWORK_ERROR, Toast.LENGTH_LONG).show()

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(PARAM_USERNAME, mSettings.getString(APP_PREFERENCES_USERNAME, EMPTY));
                params.put(PARAM_PHONE, received_phone);
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

    private void sendResult() {
        Intent intent = new Intent();
        intent.putExtra(KEY, received_phone);
        getTargetFragment().onActivityResult(
                getTargetRequestCode(), PHONE_RESULT_CODE, intent);
    }

}
