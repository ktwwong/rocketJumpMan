package com.example.kt.rocketJumpMan;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Botborder extends GameObject {

    private Bitmap img;

    public Botborder(Bitmap res, int x, int y){

        height = 100;
        width = 100;

        this.x = x;
        this.y = y;
        moveX = GamePanel.SPEED;

        img = Bitmap.createBitmap(res, 0, 0, width, height);
    }

    public void update(){
        x += moveX;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(img, x, y, null);
    }
}
