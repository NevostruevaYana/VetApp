package com.example.vetapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.toolbox.JsonArrayRequest;
import com.example.vetapp.adapters.ScheduleTableAdapter;
import com.example.vetapp.database.Schedule;
import com.example.vetapp.utils.QueueSingleton;

import org.json.JSONException;

import java.util.LinkedList;
import java.util.List;

import static com.example.vetapp.utils.Utils.APP_PREFERENCES_CLIENT;
import static com.example.vetapp.utils.Utils.EMPTY;
import static com.example.vetapp.utils.Utils.ERROR_REQUEST;
import static com.example.vetapp.utils.Utils.GET_SCHEDULE_BY_CLIENT_ID;
import static com.example.vetapp.utils.Utils.GET_UPCOMING_VISITS;
import static com.example.vetapp.utils.Utils.NETWORK_ERROR;
import static com.example.vetapp.utils.Utils.SPACE;
import static com.example.vetapp.utils.Utils.T_SPLIT;
import static com.example.vetapp.utils.Utils.formatDateForUI;
import static com.example.vetapp.utils.Utils.mSettings;

public class ClientScheduleActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ScheduleTableAdapter adapter;
    QueueSingleton mRequestQueue;
    List<Schedule> schList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_schedule);

        recyclerView = findViewById(R.id.rw_client_schedule);

        Button showOnlyUpcoming = findViewById(R.id.show_only_upcoming);
        Button showAll = findViewById(R.id.show_all);
        Button back = findViewById(R.id.back_to_profile);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, PersonalClientAccountActivity.class);
            startActivity(intent);
        });
        showOnlyUpcoming.setOnClickListener(v -> {
            schList.clear();
            parseJson2();
        });
        showAll.setOnClickListener(v -> {
            schList.clear();
            parseJson();
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setHasFixedSize(true);

        schList = new LinkedList<>();

        mRequestQueue = QueueSingleton.getInstance(this);
        parseJson();
    }

    private void parseJson(){

        JsonArrayRequest request = new JsonArrayRequest(
                String.format(GET_SCHEDULE_BY_CLIENT_ID,
                        mSettings.getString(APP_PREFERENCES_CLIENT, EMPTY)),
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            String pet_id = response.getJSONObject(i).getString("pet_id");
                            String client_id = response.getJSONObject(i).getString("client_id");
                            String office_id = response.getJSONObject(i).getString("office_id");
                            String worker_id = response.getJSONObject(i).getString("worker_id");
                            String service_id = response.getJSONObject(i).getString("service_id");
                            String reason = response.getJSONObject(i).getString("reason");
                            String date_and_time_of_visit = response.getJSONObject(i)
                                    .getString("date_and_time_of_visit");

                            String[] splitDateAndTime = date_and_time_of_visit.split(T_SPLIT);
                            String date = formatDateForUI(splitDateAndTime[0]);
                            String time = splitDateAndTime[1];
                            schList.add(new Schedule(pet_id, client_id, office_id, worker_id,
                                    service_id, reason, time + SPACE + date));
                        }
                        adapter = new ScheduleTableAdapter(schList, schList.size());
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        Toast.makeText(this, ERROR_REQUEST, Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, NETWORK_ERROR, Toast.LENGTH_LONG).show()

        );
        mRequestQueue.addToRequestQueue(request);
    }

    private void parseJson2(){

        JsonArrayRequest request = new JsonArrayRequest(
                String.format(GET_UPCOMING_VISITS,
                        mSettings.getString(APP_PREFERENCES_CLIENT, EMPTY)),
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            String pet_id = response.getJSONObject(i).getString("pet_id_");
                            String client_id = response.getJSONObject(i).getString("client_id_");
                            String office_id = response.getJSONObject(i).getString("office_id_");
                            String worker_id = response.getJSONObject(i).getString("worker_id_");
                            String service_id = response.getJSONObject(i).getString("service_id_");
                            String reason = response.getJSONObject(i).getString("reason_");
                            String date_and_time_of_visit = response.getJSONObject(i)
                                    .getString("date_and_time_of_visit_");

                            String[] splitDateAndTime = date_and_time_of_visit.split(T_SPLIT);
                            String date = formatDateForUI(splitDateAndTime[0]);
                            String time = splitDateAndTime[1];
                            schList.add(new Schedule(pet_id, client_id, office_id, worker_id,
                                    service_id, reason, time + " " + date));
                        }
                        adapter = new ScheduleTableAdapter(schList, schList.size());
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, ERROR_REQUEST,
                        Toast.LENGTH_LONG).show()

        );
        mRequestQueue.addToRequestQueue(request);
    }

}