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

    private ArrayList<Integer> activePointers;


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
                this.poussoir1 = new Poussoir(100,100, Color.RED, 40);
                break;

            case 2:
                this.poussoir1 = new Poussoir(100,100, Color.RED, 40);
                this.poussoir2 = new Poussoir(200,200, Color.GREEN, 40);
                break;

            case 4:
                this.joueur1 = new Joueur("Jules", Color.argb(255,253,225,45));
                this.poussoir1 = new Poussoir(100,100, joueur1.getPlayerColor(), 40);

                this.joueur2 = new Joueur("Pierre", Color.argb(255,253,225,45));
                this.poussoir2 = new Poussoir(200,200, joueur2.getPlayerColor(), 40);

                this.joueur3 = new Joueur("Antoine", Color.argb(255,127,50,195));
                this.poussoir3 = new Poussoir(300,300, joueur3.getPlayerColor(), 40);

                this.joueur4 = new Joueur("Nathan", Color.argb(255,127,50,195));
                this.poussoir4 = new Poussoir(400,400, joueur4.getPlayerColor(), 40);

                break;
        }

        Paint paintRondelle = new Paint();
        paintRondelle.setStyle(Paint.Style.FILL);
        paintRondelle.setColor(Color.WHITE);
        setPaintRondelle(paintRondelle);

        Paint paintBlack = new Paint();
        paintBlack.setStyle(Paint.Style.FILL);
        paintBlack.setColor(Color.BLACK);
        setPaintBlack(paintBlack);

        activePointers = new ArrayList<Integer>();

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
            canvas.drawCircle(poussoir.getX(), poussoir.getY(), poussoir.getRadius(), paintPoussoir);
        }

        canvas.drawCircle(getMaRondelle().getX(), getMaRondelle().getY(), getMaRondelle().getRadius(), paintRondelle);
        getMonSurfaceHolder().unlockCanvasAndPost(canvas);

    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Rondelle maRondelle = new Rondelle((float)(getWidth() /2), (float)(getHeight() /2), 30);
        maRondelle.setVitesse(0);
        setMaRondelle(maRondelle);
        reset();

        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                updateRondelle();
                dessin();
                if(maRondelle.getVitesse() > 0){
                    maRondelle.setVitesse((float) (maRondelle.getVitesse() - 0.1));
                }
            }
        }, 0, 2, TimeUnit.MILLISECONDS);

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

    public void reset(){
        poussoir1.setX(getWidth()/4);
        poussoir1.setY(getHeight()/4);

        poussoir2.setX(getWidth()/4 + getWidth()/2);
        poussoir2.setY(getHeight()/4);

        poussoir3.setX(getWidth()/4);
        poussoir3.setY(getHeight()/4 + getHeight()/2) ;

        poussoir4.setX(getWidth()/4 + getWidth()/2);
        poussoir4.setY(getHeight()/4 + getHeight()/2);
    }

    public void updateRondelle(){
        float vitesse = getMaRondelle().getVitesse();
        switch(getMaRondelle().getDirection()){
            case "N":
                getMaRondelle().setY(getMaRondelle().getY() - (1 * vitesse));
                break;
            case "S":
                getMaRondelle().setY(getMaRondelle().getY() + (1 * vitesse));
                break;
            case "E":
                getMaRondelle().setX(getMaRondelle().getX() + (1 * vitesse));
                break;
            case "O":
                getMaRondelle().setX(getMaRondelle().getX() - (1 * vitesse));
                break;
            case "NE":
                getMaRondelle().setX((float)(getMaRondelle().getX() + (1 * vitesse)));
                getMaRondelle().setY((float)(getMaRondelle().getY() - (1 * vitesse)));
                break;
            case "NO":
                getMaRondelle().setX((float)(getMaRondelle().getX() - (1 * vitesse)));
                getMaRondelle().setY((float)(getMaRondelle().getY() - (1 * vitesse)));
                break;
            case "SE":
                getMaRondelle().setX((float)(getMaRondelle().getX() + (1 * vitesse)));
                getMaRondelle().setY((float)(getMaRondelle().getY() + (1 * vitesse)));
                break;
            case "SO":
                getMaRondelle().setX((float)(getMaRondelle().getX() - (1 * vitesse)));
                getMaRondelle().setY((float)(getMaRondelle().getY() + (1 * vitesse)));
                break;
        }

        float xRondelle = getMaRondelle().getX();
        float yRondelle = getMaRondelle().getY();
        int radiusRondelle = getMaRondelle().getRadius();
        int hitboxDistance = 40;

        float[] pointNRondelle = new float[2];
        pointNRondelle[0] = xRondelle;
        pointNRondelle[1] = yRondelle - radiusRondelle;

        float[] pointNORondelle = new float[2];
        pointNORondelle[0] = xRondelle - (radiusRondelle / 2);
        pointNORondelle[1] = yRondelle - (radiusRondelle / 2);

        float[] pointNERondelle = new float[2];
        pointNERondelle[0] = xRondelle + (radiusRondelle / 2);
        pointNERondelle[1] = yRondelle - (radiusRondelle / 2);

        float[] pointSRondelle = new float[2];
        pointSRondelle[0] = xRondelle;
        pointSRondelle[1] = yRondelle + radiusRondelle;

        float[] pointSORondelle = new float[2];
        pointSORondelle[0] = xRondelle - (radiusRondelle / 2);
        pointSORondelle[1] = yRondelle + (radiusRondelle / 2);

        float[] pointSERondelle = new float[2];
        pointSERondelle[0] = xRondelle + (radiusRondelle / 2);
        pointSERondelle[1] = yRondelle + (radiusRondelle / 2);

        float[] pointERondelle = new float[2];
        pointERondelle[0] = xRondelle + radiusRondelle;
        pointERondelle[1] = yRondelle;

        float[] pointORondelle = new float[2];
        pointORondelle[0] = xRondelle - radiusRondelle;
        pointORondelle[1] = yRondelle;

        if(getMaRondelle().getCompteurAvailable() != 0){
            int currentCompteur = getMaRondelle().getCompteurAvailable();
            getMaRondelle().setCompteurAvailable(currentCompteur - 1);
        }else{
            getMaRondelle().setAvailable(true);
        }

        if(getMaRondelle().isAvailable() && getMaRondelle().getCompteurAvailable() == 0){
            int compteurAvailable = 20;
            for (Poussoir poussoir : getPoussoirs()){
                int radiusPoussoir = poussoir.getRadius();

                float[] pointNPoussoir = new float[2];
                pointNPoussoir[0] = poussoir.getX();
                pointNPoussoir[1] = poussoir.getY() - radiusPoussoir;

                float[] pointNEPoussoir = new float[2];
                pointNEPoussoir[0] = poussoir.getX() + (radiusPoussoir / 2);
                pointNEPoussoir[1] = poussoir.getY() - (radiusPoussoir);

                float[] pointNOPoussoir = new float[2];
                pointNOPoussoir[0] = poussoir.getX() - (radiusPoussoir / 2);
                pointNOPoussoir[1] = poussoir.getY() - (radiusPoussoir);

                float[] pointSPoussoir = new float[2];
                pointSPoussoir[0] = poussoir.getX();
                pointSPoussoir[1] = poussoir.getY() + radiusPoussoir;

                float[] pointSEPoussoir = new float[2];
                pointSEPoussoir[0] = poussoir.getX() + (radiusPoussoir / 2);
                pointSEPoussoir[1] = poussoir.getY() + radiusPoussoir;

                float[] pointSOPoussoir = new float[2];
                pointSOPoussoir[0] = poussoir.getX() - (radiusPoussoir / 2);
                pointSOPoussoir[1] = poussoir.getY() + radiusPoussoir;

                float[] pointEPoussoir = new float[2];
                pointEPoussoir[0] = poussoir.getX() + radiusPoussoir;
                pointEPoussoir[1] = poussoir.getY();

                float[] pointOPoussoir = new float[2];
                pointOPoussoir[0] = poussoir.getX() - radiusPoussoir;
                pointOPoussoir[1] = poussoir.getY();

                //Touche N
                if ((pointNRondelle[0] > (pointSPoussoir[0] - hitboxDistance)) && (pointNRondelle[0] < (pointSPoussoir[0] + hitboxDistance)) && (pointNRondelle[1] > (pointSPoussoir[1] - radiusPoussoir - hitboxDistance)) && (pointNRondelle[1] < (pointSPoussoir[1] + hitboxDistance))){
                    getMaRondelle().setDirection("S");
                    getMaRondelle().setCompteurAvailable(compteurAvailable);
                    getMaRondelle().setAvailable(false);
                    maRondelle.setVitesse(20);
                    //Log.d("direction", "touche N");
                } //Touche S
                else if ((pointSRondelle[0] > (pointNPoussoir[0] - hitboxDistance)) && (pointSRondelle[0] < (pointNPoussoir[0] + hitboxDistance)) && (pointSRondelle[1] > (pointNPoussoir[1] - hitboxDistance)) && (pointSRondelle[1] < (pointNPoussoir[1] + hitboxDistance))) {
                    getMaRondelle().setDirection("N");
                    getMaRondelle().setCompteurAvailable(compteurAvailable);
                    getMaRondelle().setAvailable(false);
                    maRondelle.setVitesse(20);
                    //Log.d("direction", "touche S");
                }//Touche E
                else if ((pointERondelle[0] > (pointOPoussoir[0] - hitboxDistance)) && (pointERondelle[0] < (pointOPoussoir[0] + hitboxDistance)) && (pointERondelle[1] > (pointOPoussoir[1] - hitboxDistance)) && (pointERondelle[1] < (pointOPoussoir[1] + hitboxDistance))) {
                    getMaRondelle().setDirection("O");
                    getMaRondelle().setCompteurAvailable(compteurAvailable);
                    getMaRondelle().setAvailable(false);
                    maRondelle.setVitesse(20);
                    //Log.d("direction", "touche E");
                }//Touche O
                else if ((pointORondelle[0] > (pointEPoussoir[0] - hitboxDistance)) && (pointORondelle[0] < (pointEPoussoir[0] + hitboxDistance)) && (pointORondelle[1] > (pointEPoussoir[1] - hitboxDistance)) && (pointORondelle[1] < (pointEPoussoir[1] + hitboxDistance))) {
                    getMaRondelle().setDirection("E");
                    getMaRondelle().setCompteurAvailable(compteurAvailable);
                    getMaRondelle().setAvailable(false);
                    maRondelle.setVitesse(20);
                    //Log.d("direction", "touche O");
                }//Touche NE
                else if ((pointSORondelle[0] > (pointNEPoussoir[0] - hitboxDistance)) && (pointSORondelle[0] < (pointNEPoussoir[0] + hitboxDistance)) && (pointSORondelle[1] > pointNEPoussoir[1] - hitboxDistance) && (pointSORondelle[1] < (pointNEPoussoir[1] + hitboxDistance))) {
                    getMaRondelle().setDirection("NE");
                    getMaRondelle().setCompteurAvailable(compteurAvailable);
                    getMaRondelle().setAvailable(false);
                    maRondelle.setVitesse(20);
                    Log.d("direction", "touche SO");
                }//Touche NO
                else if ((pointSERondelle[0] > (pointNOPoussoir[0] - hitboxDistance)) && (pointSERondelle[0] < (pointNOPoussoir[0] + hitboxDistance)) && (pointSERondelle[1] > pointNOPoussoir[1] - hitboxDistance) && (pointSERondelle[1] < (pointNOPoussoir[1] + hitboxDistance))) {
                    getMaRondelle().setDirection("NO");
                    getMaRondelle().setCompteurAvailable(compteurAvailable);
                    getMaRondelle().setAvailable(false);
                    maRondelle.setVitesse(20);
                    Log.d("direction", "touche SO");
                }//Touche SE
                else if ((pointNORondelle[0] > (pointSEPoussoir[0] - hitboxDistance)) && (pointNORondelle[0] < (pointSEPoussoir[0] + hitboxDistance)) && (pointNORondelle[1] > pointSEPoussoir[1] - hitboxDistance) && (pointNORondelle[1] < (pointSEPoussoir[1] + hitboxDistance))) {
                    getMaRondelle().setDirection("SE");
                    getMaRondelle().setCompteurAvailable(compteurAvailable);
                    getMaRondelle().setAvailable(false);
                    maRondelle.setVitesse(20);
                    Log.d("direction", "touche SO");
                }//Touche SO
                else if ((pointNERondelle[0] > (pointSOPoussoir[0] - hitboxDistance)) && (pointNERondelle[0] < (pointSOPoussoir[0] + hitboxDistance)) && (pointNERondelle[1] > pointSOPoussoir[1] - hitboxDistance) && (pointNERondelle[1] < (pointSOPoussoir[1] + hitboxDistance))) {
                    getMaRondelle().setDirection("SO");
                    getMaRondelle().setCompteurAvailable(compteurAvailable);
                    getMaRondelle().setAvailable(false);
                    maRondelle.setVitesse(20);
                    Log.d("direction", "touche SO");
                }
            }
        }

        //Rebondir sur les bords
        if(getMaRondelle().getDirection() == "NE" && pointNRondelle[1] < 0){
            getMaRondelle().setDirection("SE");
        }else if(getMaRondelle().getDirection() == "NO" && pointNRondelle[1] < 0){
            getMaRondelle().setDirection("SO");
        } else if(pointNRondelle[1] < 0){
            getMaRondelle().setDirection("S");
        }else if(getMaRondelle().getDirection() == "SE" && pointSRondelle[1] > getHeight()){
            getMaRondelle().setDirection("NE");
        }else if(getMaRondelle().getDirection() == "SO" && pointSRondelle[1] > getHeight()){
            getMaRondelle().setDirection("NO");
        } else if (pointSRondelle[1] > getHeight()) {
            getMaRondelle().setDirection("N");
        }else if(getMaRondelle().getDirection() == "NE" && pointERondelle[0] > getWidth()){
            getMaRondelle().setDirection("NO");
        }else if(getMaRondelle().getDirection() == "SE" && pointERondelle[0] > getWidth()){
            getMaRondelle().setDirection("SO");
        } else if (pointERondelle[0] > getWidth()) {
            getMaRondelle().setDirection("O");
        }else if(getMaRondelle().getDirection() == "NO" && pointORondelle[0] < 0){
            getMaRondelle().setDirection("NE");
        }else if(getMaRondelle().getDirection() == "SO" && pointORondelle[0] < 0){
            getMaRondelle().setDirection("SE");
        } else if (pointORondelle[0] < 0) {
            getMaRondelle().setDirection("E");
        }


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

    public ArrayList<Integer> getActivePointers() {
        return activePointers;
    }

    public void setActivePointers(ArrayList<Integer> activePointers) {
        this.activePointers = activePointers;
    }

    public boolean addActivePointer(int idToAdd) {
        Log.d("gameview", "idToAdd " + idToAdd + "");
        boolean result = this.activePointers.add(idToAdd);
        return result;
    }

    public void removeActivePointer(int toRemove) {
        int indexToRemove = this.activePointers.indexOf(toRemove);
        if (indexToRemove != -1) {
            this.activePointers.remove(indexToRemove);
        }
    }
}