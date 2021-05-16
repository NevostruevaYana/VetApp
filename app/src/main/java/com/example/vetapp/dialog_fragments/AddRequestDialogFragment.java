package com.example.vetapp.dialog_fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.vetapp.utils.Utils.*;

public class AddRequestDialogFragment extends DialogFragment {

    public static final String KEY1 = "key1";
    public static final String KEY2 = "key2";
    public static final String KEY3 = "key3";
    public static final String KEY4 = "key4";
    public static final String KEY5 = "key5";

    private final String PARAM_USERNAME = "username";
    private final String PARAM_DRUG_ID = "drugname";
    private final String PARAM_AMOUNT = "amnt";
    private final String PARAM_D_REQUEST = "d_req";
    private final String PARAM_D_SUPPLY = "d_sup";

    private QueueSingleton mRequestQueue;
    private Context context;
    private int count;
    private final List<String> data = new ArrayList<>();
    private int drug_position = 0;
    private EditText add_amount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_request_fragment, container, false);
        context = inflater.getContext();

        mRequestQueue = QueueSingleton.getInstance(v.getContext());
        add_amount = v.findViewById(R.id.add_amount);

        getData(() -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                    android.R.layout.simple_spinner_item, data);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            Spinner spinner = v.findViewById(R.id.spinner);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    drug_position = position;
                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });

            Date currentDate = new Date();
            DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            String dateText = dateFormat.format(currentDate);
            String requestDate = formatDateForDB(dateText);

            Calendar instance = Calendar.getInstance();
            instance.setTime(currentDate);
            instance.add(Calendar.DAY_OF_MONTH, 14);
            Date newDate = instance.getTime();
            String dateText2 = dateFormat.format(newDate);
            String supplyDate = formatDateForDB(dateText2);

            Button b = v.findViewById(R.id.add_r);

            b.setOnClickListener(v1 -> {
                    String amount = add_amount.getText().toString();
                    if ((amount.isEmpty()) || (Integer.parseInt(amount) == 0)) {
                        Toast.makeText(context, AMOUNT_WARNING,
                                Toast.LENGTH_LONG).show();
                    } else {
                        addRequest(mSettings.getString(APP_PREFERENCES_USERNAME, EMPTY),
                                data.get(drug_position), amount, requestDate, supplyDate);
                        sendResult(mSettings.getString(APP_PREFERENCES_ID, EMPTY),
                                String.valueOf(drug_position + 1), amount, requestDate, supplyDate);
                        dismiss();
                    }
            });

        });
        return v;
    }

    public interface VolleyCallback {
        void onSuccess();
    }

    public void getData(final VolleyCallback callback) {
        JsonArrayRequest request = new JsonArrayRequest(
                GET_DRUG,
                response -> {
                    try {
                        count = response.length();
                        for (int i = 0; i < count; i++) {
                            String drug_name = response.getJSONObject(i)
                                    .getString("drug_name");
                            data.add(drug_name);
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

    private void addRequest(String login, String drug_id, String amount,
                            String d_request, String d_supply){

        StringRequest request = new StringRequest(
                Request.Method.POST,
                PUT_REQUEST,
                response -> {
                    System.out.println(response);
                    if (response.substring(1,3).equals(FUNCTION_RETURN)) {
                        Toast.makeText(context, ADD_DRUG_REQUEST,
                                Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(context, NETWORK_ERROR, Toast.LENGTH_LONG).show()

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(PARAM_USERNAME, login);
                params.put(PARAM_DRUG_ID, drug_id);
                params.put(PARAM_AMOUNT, amount);
                params.put(PARAM_D_REQUEST, d_request);
                params.put(PARAM_D_SUPPLY, d_supply);
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

    private void sendResult(String workerId, String drugId,
                            String amount, String requestDate,
                            String supplyDate) {
        Intent intent = new Intent();
        intent.putExtra(KEY1, workerId);
        intent.putExtra(KEY2, drugId);
        intent.putExtra(KEY3, amount);
        intent.putExtra(KEY4, requestDate);
        intent.putExtra(KEY5, supplyDate);
        getTargetFragment().onActivityResult(
                getTargetRequestCode(), 34, intent);
    }
}