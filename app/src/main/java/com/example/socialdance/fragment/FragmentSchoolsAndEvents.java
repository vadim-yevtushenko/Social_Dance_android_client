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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.socialdance.MainActivity;
import com.example.socialdance.R;
import com.example.socialdance.fragment.adapter.SchoolsAndEventsRVAdapter;
import com.example.socialdance.model.AbstractBaseEntity;
import com.example.socialdance.model.Event;
import com.example.socialdance.model.School;
import com.example.socialdance.retrofit.EventApi;
import com.example.socialdance.retrofit.NetworkService;
import com.example.socialdance.retrofit.SchoolApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentSchoolsAndEvents extends Fragment {

    private RecyclerView rvSchoolsAndEvents;
    private SchoolsAndEventsRVAdapter schoolsAndEventsRVAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<AbstractBaseEntity> entityList;
    private ImageView ivBack;
    private MainActivity activity;
    private int ownerId;

    private SchoolApi schoolApi;
    private EventApi eventApi;

    @Override
    public void onAttach(@NonNull Context context) {
        schoolApi = NetworkService.getInstance().getSchoolApi();
        eventApi = NetworkService.getInstance().getEventApi();
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schools_and_events, container, false);
        activity = (MainActivity) getActivity();
        ownerId = activity.getRegisteredDancerId();
        initViews(view);
        entityList = new ArrayList<>();
        createSchoolsAndEventsRecyclerView();
        downloadSchool();
        downloadEvents();
        ivBack.setOnClickListener(this::back);
        return view;
    }

    public void downloadSchool() {
        schoolApi.getAllSchoolsByOwnerId(ownerId).enqueue(new Callback<List<School>>() {
            @Override
            public void onResponse(Call<List<School>> call, Response<List<School>> response) {
                List<School> schools = response.body();

                entityList.clear();
                entityList.addAll(schools);

                schoolsAndEventsRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<School>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error connection", Toast.LENGTH_LONG).show();
                Log.d("log", "onFailure " + t.toString());
            }
        });
    }

    private void downloadEvents() {
        eventApi.getAllEventsByOwnerId(ownerId).enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                List<Event> events = response.body();

                entityList.addAll(events);

                createSchoolsAndEventsRecyclerView();

                schoolsAndEventsRVAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error connection", Toast.LENGTH_LONG).show();
                Log.d("log", "onFailure " + t.toString());
            }
        });
    }

    private void back(View view) {
        activity.setProfile();
    }

    public void delete() {

    }

    public void save() {

    }

    public void createSchoolsAndEventsRecyclerView() {
        schoolsAndEventsRVAdapter = new SchoolsAndEventsRVAdapter(entityList, this);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvSchoolsAndEvents.setLayoutManager(linearLayoutManager);
        rvSchoolsAndEvents.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rvSchoolsAndEvents.setAdapter(schoolsAndEventsRVAdapter);
    }

    private void initViews(View view) {
        ivBack = view.findViewById(R.id.ivBack);
        rvSchoolsAndEvents = view.findViewById(R.id.rvSchoolsAndEvents);
    }

    public int getOwnerId() {
        return ownerId;
    }
}