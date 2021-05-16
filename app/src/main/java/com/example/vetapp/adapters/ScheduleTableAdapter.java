package com.example.vetapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vetapp.R;
import com.example.vetapp.database.Schedule;

import java.util.List;

public class ScheduleTableAdapter extends RecyclerView.Adapter<ScheduleTableAdapter.ScheduleVewHolder> {

    private final int numberItems;
    private final List<Schedule> schList;

    public ScheduleTableAdapter(List<Schedule> list, int numberOfItems) {
        numberItems = numberOfItems;
        schList = list;
    }

    @NonNull
    @Override
    public ScheduleVewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        int layoutIdForListItem = R.layout.rw_item_schedule;

        LayoutInflater inflater = LayoutInflater.from(ctx);

        View v = inflater.inflate(layoutIdForListItem, parent, false);

        return new ScheduleVewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleVewHolder holder, int position) {
        holder.bind(schList.get(position));
    }

    @Override
    public int getItemCount() {
        return numberItems;
    }

    static class ScheduleVewHolder extends RecyclerView.ViewHolder {

        private final TextView schPetId;
        private final TextView schClientId;
        private final TextView schOfficeId;
        private final TextView schWorkerId;
        private final TextView schServiceId;
        private final TextView schReason;
        private final TextView schDateAndTimeOfVisit;

        public ScheduleVewHolder(@NonNull View itemView) {
            super(itemView);

            schPetId = itemView.findViewById(R.id.sch_pet_id);
            schClientId = itemView.findViewById(R.id.sch_client_id);
            schOfficeId = itemView.findViewById(R.id.sch_office_id);
            schWorkerId = itemView.findViewById(R.id.sch_worker_id);
            schServiceId = itemView.findViewById(R.id.sch_service_id);
            schReason = itemView.findViewById(R.id.sch_reason);
            schDateAndTimeOfVisit = itemView.findViewById(R.id.sch_date_and_time_visit);
        }

        public void bind(Schedule sch) {
            schPetId.setText(sch.getPetId());
            schClientId.setText(sch.getClientId());
            schOfficeId.setText(sch.getOfficeId());
            schWorkerId.setText(sch.getWorkerId());
            schServiceId.setText(sch.getServiceId());
            schReason.setText(sch.getReason());
            schDateAndTimeOfVisit.setText(sch.getDateAndTimeOfVisit());
        }

    }

}

