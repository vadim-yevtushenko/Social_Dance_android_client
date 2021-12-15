package com.example.socialdance.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialdance.MainActivity;
import com.example.socialdance.R;
import com.example.socialdance.model.EntityInfo;
import com.example.socialdance.model.Event;
import com.example.socialdance.model.enums.Dances;
import com.example.socialdance.repository.DataBaseInMemory;
import com.example.socialdance.retrofit.EventApi;
import com.example.socialdance.retrofit.NetworkService;
import com.example.socialdance.utils.DateTimeUtils;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentEvent extends Fragment {

    private int id;
    private MainActivity activity;
    private ImageView ivBack;

    private TextView tvEventName;
    private TextView tvEventDescription;
    private TextView tvEventAddress;
    private TextView tvDateTime;
    private TextView tvOwner;
    private TextView tvEventDances;

    private Event event;

    private EventApi eventApi;

    @Override
    public void onAttach(@NonNull Context context) {
        eventApi = NetworkService.getInstance().getEventApi();
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        if (getArguments() != null) {
            id = getArguments().getInt(MainActivity.KEY_ID);
        }
        activity = (MainActivity) getActivity();
        initViews(view);
        downloadEvent();

        ivBack.setOnClickListener(this::back);
        return view;
    }

    private void downloadEvent() {
        eventApi.getEventById(id).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                event = response.body();
                fillForm();
                Log.d("log", "onResponse ");
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Toast.makeText(getActivity(), "Error connection", Toast.LENGTH_LONG).show();
                Log.d("log", "onFailure " + t.toString());
            }
        });
    }

    private void back(View view) {
        activity.setFragmentEventsList();
    }

    private void fillForm() {

        tvEventName.setText(event.getName());
        tvEventDescription.setText(event.getDescription());
        tvEventAddress.setText(getStringAddress(event.getEntityInfo()));
        if (event.getDateEvent() == null) {
            tvDateTime.setText("soon");
        } else {
            tvDateTime.setText(getStringTime(event.getDateEvent(), event.getDateFinishEvent()));
        }
        tvEventDances.setText(getStringListDances(event.getDances()));

    }

    private String getStringTime(Date dateEvent, Date dateFinishEvent) {
        String dateFinish = "";
        if (dateFinishEvent != null){
            dateFinish = DateTimeUtils.dateTimeFormat.format(dateFinishEvent);
        }
        return String.format("Start: %s\nFinish: %s", DateTimeUtils.dateTimeFormat.format(dateEvent), dateFinish);
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

    private String getStringListDances(List<Dances> dances) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Dances dance : dances) {
            stringBuilder.append(dance.getName()).append(" | ");
        }
        return stringBuilder.toString();
    }

    private void initViews(View view) {
        ivBack = view.findViewById(R.id.ivBack);
        tvEventName = view.findViewById(R.id.tvEventName);
        tvEventDescription = view.findViewById(R.id.tvEventDescription);
        tvEventAddress = view.findViewById(R.id.tvEventAddress);
        tvDateTime = view.findViewById(R.id.tvDateTime);
        tvOwner = view.findViewById(R.id.tvOwner);
        tvEventDances = view.findViewById(R.id.tvEventDances);
    }


}