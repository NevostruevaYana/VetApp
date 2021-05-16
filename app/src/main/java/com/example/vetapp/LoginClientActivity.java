package com.example.vetapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.vetapp.dialog_fragments.AddRequestDialogFragment;
import com.example.vetapp.utils.QueueSingleton;
import org.json.JSONException;
import static com.example.vetapp.utils.Utils.*;

public class LoginClientActivity extends AppCompatActivity {

    private EditText personalCode;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        context = this;

        Button buttonLoginClient = findViewById(R.id.button_login_client);
        Button buttonRegisterClient = findViewById(R.id.button_register_client);
        Button buttonToWorker = findViewById(R.id.to_worker_login);

        personalCode = findViewById(R.id.client_personal_code);

        buttonLoginClient.setOnClickListener(v -> {
            String clCode = personalCode.getText().toString();
            loginClient(clCode, () -> {
                Intent intent = new Intent(context, PersonalClientAccountActivity.class);
                startActivity(intent);
            });
        });
        buttonRegisterClient.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterClientActivity.class);
            startActivity(intent);
        });
        buttonToWorker.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void loginClient(String clCode, final AddRequestDialogFragment.VolleyCallback volleyCallback) {
        JsonArrayRequest request = new JsonArrayRequest(
                String.format(CHECK_CLIENT_EXISTS, clCode),
                response -> {
                    try {
                        String id = response.getJSONObject(0).getString("id");
                        editSharedPreferences(id);
                        volleyCallback.onSuccess();
                    } catch (JSONException e) {
                        Toast.makeText(this, LOGIN_CLIENT_ERROR,
                                Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, NETWORK_ERROR, Toast.LENGTH_LONG).show()
        );
        QueueSingleton.getInstance(this).addToRequestQueue(request);
    }

    private void editSharedPreferences(String put) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_CLIENT, put);
        editor.apply();
    }

}