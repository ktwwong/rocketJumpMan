package com.example.kt.rocketJumpMan;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
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

    private static final int MAX_NUM = 5;

    public GamePanel(Context context){
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

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

        smokeStartTime = System.nanoTime();
        missileStartTime = System.nanoTime();

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
                        missiles.add(new Missile(BitmapFactory.decodeResource(getResources(), R.drawable.missile), WIDTH + 10, (int) (random.nextDouble() * (HEIGHT)), 45, 15, soldier.getScore(), 13));
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

            // add smoke puffs on timer
            long elapsed = (System.nanoTime() - smokeStartTime)/1000000;
            if (elapsed > 120){
                puff.add(new Smoke(soldier.getX(), soldier.getY()+10));
                smokeStartTime = System.nanoTime();
            }

            for (int i=0; i < puff.size(); i++){
                puff.get(i).update();
                if (puff.get(i).getX() < -10){
                    puff.remove(i);
                }
            }
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

            canvas.restoreToCount(savedState);
        }
    }
}
