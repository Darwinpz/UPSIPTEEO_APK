package com.dpilaloa.upsipteeo.data.controllers;

import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

public class ZoomController {

    ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private float lastTouchX;
    private float lastTouchY;
    private float posX = 0f;
    private float posY = 0f;
    private boolean isScaling = false;
    private float imageWidth;
    private float imageHeight;
    ImageView imageView;

    public ZoomController(ImageView imageView) {
        this.imageView = imageView;

        imageView.post(() -> {
            imageWidth = imageView.getWidth();
            imageHeight = imageView.getHeight();
        });

        mScaleGestureDetector = new ScaleGestureDetector(imageView.getContext(), new ScaleListener());

    }

    public void onTouchEvent(MotionEvent motionEvent) {
        mScaleGestureDetector.onTouchEvent(motionEvent);
        final int action = motionEvent.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastTouchX = motionEvent.getX();
                lastTouchY = motionEvent.getY();
                isScaling = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isScaling) {
                    final float dx = motionEvent.getX() - lastTouchX;
                    final float dy = motionEvent.getY() - lastTouchY;
                    posX += dx;
                    posY += dy;
                    posX = Math.max(getMinTranslationX(), Math.min(posX, getMaxTranslationX()));
                    posY = Math.max(getMinTranslationY(), Math.min(posY, getMaxTranslationY()));
                    imageView.setTranslationX(posX);
                    imageView.setTranslationY(posY);
                    lastTouchX = motionEvent.getX();
                    lastTouchY = motionEvent.getY();
                }
                break;

            case MotionEvent.ACTION_UP:
                isScaling = false;
                break;
        }

    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            isScaling = true;
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, 10.0f));
            imageView.setScaleX(mScaleFactor);
            imageView.setScaleY(mScaleFactor);
            posX = Math.max(getMinTranslationX(), Math.min(posX, getMaxTranslationX()));
            posY = Math.max(getMinTranslationY(), Math.min(posY, getMaxTranslationY()));
            imageView.setTranslationX(posX);
            imageView.setTranslationY(posY);
            return true;
        }
    }

    private float getMaxTranslationX() {
        float scaledWidth = imageWidth * mScaleFactor;
        return (scaledWidth - imageWidth) / 2;
    }

    private float getMinTranslationX() {
        return -(imageWidth * (mScaleFactor - 1)) / 2;
    }

    private float getMaxTranslationY() {
        float scaledHeight = imageHeight * mScaleFactor;
        return (scaledHeight - imageHeight) / 2;
    }

    private float getMinTranslationY() {
        return -(imageHeight * (mScaleFactor - 1)) / 2;
    }

}
