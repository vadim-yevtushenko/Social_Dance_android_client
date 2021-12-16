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
import com.example.socialdance.model.Dancer;
import com.example.socialdance.fragment.FragmentDancersList;
import com.example.socialdance.utils.CircleTextView;

import java.util.List;

public class DancerRVAdapter extends RecyclerView.Adapter<DancerRVAdapter.DancerRecyclerViewHolder> {

    private Context context;
    private List<Dancer> dancers;
    private FragmentDancersList.DancerPassListener dancerPassListener;

    public DancerRVAdapter(List<Dancer> dancers) {
        this.dancers = dancers;
    }

    @NonNull
    @Override
    public DancerRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        dancerPassListener = (FragmentDancersList.DancerPassListener) context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dancer_item, parent, false);
        DancerRecyclerViewHolder viewHolder = new DancerRecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DancerRVAdapter.DancerRecyclerViewHolder holder, int position) {

        holder.ctvAvatar.setText(getCharsForAvatar(dancers.get(position).getName(), dancers.get(position).getSurname()));
        holder.tvName.setText(dancers.get(position).getName());
        holder.tvSurname.setText(dancers.get(position).getSurname());
        holder.tvDancerCity.setText(dancers.get(position).getEntityInfo().getCity());
        holder.dancerItemLayout.setOnClickListener(v -> {
            dancerPassListener.passDancerId(dancers.get(position).getId());
        });
    }

    private String getCharsForAvatar(String name, String surname) {
        String firstChar = String.valueOf(name.charAt(0)).toUpperCase();
        if (surname == null || surname.equals("")){
            return firstChar;
        }
        return firstChar + String.valueOf(surname.charAt(0)).toUpperCase();
    }

    @Override
    public int getItemCount() {
        return dancers.size();
    }

    class DancerRecyclerViewHolder extends RecyclerView.ViewHolder{

        private ConstraintLayout dancerItemLayout;
        private TextView tvName;
        private TextView tvSurname;
        private TextView tvDancerCity;
        private CircleTextView ctvAvatar;

        public DancerRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            dancerItemLayout = itemView.findViewById(R.id.dancerItemLayout);
            tvName = itemView.findViewById(R.id.tvName);
            tvSurname = itemView.findViewById(R.id.tvSurname);
            tvDancerCity = itemView.findViewById(R.id.tvDancerCity);
            ctvAvatar = itemView.findViewById(R.id.ctvAvatar);
        }
    }
}
