package com.example.kt.rocketJumpMan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.kt.rocketJumpMan.objects.Botborder;
import com.example.kt.rocketJumpMan.objects.GameObject;
import com.example.kt.rocketJumpMan.objects.Bullet;
import com.example.kt.rocketJumpMan.objects.Player;
import com.example.kt.rocketJumpMan.objects.Smoke;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;
    public static final int SPEED = -5;
    private static final int MAX_BULLET = 5;

    private static final int STATE_INTRO = 0;
    private static final int STATE_IN_GAME = 1;
    private static final int STATE_GAME_OVER = 2;

    private long smokeStartTime;
    private long missileStartTime;

    private Random random = new Random();

    private Context context;
    private static MainThread thread;
    private Background bgFactory;
    private Player soldier;
    private final ArrayList<Smoke> puff = new ArrayList<>();
    private final ArrayList<Bullet> missiles = new ArrayList<>();
    private final ArrayList<Botborder> platform = new ArrayList<>();

    float scaleFactorX, scaleFactorY;
    int gameState = STATE_INTRO;
    int diff;
    int best;

    Paint hudPaint = new Paint();
    Paint textPaint = new Paint();
    Paint text2Paint = new Paint();
    Paint text3Paint = new Paint();

    private SoundPool soundPool = new SoundPool(10,AudioManager.STREAM_MUSIC,0);;
    private int id = -1;

    public GamePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        getHolder().addCallback(this);

        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            descriptor = assetManager.openFd("engine_takeoff.wav");
            id = soundPool.load(descriptor, 0);
        } catch(IOException e){
            // Print an error message to the console
            Log.e("error", "failed to load sound files");
        }


        SharedPreferences pref = context.getSharedPreferences("game_prefs", Context.MODE_PRIVATE);
        best = pref.getInt("high_score", 0);

        hudPaint.setColor(Color.WHITE);
        hudPaint.setTextSize(30);
        hudPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        text2Paint.setColor(Color.BLACK);
        text2Paint.setTextSize(20);
        text2Paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        text3Paint.setColor(Color.WHITE);
        text3Paint.setTextSize(20);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        bgFactory = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.sci_fi_bg_low));
        soldier = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.soldier), 26, 40, 2);

        smokeStartTime = System.nanoTime();
        missileStartTime = System.nanoTime();

        // Start game loop
        if (thread == null) {
            thread = new MainThread(getHolder(), this);
            thread.setRunning(true);
            thread.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        scaleFactorX = (float) width / (float) WIDTH;
        scaleFactorY = (float) height / (float) HEIGHT;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (thread == null)
            return;

        thread.setRunning(false);

        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                thread = null;
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (gameState) {
            case STATE_INTRO: {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startGame();
                        return true;
                    default:
                        return super.onTouchEvent(event);
                }
            }
            case STATE_IN_GAME: {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        soldier.setUp(true);
                        soundPool.play(id, 1, 1, 0, 0,1);
                        return true;
                    case MotionEvent.ACTION_UP:
                        soldier.setUp(false);
                        return true;
                    default:
                        return super.onTouchEvent(event);
                }
            }
            case STATE_GAME_OVER: {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (context instanceof MainActivity) {
                            MainActivity activity = (MainActivity) context;
                            Intent intent = new Intent(activity, UIrank.class);
                            intent.putExtra(UIrank.EXTRA_MY_SCORE, soldier.getScore());
                            activity.startActivity(intent);
                            activity.finish();
                        }
                        return true;
                    default:
                        return super.onTouchEvent(event);
                }
            }
            default:
                return super.onTouchEvent(event);
        }
    }

    public void setDiff(int value) {
        diff = value;
    }

    public void update() {
        if (gameState != STATE_IN_GAME)
            return;

        // if player fall out from screen
        if (soldier.y >= HEIGHT) {
            gameOver();
            return;
        }

        bgFactory.update();
        soldier.update();

        // add missiles on timer
        long missileElapsed = (System.nanoTime() - missileStartTime) / 1000000;
        if (missileElapsed > (2000 - soldier.getScore()) / 4) {
            // first missile always goes down the middle
            if (missiles.size() <= MAX_BULLET) {
                if (missiles.size() == 0) {
                    missiles.add(new Bullet(BitmapFactory.decodeResource(getResources(), R.drawable.missile), WIDTH + 10, HEIGHT / 2, 45, 15, soldier.getScore(), 13));
                } else {
                    missiles.add(new Bullet(BitmapFactory.decodeResource(getResources(), R.drawable.missile), WIDTH + 10, (int) (random.nextDouble() * (HEIGHT / 1.3)), 45, 15, soldier.getScore(), 13));
                }
            }
            // reset timer
            missileStartTime = System.nanoTime();
        }

        for (int i = 0; i < missiles.size(); i++) {
            missiles.get(i).setSpeed(6 + diff);
            // update missile
            missiles.get(i).update();
            if (collision(missiles.get(i), soldier)) {
                // remove when hit
                missiles.remove(i);
                // the game end
                gameOver();
                break;
            }
            // remove missile if the is out of the screen
            if (missiles.get(i).getX() < -100) {
                missiles.remove(i);
                break;
            }
        }

        // first missile always goes down the middle
        if (platform.size() <= MAX_BULLET) {
            if (random.nextBoolean())
                platform.add(new Botborder(BitmapFactory.decodeResource(getResources(), R.drawable.floorbox), WIDTH, HEIGHT - 100));
        }
        for (int i = 0; i < platform.size(); i++) {
            platform.get(i).update();
            if (collision(platform.get(i), soldier)) {
                soldier.y = HEIGHT - platform.get(i).getHeight() - 40;
            }
            if (platform.get(i).getX() < -100) {
                platform.remove(i);
                break;
            }
        }

        // add smoke puffs on timer
        long elapsed = (System.nanoTime() - smokeStartTime) / 1000000;
        if (elapsed > 120) {
            puff.add(new Smoke(soldier.getX(), soldier.getY() + 30));
            smokeStartTime = System.nanoTime();
        }

        for (int i = 0; i < puff.size(); i++) {
            puff.get(i).update();
            if (puff.get(i).getX() < -10) {
                puff.remove(i);
            }
        }
    }

    public boolean collision(GameObject a, GameObject b) {
        return Rect.intersects(a.getRect(), b.getRect());
    }

    public void drawObjects(Canvas canvas) {
        final int savedState = canvas.save();
        canvas.scale(scaleFactorX, scaleFactorY);

        bgFactory.draw(canvas);

        if (gameState == STATE_IN_GAME) {
            soldier.draw(canvas);
            for (Smoke smokepuff : puff) {
                smokepuff.draw(canvas);
            }

            for (Botborder floor : platform) {
                floor.draw(canvas);
            }

            for (Bullet m : missiles) {
                m.draw(canvas);
            }
        }

        drawText(canvas);
        canvas.restoreToCount(savedState);
    }

    public void startGame() {
        soldier.setPlaying(true);
        soldier.resetMoveY();
        soldier.resetScore();
        soldier.setY(300);

        for (int i = 0; i < WIDTH; i += 100) {
            platform.add(new Botborder(BitmapFactory.decodeResource(getResources(), R.drawable.floorbox), i, HEIGHT - 100));
        }

        gameState = STATE_IN_GAME;
    }

    public void gameOver() {
        soldier.setPlaying(false);

        SharedPreferences pref = context.getSharedPreferences("game_prefs", Context.MODE_PRIVATE);
        if (soldier.getScore() > pref.getInt("high_score", 0)) {
            best = soldier.getScore();
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("high_score", soldier.getScore());
            editor.apply();
        }

        platform.clear();
        missiles.clear();
        puff.clear();

        gameState = STATE_GAME_OVER;
    }

    public void drawText(Canvas canvas) {
        switch (gameState) {
            case STATE_INTRO: {
                canvas.drawText("PRESS TO START", WIDTH / 2 -100, HEIGHT / 2, textPaint);
                canvas.drawText("PRESS AND HOLD TO GO UP", WIDTH / 2 -100, HEIGHT / 2 + 20, text2Paint);
                canvas.drawText("RELEASE TO GO DOWN", WIDTH / 2 - 100, HEIGHT / 2 + 40, text2Paint);
            }
            break;
            case STATE_IN_GAME: {
                canvas.drawText("Score: " + soldier.getScore(), 10, hudPaint.getTextSize(), hudPaint);
                canvas.drawText("Best: " + best, 10, hudPaint.getTextSize() * 2, hudPaint);
                canvas.drawText("You Can Walk On This Platform, Won't fall", WIDTH / 2 - 50, HEIGHT - 110, text3Paint);
            }
            break;
            case STATE_GAME_OVER: {
                canvas.drawText("Your Score", (WIDTH / 2) - textPaint.measureText("Your Score") / 2, HEIGHT / 2, textPaint);
                canvas.drawText(String.valueOf(soldier.getScore()), (WIDTH / 2) - textPaint.measureText(String.valueOf(soldier.getScore())) / 2, (HEIGHT / 2) + textPaint.getTextSize(), textPaint);
            }
            break;
        }
    }
}
