package com.dpilaloa.upsipteeo.Fragmentos;

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

import com.dpilaloa.upsipteeo.Det_asistencia;
import com.dpilaloa.upsipteeo.Objetos.Ob_asistencia;
import com.dpilaloa.upsipteeo.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Fragmento_add_asistencia extends DialogFragment {

    public static DialogFragment dialogFragment;
    private long FECHA;
    private String HORA;
    public static String UID;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_asistencia, null);
        builder.setView(view);

        Button btn_guardar = view.findViewById(R.id.btn_guardar);
        TimePicker horario = view.findViewById(R.id.horario);
        CalendarView calendario = view.findViewById(R.id.calendario);

        Date dia = new Date();

        calendario.setMinDate(dia.getTime());

        FECHA = dia.getTime();
        HORA = String.format(Locale.getDefault(),"%02d:%02d", dia.getHours(), dia.getMinutes())+ " "+ ((dia.getHours()<12) ? "am":"pm");

        btn_guardar.setOnClickListener(view1 -> {

            if(dialogFragment!=null) {
                Ob_asistencia obAsistencia = new Ob_asistencia();
                obAsistencia.fecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(FECHA);
                obAsistencia.hora = HORA;

                if(!TextUtils.isEmpty(UID)) {

                    Det_asistencia.ctlAsistencia.add_asistencia(UID, obAsistencia).addOnCompleteListener(task -> {

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

        calendario.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year,month,dayOfMonth);
            calendarView.setDate(calendar.getTimeInMillis());
            FECHA = calendarView.getDate();
        });
        horario.setOnTimeChangedListener((timePicker, hour, minute) ->  HORA = String.format(Locale.getDefault(),"%02d:%02d", hour, minute)+ " "+ ((hour<12) ? "am":"pm"));
        dialogFragment = this;

        return builder.create();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        dialogFragment = null;
    }

}
