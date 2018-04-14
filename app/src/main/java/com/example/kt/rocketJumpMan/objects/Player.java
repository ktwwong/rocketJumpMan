package com.example.kt.rocketJumpMan.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.kt.rocketJumpMan.Animation;
import com.example.kt.rocketJumpMan.objects.GameObject;

public class Player extends GameObject {
    private Bitmap spritesheet;
    private int score;
    private boolean jumping;
    private boolean playing;
    private Animation animation = new Animation();
    private long startTime;
    private static final int START_POSITION = 300;

    public Player(Bitmap res, int w, int h, int numFrames){
        x = 100;
        y = START_POSITION;
        moveY = 0;
        score = 0;
        height = h;
        width = w;

        Bitmap[] img = new Bitmap[numFrames];
        spritesheet = res;

        for (int i = 0; i < img.length; i++){
            img[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }

        animation.setFrames(img);
        animation.setDelay(10);
        startTime = System.nanoTime();
    }

    public void setUp(boolean jump){jumping = jump;}

    public void update(){
        long elasped = (System.nanoTime() - startTime)/1000000;
        if (elasped > 100){
            score++;
            startTime = System.nanoTime();
        }
        animation.update();

        if (jumping) {
            moveY -= 1;
        } else {
            moveY += 1;
        }

        if (moveY > 7)
            moveY = 7;
        if (moveY < -7)
            moveY = -7;

        y += moveY * 2;

//        //MARK: don't fall down for now
//        if (y >= START_POSITION){
//            y = START_POSITION;
//        }

        // don't fly out of the top screen
        if (y <= 0){
            y = 0;
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(animation.getImage(), x, y, null);
    }

    public int getScore() {
        return score;
    }
    public boolean getPlaying(){return playing;}

    public void setPlaying(boolean isPlaying) {
        this.playing = isPlaying;
    }
    public void resetMoveY(){moveY = 0;}
    public void resetScore(){score = 0;}
}
