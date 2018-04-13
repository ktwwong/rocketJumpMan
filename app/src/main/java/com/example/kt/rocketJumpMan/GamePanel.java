package com.example.kt.rocketJumpMan;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
	public static final int WIDTH = 856;
	public static final int HEIGHT = 480;
	public static final int SPEED = -5;

	private long smokeStartTime;

	private long missileStartTime;

	private Random random = new Random();

    private MainThread thread;
    private Background bgfactory;
    private Player soldier;
    private ArrayList<Smoke> puff;
    private ArrayList<Missile> missiles;
    private ArrayList<Botborder> platform;

    private static final int MAX_NUM = 5;

    private int best;

    public GamePanel(Context context){
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while(retry){
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        bgfactory = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.sci_fi_bg_low));
        soldier = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.soldier), 26, 40, 2);
        puff = new ArrayList<Smoke>();
        missiles = new ArrayList<Missile>();
        platform = new ArrayList<Botborder>();

        for (int i = 0; i < WIDTH; i+=100) {
            platform.add(new Botborder(BitmapFactory.decodeResource(getResources(), R.drawable.floorbox), 0 + i, HEIGHT - 100));
        }

        smokeStartTime = System.nanoTime();
        missileStartTime = System.nanoTime();

        thread = new MainThread(getHolder(), this);
		//start game loop
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            if (!soldier.getPlaying()) {
                soldier.setPlaying(true);
            }else{
                soldier.setUp(true);
            }
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP){
            soldier.setUp(false);
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void update(){
        if (soldier.getPlaying()) {
            bgfactory.update();
            soldier.update();

            if (fallOutScreen())
                soldier.setPlaying(false);
            else
                soldier.setPlaying(true);

            // add missiles on timer
            long missileElapsed = (System.nanoTime() - missileStartTime)/1000000;
            if (missileElapsed > (2000 - soldier.getScore())/4){
                // first missile always goes down the middle
                if (missiles.size() <= MAX_NUM) {
                    if (missiles.size() == 0) {
                        missiles.add(new Missile(BitmapFactory.decodeResource(getResources(), R.drawable.missile), WIDTH + 10, HEIGHT / 2, 45, 15, soldier.getScore(), 13));
                    } else {
                        missiles.add(new Missile(BitmapFactory.decodeResource(getResources(), R.drawable.missile), WIDTH + 10, (int) (random.nextDouble() * (HEIGHT/1.3)), 45, 15, soldier.getScore(), 13));
                    }
                }
                // reset timer
                missileStartTime = System.nanoTime();
            }

            for (int i = 0; i < missiles.size(); i++){
                // update missile
                missiles.get(i).update();
                if (collision(missiles.get(i), soldier)){
                    // remove when hit
                    missiles.remove(i);
                    // the game end
                    soldier.setPlaying(false);
                    break;
                }
                // remove missile if the is out of the screen
                if (missiles.get(i).getX() < -100){
                    missiles.remove(i);
                    break; 
                }
            }

            // first missile always goes down the middle
            if (platform.size() <= MAX_NUM) {
                if (random.nextBoolean())
                    platform.add(new Botborder(BitmapFactory.decodeResource(getResources(), R.drawable.floorbox), WIDTH, HEIGHT -100));
            }
            for (int i =0; i < platform.size(); i++){
                platform.get(i).update();
                if (collision(platform.get(i), soldier)){
                    soldier.y = HEIGHT - platform.get(i).getHeight() - 40;
                }
                if (platform.get(i).getX() < -100){
                    platform.remove(i);
                    break;
                }
            }

            // add smoke puffs on timer
            long elapsed = (System.nanoTime() - smokeStartTime)/1000000;
            if (elapsed > 120){
                puff.add(new Smoke(soldier.getX(), soldier.getY()+30));
                smokeStartTime = System.nanoTime();
            }

            for (int i=0; i < puff.size(); i++){
                puff.get(i).update();
                if (puff.get(i).getX() < -10){
                    puff.remove(i);
                }
            }
        }
        else {
            reStart();
        }
    }

    public boolean collision(GameObject a, GameObject b){
        if (Rect.intersects(a.getRect(), b.getRect())){
            return true;
        }
        return false;
    }

    public boolean fallOutScreen(){
        // if player fall out from screen
        if (soldier.y >= HEIGHT){
            return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);
        if (canvas != null) {
            final int savedState = canvas.save();

            canvas.scale(scaleFactorX, scaleFactorY);

            bgfactory.draw(canvas);
            soldier.draw(canvas);
            for (Smoke smokepuff:puff){
                smokepuff.draw(canvas);
            }

            for (Missile m: missiles){
                m.draw(canvas);
            }

            for (Botborder floor: platform){
                floor.draw(canvas);
            }

            drawText(canvas);
            canvas.restoreToCount(savedState);
        }
    }

    public void reStart(){
        platform.clear();
        missiles.clear();
        puff.clear();

        soldier.resetMoveY();
        soldier.resetScore();
        soldier.setY(300);

        if (soldier.getScore() > best) {
            best = soldier.getScore();
        }

        for (int i = 0; i < WIDTH; i += 100) {
            platform.add(new Botborder(BitmapFactory.decodeResource(getResources(), R.drawable.floorbox), 0 + i, HEIGHT - 100));
        }
    }

    public void drawText(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Distance: " + (soldier.getScore()*3), 10, 0 + 30, paint);
        canvas.drawText("Best: " + best, WIDTH - 215, 0 + 30, paint);

        if (!soldier.getPlaying()){
            Paint paint1 = new Paint();
            paint1.setTextSize(40);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("PRESS TO START", WIDTH/2 - 50, HEIGHT/2, paint1);

            paint1.setTextSize(20);
            canvas.drawText("PRESS AND HOLD TO GO UP", WIDTH/2 - 50, HEIGHT/2 + 20, paint1);
            canvas.drawText("RELEASE TO GO DOWN", WIDTH/2 - 50, HEIGHT/2 + 40, paint1);

            Paint paint2 = new Paint();
            paint2.setColor(Color.WHITE);
            paint2.setTextSize(20);
            canvas.drawText("You Can Walk On This Platform, Won't fall", WIDTH/2 - 50, HEIGHT - 110, paint2);
        }

    }
}
