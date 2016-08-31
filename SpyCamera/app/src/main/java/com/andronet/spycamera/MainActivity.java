package com.andronet.spycamera;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

    public static int currentVolume;
    private int tapCount;
    private Button kill;
    private SharedPreferences sharedPref;
    public static String ENABLED_KEY = "enabled";
    private Handler handler = new Handler();

    private static final int
            PERMISSION_PHOTO_CAMERA = 151,
            PERMISSION_VIDEO_RECORDER = 152;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kill = (Button) findViewById(R.id.killMe);
        kill.setOnClickListener(this);
        RelativeLayout root = (RelativeLayout) findViewById(R.id.root);
        root.setOnClickListener(this);

        sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        currentVolume = getVolume(audio);

        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(MainActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1234);
            }
        }

        List<String> permissions = new ArrayList<>();
        if (!isPermissionGranted(this, Manifest.permission.CAMERA)) {
            permissions.add(Manifest.permission.CAMERA);
        }
        if (!isPermissionGranted(this, Manifest.permission.RECORD_AUDIO)) {
            permissions.add(Manifest.permission.RECORD_AUDIO);
        }
        if (!isPermissionGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (permissions.size() > 0) {
            String[] arr = new String[permissions.size()];
            permissions.toArray(arr);
            ActivityCompat.requestPermissions(this, arr, PERMISSION_VIDEO_RECORDER);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.root) {
            screenTapped();
        } else if (view.getId() == R.id.killMe) {
            killTapped();
        }
    }

    public static int getVolume(AudioManager audio) {
        return audio.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public static boolean isPermissionGranted(Context context, String permission) {
        int permissionCheck = ContextCompat.checkSelfPermission(context, permission);

        if (PackageManager.PERMISSION_GRANTED == permissionCheck) {
            return true;
        }
        return false;
    }

    public void screenTapped() {
        tapCount = tapCount + 1;
        if (tapCount == 3) {
            kill.setVisibility(View.VISIBLE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    kill.setVisibility(View.GONE);
                }
            }, 5000L);
            if (sharedPref.getBoolean(ENABLED_KEY, false)) {
                kill.setText("Disable");
            } else {
                kill.setText("Enable");
            }
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tapCount = 0;
            }
        }, 3000L);
    }

    public void killTapped() {
        boolean enable;
        if (sharedPref.getBoolean(ENABLED_KEY, false)) {
            enable = false;
        } else {
            enable = true;
        }
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(ENABLED_KEY, enable);
        editor.commit();
        kill.setVisibility(View.GONE);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    Intent i = new Intent(MainActivity.this, BackgroundVideoRecorder.class);
                    //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Log.e("AAAAAAAAAAAAAA", "AAAAAAAAAAAA");
                    if (!BackgroundVideoRecorder.isRunning) {
                        startService(i);
                    }
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    Log.e("BBBBBBBBBBBBBB", "BBBBBBBBBBBB");
                    if (BackgroundVideoRecorder.isRunning) {
                        stopService(new Intent(MainActivity.this, BackgroundVideoRecorder.class));
                    }
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }
}
