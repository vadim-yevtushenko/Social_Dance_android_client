package com.example.socialdance.fragment.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialdance.R;
import com.example.socialdance.model.School;
import com.example.socialdance.fragment.FragmentSchoolsList;

import java.util.List;

public class SchoolRVAdapter extends RecyclerView.Adapter<SchoolRVAdapter.SchoolRecyclerVieHolder> {
    private final List<School> schools;
    private FragmentSchoolsList.SchoolPassListener passListener;

    public SchoolRVAdapter(List<School> schools) {
        this.schools = schools;

    }

    @NonNull
    @Override
    public SchoolRVAdapter.SchoolRecyclerVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        passListener = (FragmentSchoolsList.SchoolPassListener) context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.school_item, parent, false);
        SchoolRecyclerVieHolder vieHolder = new SchoolRecyclerVieHolder(view);
        return vieHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolRVAdapter.SchoolRecyclerVieHolder holder, int position) {
        holder.tvName.setText(schools.get(position).getName());
        holder.tvDescription.setText(schools.get(position).getDescription());
        holder.tvCity.setText(schools.get(position).getEntityInfo().getCity());
        holder.tvRating.setText(schools.get(position).getRating());
        holder.schoolItemLayout.setOnClickListener(v -> {
            passListener.passSchoolId(schools.get(position).getId());
        });
    }

    @Override
    public int getItemCount() {
        return schools.size();
    }

    class SchoolRecyclerVieHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout schoolItemLayout;
        private final TextView tvName;
        private final TextView tvDescription;
        private final TextView tvCity;
        private final TextView tvRating;

        public SchoolRecyclerVieHolder(@NonNull View itemView) {
            super(itemView);
            schoolItemLayout = itemView.findViewById(R.id.schoolItemLayout);
            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvRating = itemView.findViewById(R.id.tvRating);
        }
    }
}
