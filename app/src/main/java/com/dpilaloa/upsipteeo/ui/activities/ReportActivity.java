package com.dpilaloa.upsipteeo.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.dpilaloa.upsipteeo.MainActivity;
import com.dpilaloa.upsipteeo.R;
import com.dpilaloa.upsipteeo.data.controllers.AlertDialogController;
import com.dpilaloa.upsipteeo.data.controllers.ReportController;
import com.dpilaloa.upsipteeo.data.controllers.StoragePermissionController;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReportActivity extends AppCompatActivity {

    HSSFWorkbook hssfWorkbook;
    private AlertDialogController alertDialog;
    private StoragePermissionController storagePermissionController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Toolbar toolbar = findViewById(R.id.toolbar);
        CardView cardSummary = findViewById(R.id.cardSummary);

        storagePermissionController = new StoragePermissionController(this,requestPermission,android11StoragePermission);

        getReport();

        ReportController reportController = new ReportController(MainActivity.databaseReference);
        alertDialog = new AlertDialogController(this);
        toolbar.setOnClickListener(view -> finish());

        hssfWorkbook = new HSSFWorkbook();

        cardSummary.setOnClickListener(view -> {

            alertDialog.showProgressMessage(getString(R.string.msgCreatingReport));
            reportController.removeSheetByName(hssfWorkbook,"SUMMARY");
            HSSFSheet hssfSheet = hssfWorkbook.createSheet("SUMMARY");
            reportController.headUsers(hssfSheet);

            reportController.reportUsers("Rol", hssfSheet, val -> {

                if(val){

                    try {

                        String reportName = "/rpt_cne_summary_"+(new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.getDefault()).format(new Date()))+".xls";
                        File file = new File(Environment.getExternalStorageDirectory()+"/"+reportName);

                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        hssfWorkbook.write(fileOutputStream);
                        hssfWorkbook.close();
                        fileOutputStream.close();

                        alertDialog.showSuccess(getString(R.string.msgSuccessReport));

                        Uri fileUri = FileProvider.getUriForFile(this, getString(R.string.fileProviderName), file);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setDataAndType(fileUri,"application/vnd.ms-excel");
                        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(i);

                    }catch (Exception e){
                        alertDialog.showError(getString(R.string.msgNotCreateReport));
                    }
                }

            }, databaseError -> alertDialog.showError(getString(R.string.msgGetDbError)));

        });

    }

    private void getReport() {
        if (storagePermissionController.isPermitted()) {
            setupStrictMode();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            storagePermissionController.requestAndroid11StoragePermission();
        } else {
            storagePermissionController.setRequestPermission();
        }
    }

    private final ActivityResultLauncher<String> requestPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {

        if (isGranted) {
            setupStrictMode();
        } else {
            finish();
            alertDialog.showError(getString(R.string.msgNotPermission));
        }

    });

    ActivityResultLauncher<Intent> android11StoragePermission = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (storagePermissionController.isPermitted()) {
            setupStrictMode();
        } else {
            finish();
            alertDialog.showError(getString(R.string.msgNotPermission));
        }
    });

    private void setupStrictMode() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

}