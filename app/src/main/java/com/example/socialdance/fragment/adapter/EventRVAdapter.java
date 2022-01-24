package com.example.socialdance.fragment.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialdance.R;
import com.example.socialdance.model.Event;
import com.example.socialdance.fragment.FragmentEventsList;
import com.example.socialdance.retrofit.EventApi;
import com.example.socialdance.retrofit.NetworkService;
import com.example.socialdance.utils.DateTimeUtils;

import java.io.InputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventRVAdapter extends RecyclerView.Adapter<EventRVAdapter.EventRecyclerVieHolder> {
    private Context context;
    private List<Event> events;
    private FragmentEventsList.EventPassListener passListener;
    private EventApi eventApi;

    public EventRVAdapter(List<Event> events) {
        this.events = events;
        eventApi = NetworkService.getInstance().getEventApi();
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
        Event event = events.get(position);
        if (event.getImage() != null) {
            eventApi.downloadImage(event.getId()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.body() != null) {
                        InputStream inputStream = response.body().byteStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        if (bitmap != null) {
                            holder.ivAvatar.setImageBitmap(bitmap);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } else {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.ivAvatar.getLayoutParams();
            params.height = 0;
            holder.ivAvatar.setLayoutParams(params);
        }

        holder.tvName.setText(event.getName());
        holder.tvDescription.setText(event.getDescription());
        holder.tvDatePublication.setText(DateTimeUtils.dateTimeFormat.format(event.getDatePublication()));
        holder.tvFrom.setText(DateTimeUtils.dateTimeFormat.format(event.getDateEvent()));
        holder.tvTo.setText(DateTimeUtils.dateTimeFormat.format(event.getDateFinishEvent()));
        holder.tvEventCity.setText(event.getEntityInfo().getCity());
        holder.eventItemLayout.setOnClickListener(v -> {
            passListener.passEventId(event.getId());
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
        private ImageView ivAvatar;

        public EventRecyclerVieHolder(@NonNull View itemView) {
            super(itemView);
            eventItemLayout = itemView.findViewById(R.id.eventItemLayout);
            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDatePublication = itemView.findViewById(R.id.tvDatePublication);
            tvFrom = itemView.findViewById(R.id.tvFrom);
            tvTo = itemView.findViewById(R.id.tvTo);
            tvEventCity = itemView.findViewById(R.id.tvEventCity);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
        }
    }
}
