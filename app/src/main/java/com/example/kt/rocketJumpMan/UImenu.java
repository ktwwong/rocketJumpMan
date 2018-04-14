package com.example.kt.rocketJumpMan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class UImenu extends Activity{
    private final View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int value = -1;
            switch (v.getId()) {
                case R.id.btn1:
                    value = 1;
                    break;
                case R.id.btn2:
                    value = 2;
                    break;
                case R.id.btn3:
                    value = 3;
                    break;
            }
            startGame(value);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_menu);
        findViewById(R.id.btn1).setOnClickListener(mClickListener);
        findViewById(R.id.btn2).setOnClickListener(mClickListener);
        findViewById(R.id.btn3).setOnClickListener(mClickListener);
    }

    public void startGame(int diff){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_DIFF, diff);
        startActivity(intent);
    }

    public void newDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to quit game?");
        builder.setTitle("Exit");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).show();
    }

    public boolean dispatchKeyEvent(KeyEvent event){

        if (event.getAction() == KeyEvent.ACTION_DOWN){
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_BACK:
                    newDialog();
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
