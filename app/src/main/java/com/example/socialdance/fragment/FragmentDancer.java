package com.example.socialdance.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialdance.MainActivity;
import com.example.socialdance.R;
import com.example.socialdance.model.Dancer;
import com.example.socialdance.model.EntityInfo;
import com.example.socialdance.model.enums.Dances;
import com.example.socialdance.retrofit.DancerApi;
import com.example.socialdance.retrofit.NetworkService;
import com.example.socialdance.utils.CircleTextView;
import com.example.socialdance.utils.DateTimeUtils;

import java.io.InputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.socialdance.MainActivity.TOAST_Y_GRAVITY;


public class FragmentDancer extends Fragment {

    private CircleTextView ctvAvatar;
    private ImageView ivAvatar;
    private TextView tvDancerName;
    private TextView tvDancerSurname;
    private TextView tvDancerDescription;
    private TextView tvBirthday;
    private TextView tvSex;
    private TextView tvDancerRole;
    private TextView tvPhone;
    private TextView tvEmail;
    private TextView tvAddress;
    private TextView tvDances;
    private ImageView ivBack;

    private int id;
    private MainActivity activity;

    private DancerApi dancerApi;
    private Dancer dancer;

    @Override
    public void onAttach(@NonNull Context context) {
        dancerApi = NetworkService.getInstance().getDancerApi();
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dancer, container, false);
        if (getArguments() != null) {
            id = getArguments().getInt(MainActivity.KEY_ID);
        }
        activity = (MainActivity) getActivity();
        initViews(view);
        downloadDancer();
        ivBack.setOnClickListener(this::back);
        return view;
    }

    private void downloadDancer() {
        activity.getPbConnect().setVisibility(View.VISIBLE);
        dancerApi.getDancerById(id).enqueue(new Callback<Dancer>() {
            @Override
            public void onResponse(Call<Dancer> call, Response<Dancer> response) {

                dancer = response.body();
                fillForm();
                activity.getPbConnect().setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<Dancer> call, Throwable t) {
                activity.getPbConnect().setVisibility(View.INVISIBLE);
                Toast toast = Toast.makeText(getActivity(), "Error connection", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                toast.show();
            }
        });
    }

    private void back(View view) {
        activity.setFragmentDancersList();
    }

    private void fillForm() {
        if (dancer != null) {
            ctvAvatar.setText(getCharsForAvatar(dancer.getName(), dancer.getSurname()));
            if (dancer.getImage() != null) {

                dancerApi.downloadImage(dancer.getId()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        InputStream inputStream = response.body().byteStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        if (bitmap != null) {
                            ctvAvatar.setVisibility(View.INVISIBLE);
                            ivAvatar.setImageBitmap(bitmap);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
            tvDancerName.setText(dancer.getName());
            tvDancerSurname.setText(dancer.getSurname());
            tvDancerDescription.setText(dancer.getDescription());
            if (dancer.getBirthday() != null) {
                tvBirthday.setText(DateTimeUtils.dateFormat.format(dancer.getBirthday()));
            }
            tvSex.setText(dancer.getSex());

            if (dancer.getRole() != null) {
                tvDancerRole.setText(dancer.getRole().toString());
            }

            tvPhone.setText(dancer.getEntityInfo().getPhoneNumber());
            tvEmail.setText(dancer.getEntityInfo().getEmail());
            tvAddress.setText(getStringAddress(dancer.getEntityInfo()));
            tvDances.setText(getStringListDances(dancer.getDances()));
        }

    }

    private String getStringListDances(List<Dances> dances) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Dances dance : dances) {
            stringBuilder.append(dance.getName()).append(" | ");
        }
        return stringBuilder.toString();
    }

    private String getCharsForAvatar(String name, String surname) {
        String firstChar = String.valueOf(name.charAt(0)).toUpperCase();
        if (surname == null || surname.equals("")){
            return firstChar;
        }
        return firstChar + String.valueOf(surname.charAt(0)).toUpperCase();
    }

    private String getStringAddress(EntityInfo entityInfo) {
        return entityInfo.getCity();
    }

    private void initViews(View view) {
        ctvAvatar = view.findViewById(R.id.ctvAvatar);
        ivAvatar = view.findViewById(R.id.ivAvatar);
        tvDancerName = view.findViewById(R.id.tvDancerName);
        tvDancerSurname = view.findViewById(R.id.tvDancerSurname);
        tvDancerDescription = view.findViewById(R.id.tvDancerDescription);
        tvBirthday = view.findViewById(R.id.tvBirthday);
        tvSex = view.findViewById(R.id.tvSex);
        tvDancerRole = view.findViewById(R.id.tvDancerRole);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvDances = view.findViewById(R.id.tvDances);

        ivBack = view.findViewById(R.id.ivBack);

    }
}