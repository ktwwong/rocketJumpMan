package com.example.kt.rocketJumpMan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
    public static final String EXTRA_DIFF = "diff";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int diff = 1;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            diff = extras.getInt(EXTRA_DIFF);
        }

        GamePanel gamePanel = findViewById(R.id.gamePanel);
        gamePanel.setDiff(diff);
    }
}
