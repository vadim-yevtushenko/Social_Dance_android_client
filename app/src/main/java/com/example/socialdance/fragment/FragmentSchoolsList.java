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
import com.example.socialdance.model.School;
import com.example.socialdance.fragment.adapter.SchoolRVAdapter;
import com.example.socialdance.retrofit.NetworkService;
import com.example.socialdance.retrofit.SchoolApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.socialdance.MainActivity.TOAST_Y_GRAVITY;


public class FragmentSchoolsList extends Fragment {

    private RecyclerView rvSchoolsList;
    private LinearLayoutManager linearLayoutManager;
    private SchoolRVAdapter adapter;
    private List<School> schoolsList;
    private EditText etForSearch;

    private SchoolApi schoolApi;

    private MainActivity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        schoolApi = NetworkService.getInstance().getSchoolApi();
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schools_list, container, false);
        activity = (MainActivity) getActivity();
        initViews(view);
        schoolsList = new ArrayList<>();
        createRVList();
        downloadSchools();
        setHasOptionsMenu(true);
        return view;
    }

    private void downloadSchools() {
        activity.getPbConnect().setVisibility(View.VISIBLE);
        schoolApi.getAllSchools().enqueue(new Callback<List<School>>() {
            @Override
            public void onResponse(Call<List<School>> call, Response<List<School>> response) {
                schoolsList.clear();
                if (response.body() != null) {
                    schoolsList.addAll(response.body());
                }
                adapter.notifyDataSetChanged();
                activity.getPbConnect().setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<School>> call, Throwable t) {
                activity.getPbConnect().setVisibility(View.INVISIBLE);
                Toast toast = Toast.makeText(activity, "Error connection", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                toast.show();
            }
        });
    }

    private void createRVList() {
        adapter = new SchoolRVAdapter(schoolsList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvSchoolsList.setLayoutManager(linearLayoutManager);

        rvSchoolsList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rvSchoolsList.setAdapter(adapter);
    }

    private void initViews(View view) {
        rvSchoolsList = view.findViewById(R.id.rvSchoolsList);
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
            alertDialog.setPositiveButton("SEARCH", (dialog, which) -> {
                downloadSchoolsByCity();
            });
            alertDialog.setNeutralButton("CANCEL", (dialog, which) -> {
            });
            if (etForSearch.getParent() != null) {
                ((ViewGroup)etForSearch.getParent()).removeView(etForSearch);
            }
            alertDialog.show();
        } else if (itemId == R.id.itemAll){
            downloadSchools();
        }
        return super.onOptionsItemSelected(item);
    }

    private void downloadSchoolsByCity() {
        String city = etForSearch.getText().toString();
        etForSearch.setText("");
        if (!city.trim().isEmpty()){
            activity.getPbConnect().setVisibility(View.VISIBLE);
            schoolApi.getAllSchoolsByCity(city).enqueue(new Callback<List<School>>() {
                @Override
                public void onResponse(Call<List<School>> call, Response<List<School>> response) {
                    List<School> schoolsByCity = response.body();
                    activity.getPbConnect().setVisibility(View.INVISIBLE);
                    schoolsList.clear();
                    schoolsList.addAll(schoolsByCity);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<List<School>> call, Throwable t) {
                    activity.getPbConnect().setVisibility(View.INVISIBLE);
                    Toast toast = Toast.makeText(activity, "Error connection", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                    toast.show();
                }
            });
        }
    }

    public interface SchoolPassListener {
        void passSchoolId(int id);
    }
}