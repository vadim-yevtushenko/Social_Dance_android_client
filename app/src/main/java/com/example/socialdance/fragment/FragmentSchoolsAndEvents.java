package com.example.socialdance.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.socialdance.R;
import com.example.socialdance.fragment.adapter.ProfileRVAdapter;
import com.example.socialdance.fragment.adapter.SchoolsAndEventsRVAdapter;
import com.example.socialdance.model.School;

import java.util.List;


public class FragmentSchoolsAndEvents extends Fragment {

    private RecyclerView rvSchoolsAndEvents;
    private SchoolsAndEventsRVAdapter schoolsAndEventsRVAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<School> schoolList;
    private ImageView ivBack;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schools_and_events, container, false);
        initViews(view);
        createProfileRecyclerView();
        ivBack.setOnClickListener(this::back);
        return view;
    }

    private void back(View view) {

    }

    public void createProfileRecyclerView() {
        schoolsAndEventsRVAdapter = new SchoolsAndEventsRVAdapter(schoolList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvSchoolsAndEvents.setLayoutManager(linearLayoutManager);
        rvSchoolsAndEvents.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rvSchoolsAndEvents.setAdapter(schoolsAndEventsRVAdapter);
    }

    private void initViews(View view) {
        ivBack = view.findViewById(R.id.ivBack);
        rvSchoolsAndEvents = view.findViewById(R.id.rvSchoolsAndEvents);
    }
}