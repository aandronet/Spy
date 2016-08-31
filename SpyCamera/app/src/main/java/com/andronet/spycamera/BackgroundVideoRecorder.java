package com.andronet.spycamera;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.File;
import java.util.Date;

public class BackgroundVideoRecorder extends Service implements SurfaceHolder.Callback {

    private WindowManager windowManager;
    private SurfaceView surfaceView;
    private Camera camera;
    private MediaRecorder mediaRecorder = null;
    public static boolean isRunning;
    boolean mIsFrontCameraRunning = false;
    int displayRotation;

    @Override
    public void onCreate() {
        //start();
        camera = Camera.open();
        isRunning = true;
        windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

        Camera.CameraInfo camInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(0, camInfo);
        int cameraRotationOffset = camInfo.orientation;
        int rotation = windowManager.getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break; // Natural orientation
            case Surface.ROTATION_90:
                degrees = 90;
                break; // Landscape left
            case Surface.ROTATION_180:
                degrees = 180;
                break;// Upside down
            case Surface.ROTATION_270:
                degrees = 270;
                break;// Landscape right
        }

        if (mIsFrontCameraRunning) {
            displayRotation = (cameraRotationOffset + degrees) % 360;
            displayRotation = (360 - displayRotation) % 360; // compensate the mirror
        } else {
            displayRotation = (cameraRotationOffset - degrees + 360) % 360; // back-facing
        }


        // Create new SurfaceView, set its size to 1x1, move it to the top left corner and set this service as a callback

        surfaceView = new SurfaceView(this);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                1, 1,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT
        );
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        windowManager.addView(surfaceView, layoutParams);
        surfaceView.getHolder().addCallback(this);
    }

    // Method called right after Surface created (initializing and starting MediaRecorder)
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        mediaRecorder = new MediaRecorder();
        camera.unlock();

        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
        mediaRecorder.setCamera(camera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        mediaRecorder.setOrientationHint(displayRotation);
        String dirName = Environment.getExternalStorageDirectory() + "/spy/";
        File dir = new File(dirName);
        if (!dir.exists()) {
            dir.mkdir();
        }
        String fName = dirName + DateFormat.format("yyyy-MM-dd_kk-mm-ss", new Date().getTime()) + ".mp4";

        mediaRecorder.setOutputFile(fName);

        try {
            mediaRecorder.prepare();
        } catch (Exception e) {
            Log.e("QQQQQQQQQ", "cayr: " + e.getMessage());
        }
        Log.e("Start recording", "yes");
        mediaRecorder.start();
    }


    @Override
    public void onDestroy() {
        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();

        camera.lock();
        camera.release();

        windowManager.removeView(surfaceView);
        Log.e("Stop recording", "yes");
        isRunning = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}

