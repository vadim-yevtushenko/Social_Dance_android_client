package com.example.socialdance.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

import com.example.socialdance.MainActivity;
import com.example.socialdance.R;
import com.example.socialdance.model.Event;
import com.example.socialdance.model.School;
import com.example.socialdance.retrofit.DancerApi;
import com.example.socialdance.retrofit.EventApi;
import com.example.socialdance.retrofit.SchoolApi;
import com.example.socialdance.utils.CircleTextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class FragmentCreateSchoolOrEvent extends Fragment {

    private EditText etName;
    private EditText etPhone;
    private EditText etDescription;
    private EditText etAddress;
    private TextView tvDateShow;
    private Button bDate;
    private TextView tvDateToShow;
    private Button bDateTo;
    private ImageView ivSave;
    private Button bDelete;
    private ImageView ivBack;
    private ImageView ivAvatar;
    private CircleTextView ctvAvatar;
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

    private School school;
    private MainActivity activity;

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
        return view;
    }

    private void initListeners() {
        spRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (spRole.getSelectedItem().equals("School")){
                    bDate.setVisibility(View.INVISIBLE);
                    bDateTo.setVisibility(View.INVISIBLE);
                    tvDateShow.setVisibility(View.INVISIBLE);
                    tvDateToShow.setVisibility(View.INVISIBLE);
                    school = new School();
                }else {
                    bDate.setVisibility(View.VISIBLE);
                    bDateTo.setVisibility(View.VISIBLE);
                    tvDateShow.setVisibility(View.VISIBLE);
                    tvDateToShow.setVisibility(View.VISIBLE);
//                    school = new Event();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bDate.setOnClickListener(v -> {
            Calendar calendar = new GregorianCalendar();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getActivity(),
                    (DatePickerDialog.OnDateSetListener) (view1, year, month, dayOfMonth) -> {
                        tvDateShow.setText(dayOfMonth + "." + (month + 1) + "." + year);
//                        ((Event)school).setDateEvent(new GregorianCalendar(year, month, dayOfMonth).getTime());
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)

            );
            datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
            datePickerDialog.show();
        });
        bDateTo.setOnClickListener(v -> {
            Calendar calendar = new GregorianCalendar();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getActivity(),
                    (DatePickerDialog.OnDateSetListener) (view1, year, month, dayOfMonth) -> {
                        tvDateToShow.setText(dayOfMonth + "." + (month + 1) + "." + year);
//                        ((Event)school).setDateFinishEvent(new GregorianCalendar(year, month, dayOfMonth).getTime());
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)

            );
            datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
            datePickerDialog.show();
        });

        bDelete.setOnClickListener(v -> {

        });
        ivSave.setOnClickListener(v -> {

        });
        ivBack.setOnClickListener(v -> {

        });
    }

    private void initViews(View view) {
        etName = view.findViewById(R.id.etName);
        etPhone = view.findViewById(R.id.etPhone);
        etDescription = view.findViewById(R.id.etDescription);
        tvDateShow = view.findViewById(R.id.tvDateShow);
        bDate = view.findViewById(R.id.bDate);
        etAddress = view.findViewById(R.id.etCity);
        ivSave = view.findViewById(R.id.ivSave);
        ivBack = view.findViewById(R.id.ivBack);
        bDelete = view.findViewById(R.id.bCreate);
//        ivAvatar = view.findViewById(R.id.ivAvatar);
        ctvAvatar = view.findViewById(R.id.ctvAvatar);
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