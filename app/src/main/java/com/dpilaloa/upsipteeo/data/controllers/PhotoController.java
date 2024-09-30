package com.dpilaloa.upsipteeo.data.controllers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;

public class PhotoController {

    private final ActivityResultLauncher<Intent> getImage;
    private final StoragePermissionController storagePermissionController;
    private final ActivityResultLauncher<CropImageContractOptions> cropImage;

    public PhotoController(ActivityResultLauncher<Intent> getImage, StoragePermissionController storagePermissionController, ActivityResultLauncher<CropImageContractOptions> cropImage) {
        this.getImage = getImage;
        this.storagePermissionController = storagePermissionController;
        this.cropImage = cropImage;
    }

    public void uploadPhoto() {
        if (storagePermissionController.isPermitted()) {
            getImageFile();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            storagePermissionController.requestAndroid11StoragePermission();
        } else {
            storagePermissionController.setRequestPermission();
        }
    }

    public void getImageFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getImage.launch(intent);
    }

    public void handleImageResult(@NonNull ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                launchImageCropper(imageUri);
            }
        }
    }

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
