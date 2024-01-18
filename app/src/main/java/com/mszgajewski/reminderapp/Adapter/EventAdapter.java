package com.mszgajewski.reminderapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mszgajewski.reminderapp.Database.EntityClass;
import com.mszgajewski.reminderapp.R;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    Context context;
    List <EntityClass> entityClasses;

    public EventAdapter(Context context, List<EntityClass> entityClasses) {
        this.context = context;
        this.entityClasses = entityClasses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listings_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.eventText.setText(entityClasses.get(position).getEventName());
        holder.timeAndDateText.setText(entityClasses.get(position).getEventDate() + " " + entityClasses.get(position).getEventTime());
    }

    @Override
    public int getItemCount() {
        return entityClasses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView eventText, timeAndDateText;
        private LinearLayout topLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventText = itemView.findViewById(R.id.event);
            timeAndDateText = itemView.findViewById(R.id.time_and_date);
            topLayout = itemView.findViewById(R.id.toplayout);
        }
    }
}