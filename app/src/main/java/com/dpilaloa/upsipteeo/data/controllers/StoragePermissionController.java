package com.dpilaloa.upsipteeo.data.controllers;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.ContextCompat;

public class StoragePermissionController {

    private final Context context;
    private final ActivityResultLauncher<String> requestPermission;
    private final ActivityResultLauncher<Intent> android11StoragePermission;

    public StoragePermissionController(Context context, ActivityResultLauncher<String> requestPermission, ActivityResultLauncher<Intent> android11StoragePermission) {
        this.context = context;
        this.requestPermission = requestPermission;
        this.android11StoragePermission = android11StoragePermission;
    }

    public boolean isPermitted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    @TargetApi(Build.VERSION_CODES.R)
    public void requestAndroid11StoragePermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse(String.format("package:%s", context.getPackageName())));
        android11StoragePermission.launch(intent);
    }

    public void setRequestPermission(){
        requestPermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

}
