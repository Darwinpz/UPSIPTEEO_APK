package com.dpilaloa.upsipteeo.data.controllers;

import androidx.annotation.NonNull;

import com.dpilaloa.upsipteeo.data.interfaces.CompleteInterface;
import com.dpilaloa.upsipteeo.data.interfaces.DbErrorInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ReportController {

    DatabaseReference databaseReference;

    public ReportController(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public void headUsers(HSSFSheet hssfSheet){

        String [] columns = {"ced","name","phone","email","rol","canton","photo","url"};
        HSSFRow hssfRow0 = hssfSheet.createRow(0);
        int i = 0;
        for (String column : columns) {
            hssfRow0.createCell(i++).setCellValue(column);
        }

    }

    public void removeSheetByName(HSSFWorkbook  hssfWorkbook, String name){
        int index = hssfWorkbook.getSheetIndex(name);
        if (index != -1) {
            hssfWorkbook.removeSheetAt(index);
        }
    }

    public void reportUsers(String rol, HSSFSheet hssfSheet, CompleteInterface completeInterface, DbErrorInterface dbErrorInterface){

        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    int count = 1;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        String userRol = snapshot.child("rol").getValue(String.class);

                        if(userRol!=null && (rol.equals("Rol") || userRol.equals(rol))) {

                            HSSFRow hssfRow = hssfSheet.createRow(count);
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

                            count++;
                        }

                    }

                    completeInterface.isComplete(true);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {dbErrorInterface.onProcessError(error);}
        });

    }


}
