package com.example.socialdance.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.example.socialdance.model.Dancer;
import com.example.socialdance.fragment.adapter.ProfileRVAdapter;
import com.example.socialdance.model.enums.Dances;
import com.example.socialdance.model.enums.Role;
import com.example.socialdance.retrofit.DancerApi;
import com.example.socialdance.retrofit.NetworkService;
import com.example.socialdance.utils.DateTimeUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentProfile extends Fragment {

    private ImageView ivSave;
    private RecyclerView rvProfile;
    private LinearLayoutManager linearLayoutManager;
    private ProfileRVAdapter profileRVAdapter;
    private List<Dancer> dancerList;
    private Dancer dancer;
    private int id;

    private DancerApi dancerApi;

    @Override
    public void onAttach(@NonNull Context context) {
        dancerApi = NetworkService.getInstance().getDancerApi();
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(view);
        if (getArguments() != null){
            id = getArguments().getInt(MainActivity.KEY_ID);
        }
        dancerList = new ArrayList<>();
        downloadDancer();
        ivSave.setOnClickListener(this::save);
        return view;
    }

    private void downloadDancer() {
        dancerApi.getDancerById(id).enqueue(new Callback<Dancer>() {
            @Override
            public void onResponse(Call<Dancer> call, Response<Dancer> response) {
                dancer = response.body();
                dancerList.add(dancer);
                Log.d("log", "downloadDancer onResponse " + dancerList);
                createProfileRecyclerView();
            }

            @Override
            public void onFailure(Call<Dancer> call, Throwable t) {
                Toast.makeText(getActivity(), "Error connection", Toast.LENGTH_LONG).show();
                Log.d("log", "onFailure " + t.toString());
            }
        });
    }

    private void save(View view) {
        createDancerForSave();
        Log.d("log", "save " + dancer);
        dancerApi.updateDancer(dancer).enqueue(new Callback<Dancer>() {
            @Override
            public void onResponse(Call<Dancer> call, Response<Dancer> response) {
                dancer = response.body();
                if (dancer != null){
                    dancerList.clear();
                    dancerList.add(dancer);
                }else {
                    downloadDancer();
                }
                profileRVAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "SAVED", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Dancer> call, Throwable t) {
                Toast.makeText(getActivity(), "Error connection", Toast.LENGTH_LONG).show();
                Log.d("log", "onFailure " + t.toString());
            }
        });
    }



    private void createDancerForSave() {

        ProfileRVAdapter.ProfileRecyclerViewHolder holder = profileRVAdapter.getHolderGlobal();
        dancer.setName(holder.getEtName().getText().toString());
        dancer.setSurname(holder.getEtSurname().getText().toString());
        dancer.setDescription(holder.getEtDescription().getText().toString());
        dancer.getEntityInfo().setCity(holder.getEtCity().getText().toString());
        dancer.getEntityInfo().setPhoneNumber(holder.getEtPhone().getText().toString());
        dancer.getEntityInfo().setEmail(holder.getEtEmail().getText().toString());
        if (holder.getRbMale().isChecked()) {
            dancer.setSex("male");
        } else {
            dancer.setSex("female");
        }
        try {
            dancer.setBirthday(DateTimeUtils.dateFormat.parse(holder.getTvBirthdayShow().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (holder.getSpRole().getSelectedItem() == Role.DANCER) {
            dancer.setRole(Role.DANCER);
        } else {
            dancer.setRole(Role.TEACHER);
        }
        dancer.setDances(new ArrayList<>());
        if (holder.getCbSalsa().isChecked()){
            dancer.getDances().add(Dances.SALSA);
        }
        if (holder.getCbBachata().isChecked()){
            dancer.getDances().add(Dances.BACHATA);
        }
        if (holder.getCbKizomba().isChecked()){
            dancer.getDances().add(Dances.KIZOMBA);
        }
        if (holder.getCbZouk().isChecked()){
            dancer.getDances().add(Dances.ZOUK);
        }
        if (holder.getCbReggaeton().isChecked()){
            dancer.getDances().add(Dances.REGGAETON);
        }
        if (holder.getCbMerenge().isChecked()){
            dancer.getDances().add(Dances.MERENGE);
        }
        if (holder.getCbMambo().isChecked()){
            dancer.getDances().add(Dances.MAMBO);
        }
        if (holder.getCbTango().isChecked()){
            dancer.getDances().add(Dances.TANGO);
        }
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

    public DancerApi getDancerApi() {
        return dancerApi;
    }
}