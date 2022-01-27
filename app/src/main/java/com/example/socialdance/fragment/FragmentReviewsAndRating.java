package com.example.socialdance.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialdance.MainActivity;
import com.example.socialdance.R;
import com.example.socialdance.fragment.adapter.ReviewRVAdapter;
import com.example.socialdance.model.Rating;
import com.example.socialdance.model.Review;
import com.example.socialdance.model.School;
import com.example.socialdance.retrofit.NetworkService;
import com.example.socialdance.retrofit.SchoolApi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.socialdance.MainActivity.TOAST_Y_GRAVITY;


public class FragmentReviewsAndRating extends Fragment {

    private LinearLayoutManager linearLayoutManager;
    private ReviewRVAdapter adapter;
    private int schoolId;
    private MainActivity activity;
    private ImageView ivBack;
    private RecyclerView rvReviews;
    private Spinner spSetRating;
    private TextView tvRating;
    private TextView tvSchoolName;
    private EditText etReview;
    private Button bSend;
    private Button bSendRating;
    private CheckBox cbIncognito;
    private RatingBar ratingBar;

    private ArrayAdapter<Integer> spinnerAdapter;

    private SchoolApi schoolApi;
    private School school;

    private List<Review> reviewList;


    @Override
    public void onAttach(@NonNull Context context) {
        schoolApi = NetworkService.getInstance().getSchoolApi();
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reviews_and_rating, container, false);
        if (getArguments() != null) {
            schoolId = getArguments().getInt(MainActivity.KEY_ID);
        }
        activity = (MainActivity) getActivity();
        reviewList = new ArrayList<>();
        initViews(view);
        Integer[] grades = new Integer[]{1, 2, 3, 4, 5};
        spinnerAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, grades);
        spSetRating.setAdapter(spinnerAdapter);
        setSpinnerValue();
        downloadSchool();
        downloadReviews();
        createRecyclerView();
        bSend.setOnClickListener(this::sendReview);
        bSendRating.setOnClickListener(this::sendRating);
        ivBack.setOnClickListener(this::back);
        setHasOptionsMenu(true);
        return view;
    }

    private void setSpinnerValue() {
        schoolApi.getRatingByDancerId(schoolId, activity.getRegisteredDancerId()).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Integer rating = response.body();
                if (rating != null && rating > 0){
                    spSetRating.setSelection(rating - 1);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    private void back(View view) {
        activity.passSchoolId(schoolId);
    }

    private void downloadSchool() {
        schoolApi.getSchoolById(schoolId).enqueue(new Callback<School>() {
            @Override
            public void onResponse(Call<School> call, Response<School> response) {
                school = response.body();
                activity.getPbConnect().setVisibility(View.INVISIBLE);
                if (school != null) {
                    if (school.getRating() != null) {
                        ratingBar.setRating(school.getRating().getAverageRating());
                        tvRating.setText("rating count: " + school.getRating().getRatingCount());
                    }
                    tvSchoolName.setText(school.getName());
                }

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

    private void sendRating(View view) {
        if (activity.isEntered()) {
            Rating rating = new Rating(schoolId, activity.getRegisteredDancerId(), Integer.parseInt(spSetRating.getSelectedItem().toString()));
            activity.getPbConnect().setVisibility(View.VISIBLE);
            schoolApi.createRating(rating).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    downloadSchool();

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    activity.getPbConnect().setVisibility(View.INVISIBLE);
                    Toast toast = Toast.makeText(activity, "Error connection", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                    toast.show();
                }
            });
        } else {
            Toast toast = Toast.makeText(activity, "Sign In or Sign UP\nto set a rating!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
            toast.show();
        }
    }

    private void sendReview(View view) {
        if (activity.isEntered()) {
            if (etReview.getText().toString().trim().isEmpty()){
                return;
            }
            Boolean incognito = cbIncognito.isChecked();
            Review review = new Review(incognito, activity.getRegisteredDancerId(), schoolId, etReview.getText().toString(), new Date());
            Log.d("log", "sendReview " + review);
            activity.getPbConnect().setVisibility(View.VISIBLE);
            schoolApi.createReview(review).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    etReview.setText("");
                    downloadReviews();

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    activity.getPbConnect().setVisibility(View.INVISIBLE);
                    Toast toast = Toast.makeText(activity, "Error connection", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                    toast.show();
                }
            });
        } else {
            Toast toast = Toast.makeText(activity, "Sign In or Sign UP\nto send a review!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
            toast.show();
        }
    }

    private void downloadReviews() {
        schoolApi.getAllReviewsBySchool(schoolId).enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                reviewList.clear();
                if (response.body() != null) {
                    reviewList.addAll(response.body());
                }
                activity.getPbConnect().setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                activity.getPbConnect().setVisibility(View.INVISIBLE);
                Toast toast = Toast.makeText(activity, "Error connection", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                toast.show();
            }
        });
    }

    private void createRecyclerView() {
        adapter = new ReviewRVAdapter(reviewList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvReviews.setLayoutManager(linearLayoutManager);
        rvReviews.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rvReviews.setAdapter(adapter);
    }

    private void initViews(View view) {
        ivBack = view.findViewById(R.id.ivBack);
        rvReviews = view.findViewById(R.id.rvReviews);
        spSetRating = view.findViewById(R.id.spSetRating);
        tvRating = view.findViewById(R.id.tvRating);
        etReview = view.findViewById(R.id.etReview);
        bSend = view.findViewById(R.id.bSend);
        bSendRating = view.findViewById(R.id.bSendRating);
        tvSchoolName = view.findViewById(R.id.tvSchoolName);
        cbIncognito = view.findViewById(R.id.cbIncognito);
        ratingBar = view.findViewById(R.id.ratingBar);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }
}