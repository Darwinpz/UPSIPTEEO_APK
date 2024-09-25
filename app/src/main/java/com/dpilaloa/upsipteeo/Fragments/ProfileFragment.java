package com.dpilaloa.upsipteeo.Fragments;

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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.dpilaloa.upsipteeo.Controllers.AlertDialogController;
import com.dpilaloa.upsipteeo.DetAssistanceView;
import com.dpilaloa.upsipteeo.MainActivity;
import com.dpilaloa.upsipteeo.Objects.User;
import com.dpilaloa.upsipteeo.PrimaryView;
import com.dpilaloa.upsipteeo.R;
import com.dpilaloa.upsipteeo.ImageView;
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

        TextView txtRol = view.findViewById(R.id.txt_rol);
        TextView txtName = view.findViewById(R.id.txt_nombre);
        TextView txtCed = view.findViewById(R.id.txt_cedula);
        EditText editTextEmail = view.findViewById(R.id.txt_correo);
        EditText editTextPhone = view.findViewById(R.id.txt_telefono);
        EditText editTextPassword = view.findViewById(R.id.txt_clave);
        imgProfile = view.findViewById(R.id.img_perfil);
        Spinner spinner_canton = view.findViewById(R.id.spinner_canton);
        Button btnUpdate = view.findViewById(R.id.btn_actualizar);
        Button btnLogOut = view.findViewById(R.id.btn_salir);
        ImageButton imageButton = view.findViewById(R.id.btn_ver_asist);

        alertDialog = new AlertDialogController(view.getContext());

        ArrayAdapter<CharSequence> adapterSpinnerCanton = ArrayAdapter.createFromResource(view.getContext(), R.array.canton, android.R.layout.simple_spinner_item);
        adapterSpinnerCanton.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_canton.setAdapter(adapterSpinnerCanton);

        if(!PrimaryView.id.isEmpty()) {

            editTextEmail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                @Override
                public void afterTextChanged(Editable editable) {
                    if(!PrimaryView.userController.valEmail(editable.toString().trim())){
                        editTextEmail.setError("Ingresa un correo válido");
                    }
                }
            });

            editTextPhone.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                @Override
                public void afterTextChanged(Editable editable) {
                    String phone = editable.toString().trim();
                    if (phone.length() != 10) {
                        editTextPhone.setError("Ingresa 10 dígitos");
                    } else if (!PrimaryView.userController.valPhone(phone)) {
                        editTextPhone.setError("Ingresa un celular válido");
                    }
                }
            });


            PrimaryView.userController.getProfile(PrimaryView.id, user -> {

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
                startActivity(new Intent(view.getContext(), DetAssistanceView.class)
                        .putExtra("uid", PrimaryView.id)
                        .putExtra("nombre", USERNAME))
            );

            imgProfile.setOnClickListener(view1 -> {

                if(!TextUtils.isEmpty(URL_PHOTO)) {
                    alertDialog.createMessage("Información", "Selecciona una opción", builder -> {
                        builder.setPositiveButton("Ver Foto", (dialogInterface, i) ->
                            startActivity(new Intent(getContext(), ImageView.class).putExtra("url", URL_PHOTO))
                        );
                        builder.setNeutralButton("Actualizar Foto", (dialogInterface, i) -> uploadPhoto());
                        builder.setCancelable(true);
                        builder.create().show();
                    });
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
                    user.uid = PrimaryView.id;
                    user.ced = txtCed.getText().toString();
                    user.name = txtName.getText().toString().toUpperCase();
                    user.email = editTextEmail.getText().toString().toLowerCase();
                    user.phone = editTextPhone.getText().toString();
                    user.canton = spinner_canton.getSelectedItem().toString();
                    user.rol = txtRol.getText().toString();
                    user.password = editTextPassword.getText().toString();

                    if(!TextUtils.isEmpty(PrimaryView.id)) {
                        PrimaryView.userController.updateUser(user).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                alertDialog.hideProgressMessage();
                                alertDialog.createMessage("Correcto", "Perfil Actualizado Correctamente", builder -> {
                                    builder.setCancelable(false);
                                    builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {
                                    });
                                    builder.create().show();
                                });
                            } else {
                                alertDialog.hideProgressMessage();
                                alertDialog.createMessage("¡Advertencia!", "Ocurrió un error al Actualizar el Perfil", builder -> {
                                    builder.setCancelable(true);
                                    builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {
                                    });
                                    builder.create().show();
                                });
                            }
                        });
                    }else{
                        alertDialog.hideProgressMessage();
                        Toast.makeText(getContext(), "Ocurrió un error al obtener la id del Perfil",Toast.LENGTH_LONG).show();
                    }

                }else{
                    alertDialog.hideProgressMessage();
                    alertDialog.createMessage("¡Advertencia!", "Completa todos los campos", builder -> {
                        builder.setCancelable(true);
                        builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                        builder.create().show();
                    });
                }
            });

            btnLogOut.setOnClickListener(view1 -> {
                alertDialog.showProgressMessage("Cerrando Sesión...");
                PrimaryView.userController.logOut(PrimaryView.preferences);
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
        StorageReference ref = PrimaryView.storageReference.child("usuarios").child(PrimaryView.id);

        alertDialog.showProgressMessage("Actualizando Foto...");

        ref.putBytes(thumb_byte).addOnSuccessListener(taskSnapshot ->

            ref.getDownloadUrl().addOnSuccessListener(uri -> {
                URL_PHOTO = uri.toString();
                if(!TextUtils.isEmpty(PrimaryView.id)) {
                    PrimaryView.userController.updatePhoto(PrimaryView.id, URL_PHOTO).addOnCompleteListener(task -> {
                        alertDialog.hideProgressMessage();
                        if (task.isSuccessful()) {
                            alertDialog.createMessage("Correcto", "Foto Actualizada Correctamente", builder -> {
                                builder.setCancelable(false);
                                builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {
                                });
                                builder.create().show();
                            });
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
        cropImageOptions.maxCropResultWidth = 512;
        cropImageOptions.maxCropResultHeight = 512;
        //cropImageOptions.minCropResultHeight = 512;
        //cropImageOptions.minCropResultWidth = 512;
        CropImageContractOptions cropImageContractOptions = new CropImageContractOptions(uri, cropImageOptions);
        cropImage.launch(cropImageContractOptions);
    }

}
