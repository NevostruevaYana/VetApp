package com.example.vetapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.vetapp.utils.QueueSingleton;
import com.example.vetapp.utils.Utils;

import org.json.JSONException;
import java.util.Base64;

import static com.example.vetapp.utils.Utils.APP_PREFERENCES_CACHE_SCH;
import static com.example.vetapp.utils.Utils.APP_PREFERENCES_ID;
import static com.example.vetapp.utils.Utils.APP_PREFERENCES_USERNAME;
import static com.example.vetapp.utils.Utils.BRACKET;
import static com.example.vetapp.utils.Utils.CHECK_WORKER_EXISTS;
import static com.example.vetapp.utils.Utils.COLON_SPLIT;
import static com.example.vetapp.utils.Utils.DOT_SPLIT;
import static com.example.vetapp.utils.Utils.EMPTY;
import static com.example.vetapp.utils.Utils.ERROR_REQUEST;
import static com.example.vetapp.utils.Utils.GET_TOKEN;
import static com.example.vetapp.utils.Utils.LOGIN_ERROR;
import static com.example.vetapp.utils.Utils.NETWORK_ERROR;
import static com.example.vetapp.utils.Utils.NO_CACHE;
import static com.example.vetapp.utils.Utils.mSettings;

import static com.example.vetapp.utils.Utils.APP_PREFERENCES;
import static com.example.vetapp.utils.Utils.APP_PREFERENCES_TOKEN;

public class LoginActivity extends AppCompatActivity {

    private EditText login, password;
    private QueueSingleton mQueueSingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button iniButton = findViewById(R.id.button_ini);
        Button clientButton = findViewById(R.id.button_client);
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        mQueueSingleton = QueueSingleton.getInstance(this);

        editSharedPreferences(APP_PREFERENCES_CACHE_SCH, NO_CACHE);

        if (mSettings.contains(APP_PREFERENCES_TOKEN)) {
            String expTokenTime = getTokenExpirationTime();
            long time = System.currentTimeMillis() / 1000;
            if (time < Long.parseLong(expTokenTime)) {
                makeAnIntention(PersonalWorkerAccountActivity.class);
            }
        }
        iniButton.setOnClickListener(v -> {
            onClickButtonIni();
        });
        clientButton.setOnClickListener(v -> {
            makeAnIntention(LoginClientActivity.class);
        });
    }

    private void onClickButtonIni() {
        JsonArrayRequest requestToken = new JsonArrayRequest(
                String.format(GET_TOKEN, login.getText().toString()),
                response -> {
                    try {
                        String token = response.getJSONObject(0)
                                .getString(APP_PREFERENCES_TOKEN);
                        editSharedPreferences(APP_PREFERENCES_TOKEN, token);
                    } catch (JSONException e) {
                        Toast.makeText(this, NETWORK_ERROR,
                                Toast.LENGTH_LONG).show();
                    }
                    makeAnIntention(PersonalWorkerAccountActivity.class);
                    },
                error -> Toast.makeText(this, NETWORK_ERROR, Toast.LENGTH_LONG).show()
        );
        JsonArrayRequest request = new JsonArrayRequest(
                String.format(CHECK_WORKER_EXISTS,
                        login.getText().toString()),
                response -> {
                    try {
                        char[] passwordIn = password.getText().toString().toCharArray();
                        String id_ = response.getJSONObject(0).getString("id");
                        editSharedPreferences(APP_PREFERENCES_ID, id_);
                        String salt = response.getJSONObject(0)
                                .getString("salt");
                        String excepted = response.getJSONObject(0)
                                .getString("password");
                        byte[] salt2 = org.postgresql.util.Base64.decode(salt);
                        String actual = org.postgresql.util.Base64
                                .encodeBytes(Utils.getHash(passwordIn, salt2));
                        if (!excepted.equals(actual)) {
                            Toast.makeText(this, LOGIN_ERROR,
                                    Toast.LENGTH_LONG).show();
                        } else {
                            String username = login.getText().toString();
                            editSharedPreferences(APP_PREFERENCES_USERNAME, username);
                            mQueueSingleton.addToRequestQueue(requestToken);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, ERROR_REQUEST,
                                Toast.LENGTH_LONG).show();
                    }
                    },
                error -> Toast.makeText(this, NETWORK_ERROR, Toast.LENGTH_LONG).show()
        );
        mQueueSingleton.addToRequestQueue(request);
    }

    private void editSharedPreferences(String preferencesName, String put) {
        Editor editor = mSettings.edit();
        editor.putString(preferencesName, put);
        editor.apply();
    }

    private String getTokenExpirationTime() {
        String token = mSettings.getString(APP_PREFERENCES_TOKEN, EMPTY);
        Base64.Decoder decoder = Base64.getDecoder();
        String usernameAndExp = new String(decoder.decode(token.split(DOT_SPLIT)[1]));
        return usernameAndExp.replace(BRACKET, EMPTY).split(COLON_SPLIT)[2];
    }

    private void makeAnIntention(Class class_) {
        Intent intent = new Intent(this, class_);
        startActivity(intent);
    }
}