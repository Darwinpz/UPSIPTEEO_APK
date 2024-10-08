package com.dpilaloa.upsipteeo.data.controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dpilaloa.upsipteeo.R;
import com.dpilaloa.upsipteeo.data.interfaces.MessagesHandlerInterface;

import java.lang.ref.WeakReference;

public class AlertDialogController implements MessagesHandlerInterface {

    private AlertDialog dialog;
    private final WeakReference<Context> contextRef;

    public AlertDialogController(@NonNull Context context) {
        this.contextRef = new WeakReference<>(context);
    }

    public interface DialogBuilderCallback {
        void onBuild(AlertDialog.Builder builder);
    }

    public void createMessage(String title, String message, @NonNull DialogBuilderCallback callback) {
        Context context = contextRef.get();
        if (context != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title).setMessage(message);
            callback.onBuild(builder);
        }
    }

    public void showMessageDialog(String title, String message, boolean cancelable, DialogInterface.OnClickListener onClickListener) {
        createMessage(title, message, builder -> {
            builder.setCancelable(cancelable);
            builder.setNeutralButton(contextRef.get().getString(R.string.msgAccept), onClickListener);
            builder.create().show();
        });
    }

    public void showConfirmDialog(String title, String message, String positive, String neutral,
                                  DialogInterface.OnClickListener onAcceptClickListener,
                                  DialogInterface.OnClickListener onCancelClickListener) {
        createMessage(title, message, builder -> {
            builder.setPositiveButton(positive, onAcceptClickListener);
            builder.setNeutralButton(neutral, onCancelClickListener);
            builder.setCancelable(true);
            builder.create().show();
        });
    }

    public void showProgressMessage(String message) {
        if (dialog == null || !dialog.isShowing()) {
            dialog = createProgressDialog(message);
            if (dialog != null) {
                dialog.show();
            }
        }
    }

    private AlertDialog createProgressDialog(String message) {
        Context context = contextRef.get();
        if (context != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_progress, null);
            TextView textViewMessage = view.findViewById(R.id.text_mensaje);
            textViewMessage.setText(message);
            builder.setView(view);
            builder.setCancelable(false);
            return builder.create();
        }
        return null;
    }

    public void hideProgressMessage() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void showSuccess(String message) {
        hideProgressMessage();
        Toast.makeText(contextRef.get(), "✔ " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String message) {
        hideProgressMessage();
        Toast.makeText(contextRef.get(), "❌ " + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showWarning(String message) {
        hideProgressMessage();
        Toast.makeText(contextRef.get(), "⚠ " + message, Toast.LENGTH_SHORT).show();
    }

}
