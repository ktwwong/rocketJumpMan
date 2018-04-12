package com.example.kt.rocketJumpMan;

import android.graphics.Rect;

public abstract class GameObject {
    protected int x;
    protected int y;
    protected int moveX;
    protected int moveY;
    protected int width;
    protected int height;

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
