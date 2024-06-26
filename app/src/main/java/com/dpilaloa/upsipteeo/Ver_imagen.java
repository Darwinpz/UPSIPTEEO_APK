package com.dpilaloa.upsipteeo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.Objects;

public class Ver_imagen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_imagen);

        PhotoView photoView = (PhotoView) findViewById(R.id.imagen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        String link = Objects.requireNonNull(getIntent().getExtras()).getString("url");
        Glide.with(this).load(link).into(photoView);
        toolbar.setOnClickListener(view -> finish());

    }
}