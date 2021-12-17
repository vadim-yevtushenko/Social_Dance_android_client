package com.example.socialdance.fragment.adapter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialdance.R;
import com.example.socialdance.fragment.FragmentSchoolsAndEvents;
import com.example.socialdance.model.AbstractBaseEntity;
import com.example.socialdance.model.Event;
import com.example.socialdance.model.School;
import com.example.socialdance.model.enums.Dances;
import com.example.socialdance.retrofit.EventApi;
import com.example.socialdance.retrofit.NetworkService;
import com.example.socialdance.retrofit.SchoolApi;
import com.example.socialdance.utils.CircleTextView;
import com.example.socialdance.utils.DateTimeUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SchoolsAndEventsRVAdapter extends RecyclerView.Adapter<SchoolsAndEventsRVAdapter.SchoolsAndEventsRecyclerViewHolder> {
    private Context context;
    private List<AbstractBaseEntity> entityList;
    private FragmentSchoolsAndEvents fragmentSchoolsAndEvents;
    private ArrayAdapter<String> spinnerAdapter;
    private final String EVENT = "Event";
    private final String SCHOOL = "School";

    private SchoolApi schoolApi;
    private EventApi eventApi;


    public SchoolsAndEventsRVAdapter(List<AbstractBaseEntity> entityList, FragmentSchoolsAndEvents fragmentSchoolsAndEvents) {
        this.entityList = entityList;
        this.fragmentSchoolsAndEvents = fragmentSchoolsAndEvents;
        schoolApi = NetworkService.getInstance().getSchoolApi();
        eventApi = NetworkService.getInstance().getEventApi();
    }

    @NonNull
    @Override
    public SchoolsAndEventsRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.schools_and_events_item, parent, false);
        SchoolsAndEventsRecyclerViewHolder viewHolder = new SchoolsAndEventsRecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolsAndEventsRecyclerViewHolder holder, int position) {
        AbstractBaseEntity entity = entityList.get(position);

        holder.etName.setText(entity.getName());
        holder.etDescription.setText(entity.getDescription());
        if (entity.getEntityInfo() != null) {
            holder.etCountry.setText(entity.getEntityInfo().getCountry());
            holder.etCity.setText(entity.getEntityInfo().getCity());
            holder.etStreet.setText(entity.getEntityInfo().getStreet());
            holder.etBuilding.setText(entity.getEntityInfo().getBuilding());
            holder.etSuite.setText(entity.getEntityInfo().getSuites());
            holder.etPhone.setText(entity.getEntityInfo().getPhoneNumber());
        }

        setCheckBoxes(holder, position);
        if (entity instanceof Event) {
            holder.tvRole.setText(EVENT);
            holder.tvDateShow.setText(DateTimeUtils.dateTimeFormat.format(((Event)entity).getDateEvent()));
            holder.tvDateToShow.setText(DateTimeUtils.dateTimeFormat.format(((Event)entity).getDateFinishEvent()));

            holder.bDate.setOnClickListener(v -> {
                Calendar calendar = new GregorianCalendar();
                StringBuilder dateBuilder = new StringBuilder();
                StringBuilder timeBuilder = new StringBuilder();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        context,
                        (DatePickerDialog.OnDateSetListener) (view1, year, month, dayOfMonth) -> {
                            dateBuilder.append(dayOfMonth).
                                    append(".").append(month + 1).
                                    append(".").append(year);
                            holder.tvDateShow.setText(dateBuilder.toString() + timeBuilder.toString());
                            try {
                                ((Event) entity).setDateEvent(DateTimeUtils.dateTimeFormat.parse(dateBuilder.toString() + timeBuilder.toString()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Log.d("log", "date " + ((Event)entity).getDateFinishEvent());
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
                            dateBuilder.append(dayOfMonth).
                                    append(".").append(month + 1).
                                    append(".").append(year);
                            holder.tvDateToShow.setText(dateBuilder.toString() + timeBuilder.toString());
                            try {
                                ((Event)entity).setDateFinishEvent(DateTimeUtils.dateTimeFormat.parse(dateBuilder.toString() + timeBuilder.toString()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Log.d("log", "date to " + ((Event)entity).getDateFinishEvent());

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
        } else {
            holder.tvRole.setText(SCHOOL);
            holder.bDate.setVisibility(View.INVISIBLE);
            holder.bDateTo.setVisibility(View.INVISIBLE);
            holder.tvDateShow.setVisibility(View.INVISIBLE);
            holder.tvDateToShow.setVisibility(View.INVISIBLE);
        }
        holder.bDelete.setOnClickListener(v -> {
            if (entity instanceof Event){
                deleteEvent(entity.getId());
            }else {
                deleteSchool(entity.getId());
            }
            fragmentSchoolsAndEvents.createSchoolsAndEventsRecyclerView();
        });

        holder.bSave.setOnClickListener(v -> {
            AbstractBaseEntity entityForUpdate = updateEntityForSave(entity, holder);
            if (entity instanceof Event){
                saveEvent((Event) entityForUpdate);
            }else {
                saveSchool((School) entityForUpdate);
            }
        });
    }

    private void saveSchool(School school) {
        schoolApi.updateSchool(school).enqueue(new Callback<School>() {
            @Override
            public void onResponse(Call<School> call, Response<School> response) {
                School newSchool = response.body();
                if (newSchool != null){
                    Toast.makeText(fragmentSchoolsAndEvents.getActivity(), "School saved", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<School> call, Throwable t) {
                Toast.makeText(fragmentSchoolsAndEvents.getActivity(), "Error connection", Toast.LENGTH_LONG).show();
                Log.d("log", "onFailure " + t.toString());
            }
        });
    }

    private void saveEvent(Event event) {
        eventApi.updateEvent(event).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                Event newEvent = response.body();
                if (newEvent != null){
                    Toast.makeText(fragmentSchoolsAndEvents.getActivity(), "Event saved", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Toast.makeText(fragmentSchoolsAndEvents.getActivity(), "Error connection", Toast.LENGTH_LONG).show();
                Log.d("log", "onFailure " + t.toString());
            }
        });
    }

    private AbstractBaseEntity updateEntityForSave(AbstractBaseEntity entity, SchoolsAndEventsRecyclerViewHolder holder) {
        entity.setName(holder.etName.getText().toString());
        entity.getEntityInfo().setCountry(holder.etCountry.getText().toString());
        entity.getEntityInfo().setCity(holder.etCity.getText().toString());
        entity.getEntityInfo().setStreet(holder.etStreet.getText().toString());
        entity.getEntityInfo().setBuilding(holder.etBuilding.getText().toString());
        entity.getEntityInfo().setSuites(holder.etSuite.getText().toString());
        entity.getEntityInfo().setPhoneNumber(holder.etPhone.getText().toString());
        entity.setDescription(holder.etDescription.getText().toString());

        entity.getDances().clear();
        if (holder.cbSalsa.isChecked()){
            entity.getDances().add(Dances.SALSA);
        }
        if (holder.cbBachata.isChecked()){
            entity.getDances().add(Dances.BACHATA);
        }
        if (holder.cbKizomba.isChecked()){
            entity.getDances().add(Dances.KIZOMBA);
        }
        if (holder.cbZouk.isChecked()){
            entity.getDances().add(Dances.ZOUK);
        }
        if (holder.cbReggaeton.isChecked()){
            entity.getDances().add(Dances.REGGAETON);
        }
        if (holder.cbMerenge.isChecked()){
            entity.getDances().add(Dances.MERENGE);
        }
        if (holder.cbMambo.isChecked()){
            entity.getDances().add(Dances.MAMBO);
        }
        if (holder.cbTango.isChecked()){
            entity.getDances().add(Dances.TANGO);
        }

        if (entity instanceof Event){
            ((Event)entity).setDatePublication(new Date());
        }
        return entity;
    }

    private void deleteEvent(int id) {
        eventApi.deleteEvent(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(fragmentSchoolsAndEvents.getActivity(), "Your event deleted", Toast.LENGTH_LONG).show();
                fragmentSchoolsAndEvents.downloadSchool();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(fragmentSchoolsAndEvents.getActivity(), "Error connection", Toast.LENGTH_LONG).show();
                Log.d("log", "onFailure " + t.toString());
            }
        });
    }

    private void deleteSchool(int id) {
        schoolApi.deleteSchool(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(fragmentSchoolsAndEvents.getActivity(), "Your school deleted", Toast.LENGTH_LONG).show();
                fragmentSchoolsAndEvents.downloadSchool();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(fragmentSchoolsAndEvents.getActivity(), "Error connection", Toast.LENGTH_LONG).show();
                Log.d("log", "onFailure " + t.toString());
            }
        });
    }

    private void setCheckBoxes(SchoolsAndEventsRecyclerViewHolder holder, int position) {
        if (entityList.get(position).getDances().contains(Dances.SALSA)) {
            holder.cbSalsa.setChecked(true);
        }

        if (entityList.get(position).getDances().contains(Dances.BACHATA)) {
            holder.cbBachata.setChecked(true);
        }

        if (entityList.get(position).getDances().contains(Dances.KIZOMBA)) {
            holder.cbKizomba.setChecked(true);
        }

        if (entityList.get(position).getDances().contains(Dances.ZOUK)) {
            holder.cbZouk.setChecked(true);
        }

        if (entityList.get(position).getDances().contains(Dances.REGGAETON)) {
            holder.cbReggaeton.setChecked(true);
        }

        if (entityList.get(position).getDances().contains(Dances.MERENGE)) {
            holder.cbMerenge.setChecked(true);
        }

        if (entityList.get(position).getDances().contains(Dances.MAMBO)) {
            holder.cbMambo.setChecked(true);
        }

        if (entityList.get(position).getDances().contains(Dances.TANGO)) {
            holder.cbTango.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return entityList.size();
    }

    class SchoolsAndEventsRecyclerViewHolder extends RecyclerView.ViewHolder {

        private EditText etName;
        private EditText etDescription;
        private EditText etPhone;
        private EditText etCountry;
        private EditText etCity;
        private EditText etStreet;
        private EditText etBuilding;
        private EditText etSuite;
        private TextView tvDateShow;
        private Button bDate;
        private TextView tvDateToShow;
        private Button bDateTo;
        private Button bDelete;
        private Button bSave;
        //        private ImageView ivAvatar;
        private CircleTextView ctvAvatar;
        private TextView tvRole;
        private CheckBox cbBachata;
        private CheckBox cbSalsa;
        private CheckBox cbKizomba;
        private CheckBox cbZouk;
        private CheckBox cbMambo;
        private CheckBox cbMerenge;
        private CheckBox cbReggaeton;
        private CheckBox cbTango;

        public SchoolsAndEventsRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            etName = itemView.findViewById(R.id.etName);
            etPhone = itemView.findViewById(R.id.etPhone);
            etDescription = itemView.findViewById(R.id.etDescription);
            tvDateShow = itemView.findViewById(R.id.tvDateShow);
            bDate = itemView.findViewById(R.id.bDate);
            etCountry = itemView.findViewById(R.id.etCountry);
            etCity = itemView.findViewById(R.id.etCity);
            etStreet = itemView.findViewById(R.id.etStreet);
            etBuilding = itemView.findViewById(R.id.etBuilding);
            etSuite = itemView.findViewById(R.id.etSuite);
            bDelete = itemView.findViewById(R.id.bDelete);
            bSave = itemView.findViewById(R.id.bSave);
//            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            ctvAvatar = itemView.findViewById(R.id.ctvAvatar);
            tvRole = itemView.findViewById(R.id.tvRole);
            cbBachata = itemView.findViewById(R.id.cbBachata);
            cbSalsa = itemView.findViewById(R.id.cbSalsa);
            cbKizomba = itemView.findViewById(R.id.cbKizomba);
            cbZouk = itemView.findViewById(R.id.cbZouk);
            cbMambo = itemView.findViewById(R.id.cbMambo);
            cbMerenge = itemView.findViewById(R.id.cbMerenge);
            cbReggaeton = itemView.findViewById(R.id.cbReggaeton);
            cbTango = itemView.findViewById(R.id.cbTango);
            tvDateToShow = itemView.findViewById(R.id.tvDateToShow);
            bDateTo = itemView.findViewById(R.id.bDateTo);
        }
    }
}
