package com.example.kt.rocketJumpMan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
}
