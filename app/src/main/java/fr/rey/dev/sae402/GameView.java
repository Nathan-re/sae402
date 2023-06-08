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
    private int nbJoueurs;

    private Paint paintBlack;
    private Paint paintRondelle;
    private Rondelle maRondelle;
    private Poussoir poussoir1;
    private Poussoir poussoir2;
    private Poussoir poussoir3;
    private Poussoir poussoir4;

    private Joueur joueur1;
    private Joueur joueur2;
    private Joueur joueur3;
    private Joueur joueur4;


    public GameView(Context context, int nbJoueurs) {
        super(context);

        this.setFocusable(true);
        this.setZOrderOnTop(true);

        SurfaceHolder monSurfaceHolder = getHolder();
        monSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        monSurfaceHolder.addCallback(this);

        this.setMonSurfaceHolder(monSurfaceHolder);

        this.nbJoueurs = nbJoueurs;

        switch (nbJoueurs){
            case 1:
                this.poussoir1 = new Poussoir(100,100, Color.RED);
                break;

            case 2:
                this.poussoir1 = new Poussoir(100,100, Color.RED);
                this.poussoir2 = new Poussoir(200,200, Color.GREEN);
                break;

            case 4:
                this.joueur1 = new Joueur("Jules", Color.YELLOW);
                this.poussoir1 = new Poussoir(100,100, joueur1.getPlayerColor());

                this.joueur2 = new Joueur("Pierre", Color.BLUE);
                this.poussoir2 = new Poussoir(200,200, joueur2.getPlayerColor());

                this.joueur3 = new Joueur("Antoine", Color.GREEN);
                this.poussoir3 = new Poussoir(300,300, joueur3.getPlayerColor());

                this.joueur4 = new Joueur("Nathan", Color.RED);
                this.poussoir4 = new Poussoir(400,400, joueur4.getPlayerColor());

                break;
        }

        Paint paintRondelle = new Paint();
        paintRondelle.setStyle(Paint.Style.FILL);
        paintRondelle.setColor(Color.CYAN);
        setPaintRondelle(paintRondelle);

        Paint paintBlack = new Paint();
        paintBlack.setStyle(Paint.Style.FILL);
        paintBlack.setColor(Color.BLACK);
        setPaintBlack(paintBlack);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void dessin(){

        Paint paintPoussoir = new Paint();
        paintPoussoir.setStyle(Paint.Style.FILL);

        Poussoir[] poussoirs = getPoussoirs();

        Canvas canvas = getHolder().lockCanvas();
        canvas.drawRect(0,0,getWidth(), getHeight(), paintBlack);

        for (Poussoir poussoir:poussoirs) {

            paintPoussoir.setColor(poussoir.getCouleur());
            canvas.drawCircle(poussoir.getX(), poussoir.getY(), 40, paintPoussoir);
        }

        canvas.drawCircle(getMaRondelle().getX(), getMaRondelle().getY(), getMaRondelle().getRadius(), paintRondelle);
        getMonSurfaceHolder().unlockCanvasAndPost(canvas);

    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Rondelle maRondelle = new Rondelle((float)(getWidth() /2), (float)(getHeight() /2), 40);
        setMaRondelle(maRondelle);

        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                updateRondelle();
                dessin();
            }
        }, 0, 20, TimeUnit.MILLISECONDS);

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

    public void updateRondelle(){
        getMaRondelle().setX(getMaRondelle().getX() + 1);
        getMaRondelle().setY(getMaRondelle().getY() + 1);

    }

    public Poussoir[] getPoussoirs() {
        Poussoir[] poussoirs = new Poussoir[this.nbJoueurs];
        if(getPoussoir1().getCouleur() != 0){
            poussoirs[0] = poussoir1;
        }

        if(getPoussoir2().getCouleur() != 0){
            poussoirs[1] = poussoir2;
        }

        if(getPoussoir3().getCouleur() != 0){
            poussoirs[2] = poussoir3;
        }

        if(getPoussoir4().getCouleur() != 0){
            poussoirs[3] = poussoir4;
        }

        return poussoirs;
    }

    public Rondelle getMaRondelle() {
        return maRondelle;
    }

    public void setMaRondelle(Rondelle maRondelle) {
        this.maRondelle = maRondelle;
    }

    public int getNbJoueurs() {
        return nbJoueurs;
    }

    public void setNbJoueurs(int nbJoueurs) {
        this.nbJoueurs = nbJoueurs;
    }

    public Poussoir getPoussoir1() {
        return poussoir1;
    }

    public void setPoussoir1(Poussoir poussoir1) {
        this.poussoir1 = poussoir1;
    }

    public Poussoir getPoussoir2() {
        return poussoir2;
    }

    public void setPoussoir2(Poussoir poussoir2) {
        this.poussoir2 = poussoir2;
    }

    public Poussoir getPoussoir3() {
        return poussoir3;
    }

    public void setPoussoir3(Poussoir poussoir3) {
        this.poussoir3 = poussoir3;
    }

    public Poussoir getPoussoir4() {
        return poussoir4;
    }

    public void setPoussoir4(Poussoir poussoir4) {
        this.poussoir4 = poussoir4;
    }

    public Paint getPaintBlack() {
        return paintBlack;
    }

    public void setPaintBlack(Paint paintBlack) {
        this.paintBlack = paintBlack;
    }

    public Paint getPaintRondelle() {
        return paintRondelle;
    }

    public void setPaintRondelle(Paint paintRondelle) {
        this.paintRondelle = paintRondelle;
    }
}