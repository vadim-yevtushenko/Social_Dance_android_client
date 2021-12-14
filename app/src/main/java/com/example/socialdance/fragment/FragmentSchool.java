package com.example.socialdance.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialdance.MainActivity;
import com.example.socialdance.R;
import com.example.socialdance.model.EntityInfo;
import com.example.socialdance.model.School;
import com.example.socialdance.model.enums.Dances;
import com.example.socialdance.retrofit.NetworkService;
import com.example.socialdance.retrofit.SchoolApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentSchool extends Fragment {

    private int id;
    private MainActivity activity;
    private ImageView ivBack;

    private TextView tvSchoolName;
    private TextView tvSchoolDescription;
    private TextView tvSchoolAddress;
    private TextView tvSchoolRating;
    private TextView tvSchoolDances;
    private TextView tvOwner;
    private Button bReviews;

    private SchoolApi schoolApi;

    private School school;

    @Override
    public void onAttach(@NonNull Context context) {
        schoolApi = NetworkService.getInstance().getSchoolApi();
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
        initViews(view);
        downloadSchool();
        ivBack.setOnClickListener(this::back);
        return view;
    }

    private void downloadSchool() {
        schoolApi.getSchoolById(id).enqueue(new Callback<School>() {
            @Override
            public void onResponse(Call<School> call, Response<School> response) {
                school = response.body();
                fillForm();
                Log.d("log", "onResponse ");
            }

            @Override
            public void onFailure(Call<School> call, Throwable t) {
                Toast.makeText(getActivity(), "Error connection", Toast.LENGTH_LONG).show();
                Log.d("log", "onFailure " + t.toString());
            }
        });
    }

    private void back(View view) {
        activity.setFragmentSchoolsList();
    }

    private void fillForm() {
        tvSchoolName.setText(school.getName());
        tvSchoolDescription.setText(school.getDescription());
        tvSchoolAddress.setText(getStringAddress(school.getEntityInfo()));
        tvSchoolRating.setText(school.getRating());
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
            stringBuilder.append(entityInfo.getCountry()).append(", ");
        }
        if (entityInfo.getCity() != null){
            stringBuilder.append(entityInfo.getCity()).append(", ");
        }
        if (entityInfo.getStreet() != null){
            stringBuilder.append(entityInfo.getStreet()).append(", ");
        }
        if (entityInfo.getBuilding() != null){
            stringBuilder.append(entityInfo.getBuilding()).append(", ");
        }
        if (entityInfo.getSuites() != null){
            stringBuilder.append(entityInfo.getSuites());
        }
        return stringBuilder.toString();
    }

    private void initViews(View view) {
        ivBack = view.findViewById(R.id.ivBack);
        tvSchoolName = view.findViewById(R.id.tvSchoolName);
        tvSchoolDescription = view.findViewById(R.id.tvSchoolDescription);
        tvSchoolAddress = view.findViewById(R.id.tvSchoolAddress);
        tvSchoolRating = view.findViewById(R.id.tvSchoolRating);
        tvSchoolDances = view.findViewById(R.id.tvSchoolDances);
        tvOwner = view.findViewById(R.id.tvOwner);
        bReviews = view.findViewById(R.id.bReviews);

    }
}