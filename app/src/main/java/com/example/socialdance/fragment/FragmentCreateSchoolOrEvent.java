package com.example.socialdance.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.socialdance.MainActivity;
import com.example.socialdance.R;
import com.example.socialdance.fragment.adapter.CreateSchoolOrEventRVAdapter;
import com.example.socialdance.model.EntityInfo;
import com.example.socialdance.model.Event;
import com.example.socialdance.model.School;
import com.example.socialdance.model.enums.Dances;
import com.example.socialdance.retrofit.EventApi;
import com.example.socialdance.retrofit.NetworkService;
import com.example.socialdance.retrofit.SchoolApi;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.socialdance.MainActivity.*;
import static com.example.socialdance.utils.UploadImageUtils.getCompressImage;
import static com.example.socialdance.utils.UploadImageUtils.getPath;


public class FragmentCreateSchoolOrEvent extends Fragment {

    private ImageView ivSave;
    private ImageView ivBack;
    private RecyclerView rvCreateSchoolOrEvent;

    private SchoolApi schoolApi;
    private EventApi eventApi;

    private MainActivity activity;
    private CreateSchoolOrEventRVAdapter.CreateSchoolOrEventRVHolder holder;
    private CreateSchoolOrEventRVAdapter adapter;

    private ImagePassListener imagePassListener;

    @Override
    public void onAttach(@NonNull Context context) {
        schoolApi = NetworkService.getInstance().getSchoolApi();
        eventApi = NetworkService.getInstance().getEventApi();
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        imagePassListener = activity;
        View view = inflater.inflate(R.layout.fragment_create_school_or_event, container, false);
        initViews(view);
        createRecyclerView();
        holder = adapter.getSchoolOrEventRVHolder();

        initListeners();
        ivSave.setOnClickListener(this::create);
        ivBack.setOnClickListener(this::back);
        return view;
    }

    private void createRecyclerView() {
        adapter = new CreateSchoolOrEventRVAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvCreateSchoolOrEvent.setLayoutManager(linearLayoutManager);
        rvCreateSchoolOrEvent.setAdapter(adapter);
    }

    private void back(View view) {
        activity.setImage(null);
        activity.setProfile();
    }

    private void create(View view) {
        if (holder.getSpRole().getSelectedItem().equals(adapter.getEVENT())) {
            createEvent();
        } else if (holder.getSpRole().getSelectedItem().equals(adapter.getSCHOOL())) {
            createSchool();
        }

    }

    private void createSchool() {

        School school = prepareSchoolForCreate();
        if (school.getName() == null || school.getName().isEmpty()) {
            Toast toast = Toast.makeText(activity, "Enter the name school", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
            toast.show();
            return;
        }
        activity.getPbConnect().setVisibility(View.VISIBLE);
        schoolApi.createSchool(school).enqueue(new Callback<School>() {
            @Override
            public void onResponse(Call<School> call, Response<School> response) {

                School newSchool = response.body();

                if (newSchool != null) {
                    uploadSchoolImage(newSchool.getId());
                    Toast toast = Toast.makeText(activity, "School saved successfully", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(activity, "School not saved", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                    toast.show();
                }
                activity.getPbConnect().setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<School> call, Throwable t) {
                activity.getPbConnect().setVisibility(View.INVISIBLE);
                Toast toast = Toast.makeText(activity, "Error connection", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                toast.show();
            }
        });

    }

    private void uploadSchoolImage(Integer id) {
        if (activity.getImage() != null) {

            File image = new File(getPath(activity.getImage(), activity));
            byte[] byteArray = getCompressImage(getPath(activity.getImage(), activity));
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), byteArray);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", image.getName(), requestBody);
            schoolApi.uploadImage(id, fileToUpload).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    activity.setImage(null);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("log", "onFailure " + t.toString());
                }
            });
        }
    }

    private School prepareSchoolForCreate() {
        School school = new School();
        school.setOwnerId(activity.getRegisteredDancerId());
        EntityInfo entityInfo = new EntityInfo(holder.getEtCountry().getText().toString(), holder.getEtCity().getText().toString(),
                holder.getEtStreet().getText().toString(), holder.getEtBuilding().getText().toString(), holder.getEtSuite().getText().toString(),
                holder.getEtPhone().getText().toString(), null);
        school.setName(holder.getEtName().getText().toString());
        school.setDescription(holder.getTvDescription().getText().toString());
        school.setEntityInfo(entityInfo);
        school.setDances(new ArrayList<>());
        if (holder.getCbSalsa().isChecked()) {
            school.getDances().add(Dances.SALSA);
        }
        if (holder.getCbBachata().isChecked()) {
            school.getDances().add(Dances.BACHATA);
        }
        if (holder.getCbKizomba().isChecked()) {
            school.getDances().add(Dances.KIZOMBA);
        }
        if (holder.getCbZouk().isChecked()) {
            school.getDances().add(Dances.ZOUK);
        }
        if (holder.getCbReggaeton().isChecked()) {
            school.getDances().add(Dances.REGGAETON);
        }
        if (holder.getCbMerenge().isChecked()) {
            school.getDances().add(Dances.MERENGE);
        }
        if (holder.getCbMambo().isChecked()) {
            school.getDances().add(Dances.MAMBO);
        }
        if (holder.getCbTango().isChecked()) {
            school.getDances().add(Dances.TANGO);
        }
        return school;
    }

    private void createEvent() {
        Event event = prepareEventForCreate();
        if (event.getName() == null || event.getName().isEmpty()) {
            Toast toast = Toast.makeText(activity, "Enter the name event", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
            toast.show();
            return;
        }
        if (event.getDateEvent() == null) {
            Toast toast = Toast.makeText(activity, "Enter the date event", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
            toast.show();
            return;
        }
        activity.getPbConnect().setVisibility(View.VISIBLE);
        eventApi.createEvent(event).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {

                Event newEvent = response.body();

                if (newEvent != null) {
                    uploadEventImage(newEvent.getId());
                    Toast toast = Toast.makeText(activity, "Event saved successfully", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(activity, "Event not saved", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                    toast.show();
                }
                activity.getPbConnect().setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                activity.getPbConnect().setVisibility(View.INVISIBLE);
                Toast toast = Toast.makeText(activity, "Error connection", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                toast.show();
            }
        });
    }

    private void uploadEventImage(Integer id) {
        if (activity.getImage() != null) {

            File image = new File(getPath(activity.getImage(), activity));
            byte[] byteArray = getCompressImage(getPath(activity.getImage(), activity));
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), byteArray);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", image.getName(), requestBody);
            eventApi.uploadImage(id, fileToUpload).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    activity.setImage(null);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("log", "onFailure " + t.toString());
                }
            });
        }
    }

    private Event prepareEventForCreate() {
        Event event = new Event();
        event.setOwnerId(activity.getRegisteredDancerId());
        EntityInfo entityInfo = new EntityInfo(holder.getEtCountry().getText().toString(), holder.getEtCity().getText().toString(),
                holder.getEtStreet().getText().toString(), holder.getEtBuilding().getText().toString(), holder.getEtSuite().getText().toString(),
                holder.getEtPhone().getText().toString(), null);
        event.setName(holder.getEtName().getText().toString());
        event.setDescription(holder.getTvDescription().getText().toString());
        event.setEntityInfo(entityInfo);
        event.setDances(new ArrayList<>());
        if (holder.getCbSalsa().isChecked()) {
            event.getDances().add(Dances.SALSA);
        }
        if (holder.getCbBachata().isChecked()) {
            event.getDances().add(Dances.BACHATA);
        }
        if (holder.getCbKizomba().isChecked()) {
            event.getDances().add(Dances.KIZOMBA);
        }
        if (holder.getCbZouk().isChecked()) {
            event.getDances().add(Dances.ZOUK);
        }
        if (holder.getCbReggaeton().isChecked()) {
            event.getDances().add(Dances.REGGAETON);
        }
        if (holder.getCbMerenge().isChecked()) {
            event.getDances().add(Dances.MERENGE);
        }
        if (holder.getCbMambo().isChecked()) {
            event.getDances().add(Dances.MAMBO);
        }
        if (holder.getCbTango().isChecked()) {
            event.getDances().add(Dances.TANGO);
        }
        event.setDatePublication(new Date());
        event.setDateEvent(adapter.getDateStart());
        event.setDateFinishEvent(adapter.getDateFinish());
        return event;
    }

    private void initListeners() {
        holder.getIvAvatar().setOnClickListener(v -> setImage());
    }

    private void setImage() {
        imagePassListener.uploadPicture();
        if (activity.getImage() != null) {
            holder.getIvAvatar().setImageURI(activity.getImage());
        }
    }

    private void initViews(@NonNull View view) {

        ivSave = view.findViewById(R.id.ivSave);
        ivBack = view.findViewById(R.id.ivBack);
        rvCreateSchoolOrEvent = view.findViewById(R.id.rvCreateSchoolOrEvent);

    }
}