package com.andronet.spycamera;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class TransparentActivity extends MainActivity {

    private int tapCount;
    protected Button settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent);

        RelativeLayout root = (RelativeLayout) findViewById(R.id.root);
        root.setOnClickListener(this);

        settings = (Button) findViewById(R.id.settings);
        settings.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.root) {
            screenTapped();
        } else if (view.getId() == R.id.settings) {
            Intent i = new Intent(TransparentActivity.this, SettingsActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void screenTapped() {
        tapCount = tapCount + 1;
        if (tapCount == 3) {
            settings.setVisibility(View.VISIBLE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    settings.setVisibility(View.GONE);
                }
            }, 5000L);
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tapCount = 0;
            }
        }, 3000L);
    }
}
