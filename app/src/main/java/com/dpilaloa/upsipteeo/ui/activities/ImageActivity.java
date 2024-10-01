package com.dpilaloa.upsipteeo.ui.activities;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.dpilaloa.upsipteeo.R;
import com.dpilaloa.upsipteeo.data.controllers.ZoomController;

import java.util.Objects;

public class ImageActivity extends AppCompatActivity{

    ImageView imageView;
    ZoomController zoomController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imageView = findViewById(R.id.image);
        Toolbar toolbar = findViewById(R.id.toolbar);
        String link = Objects.requireNonNull(getIntent().getExtras()).getString("url");
        toolbar.setOnClickListener(view -> finish());

        zoomController = new ZoomController(imageView);

        Glide.with(this).load(link).into(imageView);

    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        zoomController.onTouchEvent(motionEvent);
        return true;
    }

}