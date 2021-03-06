package com.example.socialdance.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialdance.MainActivity;
import com.example.socialdance.R;
import com.example.socialdance.model.Dancer;
import com.example.socialdance.model.EntityInfo;
import com.example.socialdance.model.School;
import com.example.socialdance.model.enums.Dances;
import com.example.socialdance.retrofit.DancerApi;
import com.example.socialdance.retrofit.NetworkService;
import com.example.socialdance.retrofit.SchoolApi;

import java.io.InputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.socialdance.MainActivity.TOAST_Y_GRAVITY;


public class FragmentSchool extends Fragment {

    private int id;
    private MainActivity activity;
    private SchoolForReviewPassListener passListener;

    private ImageView ivBack;
    private ImageView ivAvatar;
    private TextView tvSchoolName;
    private TextView tvSchoolDescription;
    private TextView tvSchoolAddress;
    private TextView tvSchoolRating;
    private TextView tvSchoolDances;
    private TextView tvOwner;
    private Button bReviews;
    private RatingBar ratingBar;

    private SchoolApi schoolApi;
    private DancerApi dancerApi;

    private School school;
    private Dancer dancer;

    @Override
    public void onAttach(@NonNull Context context) {
        schoolApi = NetworkService.getInstance().getSchoolApi();
        dancerApi = NetworkService.getInstance().getDancerApi();
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_school, container, false);
        if (getArguments() != null){
            id = getArguments().getInt(MainActivity.KEY_ID);
        }
        activity = (MainActivity) getActivity();
        passListener = activity;
        initViews(view);
        downloadSchool();
        bReviews.setOnClickListener(this::reviews);
        ivBack.setOnClickListener(this::back);
        tvSchoolDescription.setOnClickListener(this::showDescription);
        setHasOptionsMenu(true);
        return view;
    }

    private void showDescription(View view) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View viewForDialog = inflater.inflate(R.layout.dialog_read_description, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle("School description");
        alertDialog.setView(viewForDialog);
        TextView tvFullDescription = viewForDialog.findViewById(R.id.tvFullDescription);
        tvFullDescription.setText(school.getDescription());
        alertDialog.setPositiveButton("OK", (dialog, which) ->{});

        alertDialog.show();
    }

    private void reviews(View view) {
        passListener.passSchoolFroReviewId(id);
    }

    private void downloadSchool() {
        activity.getPbConnect().setVisibility(View.VISIBLE);
        schoolApi.getSchoolById(id).enqueue(new Callback<School>() {
            @Override
            public void onResponse(Call<School> call, Response<School> response) {
                school = response.body();
                downloadDancer();
                activity.getPbConnect().setVisibility(View.INVISIBLE);
                fillForm();
            }

            @Override
            public void onFailure(Call<School> call, Throwable t) {
                activity.getPbConnect().setVisibility(View.INVISIBLE);
                Toast toast = Toast.makeText(activity, "Error connection", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                toast.show();
            }
        });
    }

    private void downloadDancer() {
        dancerApi.getDancerById(school.getOwnerId()).enqueue(new Callback<Dancer>() {
            @Override
            public void onResponse(Call<Dancer> call, Response<Dancer> response) {
                dancer = response.body();
                if (dancer != null){
                    tvOwner.setText(dancer.getName() + " " + dancer.getSurname());
                }
            }

            @Override
            public void onFailure(Call<Dancer> call, Throwable t) {

            }
        });
    }

    private void back(View view) {
        activity.setFragmentSchoolsList();
    }

    private void fillForm() {
        if (school.getImage() != null) {
            schoolApi.downloadImage(school.getId()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.body() != null) {
                        InputStream inputStream = response.body().byteStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        if (bitmap != null) {
                            ivAvatar.setImageBitmap(bitmap);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } else {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) ivAvatar.getLayoutParams();
            params.height = 1;
            ivAvatar.setLayoutParams(params);
        }
        tvSchoolName.setText(school.getName());
        tvSchoolDescription.setText(school.getDescription());
        tvSchoolAddress.setText(getStringAddress(school.getEntityInfo()));
        if (school.getRating() != null) {
            ratingBar.setRating(school.getRating().getAverageRating());
            tvSchoolRating.setText("rating count: " + school.getRating().getRatingCount());
        }
        tvSchoolDances.setText(getStringListDances(school.getDances()));
        tvOwner.setText("");
    }

    private String getStringListDances(List<Dances> dances) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Dances dance : dances) {
            stringBuilder.append(dance.getName()).append(" | ");
        }
        return stringBuilder.toString();
    }

    private String getStringAddress(EntityInfo entityInfo) {
        StringBuilder stringBuilder = new StringBuilder();
        if (entityInfo.getCountry() != null){
            stringBuilder.append(entityInfo.getCountry()).append("  ");
        }
        if (entityInfo.getCity() != null){
            stringBuilder.append(entityInfo.getCity()).append("  ");
        }
        if (entityInfo.getStreet() != null){
            stringBuilder.append(entityInfo.getStreet()).append(" ");
        }
        if (entityInfo.getBuilding() != null){
            stringBuilder.append(entityInfo.getBuilding()).append("  ");
        }
        if (entityInfo.getSuites() != null){
            stringBuilder.append(entityInfo.getSuites());
        }
        return stringBuilder.toString();
    }

    private void initViews(View view) {
        ivBack = view.findViewById(R.id.ivBack);
        ivAvatar = view.findViewById(R.id.ivAvatar);
        tvSchoolName = view.findViewById(R.id.tvSchoolName);
        tvSchoolDescription = view.findViewById(R.id.tvSchoolDescription);
        tvSchoolAddress = view.findViewById(R.id.tvSchoolAddress);
        tvSchoolRating = view.findViewById(R.id.tvSchoolRating);
        tvSchoolDances = view.findViewById(R.id.tvSchoolDances);
        tvOwner = view.findViewById(R.id.tvOwner);
        bReviews = view.findViewById(R.id.bReviews);
        ratingBar = view.findViewById(R.id.ratingBar);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

    public interface SchoolForReviewPassListener{
        void passSchoolFroReviewId(int id);
    }
}