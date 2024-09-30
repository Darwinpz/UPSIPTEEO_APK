package com.dpilaloa.upsipteeo.data.controllers;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;

public class PhotoController {

    private final Context context;
    private final ActivityResultLauncher<Intent> getImage;
    private final ActivityResultLauncher<String> requestPermission;
    private final ActivityResultLauncher<Intent> android11StoragePermission;
    private final ActivityResultLauncher<CropImageContractOptions> cropImage;

    public PhotoController(Context context, ActivityResultLauncher<Intent> getImage, ActivityResultLauncher<String> requestPermission, ActivityResultLauncher<Intent> android11StoragePermission, ActivityResultLauncher<CropImageContractOptions> cropImage) {
        this.context = context;
        this.getImage = getImage;
        this.requestPermission = requestPermission;
        this.android11StoragePermission = android11StoragePermission;
        this.cropImage = cropImage;
    }

    // Método para subir una foto
    public void uploadPhoto() {
        if (isPermitted()) {
            getImageFile();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requestAndroid11StoragePermission();
        } else {
            requestPermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    // Verifica si los permisos ya fueron otorgados
    public boolean isPermitted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }
    // Método para obtener el archivo de imagen
    public void getImageFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getImage.launch(intent);
    }

    // Método para solicitar permisos en Android 11 o superior
    @TargetApi(Build.VERSION_CODES.R)
    private void requestAndroid11StoragePermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse(String.format("package:%s", context.getPackageName())));
        android11StoragePermission.launch(intent);
    }

    // Método para manejar el resultado de la selección de imagen
    public void handleImageResult(@NonNull ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                launchImageCropper(imageUri);
            }
        }
    }

    // Método para lanzar el recortador de imágenes
    private void launchImageCropper(Uri uri) {
        CropImageOptions cropImageOptions = new CropImageOptions();
        cropImageOptions.imageSourceIncludeGallery = false;
        cropImageOptions.imageSourceIncludeCamera = true;
        cropImageOptions.outputCompressFormat = Bitmap.CompressFormat.JPEG;
        cropImageOptions.outputCompressQuality = 90;
        cropImageOptions.aspectRatioX = 1;
        cropImageOptions.aspectRatioY = 1;
        cropImageOptions.maxCropResultWidth = 1024;
        cropImageOptions.maxCropResultHeight = 1024;
        cropImageOptions.minCropResultHeight = 512;
        cropImageOptions.minCropResultWidth = 512;
        CropImageContractOptions cropImageContractOptions = new CropImageContractOptions(uri, cropImageOptions);
        cropImage.launch(cropImageContractOptions);
    }

}
