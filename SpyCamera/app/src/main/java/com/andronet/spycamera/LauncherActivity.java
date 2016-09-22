package com.andronet.spycamera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class LauncherActivity extends Activity{

    public static String TRANSPARENT = "transparent";

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        if(!sharedPref.getBoolean(TRANSPARENT, false)){
            startActivity(new Intent(LauncherActivity.this, MainActivity.class));
        }else{
            startActivity(new Intent(LauncherActivity.this, TransparentActivity.class));
        }
        finish();
    }
}
