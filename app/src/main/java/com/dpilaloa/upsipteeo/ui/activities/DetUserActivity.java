package com.dpilaloa.upsipteeo.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.dpilaloa.upsipteeo.R;
import com.dpilaloa.upsipteeo.data.controllers.AlertDialogController;
import com.dpilaloa.upsipteeo.data.controllers.PhotoController;
import com.dpilaloa.upsipteeo.data.controllers.StoragePermissionController;
import com.dpilaloa.upsipteeo.data.models.User;
import com.dpilaloa.upsipteeo.ui.adapters.ArraySpinnerAdapter;
import com.dpilaloa.upsipteeo.utils.ValEditTextWatcher;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.StorageException;

import java.util.Objects;

public class DetUserActivity extends AppCompatActivity {

    String UID_USER = "", USERNAME = "", URL_PHOTO = "" , ROL_USER = "";
    private PhotoController photoController;
    private AlertDialogController alertDialog;
    private StoragePermissionController storagePermissionController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_det_user);

        EditText editTextCed = findViewById(R.id.textViewCed);
        EditText editTextFirstName = findViewById(R.id.editTextFirstName);
        EditText editTextLastName = findViewById(R.id.editTextLastName);
        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextPhone = findViewById(R.id.editTextPhone);
        TextView textViewPassword = findViewById(R.id.labelPassword);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        TextInputLayout textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        ImageView imgProfile = findViewById(R.id.imageViewProfile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        Button btnUpdate = findViewById(R.id.buttonUpdate);
        Button btnDelete = findViewById(R.id.btn_eliminar);

        UID_USER = Objects.requireNonNull(getIntent().getExtras()).getString("uid","");
        ROL_USER = Objects.requireNonNull(getIntent().getExtras()).getString("rol","");

        Spinner spinner_rol = findViewById(R.id.spinnerRol);
        Spinner spinner_canton = findViewById(R.id.spinnerCanton);
        ImageButton imageButton = findViewById(R.id.btn_ver_asistencia);

        alertDialog = new AlertDialogController(this);
        storagePermissionController = new StoragePermissionController(this,requestPermission,android11StoragePermission);
        photoController = new PhotoController(getImage,storagePermissionController,cropImage);

        toolbar.setOnClickListener(view -> finish());

        ArrayAdapter<CharSequence> adapterSpinnerRol = new ArraySpinnerAdapter(this,android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.rol), PrimaryActivity.rol,1);
        adapterSpinnerRol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_rol.setAdapter(adapterSpinnerRol);

        ArrayAdapter<CharSequence> adapterSpinnerCanton = ArrayAdapter.createFromResource(this, R.array.canton, android.R.layout.simple_spinner_item);
        adapterSpinnerCanton.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_canton.setAdapter(adapterSpinnerCanton);

        if(!UID_USER.isEmpty()){

            editTextCed.addTextChangedListener(new ValEditTextWatcher(editTextCed,
                    input -> PrimaryActivity.userController.valCed(input),getString(R.string.msgNotValCed)));

            editTextFirstName.addTextChangedListener(new ValEditTextWatcher(editTextFirstName,
                    input -> PrimaryActivity.userController.valUser(input),getString(R.string.msgNotValName)));

            editTextLastName.addTextChangedListener(new ValEditTextWatcher(editTextLastName,
                    input -> PrimaryActivity.userController.valUser(input),getString(R.string.msgNotValName)));

            editTextEmail.addTextChangedListener(new ValEditTextWatcher(editTextEmail,
                    input -> PrimaryActivity.userController.valEmail(input),getString(R.string.msgNotValEmail)));

            editTextPhone.addTextChangedListener(new ValEditTextWatcher(editTextPhone,
                    input -> PrimaryActivity.userController.valPhone(input),getString(R.string.msgNotValPhone)));

            PrimaryActivity.userController.getProfile(UID_USER, user -> {

                if(user!=null){

                    editTextCed.setText(user.ced);
                    editTextFirstName.setText(user.firstName);
                    editTextLastName.setText(user.lastName);
                    editTextEmail.setText(user.email);
                    editTextPhone.setText(user.phone);
                    editTextPassword.setText(user.password);

                    USERNAME = user.lastName + " " + user.firstName;

                    int spinnerPosition_rol = adapterSpinnerRol.getPosition(user.rol);
                    spinner_rol.setSelection(spinnerPosition_rol);

                    int spinnerPosition_canton = adapterSpinnerCanton.getPosition(user.canton);
                    spinner_canton.setSelection(spinnerPosition_canton);

                    if (!TextUtils.isEmpty(user.photo)) {
                        Glide.with(getApplicationContext()).load(user.photo).centerCrop().into(imgProfile);
                        URL_PHOTO = user.photo;
                    }

                }

            }, databaseError ->  alertDialog.showError(getString(R.string.msgGetDbError)));

            boolean isAdminOneDiffUid = PrimaryActivity.rol.equals(getString(R.string.admin_one)) && !getString(R.string.admin_uid).equals(UID_USER);
            boolean isAdminTwoDiffUid = (isAdminOneDiffUid) || (PrimaryActivity.rol.equals(getString(R.string.admin_two)) &&
                            !getString(R.string.admin_uid).equals(UID_USER) && !ROL_USER.equals(getString(R.string.admin_one)));

            btnDelete.setVisibility( isAdminOneDiffUid ? View.VISIBLE : View.GONE);
            btnUpdate.setVisibility( isAdminTwoDiffUid ? View.VISIBLE : View.GONE);

            editTextCed.setEnabled(isAdminTwoDiffUid);
            editTextFirstName.setEnabled(isAdminTwoDiffUid);
            editTextLastName.setEnabled(isAdminTwoDiffUid);
            editTextEmail.setEnabled(isAdminTwoDiffUid);
            editTextPhone.setEnabled(isAdminTwoDiffUid);
            spinner_canton.setEnabled(isAdminTwoDiffUid);
            spinner_rol.setEnabled(isAdminTwoDiffUid);

            textViewPassword.setVisibility(isAdminOneDiffUid? View.VISIBLE : View.GONE);
            textInputLayoutPassword.setVisibility(isAdminOneDiffUid? View.VISIBLE : View.GONE);

            imgProfile.setOnClickListener(view1 -> {

                if(!TextUtils.isEmpty(URL_PHOTO)) {
                    alertDialog.showConfirmDialog(getString(R.string.msgTitleInformation), getString(R.string.msgChooseOption)
                            ,getString(R.string.msgViewPhoto),getString(R.string.msgUpdatePhoto), (dialogInterface, i) ->
                                    startActivity(new Intent(this, ImageActivity.class).putExtra("url", URL_PHOTO))
                            , (dialogInterface, i) -> photoController.uploadPhoto());
                }else{
                    photoController.uploadPhoto();
                }

            });

            btnUpdate.setOnClickListener(view1 -> {
                alertDialog.showProgressMessage(getString(R.string.msgUpdateProfile));
                if(!editTextCed.getText().toString().trim().isEmpty() && editTextCed.getError() == null &&
                        !editTextFirstName.getText().toString().trim().isEmpty() && editTextFirstName.getError() == null &&
                        !editTextLastName.getText().toString().trim().isEmpty() && editTextLastName.getError() == null &&
                        !editTextEmail.getText().toString().trim().isEmpty() && editTextEmail.getError() == null &&
                        !editTextPhone.getText().toString().trim().isEmpty() && editTextPhone.getError() == null &&
                        !editTextPassword.getText().toString().trim().isEmpty() &&
                        !spinner_canton.getSelectedItem().toString().equals(getString(R.string.spinnerDefaultCanton)) &&
                        !spinner_rol.getSelectedItem().toString().equals(getString(R.string.spinnerDefaultRol))) {

                    User user = new User();
                    user.uid = UID_USER;
                    user.ced = editTextCed.getText().toString().trim();
                    user.firstName = editTextFirstName.getText().toString().trim().toUpperCase();
                    user.lastName = editTextLastName.getText().toString().trim().toUpperCase();
                    user.email = editTextEmail.getText().toString().trim().toLowerCase();
                    user.phone = editTextPhone.getText().toString().trim();
                    user.canton = spinner_canton.getSelectedItem().toString().trim();
                    user.rol = spinner_rol.getSelectedItem().toString().trim();
                    user.password = editTextPassword.getText().toString().trim();

                    PrimaryActivity.userController.updateUser(user).addOnCompleteListener(task -> {
                        alertDialog.hideProgressMessage();
                        if (task.isSuccessful()) {
                            alertDialog.showSuccess(getString(R.string.msgSuccessUpdateProfile));
                        } else {
                            alertDialog.showError(getString(R.string.msgNotUpdateProfile));
                        }
                    });

                }else{
                    alertDialog.showWarning(getString(R.string.msgEmptyFields));
                }
            });

            btnDelete.setOnClickListener(view ->
                alertDialog.showConfirmDialog(getString(R.string.msgConfirmDelProfile), getString(R.string.msgNotReversible),
                        getString(R.string.msgAccept),getString(R.string.msgCancel), (dialogInterface, i) ->
                {
                    alertDialog.showProgressMessage(getString(R.string.msgDelProfile));

                    PrimaryActivity.userController.deleteUserAndPhoto(UID_USER, PrimaryActivity.storageReference).addOnCompleteListener(task -> {
                        alertDialog.hideProgressMessage();

                        if (task.isSuccessful()) {
                            alertDialog.showSuccess(getString(R.string.msgSuccessDelProfile));
                        } else {
                            Exception exception = task.getException();
                            if (exception instanceof StorageException) {
                                StorageException storageException = (StorageException) exception;
                                if (storageException.getErrorCode() != StorageException.ERROR_OBJECT_NOT_FOUND) {
                                    alertDialog.showError(getString(R.string.msgDelProfileWithErrors));
                                }
                            }
                        }

                        finish();

                    });

                }, (dialogInterface, i) -> {})
            );

            imageButton.setOnClickListener(view ->
                startActivity(new Intent(this, DetAssistanceActivity.class)
                        .putExtra("uid", UID_USER)
                        .putExtra("name", USERNAME)
                        .putExtra("photo", URL_PHOTO))
            );

        }else{
            alertDialog.showError(getString(R.string.msgErrorUid));
        }

    }

    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->
            photoController.handleImageResult(result)
    );

    private final ActivityResultLauncher<String> requestPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            photoController.getImageFile();
        } else {
            alertDialog.showError(getString(R.string.msgNotPermission));
        }
    });

    ActivityResultLauncher<Intent> android11StoragePermission = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (storagePermissionController.isPermitted()) {
            photoController.getImageFile();
        } else {
            alertDialog.showError(getString(R.string.msgNotPermission));
        }
    });

    ActivityResultLauncher<CropImageContractOptions> cropImage = registerForActivityResult(new CropImageContract(), result -> {
        if (result.isSuccessful()) {
            Bitmap cropped = BitmapFactory.decodeFile(result.getUriFilePath(this, true));
            PrimaryActivity.userController.saveCroppedImage(cropped, UID_USER,PrimaryActivity.storageReference, alertDialog, this);
        }
    });

}