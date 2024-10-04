package com.dpilaloa.upsipteeo.ui.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.dpilaloa.upsipteeo.data.controllers.AlertDialogController;
import com.dpilaloa.upsipteeo.data.controllers.PhotoController;
import com.dpilaloa.upsipteeo.data.controllers.StoragePermissionController;
import com.dpilaloa.upsipteeo.ui.activities.DetAssistanceActivity;
import com.dpilaloa.upsipteeo.MainActivity;
import com.dpilaloa.upsipteeo.data.models.User;
import com.dpilaloa.upsipteeo.ui.activities.PrimaryActivity;
import com.dpilaloa.upsipteeo.R;
import com.dpilaloa.upsipteeo.ui.activities.ImageActivity;
import com.dpilaloa.upsipteeo.utils.ValEditTextWatcher;

public class ProfileFragment extends Fragment {

    private String USERNAME = "", URL_PHOTO = "";
    private AlertDialogController alertDialog;
    private PhotoController photoController;
    private StoragePermissionController storagePermissionController;
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
        ImageView imgProfile = view.findViewById(R.id.imageViewProfile);
        Spinner spinner_canton = view.findViewById(R.id.spinnerCanton);
        Button btnUpdate = view.findViewById(R.id.buttonUpdate);
        Button btnLogOut = view.findViewById(R.id.buttonLogOut);
        ImageButton imageButton = view.findViewById(R.id.imgButtonShowAssistance);

        alertDialog = new AlertDialogController(view.getContext());

        storagePermissionController = new StoragePermissionController(view.getContext(),requestPermission,android11StoragePermission);
        photoController = new PhotoController(getImage,storagePermissionController,cropImage);

        ArrayAdapter<CharSequence> adapterSpinnerCanton = ArrayAdapter.createFromResource(view.getContext(), R.array.canton, android.R.layout.simple_spinner_item);
        adapterSpinnerCanton.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_canton.setAdapter(adapterSpinnerCanton);

        if(!PrimaryActivity.id.isEmpty()) {

            editTextEmail.addTextChangedListener(new ValEditTextWatcher(editTextEmail,
                    input -> PrimaryActivity.userController.valEmail(input),getString(R.string.msgNotValEmail)));

            editTextPhone.addTextChangedListener(new ValEditTextWatcher(editTextPhone,
                    input -> PrimaryActivity.userController.valPhone(input),getString(R.string.msgNotValPhone)));

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

            }, databaseError -> alertDialog.showError(getString(R.string.msgGetDbError)));

            imageButton.setOnClickListener(view1 ->
                startActivity(new Intent(view.getContext(), DetAssistanceActivity.class)
                        .putExtra("uid", PrimaryActivity.id)
                        .putExtra("name", USERNAME))
            );

            imgProfile.setOnClickListener(view1 -> {

                if(!TextUtils.isEmpty(URL_PHOTO)) {
                    alertDialog.showConfirmDialog(getString(R.string.msgTitleInformation), getString(R.string.msgChooseOption),getString(R.string.msgViewPhoto),getString(R.string.msgUpdatePhoto), (dialogInterface, i) ->
                        startActivity(new Intent(getContext(), ImageActivity.class).putExtra("url", URL_PHOTO))
                    , (dialogInterface, i) -> photoController.uploadPhoto());
                }else{
                    photoController.uploadPhoto();
                }

            });

            btnUpdate.setOnClickListener(view1 -> {
                alertDialog.showProgressMessage(getString(R.string.msgUpdateProfile));
                if(!editTextEmail.getText().toString().trim().isEmpty() && editTextEmail.getError() == null &&
                        !editTextPhone.getText().toString().trim().isEmpty() && editTextPhone.getError() == null &&
                        !editTextPassword.getText().toString().trim().isEmpty() &&
                        !spinner_canton.getSelectedItem().toString().equals(getString(R.string.spinnerDefaultCanton))) {

                    User user = new User();
                    user.uid = PrimaryActivity.id;
                    user.ced = txtCed.getText().toString().trim();
                    user.name = txtName.getText().toString().trim().toUpperCase();
                    user.email = editTextEmail.getText().toString().trim().toLowerCase();
                    user.phone = editTextPhone.getText().toString().trim();
                    user.canton = spinner_canton.getSelectedItem().toString().trim();
                    user.rol = txtRol.getText().toString().trim();
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

            btnLogOut.setOnClickListener(view1 -> {
                alertDialog.showProgressMessage(getString(R.string.msgLogOut));
                PrimaryActivity.userController.logOut(PrimaryActivity.preferences);
                alertDialog.hideProgressMessage();
                requireActivity().finish();
                startActivity(new Intent(view.getContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            });

        }else{
            alertDialog.showError(getString(R.string.msgErrorUid));
        }

        return view;
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
            Bitmap cropped = BitmapFactory.decodeFile(result.getUriFilePath(requireContext(), true));
            PrimaryActivity.userController.saveCroppedImage(cropped, PrimaryActivity.id,PrimaryActivity.storageReference, alertDialog, getContext());
        }
    });

}
