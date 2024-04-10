package com.dpilaloa.upsipteeo.Controladores;

import android.app.ProgressDialog;
import android.content.Context;

public class Progress_dialog {

    ProgressDialog dialog;
    Context context;

    public Progress_dialog(Context context) {
        this.context = context;
        this.dialog = new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(false);
    }

    public void mostrar_mensaje(String mensaje) {
        dialog.setMessage(mensaje);
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void ocultar_mensaje() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

}
