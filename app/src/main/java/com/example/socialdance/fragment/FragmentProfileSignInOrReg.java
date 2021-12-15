package com.example.socialdance.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.socialdance.R;
import com.example.socialdance.model.Dancer;
import com.example.socialdance.model.EntityInfo;
import com.example.socialdance.model.LoginPassword;
import com.example.socialdance.retrofit.DancerApi;
import com.example.socialdance.retrofit.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentProfileSignInOrReg extends Fragment {

    private EditText etEmail;
    private EditText etPassword;
    private EditText etEmailReg;
    private EditText etPasswordReg;
    private EditText etPassRetype;
    private Button bSignIn;
    private Button bSignUp;

    private DancerApi dancerApi;

    private ProfilePassListener profilePassListener;

    @Override
    public void onAttach(@NonNull Context context) {
        dancerApi = NetworkService.getInstance().getDancerApi();
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_sign_in_or_reg, container, false);
        profilePassListener = (ProfilePassListener) getContext();
        initViews(view);
        bSignIn.setOnClickListener(this::signIn);
        bSignUp.setOnClickListener(this::signUp);
        return view;
    }

    private void signUp(View view) {
        dancerApi.checkSignUpByEmail(etEmailReg.getText().toString()).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Integer checkReg = response.body();
                Log.d("log", "onResponse " + checkReg);
                if (checkReg == null || checkReg == 0){
                    if (etPasswordReg.getText().toString().equals(etPassRetype.getText().toString())) {
                        registrationDancerOnServer();
                    } else {
                        Toast.makeText(getActivity(), "retype password is not correct", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "email is used", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getActivity(), "Error connection", Toast.LENGTH_LONG).show();
                Log.d("log", "signUp onFailure " + t.toString());
            }
        });
    }

    private void registrationDancerOnServer() {
        Dancer dancer = createDancer();

        dancerApi.createDancer(dancer).enqueue(new Callback<Dancer>() {
            @Override
            public void onResponse(Call<Dancer> call, Response<Dancer> response) {
                Dancer regDancer = response.body();
                Log.d("log", "registrationDancerOnServer onResponse " + regDancer);
                if (regDancer != null) {
                    profilePassListener.passRegDancerId(regDancer.getId());
                }
            }

            @Override
            public void onFailure(Call<Dancer> call, Throwable t) {
                Toast.makeText(getActivity(), "Error connection", Toast.LENGTH_LONG).show();
                Log.d("log", "registrationDancerOnServer onFailure " + t.toString());
            }
        });
    }

    private Dancer createDancer() {
        Dancer dancer = new Dancer();
        String email = etEmailReg.getText().toString();
        dancer.setLoginPassword(new LoginPassword(email, etPasswordReg.getText().toString()));
        dancer.setName(email.substring(0, email.indexOf("@")));
        dancer.setEntityInfo(new EntityInfo(email));
        return dancer;
    }

    private void signIn(View view) {
        dancerApi.checkSignInByEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).
                enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        Integer check = response.body();
                        if (check != null && check > 0){
                            profilePassListener.passRegDancerId(check);
                        }else {
                            Toast.makeText(getActivity(), "wrong login or password", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Toast.makeText(getActivity(), "Error connection", Toast.LENGTH_LONG).show();
                        Log.d("log", "signIn onFailure " + t.toString());
                    }
                });
    }

    private void initViews(View view) {
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        etEmailReg = view.findViewById(R.id.etEmailReg);
        etPasswordReg = view.findViewById(R.id.etPasswordReg);
        etPassRetype = view.findViewById(R.id.etPassRetype);
        bSignIn = view.findViewById(R.id.bSignIn);
        bSignUp = view.findViewById(R.id.bSignUp);
    }

    public interface ProfilePassListener{
        void passRegDancerId(int id);
    }
}