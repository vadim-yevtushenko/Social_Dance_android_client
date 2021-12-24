package com.example.socialdance.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.socialdance.MainActivity;
import com.example.socialdance.R;
import com.example.socialdance.model.Dancer;
import com.example.socialdance.model.EntityInfo;
import com.example.socialdance.model.LoginPassword;
import com.example.socialdance.retrofit.DancerApi;
import com.example.socialdance.retrofit.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.socialdance.MainActivity.TOAST_Y_GRAVITY;


public class FragmentProfileSignInOrReg extends Fragment {

    private EditText etEmail;
    private EditText etPassword;
    private EditText etEmailReg;
    private EditText etPasswordReg;
    private EditText etPassRetype;
    private Button bSignIn;
    private Button bSignUp;

    private DancerApi dancerApi;

    private ProfileSignInOrRegPassListener profileSignInOrRegPassListener;

    private MainActivity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        dancerApi = NetworkService.getInstance().getDancerApi();
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_sign_in_or_reg, container, false);
        activity = (MainActivity) getActivity();
        profileSignInOrRegPassListener = (ProfileSignInOrRegPassListener) getContext();
        initViews(view);
        bSignIn.setOnClickListener(this::signIn);
        bSignUp.setOnClickListener(this::signUp);
        setHasOptionsMenu(true);
        return view;
    }

    private void signUp(View view) {
        activity.getPbConnect().setVisibility(View.VISIBLE);
        dancerApi.checkSignUpByEmail(etEmailReg.getText().toString()).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Integer checkReg = response.body();
                activity.getPbConnect().setVisibility(View.INVISIBLE);
                if (checkReg == null || checkReg == 0){
                    if (etPasswordReg.getText().toString().equals(etPassRetype.getText().toString())) {
                        registrationDancerOnServer();
                    }else {
                        Toast toast = Toast.makeText(activity, "retype password is not correct", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                        toast.show();
                    }
                }else if (checkReg == -1){
                    Toast toast = Toast.makeText(activity, "email is not valid", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                    toast.show();
                }else {
                    Toast toast = Toast.makeText(activity, "email is used", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                activity.getPbConnect().setVisibility(View.INVISIBLE);
                Toast toast = Toast.makeText(activity, "Error connection", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                toast.show();

            }
        });
    }

    private void registrationDancerOnServer() {
        Dancer dancer = createDancer();
        activity.getPbConnect().setVisibility(View.VISIBLE);
        dancerApi.createDancer(dancer).enqueue(new Callback<Dancer>() {
            @Override
            public void onResponse(Call<Dancer> call, Response<Dancer> response) {
                Dancer regDancer = response.body();
                activity.getPbConnect().setVisibility(View.INVISIBLE);
                if (regDancer != null) {
                    profileSignInOrRegPassListener.passRegDancerId(regDancer.getId());
                }
            }

            @Override
            public void onFailure(Call<Dancer> call, Throwable t) {
                activity.getPbConnect().setVisibility(View.INVISIBLE);
                Toast toast = Toast.makeText(activity, "Error connection", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                toast.show();
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
        activity.getPbConnect().setVisibility(View.VISIBLE);
        dancerApi.checkSignInByEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).
                enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        Integer check = response.body();
                        activity.getPbConnect().setVisibility(View.INVISIBLE);
                        if (check != null && check > 0){
                            profileSignInOrRegPassListener.passRegDancerId(check);
                        }else {
                            Toast toast = Toast.makeText(activity, "wrong login or password", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                            toast.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        activity.getPbConnect().setVisibility(View.INVISIBLE);
                        Toast toast = Toast.makeText(getActivity(), "Error connection", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                        toast.show();
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_about, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.itemAbout) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
            alertDialog.setTitle("About the application");
            alertDialog.setMessage("\nversion: 0.1");
            alertDialog.setPositiveButton("OK", (dialog, which) -> {
            });
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public interface ProfileSignInOrRegPassListener {
        void passRegDancerId(int id);
    }
}