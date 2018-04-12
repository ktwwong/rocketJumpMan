package com.example.kt.rocketJumpMan;


import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Background {
    private Bitmap img;
    private int x, y, speed;

    public Background(Bitmap res){
        img = res;
        speed = GamePanel.SPEED;
    }

    public void update(){
		x += speed;
		if (x < -GamePanel.WIDTH)
			x = 0;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(img, x, y, null);
		if (x < 0){
			canvas.drawBitmap(img, x + GamePanel.WIDTH, y, null);
		}
    }
}
