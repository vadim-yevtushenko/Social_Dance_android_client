package com.example.socialdance.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialdance.R;
import com.example.socialdance.model.Event;
import com.example.socialdance.fragment.FragmentEventsList;
import com.example.socialdance.utils.DateTimeUtils;

import java.util.List;

public class EventRVAdapter extends RecyclerView.Adapter<EventRVAdapter.EventRecyclerVieHolder> {
    private Context context;
    private List<Event> events;
    private FragmentEventsList.EventPassListener passListener;

    public EventRVAdapter(List<Event> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public EventRecyclerVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        passListener = (FragmentEventsList.EventPassListener) context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.event_item, parent, false);
        EventRecyclerVieHolder vieHolder = new EventRecyclerVieHolder(view);
        return vieHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventRVAdapter.EventRecyclerVieHolder holder, int position) {
        holder.tvName.setText(events.get(position).getName());
        holder.tvDescription.setText(events.get(position).getDescription());
        holder.tvDatePublication.setText(DateTimeUtils.dateTimeFormat.format(events.get(position).getDatePublication()));
        holder.tvFrom.setText(DateTimeUtils.dateTimeFormat.format(events.get(position).getDateEvent()));
        holder.tvTo.setText(DateTimeUtils.dateTimeFormat.format(events.get(position).getDateFinishEvent()));
        holder.tvEventCity.setText(events.get(position).getEntityInfo().getCity());
        holder.eventItemLayout.setOnClickListener(v -> {
            passListener.passEventId(events.get(position).getId());
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class EventRecyclerVieHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout eventItemLayout;
        private TextView tvName;
        private TextView tvDescription;
        private TextView tvDatePublication;
        private TextView tvFrom;
        private TextView tvTo;
        private TextView tvEventCity;

        public EventRecyclerVieHolder(@NonNull View itemView) {
            super(itemView);
            eventItemLayout = itemView.findViewById(R.id.eventItemLayout);
            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDatePublication = itemView.findViewById(R.id.tvDatePublication);
            tvFrom = itemView.findViewById(R.id.tvFrom);
            tvTo = itemView.findViewById(R.id.tvTo);
            tvEventCity = itemView.findViewById(R.id.tvEventCity);
        }
    }
}
