package com.example.socialdance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.socialdance.fragment.FragmentCreateSchoolOrEvent;
import com.example.socialdance.fragment.FragmentDancer;
import com.example.socialdance.fragment.FragmentDancersList;
import com.example.socialdance.fragment.FragmentEvent;
import com.example.socialdance.fragment.FragmentEventsList;
import com.example.socialdance.fragment.FragmentProfile;
import com.example.socialdance.fragment.FragmentProfileSignInOrReg;
import com.example.socialdance.fragment.FragmentSchool;
import com.example.socialdance.fragment.FragmentSchoolsAndEvents;
import com.example.socialdance.fragment.FragmentSchoolsList;
import com.example.socialdance.model.Dancer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FragmentDancersList.DancerPassListener,
        FragmentEventsList.EventPassListener, FragmentSchoolsList.SchoolPassListener, FragmentProfileSignInOrReg.ProfilePassListener {

    private TextView tvEvent;
    private TextView tvSchool;
    private TextView tvDancer;
    private TextView tvProfile;

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

    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferencesCheck;
    private SharedPreferences.Editor editor;
    private String PREF_REG = "reg";
    private String PREF_CHECKER = "checker";

    private List<TextView> tabs;

    private boolean onExit;
    private boolean isEntered;
    private int registeredDancerId;

    public static final String KEY_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tabs = new ArrayList<>();
        initViews();
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
        onExit = true;
        fragmentCreateSchoolOrEvent = new FragmentCreateSchoolOrEvent();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragmentCreateSchoolOrEvent)
                .commit();
    }

    public void setFragmentSchoolsAndEvents() {
        onExit = true;
        fragmentSchoolsAndEvents = new FragmentSchoolsAndEvents();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragmentSchoolsAndEvents)
                .commit();
    }

    private void initViews() {
        tvEvent = findViewById(R.id.tvEvents);
        tvSchool = findViewById(R.id.tvSchools);
        tvDancer = findViewById(R.id.tvDancers);
        tvProfile = findViewById(R.id.tvProfiles);
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

    public void changeProfile(int id){
        editor = sharedPreferencesCheck.edit();
        editor.putInt(KEY_ID, id);
        editor.apply();
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

    public void setRegisteredDancerId(int registeredDancerId) {
        this.registeredDancerId = registeredDancerId;
    }
}