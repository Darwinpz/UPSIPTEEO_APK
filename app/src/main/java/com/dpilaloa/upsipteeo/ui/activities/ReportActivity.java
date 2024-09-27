package com.dpilaloa.upsipteeo.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.dpilaloa.upsipteeo.MainActivity;
import com.dpilaloa.upsipteeo.R;
import com.dpilaloa.upsipteeo.data.controllers.AlertDialogController;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ReportActivity extends AppCompatActivity {

    HSSFWorkbook hssfWorkbook;
    HSSFSheet hssfSheet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        AlertDialogController dialog = new AlertDialogController(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        CardView cardSummary = findViewById(R.id.cardSummary);

        toolbar.setOnClickListener(view -> finish());

        hssfWorkbook = new HSSFWorkbook();

        cardSummary.setOnClickListener(view -> {

            dialog.showProgressMessage("Creando Reporte...");

            hssfSheet = hssfWorkbook.createSheet("CONSOLIDADO");
            encabezado(hssfSheet);
            MainActivity.databaseReference.child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        int contador = 1;

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            if (!Objects.requireNonNull(snapshot.child("rol").getValue()).toString().equals(getString(R.string.admin_one))) {

                                HSSFRow hssfRow = hssfSheet.createRow(contador);
                                HSSFCell hssfCell0 = hssfRow.createCell(0);
                                HSSFCell hssfCell1 = hssfRow.createCell(1);
                                HSSFCell hssfCell2 = hssfRow.createCell(2);
                                HSSFCell hssfCell3 = hssfRow.createCell(3);
                                HSSFCell hssfCell4 = hssfRow.createCell(4);
                                HSSFCell hssfCell5 = hssfRow.createCell(5);
                                HSSFCell hssfCell6 = hssfRow.createCell(6);
                                HSSFCell hssfCell7 = hssfRow.createCell(7);

                                hssfCell0.setCellValue(snapshot.child("ced").getValue(String.class));
                                hssfCell1.setCellValue(snapshot.child("name").getValue(String.class));
                                hssfCell2.setCellValue(snapshot.child("phone").getValue(String.class));
                                hssfCell3.setCellValue(snapshot.child("email").getValue(String.class));
                                hssfCell4.setCellValue(snapshot.child("rol").getValue(String.class));
                                hssfCell5.setCellValue(snapshot.child("canton").getValue(String.class));

                                if(snapshot.child("photo").exists()){
                                    hssfCell6.setCellValue("SI");
                                    hssfCell7.setCellValue(snapshot.child("photo").getValue(String.class));
                                }else{
                                    hssfCell6.setCellValue("NO");
                                    hssfCell7.setCellValue("");
                                }

                                contador++;

                            }

                        }

                        try {

                            String reportName = "/rpt_consolidado_"+(new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.getDefault()).format(new Date()))+".xls";
                            File file = new File(Environment.getExternalStorageDirectory()+reportName);

                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            hssfWorkbook.write(fileOutputStream);
                            hssfWorkbook.close();
                            fileOutputStream.close();

                            dialog.hideProgressMessage();
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setDataAndType(Uri.fromFile(file),"application/vnd.ms-excel");
                            startActivity(i);

                        }catch (Exception e){
                            Toast.makeText(getBaseContext(),"No se puede generar el reporte",Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {throw  error.toException();}

            });

        });



    }

    public void encabezado(HSSFSheet hssfSheet){

        String [] columns = {"cedula","nombre","celular","correo","rol","canton","foto","url"};
        HSSFRow hssfRow0 = hssfSheet.createRow(0);

        for (int i = 0; i < columns.length ; i++) {
            HSSFCell hssfCell = hssfRow0.createCell(i);
            hssfCell.setCellValue(columns[i]);
        }
    }
}