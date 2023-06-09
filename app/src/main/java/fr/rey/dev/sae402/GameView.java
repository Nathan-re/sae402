package fr.rey.dev.sae402;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{
    private SurfaceHolder monSurfaceHolder;
    private int nbJoueurs;
    private Paint paintBlack;
    private Paint paintRondelle;
    private Paint paintBut;
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
    private int nbRondellesJouees;
    private But but1;
    private But but2;
    private int scoreEquipe1;
    private int scoreEquipe2;

    private PartieClassique partie;


    public GameView(Context context, int nbJoueurs, PartieClassique partie) {
        super(context);

        this.setFocusable(true);
        this.setZOrderOnTop(true);

        SurfaceHolder monSurfaceHolder = getHolder();
        monSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        monSurfaceHolder.addCallback(this);

        this.setMonSurfaceHolder(monSurfaceHolder);

        this.nbJoueurs = nbJoueurs;

        Paint paintRondelle = new Paint();
        paintRondelle.setStyle(Paint.Style.FILL);
        paintRondelle.setColor(Color.WHITE);
        setPaintRondelle(paintRondelle);

        Paint paintBlack = new Paint();
        paintBlack.setStyle(Paint.Style.FILL);
        paintBlack.setColor(Color.BLACK);
        setPaintBlack(paintBlack);

        activePointers = new ArrayList<Integer>();
        nbRondellesJouees = 0;

        Paint paintBut = new Paint();
        paintBut.setStyle(Paint.Style.FILL);
        paintBut.setColor(Color.WHITE);
        setPaintBut(paintBut);

        scoreEquipe1 = 0;
        scoreEquipe2 = 0;

        this.partie = partie;

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

        getPaintBut().setColor(getBut1().getCouleur());
        canvas.drawRect(getBut1().getLeft(),getBut1().getTop(),getBut1().getRight(), getBut1().getBottom(), paintBut);
        getPaintBut().setColor(getBut2().getCouleur());
        canvas.drawRect(getBut2().getLeft(),getBut2().getTop(),getBut2().getRight(), getBut2().getBottom(), paintBut);

        canvas.drawCircle(getMaRondelle().getX(), getMaRondelle().getY(), getMaRondelle().getRadius(), paintRondelle);


        Paint paintText = new Paint();
        paintText.setColor(Color.WHITE);
        paintText.setStyle(Paint.Style.FILL);
        paintText.setTextSize(180);

        canvas.rotate(-90, getWidth() /2, getHeight()/2);
        Log.d("test", getHeight() + "");
        canvas.drawText(scoreEquipe1 + " - " + scoreEquipe2, getWidth()/2 -200, 800, paintText);

        getMonSurfaceHolder().unlockCanvasAndPost(canvas);

    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        switch (nbJoueurs){
            case 1:
                this.poussoir1 = new Poussoir(100,100, Color.RED, getWidth()/30);
                break;

            case 2:
                this.poussoir1 = new Poussoir(100,100, Color.RED, getWidth()/30);
                this.poussoir2 = new Poussoir(200,200, Color.GREEN, getWidth()/30);
                break;

            case 4:
                this.joueur1 = new Joueur("Jules", Color.argb(255,253,225,45));
                this.poussoir1 = new Poussoir(100,100, joueur1.getPlayerColor(), getWidth()/30);

                this.joueur2 = new Joueur("Pierre", Color.argb(255,253,225,45));
                this.poussoir2 = new Poussoir(200,200, joueur2.getPlayerColor(), getWidth()/30);

                this.joueur3 = new Joueur("Antoine", Color.argb(255,127,50,195));
                this.poussoir3 = new Poussoir(300,300, joueur3.getPlayerColor(), getWidth()/30);

                this.joueur4 = new Joueur("Nathan", Color.argb(255,127,50,195));
                this.poussoir4 = new Poussoir(400,400, joueur4.getPlayerColor(), getWidth()/30);

                break;
        }

        Rondelle maRondelle = new Rondelle((float)(getWidth() /2), (float)(getHeight() /2), getWidth()/36);
        maRondelle.setVitesse(0);
        setMaRondelle(maRondelle);

        but1 = new But((getWidth() /4) + (getWidth() /8), 0, (getWidth() /2) + (getWidth() /8), 20,Color.WHITE);
        but2 = new But((getWidth() /4) + (getWidth() /8), (getHeight()-20), (getWidth() /2 + getWidth() /8), getHeight(),Color.WHITE);

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
        }, 0, 10, TimeUnit.MILLISECONDS);

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

    /**
     * Replace les poussoirs et la balle sur le terrain de jeu en modifiant leurs coordonnées
     */
    public void reset(){
        poussoir1.setX(getWidth()/4);
        poussoir1.setY(getHeight()/4);

        poussoir2.setX(getWidth()/4 + getWidth()/2);
        poussoir2.setY(getHeight()/4);

        poussoir3.setX(getWidth()/4);
        poussoir3.setY(getHeight()/4 + getHeight()/2) ;

        poussoir4.setX(getWidth()/4 + getWidth()/2);
        poussoir4.setY(getHeight()/4 + getHeight()/2);

        maRondelle.setX((float)(getWidth() /2));
        maRondelle.setY((float)(getHeight() /2));
        maRondelle.setVitesse(0);
    }

    public void updateRondelle(){
        float vitesse = getMaRondelle().getVitesse();
        switch(getMaRondelle().getDirection()){
            case "N":
                getMaRondelle().setY((float)(getMaRondelle().getY() - (1.25 * vitesse)));
                break;
            case "S":
                getMaRondelle().setY((float)(getMaRondelle().getY() + (1.25 * vitesse)));
                break;
            case "E":
                getMaRondelle().setX((float)(getMaRondelle().getX() + (1.25 * vitesse)));
                break;
            case "O":
                getMaRondelle().setX((float)(getMaRondelle().getX() - (1.25 * vitesse)));
                break;
            case "NE":
                getMaRondelle().setX((float)(getMaRondelle().getX() + (0.75 * vitesse)));
                getMaRondelle().setY((float)(getMaRondelle().getY() - (0.75 * vitesse)));
                break;
            case "NO":
                getMaRondelle().setX((float)(getMaRondelle().getX() - (0.75 * vitesse)));
                getMaRondelle().setY((float)(getMaRondelle().getY() - (0.75 * vitesse)));
                break;
            case "SE":
                getMaRondelle().setX((float)(getMaRondelle().getX() + (0.75 * vitesse)));
                getMaRondelle().setY((float)(getMaRondelle().getY() + (0.75 * vitesse)));
                break;
            case "SO":
                getMaRondelle().setX((float)(getMaRondelle().getX() - (0.75 * vitesse)));
                getMaRondelle().setY((float)(getMaRondelle().getY() + (0.75 * vitesse)));
                break;
        }

        float xRondelle = getMaRondelle().getX();
        float yRondelle = getMaRondelle().getY();
        int radiusRondelle = getMaRondelle().getRadius();
        int hitboxDistance = 20;
        int hitboxDistanceDiag = 40;
        int vitesseBalle = 40;

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

        if(maRondelle.getCompteurAvailable() != 0){
            int currentCompteur = maRondelle.getCompteurAvailable();
            maRondelle.setCompteurAvailable(currentCompteur - 1);
        }else{
            maRondelle.setAvailable(true);
        }

        if(maRondelle.isAvailable() && maRondelle.getCompteurAvailable() == 0){
            int compteurAvailable = 10;
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

                //Touche au Nord de la rondelle
                if ((pointNRondelle[0] > (pointSPoussoir[0] - hitboxDistance)) && (pointNRondelle[0] < (pointSPoussoir[0] + hitboxDistance)) && (pointNRondelle[1] > (pointSPoussoir[1] - radiusPoussoir - hitboxDistance)) && (pointNRondelle[1] < (pointSPoussoir[1] + hitboxDistance))){
                    getMaRondelle().setDirection("S");
                    getMaRondelle().setCompteurAvailable(compteurAvailable);
                    getMaRondelle().setAvailable(false);
                    maRondelle.setVitesse(vitesseBalle);
                    //Log.d("direction", "touche N");
                }
                //Touche au Sud de la rondelle
                else if ((pointSRondelle[0] > (pointNPoussoir[0] - hitboxDistance)) && (pointSRondelle[0] < (pointNPoussoir[0] + hitboxDistance)) && (pointSRondelle[1] > (pointNPoussoir[1] - hitboxDistance)) && (pointSRondelle[1] < (pointNPoussoir[1] + hitboxDistance))) {
                    getMaRondelle().setDirection("N");
                    getMaRondelle().setCompteurAvailable(compteurAvailable);
                    getMaRondelle().setAvailable(false);
                    maRondelle.setVitesse(vitesseBalle);
                    //Log.d("direction", "touche S");
                }
                //Touche à l'Est de la rondelle
                else if ((pointERondelle[0] > (pointOPoussoir[0] - hitboxDistance)) && (pointERondelle[0] < (pointOPoussoir[0] + hitboxDistance)) && (pointERondelle[1] > (pointOPoussoir[1] - hitboxDistance)) && (pointERondelle[1] < (pointOPoussoir[1] + hitboxDistance))) {
                    getMaRondelle().setDirection("O");
                    getMaRondelle().setCompteurAvailable(compteurAvailable);
                    getMaRondelle().setAvailable(false);
                    maRondelle.setVitesse(vitesseBalle);
                    //Log.d("direction", "touche E");
                }
                //Touche à l'ouest de la rondelle
                else if ((pointORondelle[0] > (pointEPoussoir[0] - hitboxDistance)) && (pointORondelle[0] < (pointEPoussoir[0] + hitboxDistance)) && (pointORondelle[1] > (pointEPoussoir[1] - hitboxDistance)) && (pointORondelle[1] < (pointEPoussoir[1] + hitboxDistance))) {
                    getMaRondelle().setDirection("E");
                    getMaRondelle().setCompteurAvailable(compteurAvailable);
                    getMaRondelle().setAvailable(false);
                    maRondelle.setVitesse(vitesseBalle);
                    //Log.d("direction", "touche O");
                }
                //Touche au Sud-ouest de la rondelle
                else if ((pointSORondelle[0] > (pointNEPoussoir[0] - hitboxDistanceDiag)) && (pointSORondelle[0] < (pointNEPoussoir[0] + hitboxDistanceDiag)) && (pointSORondelle[1] > pointNEPoussoir[1] - hitboxDistanceDiag) && (pointSORondelle[1] < (pointNEPoussoir[1] + hitboxDistanceDiag))) {
                    getMaRondelle().setDirection("NE");
                    getMaRondelle().setCompteurAvailable(compteurAvailable);
                    getMaRondelle().setAvailable(false);
                    maRondelle.setVitesse(vitesseBalle);
                    Log.d("direction", "touche SO");
                }
                //Touche au Sud-Est de la rondelle
                else if ((pointSERondelle[0] > (pointNOPoussoir[0] - hitboxDistanceDiag)) && (pointSERondelle[0] < (pointNOPoussoir[0] + hitboxDistanceDiag)) && (pointSERondelle[1] > pointNOPoussoir[1] - hitboxDistanceDiag) && (pointSERondelle[1] < (pointNOPoussoir[1] + hitboxDistanceDiag))) {
                    getMaRondelle().setDirection("NO");
                    getMaRondelle().setCompteurAvailable(compteurAvailable);
                    getMaRondelle().setAvailable(false);
                    maRondelle.setVitesse(vitesseBalle);
                    Log.d("direction", "touche SO");
                }
                //Touche au Nord-Ouest de la rondelle
                else if ((pointNORondelle[0] > (pointSEPoussoir[0] - hitboxDistanceDiag)) && (pointNORondelle[0] < (pointSEPoussoir[0] + hitboxDistanceDiag)) && (pointNORondelle[1] > pointSEPoussoir[1] - hitboxDistanceDiag) && (pointNORondelle[1] < (pointSEPoussoir[1] + hitboxDistanceDiag))) {
                    getMaRondelle().setDirection("SE");
                    getMaRondelle().setCompteurAvailable(compteurAvailable);
                    getMaRondelle().setAvailable(false);
                    maRondelle.setVitesse(vitesseBalle);
                    Log.d("direction", "touche SO");
                }
                //Touche au Nord-Est de la rondelle
                else if ((pointNERondelle[0] > (pointSOPoussoir[0] - hitboxDistanceDiag)) && (pointNERondelle[0] < (pointSOPoussoir[0] + hitboxDistanceDiag)) && (pointNERondelle[1] > pointSOPoussoir[1] - hitboxDistanceDiag) && (pointNERondelle[1] < (pointSOPoussoir[1] + hitboxDistanceDiag))) {
                    getMaRondelle().setDirection("SO");
                    getMaRondelle().setCompteurAvailable(compteurAvailable);
                    getMaRondelle().setAvailable(false);
                    maRondelle.setVitesse(vitesseBalle);
                    Log.d("direction", "touche SO");
                }
            }
        }


        if((pointNRondelle[0] > getBut1().getLeft() && pointNRondelle[0] < getBut1().getRight()) && (pointNRondelle[1] <= getBut1().getBottom())){
            Log.d("résultat" ,"but!");
            this.butMarque(1);
            reset();
        } else if ((pointSRondelle[0] > getBut2().getLeft() && pointSRondelle[0] < getBut2().getRight()) && (pointSRondelle[1] >= getBut2().getTop())) {
            Log.d("résultat" ,"but!");
            this.butMarque(2);
            reset();

        }

        //Gère le rebond de la rondelle sur les bords du terrain
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

    /*
    * Pour un but marqué
    *
    */
    public void butMarque(int idEquipe){
        nbRondellesJouees = nbRondellesJouees + 1;
        if(idEquipe == 1){
            scoreEquipe1 = scoreEquipe1 + 1;
        }else if(idEquipe == 2){
            scoreEquipe2 = scoreEquipe2 + 1;
        }

        if(scoreEquipe1 == 5 && scoreEquipe2 == 5){
            Log.d("résultat" ,"But en or !");
            reset();
        } else if(nbRondellesJouees >= 10 || (scoreEquipe1 >= 7 || scoreEquipe2 >= 7)){
            this.partie.finPartie();
            Log.d("résultat" ,"résultat");
        }else{
            Log.d("résultat" ,"reset");
            reset();
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

    public Paint getPaintBut() {
        return paintBut;
    }

    public void setPaintBut(Paint paintBut) {
        this.paintBut = paintBut;
    }

    public But getBut1() {
        return but1;
    }

    public void setBut1(But but1) {
        this.but1 = but1;
    }

    public But getBut2() {
        return but2;
    }

    public void setBut2(But but2) {
        this.but2 = but2;
    }

    public int getNbRondellesJouees() {
        return nbRondellesJouees;
    }

    public void setNbRondellesJouees(int nbRondellesJouees) {
        this.nbRondellesJouees = nbRondellesJouees;
    }

    public int getScoreEquipe1() {
        return scoreEquipe1;
    }

    public int getScoreEquipe2() {
        return scoreEquipe2;
    }

}