package com.dpilaloa.upsipteeo.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.dpilaloa.upsipteeo.DetAssistanceView;
import com.dpilaloa.upsipteeo.Objects.Assistance;
import com.dpilaloa.upsipteeo.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class AssistanceAddFragment extends DialogFragment {

    public static DialogFragment dialogFragment;
    private long DATE;
    private String TIME;
    public static String UID;

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

        Calendar dia = Calendar.getInstance();
        dia.setTimeZone(TimeZone.getTimeZone("America/Guayaquil")); // Zone of Ecuador

        calendarView.setMinDate(dia.getTimeInMillis());

        DATE = dia.getTimeInMillis();
        TIME = String.format(Locale.getDefault(),"%02d:%02d", dia.get(Calendar.HOUR_OF_DAY), dia.get(Calendar.MINUTE))+ " "+ ((dia.get(Calendar.HOUR_OF_DAY)<12) ? "am":"pm");

        btnSave.setOnClickListener(view1 -> {

            if(dialogFragment!=null) {
                Assistance assistance = new Assistance();
                assistance.date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(DATE);
                assistance.time = TIME;

                if(!TextUtils.isEmpty(UID)) {

                    DetAssistanceView.assistanceController.createAssistance(UID, assistance).addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {
                            dialogFragment.dismiss();
                        } else {
                            Toast.makeText(getContext(), "Ocurrió un error al crear la asistencia", Toast.LENGTH_LONG).show();
                        }

                    });

                }else{
                    Toast.makeText(getContext(), "Ocurrió un error al obtener el id del Usuario", Toast.LENGTH_LONG).show();
                }

            }

        });

        calendarView.setOnDateChangeListener((calendarView1, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year,month,dayOfMonth);
            calendarView1.setDate(calendar.getTimeInMillis());
            DATE = calendarView1.getDate();
        });

        schedule.setOnTimeChangedListener((timePicker, hour, minute) ->  TIME = String.format(Locale.getDefault(),"%02d:%02d", hour, minute)+ " "+ ((hour<12) ? "am":"pm"));
        dialogFragment = this;

        return builder.create();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        dialogFragment = null;
    }

}
