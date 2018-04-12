package com.example.kt.rocketJumpMan;

import android.graphics.Bitmap;

public class Animation {
    private Bitmap[] myFrames;
    private long startTime;
    private int cFrames;
    private long delay;
    private boolean played;

    public void setFrames(Bitmap[] frames){
       this.myFrames = frames;
        cFrames = 0;
       startTime = System.nanoTime();
    }

    public void setDelay(long d){
        delay = d;
    }
    public void setCurrentFrames(int i){
        cFrames = i;
    }

    public void update(){
        long elapsed = (System.nanoTime() - startTime)/1000000;

        if (elapsed > delay){
            cFrames++;
            startTime = System.nanoTime();
        }
        if (cFrames == myFrames.length){
            cFrames = 0;
            played = true;
        }
    }

    public Bitmap getImage(){
        return myFrames[cFrames];
    }

    public int getCurrentFrames() {
        return cFrames;
    }

    public boolean isPlayed() {
        return played;
    }
}
