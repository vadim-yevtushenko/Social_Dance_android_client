package com.example.socialdance.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.socialdance.R;


public class FragmentProfileSignInOrReg extends Fragment {

    private EditText etEmail;
    private EditText etPassword;
    private EditText etEmailReg;
    private EditText etPasswordReg;
    private EditText etPassRetype;
    private Button bSignIn;
    private Button bSignUp;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_sign_in_or_reg, container, false);
        return view;
    }
}