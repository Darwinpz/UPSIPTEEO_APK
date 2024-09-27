package com.dpilaloa.upsipteeo.ui.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.dpilaloa.upsipteeo.data.controllers.AlertDialogController;
import com.dpilaloa.upsipteeo.ui.activities.DetAssistanceActivity;
import com.dpilaloa.upsipteeo.MainActivity;
import com.dpilaloa.upsipteeo.data.models.User;
import com.dpilaloa.upsipteeo.ui.activities.PrimaryActivity;
import com.dpilaloa.upsipteeo.R;
import com.dpilaloa.upsipteeo.ui.activities.ImageActivity;
import com.dpilaloa.upsipteeo.utils.ValEditTextWatcher;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

public class ProfileFragment extends Fragment {

    String USERNAME = "", URL_PHOTO = "";
    android.widget.ImageView imgProfile;
    AlertDialogController alertDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile,container,false);

        TextView txtRol = view.findViewById(R.id.textViewRol);
        TextView txtName = view.findViewById(R.id.textViewName);
        TextView txtCed = view.findViewById(R.id.textViewCed);
        EditText editTextEmail = view.findViewById(R.id.editTextEmail);
        EditText editTextPhone = view.findViewById(R.id.editTextPhone);
        EditText editTextPassword = view.findViewById(R.id.editTextPassword);
        imgProfile = view.findViewById(R.id.imageViewProfile);
        Spinner spinner_canton = view.findViewById(R.id.spinnerCanton);
        Button btnUpdate = view.findViewById(R.id.buttonUpdate);
        Button btnLogOut = view.findViewById(R.id.buttonLogOut);
        ImageButton imageButton = view.findViewById(R.id.imgButtonShowAssistance);

        alertDialog = new AlertDialogController(view.getContext());

        ArrayAdapter<CharSequence> adapterSpinnerCanton = ArrayAdapter.createFromResource(view.getContext(), R.array.canton, android.R.layout.simple_spinner_item);
        adapterSpinnerCanton.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_canton.setAdapter(adapterSpinnerCanton);

        if(!PrimaryActivity.id.isEmpty()) {

            editTextEmail.addTextChangedListener(new ValEditTextWatcher(editTextEmail,
                    input -> PrimaryActivity.userController.valEmail(input),"Ingresa un correo válido"));

            editTextPhone.addTextChangedListener(new ValEditTextWatcher(editTextPhone,
                    input -> PrimaryActivity.userController.valPhone(input),"Ingresa un teléfono válido"));

            PrimaryActivity.userController.getProfile(PrimaryActivity.id, user -> {

                if (user != null) {

                    txtName.setText(user.name);
                    editTextEmail.setText(user.email);
                    editTextPhone.setText(user.phone);
                    txtCed.setText(user.ced);
                    txtRol.setText(user.rol);
                    editTextPassword.setText(user.password);

                    USERNAME = user.name;
                    URL_PHOTO = user.photo;

                    int spinnerPosition = adapterSpinnerCanton.getPosition(user.canton);
                    spinner_canton.setSelection(spinnerPosition);

                    if (!TextUtils.isEmpty(user.photo)) {
                        Glide.with(view.getContext().getApplicationContext()).load(user.photo).centerCrop().into(imgProfile);
                    }

                }

            });

            imageButton.setOnClickListener(view1 ->
                startActivity(new Intent(view.getContext(), DetAssistanceActivity.class)
                        .putExtra("uid", PrimaryActivity.id)
                        .putExtra("nombre", USERNAME))
            );

            imgProfile.setOnClickListener(view1 -> {

                if(!TextUtils.isEmpty(URL_PHOTO)) {
                    alertDialog.showConfirmDialog("Información", "Selecciona una opción","Ver Foto","Actualizar Foto", (dialogInterface, i) -> {
                        startActivity(new Intent(getContext(), ImageActivity.class).putExtra("url", URL_PHOTO));
                    }, (dialogInterface, i) -> uploadPhoto());
                }else{
                    uploadPhoto();
                }

            });

            btnUpdate.setOnClickListener(view1 -> {
                alertDialog.showProgressMessage("Actualizando...");
                if(!editTextEmail.getText().toString().trim().isEmpty() && editTextEmail.getError() == null &&
                   !editTextPhone.getText().toString().trim().isEmpty() && editTextPhone.getError() == null &&
                   !spinner_canton.getSelectedItem().toString().equals("Cantones")) {

                    User user = new User();
                    user.uid = PrimaryActivity.id;
                    user.ced = txtCed.getText().toString();
                    user.name = txtName.getText().toString().toUpperCase();
                    user.email = editTextEmail.getText().toString().toLowerCase();
                    user.phone = editTextPhone.getText().toString();
                    user.canton = spinner_canton.getSelectedItem().toString();
                    user.rol = txtRol.getText().toString();
                    user.password = editTextPassword.getText().toString();

                    if(!TextUtils.isEmpty(PrimaryActivity.id)) {
                        PrimaryActivity.userController.updateUser(user).addOnCompleteListener(task -> {
                            alertDialog.hideProgressMessage();
                            if (task.isSuccessful()) {
                                alertDialog.showMessageDialog("Correcto", "Perfil Actualizado Correctamente", false, (dialogInterface, i) -> {});
                            } else {
                                alertDialog.showMessageDialog("¡Advertencia!", "Ocurrió un error al Actualizar el Perfil", true, (dialogInterface, i) -> {});
                            }
                        });
                    }else{
                        alertDialog.hideProgressMessage();
                        Toast.makeText(getContext(), "Ocurrió un error al obtener la id del Perfil",Toast.LENGTH_LONG).show();
                    }

                }else{
                    alertDialog.hideProgressMessage();
                    alertDialog.showMessageDialog("¡Advertencia!", "Completa todos los campos", true, (dialogInterface, i) -> {});
                }
            });

            btnLogOut.setOnClickListener(view1 -> {
                alertDialog.showProgressMessage("Cerrando Sesión...");
                PrimaryActivity.userController.logOut(PrimaryActivity.preferences);
                alertDialog.hideProgressMessage();
                requireActivity().finish();
                startActivity(new Intent(view.getContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            });

        }else{
            Toast.makeText(view.getContext(), "Ocurrió un error al cargar el Perfil",Toast.LENGTH_LONG).show();
        }

        return view;
    }

    private void uploadPhoto(){
        if (isPermitted()) {
            getImageFile();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requestAndroid11StoragePermission();
        } else {
            requestPermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                launchImageCropper(imageUri);
            }
        }
    });

    private final ActivityResultLauncher<String> requestPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {

        if (isGranted) {
            getImageFile();
        } else {
            Toast.makeText(getContext(), "Permiso Denegado", Toast.LENGTH_LONG).show();
        }

    });

    ActivityResultLauncher<Intent> android11StoragePermission = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (isPermitted()) {
            getImageFile();
        } else {
            Toast.makeText(getContext(), "Permiso Denegado", Toast.LENGTH_LONG).show();
        }
    });

    ActivityResultLauncher<CropImageContractOptions> cropImage = registerForActivityResult(new CropImageContract(), result -> {
        if (result.isSuccessful()) {
            Bitmap cropped = BitmapFactory.decodeFile(result.getUriFilePath(requireContext(), true));
            saveCroppedImage(cropped);
        }
    });

    private void saveCroppedImage(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);

        final byte [] thumb_byte = byteArrayOutputStream.toByteArray();
        StorageReference ref = PrimaryActivity.storageReference.child("usuarios").child(PrimaryActivity.id);

        alertDialog.showProgressMessage("Actualizando Foto...");

        ref.putBytes(thumb_byte).addOnSuccessListener(taskSnapshot ->

            ref.getDownloadUrl().addOnSuccessListener(uri -> {
                URL_PHOTO = uri.toString();
                if(!TextUtils.isEmpty(PrimaryActivity.id)) {
                    PrimaryActivity.userController.updatePhoto(PrimaryActivity.id, URL_PHOTO).addOnCompleteListener(task -> {
                        alertDialog.hideProgressMessage();
                        if (task.isSuccessful()) {
                            alertDialog.showMessageDialog("Correcto", "Foto Actualizada Correctamente", false, (dialogInterface, i) -> {});
                        } else {
                            Toast.makeText(getContext(), "Ocurrió un error al actualizar la foto", Toast.LENGTH_LONG).show();
                        }

                    });
                }else{
                    alertDialog.hideProgressMessage();
                    Toast.makeText(getContext(), "Ocurrió un error al obtener la id del Perfil",Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(e -> {
                alertDialog.hideProgressMessage();
                Toast.makeText(getContext(), "Ocurrió un error al obtener la foto",Toast.LENGTH_LONG).show();
            })

        ).addOnFailureListener(e -> {
            alertDialog.hideProgressMessage();
            Toast.makeText(getContext(), "Ocurrió un error al actualizar la foto",Toast.LENGTH_LONG).show();
        });

    }

    private void getImageFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getImage.launch(intent);
    }

    @TargetApi(Build.VERSION_CODES.R)
    private void requestAndroid11StoragePermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse(String.format("package:%s", requireContext().getPackageName())));
        android11StoragePermission.launch(intent);
    }

    private boolean isPermitted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
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
