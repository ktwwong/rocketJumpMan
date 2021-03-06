package com.example.kt.rocketJumpMan.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.kt.rocketJumpMan.Animation;
import com.example.kt.rocketJumpMan.objects.GameObject;

import java.util.Random;

public class Bullet extends GameObject {
    private int score;
    private int speed;
    private Random random = new Random();
    private Animation animation = new Animation();
    private Bitmap spritesheet;

    public Bullet(Bitmap res, int x, int y, int w, int h, int s, int numFrames){

        super.x = x;
        super.y = y;
        width = w;
        height = h;

        // cap  speed
        if (speed > 40)
            speed = 40;

        Bitmap[] image = new Bitmap[numFrames];

        spritesheet = res;

        for (int i = 0; i < image.length; i++){
            image[i] = Bitmap.createBitmap(spritesheet, 0, i*height, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(100-(speed));

    }

    public void update(){
        x -= speed;
        animation.update();
    }

    public void draw(Canvas canvas){
        try {
            canvas.drawBitmap(animation.getImage(), x, y, null);
        } catch (Exception e){}
    }

    @Override
    public int getWidth(){
        // offset slightly for more realistic collision detection
        return width-10;
    }

    public void setSpeed(int i){
        speed = i + (int)(random.nextDouble()*score/30);
    }
}
