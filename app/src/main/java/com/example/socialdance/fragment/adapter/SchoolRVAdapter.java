package com.example.socialdance.fragment.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialdance.R;
import com.example.socialdance.model.School;
import com.example.socialdance.fragment.FragmentSchoolsList;
import com.example.socialdance.retrofit.NetworkService;
import com.example.socialdance.retrofit.SchoolApi;

import java.io.InputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SchoolRVAdapter extends RecyclerView.Adapter<SchoolRVAdapter.SchoolRecyclerVieHolder> {
    private final List<School> schools;
    private FragmentSchoolsList.SchoolPassListener passListener;
    private SchoolApi schoolApi;

    public SchoolRVAdapter(List<School> schools) {
        this.schools = schools;
        schoolApi = NetworkService.getInstance().getSchoolApi();
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
        School school = schools.get(position);

        if (school.getImage() != null) {
            schoolApi.downloadImage(school.getId()).enqueue(new Callback<ResponseBody>() {
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

        holder.tvName.setText(school.getName());
        holder.tvDescription.setText(school.getDescription());
        holder.tvCity.setText(school.getEntityInfo().getCity());
        holder.ratingBar.setRating(school.getRating().getAverageRating());
        holder.tvRating.setText("rating count: " + school.getRating().getRatingCount());
        holder.schoolItemLayout.setOnClickListener(v -> {
            passListener.passSchoolId(school.getId());
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
        private final ImageView ivAvatar;
        private final RatingBar ratingBar;

        public SchoolRecyclerVieHolder(@NonNull View itemView) {
            super(itemView);
            schoolItemLayout = itemView.findViewById(R.id.schoolItemLayout);
            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvRating = itemView.findViewById(R.id.tvRating);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
        }
    }
}
