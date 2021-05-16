package com.example.vetapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vetapp.R;
import com.example.vetapp.database.Pet;

import java.util.List;

public class PetTableAdapter extends RecyclerView.Adapter<PetTableAdapter.PetVewHolder> {

    private final int numberItems;
    private final List<Pet> pList;

    public PetTableAdapter(List<Pet> list, int numberOfItems) {
        numberItems = numberOfItems;
        pList = list;
    }

    @NonNull
    @Override
    public PetVewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        int layoutIdForListItem = R.layout.rw_item_pet;

        LayoutInflater inflater = LayoutInflater.from(ctx);

        View v = inflater.inflate(layoutIdForListItem, parent, false);

        return new PetVewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PetVewHolder holder, int position) {
        holder.bind(pList.get(position));
    }

    @Override
    public int getItemCount() {
        return numberItems;
    }

    static class PetVewHolder extends RecyclerView.ViewHolder {

        private final TextView pClientId;
        private final TextView pAnimal;
        private final TextView pName;
        private final TextView pGender;
        private final TextView pDateOfBirth;
        private final TextView pWeight;

        public PetVewHolder(@NonNull View itemView) {
            super(itemView);

            pClientId = itemView.findViewById(R.id.pet_client_id);
            pAnimal = itemView.findViewById(R.id.pet_animal);
            pName = itemView.findViewById(R.id.pet_name);
            pGender = itemView.findViewById(R.id.pet_gender);
            pDateOfBirth = itemView.findViewById(R.id.pet_date_of_birth);
            pWeight = itemView.findViewById(R.id.pet_weight);
        }

        public void bind(Pet p) {
            pClientId.setText(p.getClientId());
            pAnimal.setText(p.getAnimal());
            pName.setText(p.getName());
            pGender.setText(p.getGender());
            String[] date = p.getDateOfBirth().split("-");
            pDateOfBirth.setText(date[2] + "." + date[1] + "." + date[0]);
            pWeight.setText(p.getWeight());
        }

    }

}
