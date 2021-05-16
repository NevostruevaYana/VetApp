package com.example.vetapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vetapp.R;
import com.example.vetapp.database.Worker;

import java.util.List;

public class WorkerTableAdapter extends RecyclerView.Adapter<WorkerTableAdapter.WorkerVewHolder> {

    private final int numberItems;
    private final List<Worker> wList;

    public WorkerTableAdapter(List<Worker> list, int numberOfItems) {
        numberItems = numberOfItems;
        wList = list;
    }

    @NonNull
    @Override
    public WorkerVewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        int layoutIdForListItem = R.layout.rw_item_worker;

        LayoutInflater inflater = LayoutInflater.from(ctx);

        View v = inflater.inflate(layoutIdForListItem, parent, false);

        return new WorkerVewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkerVewHolder holder, int position) {
        holder.bind(wList.get(position));
    }

    @Override
    public int getItemCount() {
        return numberItems;
    }

    static class WorkerVewHolder extends RecyclerView.ViewHolder {

        private final TextView wName;
        private final TextView wPosition;
        private final TextView wDateOfBirth;
        private final TextView wPhone;

            public WorkerVewHolder(@NonNull View itemView) {
                super(itemView);

                wName = itemView.findViewById(R.id.w_name);
                wPosition = itemView.findViewById(R.id.w_position);
                wDateOfBirth = itemView.findViewById(R.id.w_d_of_birth);
                wPhone = itemView.findViewById(R.id.w_phone);
            }

            public void bind(Worker w) {
                wName.setText(w.getFullName());
                wPosition.setText(w.getPosition());
                wDateOfBirth.setText(w.getDateOfBirth());
                wPhone.setText(w.getPhone());
            }

        }

}
