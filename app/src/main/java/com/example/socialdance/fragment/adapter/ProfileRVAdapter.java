package com.example.socialdance.fragment.adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialdance.MainActivity;
import com.example.socialdance.R;
import com.example.socialdance.fragment.FragmentProfile;
import com.example.socialdance.model.Dancer;
import com.example.socialdance.model.enums.Dances;
import com.example.socialdance.model.enums.Role;
import com.example.socialdance.utils.CircleTextView;
import com.example.socialdance.utils.DateTimeUtils;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.socialdance.MainActivity.TOAST_Y_GRAVITY;

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
        spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, Role.values());
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.profile_item, parent, false);

        return new ProfileRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileRVAdapter.ProfileRecyclerViewHolder holder, int position) {
        Dancer dancer = dancerList.get(position);
        this.holderGlobal = holder;
        holder.ivAvatar.setOnClickListener(v -> fragmentProfile.setAvatar());

        holder.ctvAvatar.setVisibility(View.VISIBLE);
        holder.ctvAvatar.setText(getCharsForAvatar(dancer.getName(), dancer.getSurname()));

        fragmentProfile.getDancerApi().downloadImage(dancer.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.body() != null) {
                    InputStream inputStream = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    if (bitmap != null) {
                        holder.ctvAvatar.setVisibility(View.INVISIBLE);
                        holder.ivAvatar.setImageBitmap(bitmap);
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        holder.spRole.setAdapter(spinnerAdapter);
        if (dancerList.size() > 0) {
            holder.etName.setText(dancer.getName());
            holder.etSurname.setText(dancer.getSurname());
            holder.etDescription.setText(dancer.getDescription());
            if (dancer.getBirthday() != null) {
                holder.tvBirthdayShow.setText(DateTimeUtils.dateFormat.format(dancer.getBirthday()));
            } else {
                holder.tvBirthdayShow.setText("");
            }
        }

        holder.bBirthday.setOnClickListener(v -> {
            Calendar calendar = new GregorianCalendar();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    context,
                    (DatePickerDialog.OnDateSetListener) (view1, year, month, dayOfMonth) -> {
                        Integer m = (month + 1);
                        String monthStr = String.valueOf(m).length() > 1 ? String.valueOf(m) : "0" + m;
                        holder.tvBirthdayShow.setText(dayOfMonth + "." + (monthStr) + "." + year);
                        dancer.setBirthday(new GregorianCalendar(year, month, dayOfMonth).getTime());
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)

            );
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            datePickerDialog.show();
        });

        holder.etCity.setText(dancer.getEntityInfo().getCity());
        holder.etPhone.setText(dancer.getEntityInfo().getPhoneNumber());
        holder.etEmail.setText(dancer.getEntityInfo().getEmail());
        if (dancer.getSex() == null || dancer.getSex().equals("male")) {
            holder.rbMale.setChecked(true);
        } else {
            holder.rbFemale.setChecked(true);
        }

        if (dancer.getRole() == Role.TEACHER) {
            holder.spRole.setSelection(1);
        } else {
            holder.spRole.setSelection(0);
        }

        setCheckBoxes(holder, position);

        holder.bCreate.setOnClickListener(v -> activity.setFragmentCreateSchoolOrEvent());
        holder.bSchoolAndEvents.setOnClickListener(v -> activity.setFragmentSchoolsAndEvents());

        holder.bExit.setOnClickListener(v -> fragmentProfile.exitProfile());

        holder.bDelete.setOnClickListener(v -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity).
                    setTitle("Your profile will be deleted!").
                    setMessage("Do you want delete your profile?").
                    setPositiveButton("YES", (dialog, which) -> deleteDancer(dancer.getId())).
                    setNeutralButton("CANCEL", (dialog, which) -> {
                    });
            alertDialog.show();

        });

        holder.bCreate.setOnClickListener(v -> {
            if (dancer.getRole() != null) {
                activity.setFragmentCreateSchoolOrEvent();
            } else {
                Toast toast = Toast.makeText(activity, "save your ROLE, please", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                toast.show();
            }
        });

        holder.bSchoolAndEvents.setOnClickListener(v -> activity.setFragmentSchoolsAndEvents());
    }

    public void deleteDancer(int id) {
        activity.getPbConnect().setVisibility(View.VISIBLE);
        fragmentProfile.getDancerApi().deleteDancer(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                activity.getPbConnect().setVisibility(View.INVISIBLE);
                fragmentProfile.exitProfile();
                Toast toast = Toast.makeText(activity, "DELETED", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                toast.show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                activity.getPbConnect().setVisibility(View.INVISIBLE);
                Toast toast = Toast.makeText(activity, "Error connection", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, TOAST_Y_GRAVITY);
                toast.show();
            }
        });
    }


    private String getCharsForAvatar(String name, String surname) {
        String firstChar = String.valueOf(name.charAt(0)).toUpperCase();
        if (surname == null || surname.equals("")) {
            return firstChar;
        }
        return firstChar + String.valueOf(surname.charAt(0)).toUpperCase();
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

    public class ProfileRecyclerViewHolder extends RecyclerView.ViewHolder {

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
