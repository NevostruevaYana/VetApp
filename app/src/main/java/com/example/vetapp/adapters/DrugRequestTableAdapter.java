package com.example.vetapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vetapp.R;
import com.example.vetapp.database.DrugRequest;

import java.util.List;

public class DrugRequestTableAdapter extends RecyclerView.Adapter<DrugRequestTableAdapter.DrugRequestVewHolder> {

    private final int numberItems;
    private final List<DrugRequest> drList;

    public DrugRequestTableAdapter(List<DrugRequest> list, int numberOfItems) {
        numberItems = numberOfItems;
        drList = list;
    }

    @NonNull
    @Override
    public DrugRequestVewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        int layoutIdForListItem = R.layout.rw_item_drug_request;

        LayoutInflater inflater = LayoutInflater.from(ctx);

        View v = inflater.inflate(layoutIdForListItem, parent, false);

        return new DrugRequestVewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DrugRequestVewHolder holder, int position) {
        holder.bind(drList.get(position));
    }

    @Override
    public int getItemCount() {
        return numberItems;
    }

    static class DrugRequestVewHolder extends RecyclerView.ViewHolder {

        private final TextView drWorkerId;
        private final TextView drDrugId;
        private final TextView drAmount;
        private final TextView drRequestDate;
        private final TextView drSupplyDate;

        public DrugRequestVewHolder(@NonNull View itemView) {
            super(itemView);

            drWorkerId = itemView.findViewById(R.id.dr_worker_id);
            drDrugId = itemView.findViewById(R.id.dr_drug_id);
            drAmount = itemView.findViewById(R.id.dr_amount);
            drRequestDate = itemView.findViewById(R.id.dr_request_date);
            drSupplyDate = itemView.findViewById(R.id.dr_supply_date);
        }

        public void bind(DrugRequest dr) {
            drWorkerId.setText(dr.getWorkerId());
            drDrugId.setText(dr.getDrugId());
            drAmount.setText(dr.getAmount());
            drRequestDate.setText(dr.getRequestDate());
            drSupplyDate.setText(dr.getSupplyDate());
        }

    }

}

