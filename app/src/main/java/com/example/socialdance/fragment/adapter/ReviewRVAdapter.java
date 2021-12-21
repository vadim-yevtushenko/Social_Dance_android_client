package com.example.socialdance.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialdance.R;
import com.example.socialdance.model.Review;
import com.example.socialdance.retrofit.DancerApi;
import com.example.socialdance.retrofit.NetworkService;
import com.example.socialdance.utils.DateTimeUtils;

import java.util.List;

public class ReviewRVAdapter extends RecyclerView.Adapter<ReviewRVAdapter.ReviewRecyclerViewHolder> {

    private List<Review> reviewList;
    private DancerApi dancerApi;

    public ReviewRVAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
//        dancerApi = NetworkService.getInstance().getDancerApi();
    }

    @NonNull
    @Override
    public ReviewRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_item, parent, false);
        ReviewRecyclerViewHolder viewHolder = new ReviewRecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewRVAdapter.ReviewRecyclerViewHolder holder, int position) {
        holder.tvReview.setText(reviewList.get(position).getReview());
        holder.tvDateReview.setText(DateTimeUtils.dateTimeFormat.format(reviewList.get(position).getDateTime()));
        holder.tvName.setText(String.valueOf(reviewList.get(position).getAbstractBaseEntityId()));
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    class ReviewRecyclerViewHolder extends RecyclerView.ViewHolder{

        private TextView tvReview;
        private TextView tvDateReview;
        private TextView tvName;

        public ReviewRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDateReview = itemView.findViewById(R.id.tvDateReview);
            tvReview = itemView.findViewById(R.id.tvReview);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}
