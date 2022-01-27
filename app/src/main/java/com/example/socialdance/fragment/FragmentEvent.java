package com.example.socialdance.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialdance.MainActivity;
import com.example.socialdance.R;
import com.example.socialdance.model.Dancer;
import com.example.socialdance.model.EntityInfo;
import com.example.socialdance.model.Event;
import com.example.socialdance.model.enums.Dances;
import com.example.socialdance.retrofit.DancerApi;
import com.example.socialdance.retrofit.EventApi;
import com.example.socialdance.retrofit.NetworkService;
import com.example.socialdance.utils.DateTimeUtils;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.socialdance.MainActivity.TOAST_Y_GRAVITY;

public class FragmentEvent extends Fragment {

    private int id;
    private MainActivity activity;
    private ImageView ivBack;
    private ImageView ivAvatar;

    private TextView tvEventName;
    private TextView tvEventDescription;
    private TextView tvEventAddress;
    private TextView tvDateTime;
    private TextView tvOwner;
    private TextView tvEventDances;

    private Event event;
    private Dancer dancer;

    private EventApi eventApi;
    private DancerApi dancerApi;

    @Override
    public void onAttach(@NonNull Context context) {
        eventApi = NetworkService.getInstance().getEventApi();
        dancerApi = NetworkService.getInstance().getDancerApi();
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
        tvEventDescription.setOnClickListener(this::showDescription);
        setHasOptionsMenu(true);
        return view;
    }

    private void showDescription(View view) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View viewForDialog = inflater.inflate(R.layout.dialog_read_description, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle("Event description");
        alertDialog.setView(viewForDialog);
        TextView tvFullDescription = viewForDialog.findViewById(R.id.tvFullDescription);
        tvFullDescription.setText(event.getDescription());
        alertDialog.setPositiveButton("OK", (dialog, which) ->{});

        alertDialog.show();
    }

    private void downloadEvent() {
        activity.getPbConnect().setVisibility(View.VISIBLE);
        eventApi.getEventById(id).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {

                event = response.body();
                downloadDancer();
                fillForm();
                activity.getPbConnect().setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                activity.getPbConnect().setVisibility(View.INVISIBLE);
                Toast toast = Toast.makeText(getActivity(), "Error connection", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                toast.show();
            }
        });
    }

    private void downloadDancer() {
        dancerApi.getDancerById(event.getOwnerId()).enqueue(new Callback<Dancer>() {
            @Override
            public void onResponse(Call<Dancer> call, Response<Dancer> response) {
                dancer = response.body();
                if (dancer != null){
                    tvOwner.setText(dancer.getName() + " " + dancer.getSurname());
                }
            }

            @Override
            public void onFailure(Call<Dancer> call, Throwable t) {

            }
        });
    }

    private void back(View view) {
        activity.setFragmentEventsList();
    }

    private void fillForm() {

        if (event.getImage() != null) {
            eventApi.downloadImage(event.getId()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.body() != null) {
                        InputStream inputStream = response.body().byteStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        if (bitmap != null) {
                            ivAvatar.setImageBitmap(bitmap);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } else {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) ivAvatar.getLayoutParams();
            params.height = 1;
            ivAvatar.setLayoutParams(params);
        }

        if (event != null) {
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
        if (entityInfo.getCountry() != null || !entityInfo.getCountry().equals("")){
            stringBuilder.append(entityInfo.getCountry()).append("  ");
        }
        if (entityInfo.getCity() != null || !entityInfo.getCity().equals("")){
            stringBuilder.append(entityInfo.getCity()).append("  ");
        }
        if (entityInfo.getStreet() != null || !entityInfo.getStreet().equals("")){
            stringBuilder.append(entityInfo.getStreet()).append(" ");
        }
        if (entityInfo.getBuilding() != null || !entityInfo.getBuilding().equals("")){
            stringBuilder.append(entityInfo.getBuilding()).append("  ");
        }
        if (entityInfo.getSuites() != null || !entityInfo.getSuites().equals("")){
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
        ivAvatar = view.findViewById(R.id.ivAvatar);
        tvEventName = view.findViewById(R.id.tvEventName);
        tvEventDescription = view.findViewById(R.id.tvEventDescription);
        tvEventAddress = view.findViewById(R.id.tvEventAddress);
        tvDateTime = view.findViewById(R.id.tvDateTime);
        tvOwner = view.findViewById(R.id.tvOwner);
        tvEventDances = view.findViewById(R.id.tvEventDances);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

}