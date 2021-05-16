package com.example.vetapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vetapp.R;
import com.example.vetapp.database.Client;

import java.util.List;

public class ClientTableAdapter extends RecyclerView.Adapter<ClientTableAdapter.ClientVewHolder> {

    private final int numberItems;
    private final List<Client> clList;

    public ClientTableAdapter(List<Client> list, int numberOfItems) {
        numberItems = numberOfItems;
        clList = list;
    }

    @NonNull
    @Override
    public ClientVewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        int layoutIdForListItem = R.layout.rw_item_client;

        LayoutInflater inflater = LayoutInflater.from(ctx);

        View v = inflater.inflate(layoutIdForListItem, parent, false);

        return new ClientVewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientVewHolder holder, int position) {
        holder.bind(clList.get(position));
    }

    @Override
    public int getItemCount() {
        return numberItems;
    }

    static class ClientVewHolder extends RecyclerView.ViewHolder {

        private final TextView clName;
        private final TextView clAddress;
        private final TextView clPhone;
        private final TextView clRegular;

        public ClientVewHolder(@NonNull View itemView) {
            super(itemView);

            clName = itemView.findViewById(R.id.cl_name);
            clAddress = itemView.findViewById(R.id.cl_address);
            clPhone = itemView.findViewById(R.id.cl_phone);
            clRegular = itemView.findViewById(R.id.cl_regular);
        }

        public void bind(Client cl) {
            clName.setText(cl.getFullName());
            clAddress.setText(cl.getAddress());
            clPhone.setText(cl.getPhone());
            clRegular.setText(cl.getRegular());
        }

    }

}

