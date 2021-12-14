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

import com.example.socialdance.MainActivity;
import com.example.socialdance.R;
import com.example.socialdance.model.Event;
import com.example.socialdance.fragment.adapter.EventRVAdapter;
import com.example.socialdance.repository.DataBaseInMemory;
import com.example.socialdance.retrofit.EventApi;
import com.example.socialdance.retrofit.NetworkService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentEventsList extends Fragment {

    private MainActivity activity;
    private RecyclerView rvEventsList;
    private LinearLayoutManager linearLayoutManager;
    private EventRVAdapter adapter;
    private List<Event> eventsList;

    private EventApi eventApi;


    @Override
    public void onAttach(@NonNull Context context) {
        eventApi = NetworkService.getInstance().getEventApi();
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_list, container, false);
        activity = (MainActivity) getActivity();
        initViews(view);
        eventsList = new ArrayList<>();
        downloadEvents();

        return view;
    }

    private void downloadEvents() {
        eventApi.getAllEvents().enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                eventsList = response.body();
                createRVList();
                Log.d("log", "onResponse ");
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Toast.makeText(activity, "Error connection", Toast.LENGTH_LONG).show();
                Log.d("log", "onFailure " + t.toString());
            }
        });
    }

    private void createRVList() {
        adapter = new EventRVAdapter(eventsList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvEventsList.setLayoutManager(linearLayoutManager);

        rvEventsList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rvEventsList.setAdapter(adapter);
    }

    private void initViews(View view) {
        rvEventsList = view.findViewById(R.id.rvEventsList);
    }

    public interface EventPassListener{
        void passEventId(int id);
    }
}