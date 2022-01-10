package com.example.socialdance.fragment.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialdance.R;
import com.example.socialdance.model.Dancer;
import com.example.socialdance.fragment.FragmentDancersList;
import com.example.socialdance.retrofit.DancerApi;
import com.example.socialdance.retrofit.NetworkService;
import com.example.socialdance.utils.CircleTextView;

import java.io.InputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DancerRVAdapter extends RecyclerView.Adapter<DancerRVAdapter.DancerRecyclerViewHolder> {

    private Context context;
    private List<Dancer> dancers;
    private FragmentDancersList.DancerPassListener dancerPassListener;
    private DancerApi dancerApi;

    public DancerRVAdapter(List<Dancer> dancers) {
        this.dancers = dancers;
        dancerApi = NetworkService.getInstance().getDancerApi();
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
        Dancer dancer = dancers.get(position);
        if (dancer.getImage() == null) {
            holder.ctvAvatar.setText(getCharsForAvatar(dancer.getName(), dancer.getSurname()));
        } else {
            dancerApi.downloadImage(dancer.getId()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    InputStream inputStream = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    if (bitmap != null) {
                        holder.ctvAvatar.setVisibility(View.INVISIBLE);
                        holder.ivAvatar.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
        holder.tvName.setText(dancer.getName());
        holder.tvSurname.setText(dancer.getSurname());
        holder.tvDancerCity.setText(dancer.getEntityInfo().getCity());
        holder.dancerItemLayout.setOnClickListener(v -> {
            dancerPassListener.passDancerId(dancer.getId());
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
        private ImageView ivAvatar;

        public DancerRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            dancerItemLayout = itemView.findViewById(R.id.dancerItemLayout);
            tvName = itemView.findViewById(R.id.tvName);
            tvSurname = itemView.findViewById(R.id.tvSurname);
            tvDancerCity = itemView.findViewById(R.id.tvDancerCity);
            ctvAvatar = itemView.findViewById(R.id.ctvAvatar);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
        }
    }
}
