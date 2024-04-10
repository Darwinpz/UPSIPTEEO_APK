package com.dpilaloa.upsipteeo.Controladores;

import android.app.AlertDialog;
import android.content.Context;

public class Alert_dialog {

    Context context;

    public Alert_dialog(Context context) {
        this.context = context;
    }

    public void crear_mensaje(String titulo, String mensaje, Interfaces.build build){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titulo).setMessage(mensaje);
        build.verbuilder(builder);

    }

}
