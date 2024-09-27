package com.dpilaloa.upsipteeo.data.controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dpilaloa.upsipteeo.data.interfaces.DialogInterface;
import com.dpilaloa.upsipteeo.R;

public class AlertDialogController {

    private AlertDialog dialog;
    Context context;

    public AlertDialogController(Context context) {
        this.context = context;
    }

    public void createMessage(String title, String message, DialogInterface build){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(message);
        build.getBuilder(builder);
    }


    public void showProgressMessage(String message) {
        if (dialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_progress, null);
            TextView textViewMessage = view.findViewById(R.id.text_mensaje);
            textViewMessage.setText(message);
            builder.setView(view);
            builder.setCancelable(false);
            dialog = builder.create();
        }

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void hideProgressMessage() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

}
