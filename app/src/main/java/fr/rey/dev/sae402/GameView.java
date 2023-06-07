package fr.rey.dev.sae402;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{
    private SurfaceHolder monSurfaceHolder;
    private Rondelle maRondelle;


    public GameView(Context context, Rondelle maRondelle) {
        super(context);

        this.setFocusable(true);
        this.setZOrderOnTop(true);

        SurfaceHolder monSurfaceHolder = getHolder();
        monSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        monSurfaceHolder.addCallback(this);

        this.setMonSurfaceHolder(monSurfaceHolder);

        this.maRondelle = maRondelle;

    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void dessin(float[][] coords){

        Paint paintBlack = new Paint();
        paintBlack.setStyle(Paint.Style.FILL);
        paintBlack.setColor(Color.WHITE);

        Paint paintBarres = new Paint();
        paintBarres.setStyle(Paint.Style.FILL);
        paintBarres.setColor(Color.RED);

        Paint paintRond = new Paint();
        paintRond.setStyle(Paint.Style.FILL);
        paintRond.setColor(Color.WHITE);

        Canvas canvas = getMonSurfaceHolder().lockCanvas();
        canvas.drawRect(0,0,getWidth(), getHeight(), paintBlack);

        for (float[] pointer: coords) {
            if((pointer[0] != 0) && (pointer[1] != 0)){

                canvas.drawRect((int)(pointer[0] - 150),(int) (pointer[1] -100),(int)(pointer[0] + 150),(int)(pointer[1] + 100), paintBarres);

            }
        }

        getMonSurfaceHolder().unlockCanvasAndPost(canvas);

    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {



    }


    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

    public SurfaceHolder getMonSurfaceHolder() {
        return monSurfaceHolder;
    }

    public void setMonSurfaceHolder(SurfaceHolder monSurfaceHolder) {
        this.monSurfaceHolder = monSurfaceHolder;
    }

    public Rondelle getMaRondelle() {
        return maRondelle;
    }

    public void setMaRondelle(Rondelle maRondelle) {
        this.maRondelle = maRondelle;
    }
}