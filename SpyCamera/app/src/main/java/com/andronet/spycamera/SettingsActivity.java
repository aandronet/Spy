package com.andronet.spycamera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class SettingsActivity extends Activity {

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
        toggle.setChecked(sharedPref.getBoolean(LauncherActivity.TRANSPARENT, false));
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor ed = sharedPref.edit();
                ed.putBoolean(LauncherActivity.TRANSPARENT, isChecked);
                ed.commit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SettingsActivity.this, LauncherActivity.class));
        finish();
        super.onBackPressed();
    }
}
