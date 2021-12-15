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
import com.example.socialdance.model.Dancer;
import com.example.socialdance.fragment.adapter.DancerRVAdapter;
import com.example.socialdance.model.Event;
import com.example.socialdance.repository.DataBaseInMemory;
import com.example.socialdance.retrofit.DancerApi;
import com.example.socialdance.retrofit.NetworkService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentDancersList extends Fragment {

    private RecyclerView rvDancersList;
    private LinearLayoutManager linearLayoutManager;
    private DancerRVAdapter adapter;
    private List<Dancer> dancersList;

    private DancerApi dancerApi;

    @Override
    public void onAttach(@NonNull Context context) {
        dancerApi = NetworkService.getInstance().getDancerApi();
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dancers_list, container, false);
        initViews(view);
        downloadEvents();

        return view;
    }

    private void downloadEvents() {
        dancerApi.getAllDancers().enqueue(new Callback<List<Dancer>>() {
            @Override
            public void onResponse(Call<List<Dancer>> call, Response<List<Dancer>> response) {
                dancersList = response.body();
                if (dancersList != null) {
                    createRVList();
                }
                Log.d("log", "onResponse ");
            }

            @Override
            public void onFailure(Call<List<Dancer>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error connection", Toast.LENGTH_LONG).show();
                Log.d("log", "onFailure " + t.toString());
            }
        });
    }

    private void createRVList() {
        adapter = new DancerRVAdapter(dancersList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvDancersList.setLayoutManager(linearLayoutManager);

        rvDancersList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rvDancersList.setAdapter(adapter);
    }

    private void initViews(View view) {
        rvDancersList = view.findViewById(R.id.rvDancersList);
    }

    public interface DancerPassListener{
        void passDancerId(int id);
    }
}