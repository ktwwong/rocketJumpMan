package com.example.kt.rocketJumpMan.objects;

import android.graphics.Rect;

public abstract class GameObject {
    public int x;
    public int y;
    public int moveX;
    public int moveY;
    public int width;
    public int height;

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Rect getRect(){
        return new Rect(x, y, x+width, y+height);
    }
}
