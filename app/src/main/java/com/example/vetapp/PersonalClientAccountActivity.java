package com.example.vetapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonArrayRequest;
import com.example.vetapp.adapters.PetTableAdapter;
import com.example.vetapp.database.Pet;
import com.example.vetapp.dialog_fragments.AddPetDialogFragment;
import com.example.vetapp.dialog_fragments.ChoosePetDialogFragment;
import com.example.vetapp.utils.QueueSingleton;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.example.vetapp.utils.Utils.*;

public class PersonalClientAccountActivity extends AppCompatActivity {

    private TextView fullName, address, phone, regular;
    private RecyclerView recyclerView;
    private PetTableAdapter adapter;
    private QueueSingleton mRequestQueue;
    private List<Pet> pList;
    private ArrayList<String> pId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_client_account);

        fullName = findViewById(R.id.full_name_client);
        address = findViewById(R.id.address_client);
        phone = findViewById(R.id.phone_client);
        regular = findViewById(R.id.regular_client);

        Button addPet = findViewById(R.id.add_pet);
        Button checkVisits = findViewById(R.id.check_all_my_visits);
        Button makeAnAppointment = findViewById(R.id.make_an_appointment);

        recyclerView = findViewById(R.id.rw_client_pets);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setHasFixedSize(true);

        mRequestQueue = QueueSingleton.getInstance(this);
        pList = new ArrayList<>();
        pId = new ArrayList<>();

        createInformation();
        parseJson();

        addPet.setOnClickListener(v -> {
            AddPetDialogFragment addFragment = new AddPetDialogFragment();
            addFragment.show(getSupportFragmentManager(), TAG);
        });
        checkVisits.setOnClickListener(v -> {
            Intent intent = new Intent(this, ClientScheduleActivity.class);
            startActivity(intent);
        });
        makeAnAppointment.setOnClickListener(v -> {
            ChoosePetDialogFragment chooseFragment = new ChoosePetDialogFragment();
            Bundle bundle = new Bundle();
            ArrayList<String> arg = new ArrayList<>();
            for (Pet pet : pList) {
                arg.add(pet.animal + SPACE + pet.name);
            }
            bundle.putStringArrayList(PETS, arg);
            bundle.putStringArrayList(PETS_ID, pId);
            chooseFragment.setArguments(bundle);
            chooseFragment.show(getSupportFragmentManager(), TAG);
        });
    }

    private void createInformation() {
        JsonArrayRequest request = new JsonArrayRequest(
                String.format(CREATE_CLIENT_INFO,
                    mSettings.getString(APP_PREFERENCES_CLIENT, EMPTY)),
                response -> {
                    try {
                        fullName.setText(response.getJSONObject(0).getString("full_name"));
                        address.setText(response.getJSONObject(0).getString("address"));
                        phone.setText(response.getJSONObject(0).getString("phone"));
                        regular.setText(response.getJSONObject(0).getString("regular"));
                    } catch (JSONException e) {
                        Toast.makeText(this, ERROR_REQUEST,
                                Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, NETWORK_ERROR, Toast.LENGTH_LONG).show()
        );
        mRequestQueue.addToRequestQueue(request);
    }

    private void parseJson(){
        JsonArrayRequest request = new JsonArrayRequest(
                String.format(CREATE_PET_INFO,
                        mSettings.getString(APP_PREFERENCES_CLIENT, EMPTY)),
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            String id = response.getJSONObject(i).getString("id");
                            String client_id = response.getJSONObject(i).getString("client_id");
                            String animal = response.getJSONObject(i).getString("animal");
                            String pet_name = response.getJSONObject(i).getString("pet_name");
                            String pet_gender = response.getJSONObject(i).getString("pet_gender");
                            String date_of_birth = response.getJSONObject(i)
                                    .getString("date_of_birth");
                            String weigh = response.getJSONObject(i).getString("weigh");
                            pList.add(new Pet(client_id, animal, pet_name,
                                    pet_gender, date_of_birth, weigh));
                            pId.add(id);
                        }
                        adapter = new PetTableAdapter(pList, pList.size());
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        Toast.makeText(this, ERROR_REQUEST,
                                Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, NETWORK_ERROR, Toast.LENGTH_LONG).show()
        );
        mRequestQueue.addToRequestQueue(request);
    }

}