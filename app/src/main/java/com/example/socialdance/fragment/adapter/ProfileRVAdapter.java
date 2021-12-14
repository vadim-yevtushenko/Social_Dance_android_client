package com.example.socialdance.fragment.adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialdance.MainActivity;
import com.example.socialdance.R;
import com.example.socialdance.fragment.FragmentProfile;
import com.example.socialdance.model.Dancer;
import com.example.socialdance.model.enums.Role;
import com.example.socialdance.utils.CircleTextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ProfileRVAdapter extends RecyclerView.Adapter<ProfileRVAdapter.ProfileRecyclerViewHolder> {

    private Context context;
    private List<Dancer> dancerList;
    private FragmentProfile fragmentProfile;
    private ArrayAdapter<Role> spinnerAdapter;
    private MainActivity activity;

    public ProfileRVAdapter(List<Dancer> dancerList, FragmentProfile fragmentProfile) {
        this.dancerList = dancerList;
        this.fragmentProfile = fragmentProfile;
    }

    @NonNull
    @Override
    public ProfileRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        activity = (MainActivity) parent.getContext();
        spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, Role.values());
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.profile_item, parent, false);
        ProfileRecyclerViewHolder viewHolder = new ProfileRecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileRVAdapter.ProfileRecyclerViewHolder holder, int position) {
        holder.spRole.setAdapter(spinnerAdapter);
        if (dancerList.size() > 0){
            holder.etName.setText(dancerList.get(position).getName());
            holder.etSurname.setText(dancerList.get(position).getSurname());
            holder.etDescription.setText(dancerList.get(position).getDescription());
            holder.tvBirthdayShow.setText(dancerList.get(position).getBirthDay().toString());
        }

        holder.bBirthday.setOnClickListener(v -> {
            Calendar calendar = new GregorianCalendar();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    context,
                    (DatePickerDialog.OnDateSetListener) (view1, year, month, dayOfMonth) -> {
                        holder.tvBirthdayShow.setText(dayOfMonth + "." + (month + 1) + "." + year);
                        dancerList.get(0).setBirthDay(new GregorianCalendar(year, month, dayOfMonth).getTime());
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)

            );
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            datePickerDialog.show();
        });
        holder.bCreate.setOnClickListener(v -> {
            activity.setFragmentCreateSchoolOrEvent();
        });
        holder.bSchoolAndEvents.setOnClickListener(v -> {
            activity.setFragmentSchoolsAndEvents();
        });
    }

    @Override
    public int getItemCount() {
        return dancerList.size();
    }

    class ProfileRecyclerViewHolder extends RecyclerView.ViewHolder{

        private EditText etName;
        private EditText etSurname;
        private EditText etDescription;
        private EditText etCity;
        private TextView tvBirthdayShow;
        private Button bBirthday;
        private Button bCreate;
        private Button bDelete;
        private Button bExit;
        private Button bSchoolAndEvents;
        private ImageView ivAvatar;
        private CircleTextView ctvAvatar;
        private RadioGroup rgSex;
        private RadioButton rbMale;
        private RadioButton rbFemale;
        private Spinner spRole;
        private CheckBox cbBachata;
        private CheckBox cbSalsa;
        private CheckBox cbKizomba;
        private CheckBox cbZouk;
        private CheckBox cbMambo;
        private CheckBox cbMerenge;
        private CheckBox cbReggaeton;
        private CheckBox cbTango;

        public ProfileRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            etName = itemView.findViewById(R.id.etName);
            etSurname = itemView.findViewById(R.id.etSurname);
            etDescription = itemView.findViewById(R.id.etDescription);
            tvBirthdayShow = itemView.findViewById(R.id.tvDateShow);
            bBirthday = itemView.findViewById(R.id.bDate);
            etCity = itemView.findViewById(R.id.etCity);
            bCreate = itemView.findViewById(R.id.bCreate);
            bDelete = itemView.findViewById(R.id.bDelete);
            bExit = itemView.findViewById(R.id.bExit);
            bSchoolAndEvents = itemView.findViewById(R.id.bSchoolAndEvents);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            ctvAvatar = itemView.findViewById(R.id.ctvAvatar);
            rgSex = itemView.findViewById(R.id.rgSex);
            rbMale = itemView.findViewById(R.id.rbMale);
            rbFemale = itemView.findViewById(R.id.rbFemale);
            spRole = itemView.findViewById(R.id.spRole);
            cbBachata = itemView.findViewById(R.id.cbBachata);
            cbSalsa = itemView.findViewById(R.id.cbSalsa);
            cbKizomba = itemView.findViewById(R.id.cbKizomba);
            cbZouk = itemView.findViewById(R.id.cbZouk);
            cbMambo = itemView.findViewById(R.id.cbMambo);
            cbMerenge = itemView.findViewById(R.id.cbMerenge);
            cbReggaeton = itemView.findViewById(R.id.cbReggaeton);
            cbTango = itemView.findViewById(R.id.cbTango);
        }
    }
}
