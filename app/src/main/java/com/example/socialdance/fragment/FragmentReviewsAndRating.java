package com.example.socialdance.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialdance.MainActivity;
import com.example.socialdance.R;
import com.example.socialdance.fragment.adapter.ReviewRVAdapter;
import com.example.socialdance.model.Rating;
import com.example.socialdance.model.Review;
import com.example.socialdance.retrofit.NetworkService;
import com.example.socialdance.retrofit.SchoolApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentReviewsAndRating extends Fragment {

    private LinearLayoutManager linearLayoutManager;
    private ReviewRVAdapter adapter;
    private int schoolId;
    private MainActivity activity;
    private ImageView ivBack;
    private RecyclerView rvReviews;
    private Spinner spSetRating;
    private TextView tvRating;
    private EditText etReview;
    private Button bSend;
    private Button bSendRating;

    private ArrayAdapter<Integer> spinnerAdapter;


    private SchoolApi schoolApi;

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
        Integer[] grades = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        spinnerAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, grades);
        downLoadReviews();
        bSend.setOnClickListener(this::sendReview);
        bSendRating.setOnClickListener(this::sendRating);
        ivBack.setOnClickListener(this::back);
        return view;
    }

    private void back(View view) {
        activity.passSchoolId(schoolId);
    }

    private void sendRating(View view) {
        Rating rating = new Rating(schoolId, activity.getRegisteredDancerId(), Integer.parseInt(spSetRating.getSelectedItem().toString()));
        schoolApi.createRating(rating).enqueue(new Callback<Rating>() {
            @Override
            public void onResponse(Call<Rating> call, Response<Rating> response) {
                if (response.body() != null) {
                    tvRating.setText("new rating");
                }
            }

            @Override
            public void onFailure(Call<Rating> call, Throwable t) {
                Toast.makeText(getActivity(), "Error connection", Toast.LENGTH_LONG).show();
                Log.d("log", "onFailure " + t.toString());
            }
        });
    }

    private void sendReview(View view) {
        Review review = new Review();
        schoolApi.createReview(review).enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                if (response.body() != null) {
                    etReview.setText("");
                }
            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                Toast.makeText(getActivity(), "Error connection", Toast.LENGTH_LONG).show();
                Log.d("log", "onFailure " + t.toString());
            }
        });
    }

    private void downLoadReviews() {
        schoolApi.getAllReviewsBySchool(schoolId).enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                reviewList = response.body();
                if (reviewList != null) {
                    createRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error connection", Toast.LENGTH_LONG).show();
                Log.d("log", "onFailure " + t.toString());
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
    }
}