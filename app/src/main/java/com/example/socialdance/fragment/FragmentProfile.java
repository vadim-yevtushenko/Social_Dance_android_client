package com.example.socialdance.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.socialdance.R;
import com.example.socialdance.model.Dancer;
import com.example.socialdance.fragment.adapter.ProfileRVAdapter;
import com.example.socialdance.repository.DataBaseInMemory;

import java.util.ArrayList;
import java.util.List;


public class FragmentProfile extends Fragment {

    private ImageView ivSave;
    private RecyclerView rvProfile;
    private LinearLayoutManager linearLayoutManager;
    private ProfileRVAdapter profileRVAdapter;
    private List<Dancer> dancerList;
    private Dancer dancer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(view);
        dancerList = new ArrayList<>();
//        dancerList.add(DataBaseInMemory.findDancerById(1));
        createProfileRecyclerView();
        ivSave.setOnClickListener(this::save);
        return view;
    }

    private void save(View view) {

    }

    public void createProfileRecyclerView() {
        profileRVAdapter = new ProfileRVAdapter(dancerList, this);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvProfile.setLayoutManager(linearLayoutManager);
        rvProfile.setAdapter(profileRVAdapter);
    }

    private void initViews(View view) {
        ivSave = view.findViewById(R.id.ivSave);
        rvProfile = view.findViewById(R.id.rvProfile);
    }

    public Dancer getDancer() {
        return dancer;
    }


}