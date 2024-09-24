package com.dpilaloa.upsipteeo.Controladores;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dpilaloa.upsipteeo.Interfaces.DialogInterface;
import com.dpilaloa.upsipteeo.R;

public class Alert_dialog {

    private AlertDialog dialog;
    Context context;

    public Alert_dialog(Context context) {
        this.context = context;
    }

    public void crear_mensaje(String titulo, String mensaje, DialogInterface build){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titulo).setMessage(mensaje);
        build.getBuilder(builder);

    }

    public void mostrar_progreso(String mensaje) {
        if (dialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_progress, null);
            TextView textViewMensaje = view.findViewById(R.id.text_mensaje);
            textViewMensaje.setText(mensaje);
            builder.setView(view);
            builder.setCancelable(false);
            dialog = builder.create();
        }

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void ocultar_progreso() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null; // Libera la referencia
        }
    }

}
