package com.example.socialdance.fragment.adapter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialdance.R;
import com.example.socialdance.model.AbstractBaseEntity;
import com.example.socialdance.utils.DateTimeUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CreateSchoolOrEventRVAdapter extends RecyclerView.Adapter<CreateSchoolOrEventRVAdapter.CreateSchoolOrEventRVHolder> {

    private Context context;
    private List<AbstractBaseEntity> entityList;
    private CreateSchoolOrEventRVHolder schoolOrEventRVHolder;

    private ArrayAdapter<String> spinnerAdapter;
    private final String EVENT = "Event";
    private final String SCHOOL = "School";

    private Date dateStart;
    private Date dateFinish;

    public CreateSchoolOrEventRVAdapter() {
        entityList = new ArrayList<>();
    }

    @NonNull
    @Override
    public CreateSchoolOrEventRVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        String[] roles = {EVENT, SCHOOL};
        spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, roles);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.profile_item, parent, false);
        return new CreateSchoolOrEventRVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreateSchoolOrEventRVAdapter.CreateSchoolOrEventRVHolder holder, int position) {
        this.schoolOrEventRVHolder = holder;

        holder.getSpRole().setAdapter(spinnerAdapter);
        holder.spRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if ( holder.spRole.getSelectedItem().equals(SCHOOL)) {
                    holder.bDate.setVisibility(View.INVISIBLE);
                    holder.bDateTo.setVisibility(View.INVISIBLE);
                    holder.tvDateShow.setVisibility(View.INVISIBLE);
                    holder.tvDateToShow.setVisibility(View.INVISIBLE);
                } else {
                    holder.bDate.setVisibility(View.VISIBLE);
                    holder.bDateTo.setVisibility(View.VISIBLE);
                    holder.tvDateShow.setVisibility(View.VISIBLE);
                    holder.tvDateToShow.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.tvDescription.setOnClickListener(v -> {
            editDescription(holder.tvDescription);
        });

        holder.bDate.setOnClickListener(v -> {
            Calendar calendar = new GregorianCalendar();
            StringBuilder dateBuilder = new StringBuilder();
            StringBuilder timeBuilder = new StringBuilder();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    context,
                    (DatePickerDialog.OnDateSetListener) (view1, year, month, dayOfMonth) -> {
                        Integer m = (month + 1);
                        String monthStr = String.valueOf(m).length() > 1 ? String.valueOf(m) : "0" + m;
                        dateBuilder.append(dayOfMonth).
                                append(".").append(monthStr).
                                append(".").append(year);
                        holder.tvDateShow.setText(dateBuilder.toString() + timeBuilder.toString());
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
                    context,
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

        holder.bDateTo.setOnClickListener(v -> {
            Calendar calendar = new GregorianCalendar();
            StringBuilder dateBuilder = new StringBuilder();
            StringBuilder timeBuilder = new StringBuilder();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    context,
                    (DatePickerDialog.OnDateSetListener) (view1, year, month, dayOfMonth) -> {
                        Integer m = (month + 1);
                        String monthStr = String.valueOf(m).length() > 1 ? String.valueOf(m) : "0" + m;
                        dateBuilder.append(dayOfMonth).
                                append(".").append(monthStr).
                                append(".").append(year);
                        holder.tvDateToShow.setText(dateBuilder.toString() + timeBuilder.toString());
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
                    context,
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

    @Override
    public int getItemCount() {
        return 0;
    }

    private void editDescription(TextView tvDescription) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View viewForDialog = inflater.inflate(R.layout.dialog_write_description, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("About myself");
        alertDialog.setView(viewForDialog);
        EditText etDescription = viewForDialog.findViewById(R.id.etDescription);

        alertDialog.setPositiveButton("OK", (dialog, which) ->{
            tvDescription.setText(etDescription.getText().toString());
        });

        alertDialog.show();
    }

    public CreateSchoolOrEventRVHolder getSchoolOrEventRVHolder() {
        return schoolOrEventRVHolder;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public Date getDateFinish() {
        return dateFinish;
    }

    public String getEVENT() {
        return EVENT;
    }

    public String getSCHOOL() {
        return SCHOOL;
    }

    public class CreateSchoolOrEventRVHolder extends RecyclerView.ViewHolder{

        private EditText etName;
        private EditText etPhone;
        private TextView tvDescription;
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
        private ImageView ivAvatar;
        private Spinner spRole;
        private CheckBox cbBachata;
        private CheckBox cbSalsa;
        private CheckBox cbKizomba;
        private CheckBox cbZouk;
        private CheckBox cbMambo;
        private CheckBox cbMerenge;
        private CheckBox cbReggaeton;
        private CheckBox cbTango;

        public CreateSchoolOrEventRVHolder(@NonNull View view) {
            super(view);
            etName = view.findViewById(R.id.etName);
            tvDescription = view.findViewById(R.id.tvDescription);
            tvDateShow = view.findViewById(R.id.tvDateShow);
            bDate = view.findViewById(R.id.bDate);
            etCountry = view.findViewById(R.id.etCountry);
            etCity = view.findViewById(R.id.etCity);
            etStreet = view.findViewById(R.id.etStreet);
            etBuilding = view.findViewById(R.id.etBuilding);
            etSuite = view.findViewById(R.id.etSuite);
            etPhone = view.findViewById(R.id.etPhone);
//        etEmail = view.findViewById(R.id.etEmail);
            ivAvatar = view.findViewById(R.id.ivAvatar);
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

        public EditText getEtName() {
            return etName;
        }

        public EditText getEtPhone() {
            return etPhone;
        }

        public TextView getTvDescription() {
            return tvDescription;
        }

        public EditText getEtCountry() {
            return etCountry;
        }

        public EditText getEtCity() {
            return etCity;
        }

        public EditText getEtStreet() {
            return etStreet;
        }

        public EditText getEtBuilding() {
            return etBuilding;
        }

        public EditText getEtSuite() {
            return etSuite;
        }

        public EditText getEtEmail() {
            return etEmail;
        }

        public TextView getTvDateShow() {
            return tvDateShow;
        }

        public Button getbDate() {
            return bDate;
        }

        public TextView getTvDateToShow() {
            return tvDateToShow;
        }

        public Button getbDateTo() {
            return bDateTo;
        }

        public ImageView getIvAvatar() {
            return ivAvatar;
        }

        public Spinner getSpRole() {
            return spRole;
        }

        public CheckBox getCbBachata() {
            return cbBachata;
        }

        public CheckBox getCbSalsa() {
            return cbSalsa;
        }

        public CheckBox getCbKizomba() {
            return cbKizomba;
        }

        public CheckBox getCbZouk() {
            return cbZouk;
        }

        public CheckBox getCbMambo() {
            return cbMambo;
        }

        public CheckBox getCbMerenge() {
            return cbMerenge;
        }

        public CheckBox getCbReggaeton() {
            return cbReggaeton;
        }

        public CheckBox getCbTango() {
            return cbTango;
        }
    }
}
