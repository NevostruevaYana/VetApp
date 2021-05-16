package com.example.vetapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vetapp.R;
import com.example.vetapp.adapters.DrugRequestTableAdapter;
import com.example.vetapp.adapters.PetTableAdapter;
import com.example.vetapp.adapters.ScheduleTableAdapter;
import com.example.vetapp.adapters.WorkerTableAdapter;
import com.example.vetapp.database.DrugRequest;
import com.example.vetapp.database.Pet;
import com.example.vetapp.database.Schedule;
import com.example.vetapp.database.Worker;
import com.example.vetapp.dialog_fragments.AddRequestDialogFragment;
import com.example.vetapp.utils.QueueSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.example.vetapp.utils.Utils.APP_PREFERENCES_CACHE_SCH;
import static com.example.vetapp.utils.Utils.APP_PREFERENCES_CLIENT;
import static com.example.vetapp.utils.Utils.EMPTY;
import static com.example.vetapp.utils.Utils.ERROR_REQUEST;
import static com.example.vetapp.utils.Utils.GET_SCHEDULE;
import static com.example.vetapp.utils.Utils.HAVE_A_CACHE;
import static com.example.vetapp.utils.Utils.LOGIN_ERROR;
import static com.example.vetapp.utils.Utils.NETWORK_ERROR;
import static com.example.vetapp.utils.Utils.PUT_PHONE;
import static com.example.vetapp.utils.Utils.PUT_WORKER;
import static com.example.vetapp.utils.Utils.SPACE;
import static com.example.vetapp.utils.Utils.T_SPLIT;
import static com.example.vetapp.utils.Utils.formatDateForUI;
import static com.example.vetapp.utils.Utils.mSettings;

public class ScheduleTableFragment extends Fragment {

    private RecyclerView recyclerView;
    private ScheduleTableAdapter adapter;
    private QueueSingleton mRequestQueue;
    private List<Schedule> schList;
    private Context context;
    private static LruCache<Integer, Schedule> mMemoryCache;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_schedule_table, container, false);
        context = inflater.getContext();

        recyclerView = v.findViewById(R.id.rw_schedule);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setHasFixedSize(true);

        schList = new LinkedList<>();

        final int maxMemorySize = (int) Runtime.getRuntime().maxMemory() / 1024;
        final int cacheSize = maxMemorySize / 10;

        mRequestQueue = QueueSingleton.getInstance(v.getContext());

        schList.clear();

//        if (mSettings.getString(APP_PREFERENCES_CACHE_SCH, EMPTY).equals(HAVE_A_CACHE)) {
//            long start = System.nanoTime();
//            for (int i = 0; i < mMemoryCache.size(); i++) {
//                Schedule s = getScheduleFromMemoryCache(i);
//                schList.add(s);
//            }
//            System.out.println(mMemoryCache.size());
//            adapter = new ScheduleTableAdapter(schList, schList.size());
//            recyclerView.setAdapter(adapter);
//            System.out.println("cache : " + (System.nanoTime() - start));
//        } else {
            long start = System.nanoTime();
            getSchedule(cacheSize, () -> {
                System.out.println("db_request : " + (System.nanoTime() - start));
            });

        return v;
    }

    public static Schedule getScheduleFromMemoryCache(int key) {
        return mMemoryCache.get(key);
    }

    public static void setScheduleFromMemoryCache(int key, Schedule schedule) {
            mMemoryCache.put(key, schedule);
    }

    private void getSchedule(int cacheSize, final AddRequestDialogFragment.VolleyCallback callback){
        JsonArrayRequest request = new JsonArrayRequest(
                GET_SCHEDULE,
                response -> {
                    try {
                        mMemoryCache = new LruCache<Integer, Schedule>(cacheSize);
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
                            schList.add(new Schedule(pet_id, client_id, office_id, worker_id,
                                    service_id, reason, splitDateAndTime[1] +
                                    SPACE + formatDateForUI(splitDateAndTime[0])));

                            setScheduleFromMemoryCache(i, new Schedule(pet_id, client_id, office_id,
                                    worker_id, service_id, reason, date_and_time_of_visit));
                        }
                        System.out.println(mMemoryCache.size());
                        adapter = new ScheduleTableAdapter(schList, schList.size());
                        recyclerView.setAdapter(adapter);
                        SharedPreferences.Editor editor = mSettings.edit();
                        editor.putString(APP_PREFERENCES_CACHE_SCH, HAVE_A_CACHE);
                        editor.apply();
                        callback.onSuccess();
                    } catch (JSONException e) {
                        Toast.makeText(context, ERROR_REQUEST, Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(context, NETWORK_ERROR, Toast.LENGTH_LONG).show()

        );
        mRequestQueue.addToRequestQueue(request);
        }
}
