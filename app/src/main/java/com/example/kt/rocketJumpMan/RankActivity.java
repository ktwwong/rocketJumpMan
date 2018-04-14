package com.example.kt.rocketJumpMan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RankActivity extends AppCompatActivity {
    public static final String EXTRA_MY_SCORE = "my_score";

    private int mMyScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        Bundle extras = getIntent().getExtras();
        mMyScore = extras != null ? extras.getInt(EXTRA_MY_SCORE) : 0;
    }
}
