package com.example.socialdance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialdance.fragment.FragmentCreateSchoolOrEvent;
import com.example.socialdance.fragment.FragmentDancer;
import com.example.socialdance.fragment.FragmentDancersList;
import com.example.socialdance.fragment.FragmentEvent;
import com.example.socialdance.fragment.FragmentEventsList;
import com.example.socialdance.fragment.FragmentProfile;
import com.example.socialdance.fragment.FragmentProfileSignInOrReg;
import com.example.socialdance.fragment.FragmentReviewsAndRating;
import com.example.socialdance.fragment.FragmentSchool;
import com.example.socialdance.fragment.FragmentSchoolsAndEvents;
import com.example.socialdance.fragment.FragmentSchoolsList;
import com.example.socialdance.fragment.ImagePassListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FragmentDancersList.DancerPassListener,
        FragmentEventsList.EventPassListener, FragmentSchoolsList.SchoolPassListener,
        FragmentProfileSignInOrReg.ProfileSignInOrRegPassListener,
        FragmentSchool.SchoolForReviewPassListener, ImagePassListener {

    private TextView tvEvent;
    private TextView tvSchool;
    private TextView tvDancer;
    private TextView tvProfile;
    private ProgressBar pbConnect;

    private FragmentEventsList fragmentEventsList;
    private FragmentEvent fragmentEvent;
    private FragmentSchoolsList fragmentSchoolsList;
    private FragmentSchool fragmentSchool;
    private FragmentDancersList fragmentDancersList;
    private FragmentDancer fragmentDancer;
    private FragmentProfile fragmentProfile;
    private FragmentProfileSignInOrReg fragmentProfileSignInOrReg;
    private FragmentCreateSchoolOrEvent fragmentCreateSchoolOrEvent;
    private FragmentSchoolsAndEvents fragmentSchoolsAndEvents;
    private FragmentReviewsAndRating fragmentReviewsAndRating;

    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferencesCheck;
    private SharedPreferences.Editor editor;
    private String PREF_REG = "reg";
    private String PREF_CHECKER = "checker";
    public final static int TOAST_Y_GRAVITY = 500;
    private static final int REQUEST_CODE_GALLERY_ACTIVITY = 1;

    private List<TextView> tabs;
    private Uri image;

    private boolean onExit;
    private boolean isEntered;
    private int registeredDancerId;

    public static final String KEY_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Log.d("log", "onCreate " );
        tabs = new ArrayList<>();
        initViews();
        pbConnect.setVisibility(View.INVISIBLE);
        addViewsToList();
        initListeners();
        sharedPreferencesCheck = getSharedPreferences(PREF_CHECKER, MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(PREF_REG, MODE_PRIVATE);
        registeredDancerId = sharedPreferencesCheck.getInt(KEY_ID, 0);
        if (registeredDancerId == 0){
            isEntered = false;
        }else {
            isEntered = true;
        }

        tvEvent.setBackgroundColor(Color.parseColor("#FF7800"));
        tvEvent.setTextColor(Color.parseColor("#FFFFFFFF"));
        setFragmentEventsList();
    }

    private void initListeners() {
        tvEvent.setOnClickListener(this::setEvents);
        tvSchool.setOnClickListener(this::setSchools);
        tvDancer.setOnClickListener(this::setDancers);
        tvProfile.setOnClickListener(v -> setProfile());
    }

    public void setProfile() {
        setColor(tvProfile);
        if (isEntered){
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_ID, registeredDancerId);
            setFragmentProfile();
            fragmentProfile.setArguments(bundle);
        }else {
            setFragmentProfileSignOrReg();
        }
    }



    private void setDancers(View view) {
        setColor(tvDancer);
        setFragmentDancersList();
    }

    private void setSchools(View view) {
        setColor(tvSchool);
        setFragmentSchoolsList();
    }

    private void setEvents(View view) {
        setColor(tvEvent);
        setFragmentEventsList();

    }


    private void setColor(TextView textView) {
        textView.setEnabled(false);
        textView.setBackgroundColor(Color.parseColor("#FF7800"));
        textView.setTextColor(Color.parseColor("#FFFFFFFF"));;
        for (TextView tab : tabs){
            if (!tab.equals(textView)){
                tab.setEnabled(true);
                tab.setBackgroundColor(Color.parseColor("#BCBABA"));
                tab.setTextColor(Color.parseColor("#FF000000"));
            }
        }
    }

    private void addViewsToList() {
        tabs.add(tvEvent);
        tabs.add(tvSchool);
        tabs.add(tvDancer);
        tabs.add(tvProfile);
    }

    public void setFragmentEventsList() {
        onExit = true;
        fragmentEventsList = new FragmentEventsList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragmentEventsList)
                .commit();
    }

    public void setFragmentEvent() {
        onExit = false;
        fragmentEvent = new FragmentEvent();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragmentEvent)
                .commit();
    }

    public void setFragmentSchoolsList() {
        onExit = true;
        fragmentSchoolsList = new FragmentSchoolsList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragmentSchoolsList)
                .commit();
    }

    public void setFragmentSchool() {
        onExit = false;
        fragmentSchool = new FragmentSchool();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragmentSchool)
                .commit();
    }

    public void setFragmentDancersList() {
        onExit = true;
        fragmentDancersList = new FragmentDancersList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragmentDancersList)
                .commit();
    }

    public void setFragmentDancer() {
        onExit = false;
        fragmentDancer = new FragmentDancer();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragmentDancer)
                .commit();
    }

    public void setFragmentProfile() {
        onExit = true;
        fragmentProfile = new FragmentProfile();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragmentProfile)
                .commit();
    }

    public void setFragmentProfileSignOrReg() {
        onExit = true;
        fragmentProfileSignInOrReg = new FragmentProfileSignInOrReg();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragmentProfileSignInOrReg)
                .commit();
    }

    public void setFragmentCreateSchoolOrEvent() {
        onExit = false;
        fragmentCreateSchoolOrEvent = new FragmentCreateSchoolOrEvent();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragmentCreateSchoolOrEvent)
                .commit();
    }

    public void setFragmentSchoolsAndEvents() {
        onExit = false;
        fragmentSchoolsAndEvents = new FragmentSchoolsAndEvents();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragmentSchoolsAndEvents)
                .commit();
    }

    private void setFragmentReviewsAndRating() {
        onExit = false;
        fragmentReviewsAndRating = new FragmentReviewsAndRating();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragmentReviewsAndRating)
                .commit();
    }

    private void initViews() {
        tvEvent = findViewById(R.id.tvEvents);
        tvSchool = findViewById(R.id.tvSchools);
        tvDancer = findViewById(R.id.tvDancers);
        tvProfile = findViewById(R.id.tvProfiles);
        pbConnect = findViewById(R.id.pbConnect);
    }

    @Override
    public void onBackPressed() {
        if (onExit) {
            finish();
        } else {

        }
    }

    @Override
    public void passDancerId(int id) {
        setFragmentDancer();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_ID, id);
        fragmentDancer.setArguments(bundle);
    }

    @Override
    public void passEventId(int id) {
        setFragmentEvent();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_ID, id);
        fragmentEvent.setArguments(bundle);
    }

    @Override
    public void passSchoolId(int id) {
        setFragmentSchool();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_ID, id);
        fragmentSchool.setArguments(bundle);
    }

    @Override
    public void passRegDancerId(int id) {
        registeredDancerId = id;
        changeProfile(registeredDancerId);
        isEntered = true;
        setFragmentProfile();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_ID, id);
        fragmentProfile.setArguments(bundle);
    }

    @Override
    public void passSchoolFroReviewId(int id) {
        setFragmentReviewsAndRating();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_ID, id);
        fragmentReviewsAndRating.setArguments(bundle);
    }

    public void changeProfile(int id){
        editor = sharedPreferencesCheck.edit();
        editor.putInt(KEY_ID, id);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public Uri uploadPicture() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_GALLERY_ACTIVITY);
        } else {
            requestStoragePermission();
        }
        return image;
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed")
                    .setPositiveButton("OK", (dialog, which) -> ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY_ACTIVITY))
                    .setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss())
                    .create().show();
        }else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY_ACTIVITY);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_GALLERY_ACTIVITY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast toast = Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                toast.show();
            } else {
                Toast toast = Toast.makeText(this, "Permission DENIED", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                toast.show();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GALLERY_ACTIVITY) {
            if (resultCode == RESULT_OK && data != null) {
                image = data.getData();
                Log.d("log", "onActivityResult " + image);
            }
        }
    }



    public boolean isOnExit() {
        return onExit;
    }

    public void setOnExit(boolean onExit) {
        this.onExit = onExit;
    }

    public boolean isEntered() {
        return isEntered;
    }

    public void setEntered(boolean entered) {
        isEntered = entered;
    }

    public int getRegisteredDancerId() {
        return registeredDancerId;
    }

    public ProgressBar getPbConnect() {
        return pbConnect;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }
}