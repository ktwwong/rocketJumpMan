package com.example.kt.rocketJumpMan;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
    private static final int FPS = 30;

    private final SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;

    MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        long targetTime = 1000 / FPS;

        while (running) {
            startTime = System.nanoTime();

            Canvas canvas = surfaceHolder.lockCanvas();
            synchronized (surfaceHolder) {
                gamePanel.update();
                gamePanel.drawObjects(canvas);
            }
            surfaceHolder.unlockCanvasAndPost(canvas);

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;

            try {
                sleep(waitTime);
            } catch (Exception e) {
                // Ignore
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == FPS) {
                float averageFPS = 1000 / (totalTime / frameCount / 1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println(averageFPS);
            }
        }
    }

    public void setRunning(boolean isRunning) {
        running = isRunning;
    }
}
