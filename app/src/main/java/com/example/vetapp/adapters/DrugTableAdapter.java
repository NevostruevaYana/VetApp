package com.example.vetapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vetapp.R;
import com.example.vetapp.database.Drug;

import java.util.List;

public class DrugTableAdapter extends RecyclerView.Adapter<DrugTableAdapter.DrugVewHolder> {

    private final int numberItems;
    private final List<Drug> dList;

    public DrugTableAdapter(List<Drug> list, int numberOfItems) {
        numberItems = numberOfItems;
        dList = list;
    }

    @NonNull
    @Override
    public DrugVewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        int layoutIdForListItem = R.layout.rw_item_drug;

        LayoutInflater inflater = LayoutInflater.from(ctx);

        View v = inflater.inflate(layoutIdForListItem, parent, false);

        return new DrugVewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DrugVewHolder holder, int position) {
        holder.bind(dList.get(position));
    }

    @Override
    public int getItemCount() {
        return numberItems;
    }

    static class DrugVewHolder extends RecyclerView.ViewHolder {

        private final TextView dName;
        private final TextView dAmount;

        public DrugVewHolder(@NonNull View itemView) {
            super(itemView);

            dName = itemView.findViewById(R.id.d_name);
            dAmount = itemView.findViewById(R.id.d_amount);
        }

        public void bind(Drug d) {
            dName.setText(d.getName());
            dAmount.setText(d.getAmount());
        }

    }

}

