package com.andronet.spycamera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.util.Log;


public class KeyBroadcastReceiver extends BroadcastReceiver {

    private SharedPreferences sharedPref;

    @Override
    public void onReceive(Context context, Intent intent) {

        sharedPref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int volume = MainActivity.getVolume(audio);

//        if (volume > MainActivity.currentVolume && sharedPref.getBoolean(MainActivity.ENABLED_KEY, false)) {
//            Intent i = new Intent(context, BackgroundVideoRecorder.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            Log.e("AAAAAAAAAAAAAA", "AAAAAAAAAAAA");
//            if (!BackgroundVideoRecorder.isRunning) {
//                context.startService(i);
//            }
//        } else if (volume < MainActivity.currentVolume && sharedPref.getBoolean(MainActivity.ENABLED_KEY, false)) {
//            Log.e("BBBBBBBBBBBBBB", "BBBBBBBBBBBB");
//            if (BackgroundVideoRecorder.isRunning) {
//                context.stopService(new Intent(context, BackgroundVideoRecorder.class));
//            }
//        }
        MainActivity.currentVolume = volume;
    }
}
