package com.example.kt.rocketJumpMan;

import android.app.Activity;
import android.app.UiAutomation;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.VideoView;

public class UIanimation extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opening_animation);

        VideoView vv = findViewById(R.id.video);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+ R.raw.opening);
        vv.setVideoURI(uri);
        vv.requestFocus();
        vv.start();

    // Do after the video finish
        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer arg0)
            {
                startActivity();
            }
        });
    }

    private void startActivity() {
        Intent intent = new Intent(this, UImenu.class);
        startActivity(intent);
        finish();
    }
}
