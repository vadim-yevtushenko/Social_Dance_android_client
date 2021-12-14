package com.example.socialdance.fragment.adapter;

import android.app.DatePickerDialog;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialdance.R;
import com.example.socialdance.fragment.FragmentProfile;
import com.example.socialdance.model.Event;
import com.example.socialdance.model.School;
import com.example.socialdance.utils.CircleTextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class SchoolsAndEventsRVAdapter extends RecyclerView.Adapter<SchoolsAndEventsRVAdapter.SchoolsAndEventsRecyclerViewHolder>{
    private Context context;
    private List<School> schoolList;
    private FragmentProfile fragmentProfile;
    private ArrayAdapter<String> spinnerAdapter;
    private final String EVENT = "Event";
    private final String SCHOOL = "School";



    public SchoolsAndEventsRVAdapter(List<School> schoolList) {
        this.schoolList = schoolList;
        this.fragmentProfile = fragmentProfile;
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
        School school = schoolList.get(position);
        holder.spRole.setAdapter(spinnerAdapter);


        if (holder.spRole.getSelectedItem().equals("School")){
            holder.bDate.setVisibility(View.INVISIBLE);
            holder.bDateTo.setVisibility(View.INVISIBLE);
            holder.tvDateShow.setVisibility(View.INVISIBLE);
            holder.tvDateToShow.setVisibility(View.INVISIBLE);
        }
        if (schoolList.size() > 0){
            holder.etName.setText(school.getName());
            holder.etDescription.setText(school.getDescription());
        }

//        if (school instanceof Event) {
//            holder.bDate.setOnClickListener(v -> {
//                Calendar calendar = new GregorianCalendar();
//                DatePickerDialog datePickerDialog = new DatePickerDialog(
//                        context,
//                        (DatePickerDialog.OnDateSetListener) (view1, year, month, dayOfMonth) -> {
//                            holder.tvDateShow.setText(dayOfMonth + "." + (month + 1) + "." + year);
//                            ((Event)school).setDateEvent(new GregorianCalendar(year, month, dayOfMonth).getTime());
//                        },
//                        calendar.get(Calendar.YEAR),
//                        calendar.get(Calendar.MONTH),
//                        calendar.get(Calendar.DAY_OF_MONTH)
//
//                );
//                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
//                datePickerDialog.show();
//            });
//        }
        holder.bDelete.setOnClickListener(v -> {
            fragmentProfile.createProfileRecyclerView();
        });
    }

    @Override
    public int getItemCount() {
        return schoolList.size();
    }

    class SchoolsAndEventsRecyclerViewHolder extends RecyclerView.ViewHolder{

        private EditText etName;
        private EditText etPhone;
        private EditText etDescription;
        private EditText etAddress;
        private TextView tvDateShow;
        private Button bDate;
        private TextView tvDateToShow;
        private Button bDateTo;
        private Button bCreate;
        private Button bDelete;
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

        public SchoolsAndEventsRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            etName = itemView.findViewById(R.id.etName);
            etPhone = itemView.findViewById(R.id.etPhone);
            etDescription = itemView.findViewById(R.id.etDescription);
            tvDateShow = itemView.findViewById(R.id.tvDateShow);
            bDate = itemView.findViewById(R.id.bDate);
            etAddress = itemView.findViewById(R.id.etCity);
            bCreate = itemView.findViewById(R.id.bCreate);
            bDelete = itemView.findViewById(R.id.bCreate);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            ctvAvatar = itemView.findViewById(R.id.ctvAvatar);
            spRole = itemView.findViewById(R.id.spRole);
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
