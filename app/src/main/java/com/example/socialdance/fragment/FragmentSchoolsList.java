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
import android.widget.Toast;

import com.example.socialdance.R;
import com.example.socialdance.model.Event;
import com.example.socialdance.model.School;
import com.example.socialdance.fragment.adapter.SchoolRVAdapter;
import com.example.socialdance.repository.DataBaseInMemory;
import com.example.socialdance.retrofit.NetworkService;
import com.example.socialdance.retrofit.SchoolApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentSchoolsList extends Fragment {

    private RecyclerView rvSchoolsList;
    private LinearLayoutManager linearLayoutManager;
    private SchoolRVAdapter adapter;
    private List<School> schoolsList;

    private SchoolApi schoolApi;

    @Override
    public void onAttach(@NonNull Context context) {
        schoolApi = NetworkService.getInstance().getSchoolApi();
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schools_list, container, false);
        initViews(view);
        schoolsList = new ArrayList<>();
        createRVList();
        downloadSchools();

        return view;
    }

    private void downloadSchools() {
        schoolApi.getAllSchools().enqueue(new Callback<List<School>>() {
            @Override
            public void onResponse(Call<List<School>> call, Response<List<School>> response) {

                schoolsList.addAll(response.body());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<School>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error connection", Toast.LENGTH_LONG).show();
                Log.d("log", "onFailure " + t.toString());
            }
        });
    }

    private void createRVList() {
        adapter = new SchoolRVAdapter(schoolsList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvSchoolsList.setLayoutManager(linearLayoutManager);

        rvSchoolsList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rvSchoolsList.setAdapter(adapter);
    }

    private void initViews(View view) {
        rvSchoolsList = view.findViewById(R.id.rvSchoolsList);
    }

    public interface SchoolPassListener {
        void passSchoolId(int id);
    }
}