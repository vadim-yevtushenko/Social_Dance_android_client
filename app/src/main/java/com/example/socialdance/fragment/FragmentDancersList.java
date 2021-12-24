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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.socialdance.MainActivity;
import com.example.socialdance.R;
import com.example.socialdance.model.Dancer;
import com.example.socialdance.fragment.adapter.DancerRVAdapter;
import com.example.socialdance.retrofit.DancerApi;
import com.example.socialdance.retrofit.NetworkService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.socialdance.MainActivity.TOAST_Y_GRAVITY;


public class FragmentDancersList extends Fragment {

    private RecyclerView rvDancersList;
    private LinearLayoutManager linearLayoutManager;
    private DancerRVAdapter adapter;
    private List<Dancer> dancersList;
    private EditText etForSearch;
    private EditText etForSearch2;

    private DancerApi dancerApi;

    private MainActivity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        dancerApi = NetworkService.getInstance().getDancerApi();
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dancers_list, container, false);
        activity = (MainActivity) getActivity();
        initViews(view);
        dancersList = new ArrayList<>();
        createRVList();
        downloadDancers();
        setHasOptionsMenu(true);
        return view;
    }

    private void downloadDancers() {
        activity.getPbConnect().setVisibility(View.VISIBLE);
        dancerApi.getAllDancers().enqueue(new Callback<List<Dancer>>() {
            @Override
            public void onResponse(Call<List<Dancer>> call, Response<List<Dancer>> response) {
                dancersList.clear();
                dancersList.addAll(response.body());

                adapter.notifyDataSetChanged();

                activity.getPbConnect().setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<Dancer>> call, Throwable t) {
                activity.getPbConnect().setVisibility(View.INVISIBLE);
                Toast toast = Toast.makeText(getActivity(), "Error connection", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                toast.show();
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
        etForSearch = new EditText(view.getContext());
        etForSearch2 = new EditText(view.getContext());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_search_dancer, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
                    downloadDancersByCity());

            alertDialog.setNeutralButton("CANCEL", (dialog, which) -> {
            });
            if (etForSearch.getParent() != null) {
                ((ViewGroup) etForSearch.getParent()).removeView(etForSearch);
            }
            alertDialog.show();
        } else if (itemId == R.id.itemAll) {
            downloadDancers();
        } else if (itemId == R.id.itemSearchByNameAndSurname) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            View viewForDialog = inflater.inflate(R.layout.dialog_name_surname, null);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
            alertDialog.setTitle("Search by name and surname");
            alertDialog.setMessage("input name and surname: ");
            alertDialog.setView(viewForDialog);
            etForSearch = viewForDialog.findViewById(R.id.etDialogName);
            etForSearch2 = viewForDialog.findViewById(R.id.etDialogSurname);
            alertDialog.setPositiveButton("SEARCH", (dialog, which) ->
                    downloadDancersByNameAndSurname());

            alertDialog.setNeutralButton("CANCEL", (dialog, which) -> {
            });

            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void downloadDancersByNameAndSurname() {
        String name = etForSearch.getText().toString().trim();
        String surname = etForSearch2.getText().toString().trim();
        etForSearch.setText("");
        etForSearch2.setText("");

        if (!name.isEmpty() || !surname.isEmpty()) {

            activity.getPbConnect().setVisibility(View.VISIBLE);
            dancerApi.getDancersByNameAndSurname(name, surname).enqueue(new Callback<List<Dancer>>() {
                @Override
                public void onResponse(Call<List<Dancer>> call, Response<List<Dancer>> response) {

                    List<Dancer> dancersByNameAndSurname = response.body();
                    activity.getPbConnect().setVisibility(View.INVISIBLE);
                    dancersList.clear();

                    if (dancersByNameAndSurname != null) {
                        dancersList.addAll(dancersByNameAndSurname);
                    }

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<List<Dancer>> call, Throwable t) {
                    activity.getPbConnect().setVisibility(View.INVISIBLE);
                    Toast toast = Toast.makeText(activity, "Error connection", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                    toast.show();
                }
            });
        }
    }

    private void downloadDancersByCity() {
        String city = etForSearch.getText().toString();
        etForSearch.setText("");
        if (!city.trim().isEmpty()) {
            activity.getPbConnect().setVisibility(View.VISIBLE);
            dancerApi.getDancersByCity(city).enqueue(new Callback<List<Dancer>>() {
                @Override
                public void onResponse(Call<List<Dancer>> call, Response<List<Dancer>> response) {
                    List<Dancer> dancersByCity = response.body();
                    activity.getPbConnect().setVisibility(View.INVISIBLE);
                    dancersList.clear();
                    dancersList.addAll(dancersByCity);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<List<Dancer>> call, Throwable t) {
                    activity.getPbConnect().setVisibility(View.INVISIBLE);
                    Toast toast = Toast.makeText(activity, "Error connection", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                    toast.show();
                }
            });
        }
    }

    public interface DancerPassListener {
        void passDancerId(int id);
    }
}