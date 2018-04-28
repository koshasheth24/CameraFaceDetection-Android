package com.example.kosha.camerafacedetection;

/**
 * Created by Kosha on 3/27/2017.
 */

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();

            startFaceDetection(); // start face detection feature

        } catch (IOException e) {
          //  Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }
    public void startFaceDetection(){
        // Try starting Face Detection
        Camera.Parameters params = mCamera.getParameters();

        // start face detection only *after* preview has started
        if (params.getMaxNumDetectedFaces() > 0){
            // camera supports face detection, so can start it:
            mCamera.startFaceDetection();
        }
    }



    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        if (mHolder.getSurface() == null){
            // preview surface does not exist
           // Log.d(TAG, "mHolder.getSurface() == null");
            return;
        }

        try {
            mCamera.stopPreview();

        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
           // Log.d(TAG, "Error stopping camera preview: " + e.getMessage());
        }

        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

            startFaceDetection(); // re-start face detection feature

        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
           // Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }
}

class MyFaceDetectionListener implements Camera.FaceDetectionListener {

    @Override
    public void onFaceDetection(Camera.Face[] faces, Camera camera) {
        if (faces.length > 0){
            Log.d("FaceDetection", "face detected: "+ faces.length +
                    " Face 1 Location X: " + faces[0].rect.centerX() +
                    "Y: " + faces[0].rect.centerY() );
        }
    }
}