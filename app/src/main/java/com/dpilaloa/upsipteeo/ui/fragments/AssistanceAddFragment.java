package com.dpilaloa.upsipteeo.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.dpilaloa.upsipteeo.ui.activities.DetAssistanceActivity;
import com.dpilaloa.upsipteeo.data.models.Assistance;
import com.dpilaloa.upsipteeo.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class AssistanceAddFragment extends DialogFragment {

    public static DialogFragment dialogFragment;
    public static String UID;
    private int HOUR, MINUTE;
    Calendar dia;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_assistance, null);
        builder.setView(view);

        Button btnSave = view.findViewById(R.id.buttonSaveAssistance);
        TimePicker schedule = view.findViewById(R.id.timePicker);
        CalendarView calendarView = view.findViewById(R.id.calendarView);

        dia = Calendar.getInstance();
        dia.setTimeZone(TimeZone.getTimeZone("America/Guayaquil"));

        calendarView.setMinDate(dia.getTimeInMillis());

        HOUR = dia.get(Calendar.HOUR_OF_DAY);
        MINUTE = dia.get(Calendar.MINUTE);

        if(!TextUtils.isEmpty(UID)) {

            btnSave.setOnClickListener(view1 -> {

                Assistance assistance = new Assistance();
                assistance.date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(dia.getTimeInMillis());
                assistance.time = String.format(Locale.getDefault(), "%02d:%02d", HOUR, MINUTE) + " " + ((HOUR < 12) ? "am" : "pm");
                assistance.dateTime = dia.getTimeInMillis();

                DetAssistanceActivity.assistanceController.createAssistance(UID, assistance).addOnCompleteListener(task -> {

                    if(dialogFragment!=null) {
                        if (task.isSuccessful()) {
                            DetAssistanceActivity.alertDialog.showSuccess(getString(R.string.msgSuccessCreateAssistance));
                            dismiss();
                        } else {
                            DetAssistanceActivity.alertDialog.showError(getString(R.string.msgNotCreateAssistance));
                        }
                    }
                });

            });

        }else{
            DetAssistanceActivity.alertDialog.showError(getString(R.string.msgErrorUid));
        }

        calendarView.setOnDateChangeListener((calendarView1, year, month, dayOfMonth) -> {
            dia.set(year,month,dayOfMonth);
            calendarView1.setDate(dia.getTimeInMillis());
        });

        schedule.setOnTimeChangedListener((timePicker, hour, minute) -> {
            HOUR = hour;
            MINUTE = minute;
            dia.set(Calendar.HOUR_OF_DAY, HOUR);
            dia.set(Calendar.MINUTE, MINUTE);
        });

        dialogFragment = this;

        return builder.create();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        dialogFragment = null;
    }

}
