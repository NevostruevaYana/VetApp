package com.example.vetapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vetapp.R;
import com.example.vetapp.database.Service;

import java.util.List;

public class ServiceTableAdapter extends RecyclerView.Adapter<ServiceTableAdapter.ServiceVewHolder> {

    private final int numberItems;
    private final List<Service> sList;

    public ServiceTableAdapter(List<Service> list, int numberOfItems) {
        numberItems = numberOfItems;
        sList = list;
    }

    @NonNull
    @Override
    public ServiceVewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        int layoutIdForListItem = R.layout.rwz_item_service;

        LayoutInflater inflater = LayoutInflater.from(ctx);

        View v = inflater.inflate(layoutIdForListItem, parent, false);

        return new ServiceVewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceVewHolder holder, int position) {
        holder.bind(sList.get(position));
    }

    @Override
    public int getItemCount() {
        return numberItems;
    }

    static class ServiceVewHolder extends RecyclerView.ViewHolder {

        private final TextView sName;
        private final TextView sDeliveryTime;
        private final TextView sAmountOfMoney;

        public ServiceVewHolder(@NonNull View itemView) {
            super(itemView);

            sName = itemView.findViewById(R.id.s_name);
            sDeliveryTime = itemView.findViewById(R.id.s_delivery_time);
            sAmountOfMoney = itemView.findViewById(R.id.s_amount_of_money);
        }

        public void bind(Service s) {
            sName.setText(s.getName());
            sDeliveryTime.setText(s.getDelivery_time());
            sAmountOfMoney.setText(s.getAmountOfMoney());
        }

    }

}

