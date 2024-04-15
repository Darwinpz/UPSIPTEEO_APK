package com.dpilaloa.upsipteeo.Fragmentos;

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
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.dpilaloa.upsipteeo.Controladores.Alert_dialog;
import com.dpilaloa.upsipteeo.Controladores.Progress_dialog;
import com.dpilaloa.upsipteeo.Det_asistencia;
import com.dpilaloa.upsipteeo.MainActivity;
import com.dpilaloa.upsipteeo.Objetos.Ob_usuario;
import com.dpilaloa.upsipteeo.Principal;
import com.dpilaloa.upsipteeo.R;
import com.dpilaloa.upsipteeo.Ver_imagen;

import java.io.ByteArrayOutputStream;

public class Fragmento_Perfil extends Fragment {

    String NOMBRE_USUARIO = "", URL_IMAGEN = "";
    ImageView img_perfil;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_perfil,container,false);

        TextView txt_rol = view.findViewById(R.id.txt_rol);
        TextView txt_nombre = view.findViewById(R.id.txt_nombre);
        TextView txt_cedula = view.findViewById(R.id.txt_cedula);
        EditText txt_correo = view.findViewById(R.id.txt_correo);
        EditText txt_telefono = view.findViewById(R.id.txt_telefono);
        EditText txt_clave = view.findViewById(R.id.txt_clave);
        img_perfil = view.findViewById(R.id.img_perfil);
        Spinner spinner_canton = view.findViewById(R.id.spinner_canton);
        Button btn_actualizar = view.findViewById(R.id.btn_actualizar);
        Button btn_salir = view.findViewById(R.id.btn_salir);
        ImageButton imageButton = view.findViewById(R.id.btn_ver_asist);

        Progress_dialog dialog = new Progress_dialog(view.getContext());
        Alert_dialog alertDialog = new Alert_dialog(view.getContext());

        ArrayAdapter<CharSequence> adapterspinner_canton = ArrayAdapter.createFromResource(view.getContext(), R.array.cantones, android.R.layout.simple_spinner_item);
        adapterspinner_canton.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_canton.setAdapter(adapterspinner_canton);

        if(!Principal.id.isEmpty()) {

            txt_correo.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                @Override
                public void afterTextChanged(Editable editable) {
                    if(!Principal.ctlUsuarios.validar_correo(editable.toString().trim())){
                        txt_correo.setError("Ingresa un correo válido");
                    }
                }
            });

            txt_telefono.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                @Override
                public void afterTextChanged(Editable editable) {
                    if(editable.toString().trim().length() == 10) {
                        if (!Principal.ctlUsuarios.validar_celular(editable.toString().trim())) {
                            txt_telefono.setError("Ingresa un celular válido");
                        }
                    }else{
                        txt_telefono.setError("Ingresa 10 dígitos");
                    }
                }
            });


            Principal.ctlUsuarios.obtener_datos_perfil(Principal.id, user -> {

                if (user != null) {

                    txt_nombre.setText(user.nombre);
                    txt_correo.setText(user.correo);
                    txt_telefono.setText(user.celular);
                    txt_cedula.setText(user.cedula);
                    txt_rol.setText(user.rol);
                    txt_clave.setText(user.clave);

                    NOMBRE_USUARIO = user.nombre;
                    URL_IMAGEN = user.url_foto;

                    int spinnerPosition = adapterspinner_canton.getPosition(user.canton);
                    spinner_canton.setSelection(spinnerPosition);

                    if (user.url_foto != null && !user.url_foto.isEmpty()) {
                        Glide.with(view.getContext()).load(user.url_foto).centerCrop().into(img_perfil);
                    }

                }

            });

            imageButton.setOnClickListener(view1 -> {
                startActivity(new Intent(view.getContext(), Det_asistencia.class)
                        .putExtra("uid",Principal.id)
                        .putExtra("nombre", NOMBRE_USUARIO));
            });

            img_perfil.setOnClickListener(view1 -> {

                if( URL_IMAGEN!=null && !URL_IMAGEN.isEmpty()) {
                    alertDialog.crear_mensaje("Información", "Selecciona una opción", builder -> {
                        builder.setPositiveButton("Ver Foto", (dialogInterface, i) -> {
                            startActivity(new Intent(getContext(), Ver_imagen.class).putExtra("url", URL_IMAGEN));
                        });
                        builder.setNeutralButton("Actualizar Foto", (dialogInterface, i) -> upload_foto());
                        builder.setCancelable(true);
                        builder.create().show();
                    });
                }else{
                    upload_foto();
                }

            });

            btn_actualizar.setOnClickListener(view1 -> {
                dialog.mostrar_mensaje("Actualizando...");
                if(!txt_correo.getText().toString().trim().isEmpty() && txt_correo.getError() == null &&
                   !txt_telefono.getText().toString().trim().isEmpty() && txt_telefono.getError() == null &&
                   !spinner_canton.getSelectedItem().toString().equals("Cantones")) {
                    Ob_usuario user = new Ob_usuario();
                    user.uid = Principal.id;
                    user.cedula = txt_cedula.getText().toString();
                    user.nombre = txt_nombre.getText().toString().toUpperCase();
                    user.correo = txt_correo.getText().toString().toLowerCase();
                    user.celular = txt_telefono.getText().toString();
                    user.canton = spinner_canton.getSelectedItem().toString();
                    user.rol = txt_rol.getText().toString();
                    user.clave = txt_clave.getText().toString();
                    Principal.ctlUsuarios.update_usuario(user);
                    dialog.ocultar_mensaje();
                    alertDialog.crear_mensaje("Correcto", "Usuario Actualizado Correctamente", builder -> {
                        builder.setCancelable(false);
                        builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                        builder.create().show();
                    });
                }else{
                    dialog.ocultar_mensaje();
                    alertDialog.crear_mensaje("¡Advertencia!", "Completa todos los campos", builder -> {
                        builder.setCancelable(true);
                        builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                        builder.create().show();
                    });
                }
            });

            btn_salir.setOnClickListener(view1 -> {
                dialog.mostrar_mensaje("Cerrando Sesión...");
                Principal.ctlUsuarios.cerrar_sesion(Principal.preferences);
                dialog.ocultar_mensaje();
                startActivity(new Intent(view.getContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                requireActivity().finish();
            });

        }else{
            Toast.makeText(view.getContext(), "Ocurrió un error al cargar el perfil",Toast.LENGTH_LONG).show();
        }

        return view;
    }

    private void upload_foto(){
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
            permissionDenied();
        }
    });

    ActivityResultLauncher<Intent> android11StoragePermission = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (isPermitted()) {
            getImageFile();
        } else {
            permissionDenied();
        }
    });

    ActivityResultLauncher<CropImageContractOptions> cropImage = registerForActivityResult(new CropImageContract(), result -> {
        if (result.isSuccessful()) {
            Bitmap cropped = BitmapFactory.decodeFile(result.getUriFilePath(getContext(), true));
            Glide.with(getContext()).load(result.getUriContent()).into(img_perfil);
            saveCroppedImage(cropped);
        }
    });

    private void saveCroppedImage(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);

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
        intent.setData(Uri.parse(String.format("package:%s", getContext().getPackageName())));
        android11StoragePermission.launch(intent);
    }

    private boolean isPermitted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
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
        cropImageOptions.minCropResultHeight = 512;
        cropImageOptions.minCropResultWidth = 512;
        CropImageContractOptions cropImageContractOptions = new CropImageContractOptions(uri, cropImageOptions);
        cropImage.launch(cropImageContractOptions);
    }

    private void permissionDenied() {
        Toast.makeText(getContext(), "Permiso Denegado", Toast.LENGTH_LONG).show();
    }

    private void showFailureMessage() {
        Toast.makeText(getContext(), "Cropped image not saved something went wrong", Toast.LENGTH_LONG).show();
    }

    private void showSuccessMessage() {
        Toast.makeText(getContext(), "Image Saved", Toast.LENGTH_LONG).show();
    }


}
