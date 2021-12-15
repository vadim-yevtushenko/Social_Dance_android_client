package com.example.socialdance.fragment.adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
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
import com.example.socialdance.model.enums.Dances;
import com.example.socialdance.model.enums.Role;
import com.example.socialdance.utils.CircleTextView;
import com.example.socialdance.utils.DateTimeUtils;

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
    private ProfileRecyclerViewHolder holderGlobal;

    public ProfileRVAdapter(List<Dancer> dancerList, FragmentProfile fragmentProfile) {
        this.dancerList = dancerList;
        this.fragmentProfile = fragmentProfile;
    }

    @NonNull
    @Override
    public ProfileRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        activity = (MainActivity) parent.getContext();
        Log.d("log", "onCreateViewHolder " + dancerList);
        spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, Role.values());
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.profile_item, parent, false);
        ProfileRecyclerViewHolder viewHolder = new ProfileRecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileRVAdapter.ProfileRecyclerViewHolder holder, int position) {
        this.holderGlobal = holder;
        holder.spRole.setAdapter(spinnerAdapter);
        if (dancerList.size() > 0){
            holder.etName.setText(dancerList.get(position).getName());
            holder.etSurname.setText(dancerList.get(position).getSurname());
            holder.etDescription.setText(dancerList.get(position).getDescription());
            if (dancerList.get(position).getBirthday() != null) {
                holder.tvBirthdayShow.setText(DateTimeUtils.dateFormat.format(dancerList.get(position).getBirthday()));
            } else {
                holder.tvBirthdayShow.setText("");
            }
        }

        holder.bBirthday.setOnClickListener(v -> {
            Calendar calendar = new GregorianCalendar();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    context,
                    (DatePickerDialog.OnDateSetListener) (view1, year, month, dayOfMonth) -> {
                        holder.tvBirthdayShow.setText(dayOfMonth + "." + (month + 1) + "." + year);
                        dancerList.get(0).setBirthday(new GregorianCalendar(year, month, dayOfMonth).getTime());
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)

            );
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            datePickerDialog.show();
        });

        holder.etCity.setText(dancerList.get(position).getEntityInfo().getCity());
        holder.etPhone.setText(dancerList.get(position).getEntityInfo().getPhoneNumber());
        holder.etEmail.setText(dancerList.get(position).getEntityInfo().getEmail());
        if (dancerList.get(position).getSex() == null || dancerList.get(position).getSex().equals("male")){
            holder.rbMale.setChecked(true);
        }else {
            holder.rbFemale.setChecked(true);
        }

        if (dancerList.get(position).getRole() == Role.DANCER) {
            holder.spRole.setSelection(0);
        } else {
            holder.spRole.setSelection(1);
        }

        setCheckBoxes(holder, position);

        holder.bCreate.setOnClickListener(v -> {
            activity.setFragmentCreateSchoolOrEvent();
        });
        holder.bSchoolAndEvents.setOnClickListener(v -> {
            activity.setFragmentSchoolsAndEvents();
        });

        holder.bExit.setOnClickListener(v -> {
            activity.setEntered(false);
            activity.changeProfile(0);
            activity.setProfile();
        });
    }

    private void setCheckBoxes(ProfileRecyclerViewHolder holder, int position) {
        if (dancerList.get(position).getDances().contains(Dances.SALSA)) {
            holder.cbSalsa.setChecked(true);
        }

        if (dancerList.get(position).getDances().contains(Dances.BACHATA)) {
            holder.cbBachata.setChecked(true);
        }

        if (dancerList.get(position).getDances().contains(Dances.KIZOMBA)) {
            holder.cbKizomba.setChecked(true);
        }

        if (dancerList.get(position).getDances().contains(Dances.ZOUK)) {
            holder.cbZouk.setChecked(true);
        }

        if (dancerList.get(position).getDances().contains(Dances.REGGAETON)) {
            holder.cbReggaeton.setChecked(true);
        }

        if (dancerList.get(position).getDances().contains(Dances.MERENGE)) {
            holder.cbMerenge.setChecked(true);
        }

        if (dancerList.get(position).getDances().contains(Dances.MAMBO)) {
            holder.cbMambo.setChecked(true);
        }

        if (dancerList.get(position).getDances().contains(Dances.TANGO)) {
            holder.cbTango.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return dancerList.size();
    }

    public class ProfileRecyclerViewHolder extends RecyclerView.ViewHolder{

        private EditText etName;
        private EditText etSurname;
        private EditText etDescription;
        private EditText etCity;
        private EditText etPhone;
        private EditText etEmail;
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
            etPhone = itemView.findViewById(R.id.etPhone);
            etEmail = itemView.findViewById(R.id.etEmail);
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

        public EditText getEtPhone() {
            return etPhone;
        }

        public EditText getEtEmail() {
            return etEmail;
        }

        public EditText getEtName() {
            return etName;
        }

        public EditText getEtSurname() {
            return etSurname;
        }

        public EditText getEtDescription() {
            return etDescription;
        }

        public EditText getEtCity() {
            return etCity;
        }

        public TextView getTvBirthdayShow() {
            return tvBirthdayShow;
        }

        public Button getbBirthday() {
            return bBirthday;
        }

        public Button getbCreate() {
            return bCreate;
        }

        public Button getbDelete() {
            return bDelete;
        }

        public Button getbExit() {
            return bExit;
        }

        public Button getbSchoolAndEvents() {
            return bSchoolAndEvents;
        }

        public ImageView getIvAvatar() {
            return ivAvatar;
        }

        public CircleTextView getCtvAvatar() {
            return ctvAvatar;
        }

        public RadioGroup getRgSex() {
            return rgSex;
        }

        public RadioButton getRbMale() {
            return rbMale;
        }

        public RadioButton getRbFemale() {
            return rbFemale;
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

    public ProfileRecyclerViewHolder getHolderGlobal() {
        return holderGlobal;
    }
}
