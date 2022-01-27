package com.example.socialdance.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.socialdance.MainActivity;
import com.example.socialdance.R;
import com.example.socialdance.model.Event;
import com.example.socialdance.fragment.adapter.EventRVAdapter;
import com.example.socialdance.retrofit.EventApi;
import com.example.socialdance.retrofit.NetworkService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.socialdance.MainActivity.TOAST_Y_GRAVITY;


public class FragmentEventsList extends Fragment {

    private MainActivity activity;
    private RecyclerView rvEventsList;
    private LinearLayoutManager linearLayoutManager;
    private EventRVAdapter adapter;
    private List<Event> eventsList;
    private EditText etForSearch;

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
        createRVList();
        downloadEvents();
        setHasOptionsMenu(true);
        return view;
    }

    private void downloadEvents() {
        activity.getPbConnect().setVisibility(View.VISIBLE);
        eventApi.getAllEvents().enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                eventsList.clear();
                if (response.body() != null) {
                    eventsList.addAll(response.body());
                }
                adapter.notifyDataSetChanged();

                activity.getPbConnect().setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                activity.getPbConnect().setVisibility(View.INVISIBLE);
                try {
                    Toast toast = Toast.makeText(activity, "Error connection", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        etForSearch = new EditText(view.getContext());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.itemSearch) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
            alertDialog.setTitle("Search by city");
            alertDialog.setMessage("input city: ");
            alertDialog.setView(etForSearch);
            alertDialog.setPositiveButton("SEARCH", (dialog, which) ->
                    downloadEventsByCity());

            alertDialog.setNeutralButton("CANCEL", (dialog, which) -> {
            });
            if (etForSearch.getParent() != null) {
                ((ViewGroup)etForSearch.getParent()).removeView(etForSearch);
            }
            alertDialog.show();
        } else if (itemId == R.id.itemAll) {
            downloadEvents();
        }
        return super.onOptionsItemSelected(item);
    }

    private void downloadEventsByCity() {
        String city = etForSearch.getText().toString();
        etForSearch.setText("");
        if (!city.trim().isEmpty()) {
            activity.getPbConnect().setVisibility(View.VISIBLE);
            eventApi.getAllEventsByCity(city).enqueue(new Callback<List<Event>>() {
                @Override
                public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                    List<Event> eventsByCity = response.body();
                    activity.getPbConnect().setVisibility(View.INVISIBLE);
                    eventsList.clear();
                    eventsList.addAll(eventsByCity);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<List<Event>> call, Throwable t) {
                    activity.getPbConnect().setVisibility(View.INVISIBLE);
                    Toast toast = Toast.makeText(activity, "Error connection", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                    toast.show();
                }
            });
        }
    }

    public interface EventPassListener {
        void passEventId(int id);
    }
}