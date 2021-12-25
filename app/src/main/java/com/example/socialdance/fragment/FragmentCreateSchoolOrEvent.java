package com.example.socialdance.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialdance.MainActivity;
import com.example.socialdance.R;
import com.example.socialdance.model.EntityInfo;
import com.example.socialdance.model.Event;
import com.example.socialdance.model.School;
import com.example.socialdance.model.enums.Dances;
import com.example.socialdance.retrofit.EventApi;
import com.example.socialdance.retrofit.NetworkService;
import com.example.socialdance.retrofit.SchoolApi;
import com.example.socialdance.utils.DateTimeUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.socialdance.MainActivity.*;


public class FragmentCreateSchoolOrEvent extends Fragment {

    private EditText etName;
    private EditText etPhone;
    private EditText etDescription;
    private EditText etCountry;
    private EditText etCity;
    private EditText etStreet;
    private EditText etBuilding;
    private EditText etSuite;
    private EditText etEmail;
    private TextView tvDateShow;
    private Button bDate;
    private TextView tvDateToShow;
    private Button bDateTo;
    private ImageView ivSave;
    private ImageView ivBack;
    //    private ImageView ivAvatar;
//    private CircleTextView ctvAvatar;
    private Spinner spRole;
    private CheckBox cbBachata;
    private CheckBox cbSalsa;
    private CheckBox cbKizomba;
    private CheckBox cbZouk;
    private CheckBox cbMambo;
    private CheckBox cbMerenge;
    private CheckBox cbReggaeton;
    private CheckBox cbTango;


    private SchoolApi schoolApi;
    private EventApi eventApi;

    private ArrayAdapter<String> spinnerAdapter;
    private final String EVENT = "Event";
    private final String SCHOOL = "School";

    private Date dateStart;
    private Date dateFinish;

    private MainActivity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        schoolApi = NetworkService.getInstance().getSchoolApi();
        eventApi = NetworkService.getInstance().getEventApi();
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_create_school_or_event, container, false);
        initViews(view);
        String[] roles = {EVENT, SCHOOL};
        spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, roles);
        spRole.setAdapter(spinnerAdapter);
        initListeners();
        ivSave.setOnClickListener(this::create);
        ivBack.setOnClickListener(this::back);
        return view;
    }

    private void back(View view) {
        activity.setProfile();
    }

    private void create(View view) {
        if (spRole.getSelectedItem().equals(EVENT)) {
            createEvent();
        } else if (spRole.getSelectedItem().equals(SCHOOL)) {
            createSchool();
        }

    }

    private void createSchool() {

        School school = prepareSchoolForCreate();
        if (school.getName() == null || school.getName().isEmpty()) {
            Toast toast = Toast.makeText(activity, "Enter the name school", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
            toast.show();
            return;
        }
        activity.getPbConnect().setVisibility(View.VISIBLE);
        schoolApi.createSchool(school).enqueue(new Callback<School>() {
            @Override
            public void onResponse(Call<School> call, Response<School> response) {

                School newSchool = response.body();

                if (newSchool != null) {
                    Toast toast = Toast.makeText(activity, "School saved successfully", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(activity, "School not saved", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                    toast.show();
                }
                activity.getPbConnect().setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<School> call, Throwable t) {
                activity.getPbConnect().setVisibility(View.INVISIBLE);
                Toast toast = Toast.makeText(activity, "Error connection", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                toast.show();
            }
        });

    }

    private School prepareSchoolForCreate() {
        School school = new School();
        school.setOwnerId(activity.getRegisteredDancerId());
        EntityInfo entityInfo = new EntityInfo(etCountry.getText().toString(), etCity.getText().toString(),
                etStreet.getText().toString(), etBuilding.getText().toString(), etSuite.getText().toString(),
                etPhone.getText().toString(), null);
        school.setName(etName.getText().toString());
        school.setDescription(etDescription.getText().toString());
        school.setEntityInfo(entityInfo);
        school.setDances(new ArrayList<>());
        if (cbSalsa.isChecked()) {
            school.getDances().add(Dances.SALSA);
        }
        if (cbBachata.isChecked()) {
            school.getDances().add(Dances.BACHATA);
        }
        if (cbKizomba.isChecked()) {
            school.getDances().add(Dances.KIZOMBA);
        }
        if (cbZouk.isChecked()) {
            school.getDances().add(Dances.ZOUK);
        }
        if (cbReggaeton.isChecked()) {
            school.getDances().add(Dances.REGGAETON);
        }
        if (cbMerenge.isChecked()) {
            school.getDances().add(Dances.MERENGE);
        }
        if (cbMambo.isChecked()) {
            school.getDances().add(Dances.MAMBO);
        }
        if (cbTango.isChecked()) {
            school.getDances().add(Dances.TANGO);
        }
        return school;
    }

    private void createEvent() {
        Event event = prepareEventForCreate();
        if (event.getName() == null || event.getName().isEmpty()) {
            Toast toast = Toast.makeText(activity, "Enter the name event", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
            toast.show();
            return;
        }
        if (event.getDateEvent() == null) {
            Toast toast = Toast.makeText(activity, "Enter the date event", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
            toast.show();
            return;
        }
        activity.getPbConnect().setVisibility(View.VISIBLE);
        eventApi.createEvent(event).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {

                Event newEvent = response.body();

                if (newEvent != null) {
                    Toast toast = Toast.makeText(activity, "Event saved successfully", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(activity, "Event not saved", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                    toast.show();
                }
                activity.getPbConnect().setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                activity.getPbConnect().setVisibility(View.INVISIBLE);
                Toast toast = Toast.makeText(activity, "Error connection", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                toast.show();
            }
        });
    }

    private Event prepareEventForCreate() {
        Event event = new Event();
        event.setOwnerId(activity.getRegisteredDancerId());
        EntityInfo entityInfo = new EntityInfo(etCountry.getText().toString(), etCity.getText().toString(),
                etStreet.getText().toString(), etBuilding.getText().toString(), etSuite.getText().toString(),
                etPhone.getText().toString(), null);
        event.setName(etName.getText().toString());
        event.setDescription(etDescription.getText().toString());
        event.setEntityInfo(entityInfo);
        event.setDances(new ArrayList<>());
        if (cbSalsa.isChecked()) {
            event.getDances().add(Dances.SALSA);
        }
        if (cbBachata.isChecked()) {
            event.getDances().add(Dances.BACHATA);
        }
        if (cbKizomba.isChecked()) {
            event.getDances().add(Dances.KIZOMBA);
        }
        if (cbZouk.isChecked()) {
            event.getDances().add(Dances.ZOUK);
        }
        if (cbReggaeton.isChecked()) {
            event.getDances().add(Dances.REGGAETON);
        }
        if (cbMerenge.isChecked()) {
            event.getDances().add(Dances.MERENGE);
        }
        if (cbMambo.isChecked()) {
            event.getDances().add(Dances.MAMBO);
        }
        if (cbTango.isChecked()) {
            event.getDances().add(Dances.TANGO);
        }
        event.setDatePublication(new Date());
        event.setDateEvent(dateStart);
        event.setDateFinishEvent(dateFinish);
        return event;
    }

    private void initListeners() {
        spRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (spRole.getSelectedItem().equals(SCHOOL)) {
                    bDate.setVisibility(View.INVISIBLE);
                    bDateTo.setVisibility(View.INVISIBLE);
                    tvDateShow.setVisibility(View.INVISIBLE);
                    tvDateToShow.setVisibility(View.INVISIBLE);
                } else {
                    bDate.setVisibility(View.VISIBLE);
                    bDateTo.setVisibility(View.VISIBLE);
                    tvDateShow.setVisibility(View.VISIBLE);
                    tvDateToShow.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bDate.setOnClickListener(v -> {
            Calendar calendar = new GregorianCalendar();
            StringBuilder dateBuilder = new StringBuilder();
            StringBuilder timeBuilder = new StringBuilder();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getActivity(),
                    (DatePickerDialog.OnDateSetListener) (view1, year, month, dayOfMonth) -> {
                        Integer m = (month + 1);
                        String monthStr = String.valueOf(m).length() > 1 ? String.valueOf(m) : "0" + m;
                        dateBuilder.append(dayOfMonth).
                                append(".").append(monthStr).
                                append(".").append(year);
                        tvDateShow.setText(dateBuilder.toString() + timeBuilder.toString());
                        try {
                            dateStart = DateTimeUtils.dateTimeFormat
                                    .parse(dateBuilder.toString() + timeBuilder.toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)

            );
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    getActivity(),
                    (view, hourOfDay, minute) -> {
                        timeBuilder.append(" ").
                                append(String.valueOf(hourOfDay).length() == 1 ? "0" + hourOfDay : hourOfDay).
                                append(":").append(String.valueOf(minute).length() == 1 ? "0" + minute : minute);
                    },
                    calendar.get(Calendar.HOUR),
                    calendar.get(Calendar.MINUTE),
                    true
            );
            datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
            datePickerDialog.show();
            timePickerDialog.show();
        });

        bDateTo.setOnClickListener(v -> {
            Calendar calendar = new GregorianCalendar();
            StringBuilder dateBuilder = new StringBuilder();
            StringBuilder timeBuilder = new StringBuilder();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getActivity(),
                    (DatePickerDialog.OnDateSetListener) (view1, year, month, dayOfMonth) -> {
                        Integer m = (month + 1);
                        String monthStr = String.valueOf(m).length() > 1 ? String.valueOf(m) : "0" + m;
                        dateBuilder.append(dayOfMonth).
                                append(".").append(monthStr).
                                append(".").append(year);
                        tvDateToShow.setText(dateBuilder.toString() + timeBuilder.toString());
                        try {
                            dateFinish = DateTimeUtils.dateTimeFormat
                                    .parse(dateBuilder.toString() + timeBuilder.toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)

            );
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    getActivity(),
                    (view, hourOfDay, minute) -> {
                        timeBuilder.append(" ").
                                append(String.valueOf(hourOfDay).length() == 1 ? "0" + hourOfDay : hourOfDay).
                                append(":").append(String.valueOf(minute).length() == 1 ? "0" + minute : minute);
                    },
                    calendar.get(Calendar.HOUR),
                    calendar.get(0),
                    true
            );
            datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
            datePickerDialog.show();
            timePickerDialog.show();
        });
    }

    private void initViews(View view) {
        etName = view.findViewById(R.id.etName);
        etDescription = view.findViewById(R.id.etDescription);
        tvDateShow = view.findViewById(R.id.tvDateShow);
        bDate = view.findViewById(R.id.bDate);
        etCountry = view.findViewById(R.id.etCountry);
        etCity = view.findViewById(R.id.etCity);
        etStreet = view.findViewById(R.id.etStreet);
        etBuilding = view.findViewById(R.id.etBuilding);
        etSuite = view.findViewById(R.id.etSuite);
        etPhone = view.findViewById(R.id.etPhone);
//        etEmail = view.findViewById(R.id.etEmail);
        ivSave = view.findViewById(R.id.ivSave);
        ivBack = view.findViewById(R.id.ivBack);
//        ivAvatar = view.findViewById(R.id.ivAvatar);
//        ctvAvatar = view.findViewById(R.id.ctvAvatar);
        spRole = view.findViewById(R.id.spRole);
        cbBachata = view.findViewById(R.id.cbBachata);
        cbSalsa = view.findViewById(R.id.cbSalsa);
        cbKizomba = view.findViewById(R.id.cbKizomba);
        cbZouk = view.findViewById(R.id.cbZouk);
        cbMambo = view.findViewById(R.id.cbMambo);
        cbMerenge = view.findViewById(R.id.cbMerenge);
        cbReggaeton = view.findViewById(R.id.cbReggaeton);
        cbTango = view.findViewById(R.id.cbTango);
        tvDateToShow = view.findViewById(R.id.tvDateToShow);
        bDateTo = view.findViewById(R.id.bDateTo);
    }
}