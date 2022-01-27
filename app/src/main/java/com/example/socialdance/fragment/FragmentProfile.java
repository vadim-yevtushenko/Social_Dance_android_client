package com.example.socialdance.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
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
import android.widget.Button;
import android.widget.EditText;
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

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.socialdance.MainActivity.TOAST_Y_GRAVITY;
import static com.example.socialdance.utils.UploadImageUtils.getCompressImage;
import static com.example.socialdance.utils.UploadImageUtils.getPath;


public class FragmentProfile extends Fragment {

    private ImageView ivSave;
    private RecyclerView rvProfile;
    private ProfileRVAdapter profileRVAdapter;
    private List<Dancer> dancerList;
    private Dancer dancer;
    private int id;

    private ImagePassListener imagePassListener;
    private MainActivity activity;
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
        activity = (MainActivity) getActivity();
        imagePassListener = activity;
        initViews(view);
        if (getArguments() != null){
            id = getArguments().getInt(MainActivity.KEY_ID);
        }
        dancerList = new ArrayList<>();
        downloadDancer();
        createProfileRecyclerView();
        ivSave.setOnClickListener(this::save);
        setHasOptionsMenu(true);
        return view;
    }

    private void downloadDancer() {
        activity.getPbConnect().setVisibility(View.VISIBLE);
        dancerApi.getDancerById(id).enqueue(new Callback<Dancer>() {
            @Override
            public void onResponse(Call<Dancer> call, Response<Dancer> response) {
                dancer = response.body();
                if (dancer == null || dancer.getId() == null){
                    exitProfile();
                    return;
                }
                dancerList.add(dancer);
                activity.getPbConnect().setVisibility(View.INVISIBLE);
                profileRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Dancer> call, Throwable t) {
                activity.getPbConnect().setVisibility(View.INVISIBLE);
                try {
                    Toast toast = Toast.makeText(getActivity(), "Error connection", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void exitProfile(){
        activity.setEntered(false);
        activity.changeProfile(0);
        activity.setProfile();
    }

    private void save(View view) {
        createDancerForSave();
        activity.getPbConnect().setVisibility(View.VISIBLE);
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
                activity.getPbConnect().setVisibility(View.INVISIBLE);
                Toast toast = Toast.makeText(getActivity(), "SAVED", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                toast.show();
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

    private void createDancerForSave() {

        ProfileRVAdapter.ProfileRecyclerViewHolder holder = profileRVAdapter.getHolderGlobal();

        dancer.setName(holder.getEtName().getText().toString());
        dancer.setSurname(holder.getEtSurname().getText().toString());
        dancer.setDescription(holder.getTvDescription().getText().toString());
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvProfile.setLayoutManager(linearLayoutManager);
        rvProfile.setAdapter(profileRVAdapter);
    }

    public void setAvatar(){
        Button button = new Button(activity);
        button.setText("Open gallery");
        button.setOnClickListener(v1 -> {
            imagePassListener.uploadPicture();
        });
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity).
                setTitle("\t\t\t\t\t\tSet the avatar!").
                setView(button).
                setPositiveButton("OK", (dialog, which) -> {
                    if (activity.getImage() != null) {
                        profileRVAdapter.notifyDataSetChanged();
                        File image = new File(getPath(activity.getImage(), activity));
                        byte[] byteArray = getCompressImage(getPath(activity.getImage(), activity));
                        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), byteArray);
                        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", image.getName(), requestBody);
                        dancerApi.uploadImage(id, fileToUpload).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                profileRVAdapter.notifyDataSetChanged();
                                activity.setImage(null);
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                            }
                        });
                    }
                }).
                setNegativeButton("DELETE", (dialog, which) -> {
                    activity.setImage(null);
                    dancerApi.deleteImage(id).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            profileRVAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                        }
                    });
                }).
                setNeutralButton("CANCEL", (dialog, which) -> {
                });
        alertDialog.show();
    }

    private void initViews(View view) {
        ivSave = view.findViewById(R.id.ivSave);
        rvProfile = view.findViewById(R.id.rvProfile);
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
            alertDialog.setMessage("\nversion: 1.0");
            alertDialog.setPositiveButton("OK", (dialog, which) -> {
            });
            alertDialog.show();
        } else if (itemId == R.id.itemChangePassword){
            dialogForChangePassword();
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialogForChangePassword() {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View viewForDialog = inflater.inflate(R.layout.dialog_change_password, null);
        EditText etDialogOldPassword = viewForDialog.findViewById(R.id.etDialogOldPassword);
        EditText etDialogNewPassword1 = viewForDialog.findViewById(R.id.etDialogNewPassword1);
        EditText etDialogNewPassword2 = viewForDialog.findViewById(R.id.etDialogNewPassword2);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle("Change password");
        alertDialog.setView(viewForDialog);
        alertDialog.setPositiveButton("OK", (dialog, which) ->{
            changePassword(etDialogOldPassword.getText().toString(),
                    etDialogNewPassword1.getText().toString(),
                    etDialogNewPassword2.getText().toString());
        });

        alertDialog.setNeutralButton("CANCEL", (dialog, which) -> {});

        alertDialog.show();
    }

    private void changePassword(String oldPassword, String newPassword1, String newPassword2) {
        if (newPassword1.length() < 3) {
            Toast toast = Toast.makeText(getActivity(), "New password too short", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
            toast.show();
            dialogForChangePassword();
        }else if (!newPassword1.equals(newPassword2)){
            Toast toast = Toast.makeText(getActivity(), "Password 1 and password 2 do not match", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
            toast.show();
            dialogForChangePassword();
        }else {
            dancerApi.checkSignInByEmailAndPassword(dancer.getEntityInfo().getEmail(), oldPassword).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    Integer id = response.body();
                    if (id == null || id < 1) {
                        Toast toast = Toast.makeText(getActivity(), "Wrong old password", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                        toast.show();
                        dialogForChangePassword();
                    } else {
                        dancerApi.changePassword(dancer.getEntityInfo().getEmail(), newPassword1).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Toast toast;
                                if (response.body().equals("changed")) {
                                    toast = Toast.makeText(getActivity(), "Password changed successfully", Toast.LENGTH_LONG);
                                } else {
                                    toast = Toast.makeText(getActivity(), "Password not changed", Toast.LENGTH_LONG);
                                }
                                toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                                toast.show();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {

                }
            });
        }

    }

    public boolean isTeacher(){
        if (dancer == null || dancer.getRole() == null){
            return false;
        }
        return dancer.getRole() == Role.TEACHER;
    }

    public Dancer getDancer() {
        return dancer;
    }

    public DancerApi getDancerApi() {
        return dancerApi;
    }

}