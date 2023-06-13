package fr.rey.dev.sae402;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{
    private SurfaceHolder monSurfaceHolder;
    private int nbJoueurs, nbRondellesJouees, scoreEquipe1, scoreEquipe2;
    private Paint paintBlack, paintPoussoir, paintRondelle, paintBut, paintScore;
    private Poussoir poussoir1, poussoir2, poussoir3, poussoir4;
    private Rondelle maRondelle;
    private Joueur joueur1, joueur2, joueur3, joueur4;
    private ArrayList<Integer> activePointers;
    private But but1, but2;
    private PartieClassique partie;

    private float proportionX, proportionY;
    private ArrayList<Float> positionXPoussoir1;
    private ArrayList<Float> positionYPoussoir1;

    private ArrayList<Float> positionXPoussoir2;
    private ArrayList<Float> positionYPoussoir2;

    private ArrayList<Float> positionXPoussoir3;
    private ArrayList<Float> positionYPoussoir3;

    private ArrayList<Float> positionXPoussoir4;
    private ArrayList<Float> positionYPoussoir4;

    private final static int REFRESH_RATE = 20;
    private final static int NB_ELEMENTS_ARRAYLIST = 15;
    private final static int DIVISEUR_PUISSANCE = 400;
    private final static float MIN_PUISS = (float)(0.2);
    private final static float MAX_PUISS = 6;
    private float coeffPoussoir;

    private boolean dehorsGauche;
    private boolean dehorsDroite;
    private boolean dehorsHaut;
    private boolean dehorsBas;

    private float vitesseTemp;


    public GameView(Context context, int nbJoueurs, PartieClassique partie) {
        super(context);

        this.setFocusable(true);
        this.setZOrderOnTop(true);

        SurfaceHolder monSurfaceHolderCreate = getHolder();
        monSurfaceHolderCreate.setFormat(PixelFormat.TRANSLUCENT);
        monSurfaceHolderCreate.addCallback(this);
        monSurfaceHolder = monSurfaceHolderCreate;

        //Objet Paint pour la rondelle
        Paint paintRondelleCreate = new Paint();
        paintRondelleCreate.setStyle(Paint.Style.FILL);
        paintRondelleCreate.setColor(Color.WHITE);
        paintRondelle = paintRondelleCreate;

        //Objet Paint pour les poussoirs
        Paint paintPoussoirCreate = new Paint();
        paintPoussoirCreate.setStyle(Paint.Style.FILL);
        paintPoussoir = paintPoussoirCreate;

        //Objet Paint pour le fond
        Paint paintBlackCreate = new Paint();
        paintBlackCreate.setStyle(Paint.Style.FILL);
        paintBlackCreate.setColor(Color.BLACK);
        paintBlack = paintBlackCreate;

        //Objet Paint pour les buts
        Paint paintButCreate = new Paint();
        paintButCreate.setStyle(Paint.Style.FILL);
        paintButCreate.setColor(Color.WHITE);
        paintBut = paintButCreate;

        //Objet Paint pour le score
        Paint paintScoreCreate = new Paint();
        paintScoreCreate.setColor(Color.WHITE);
        paintScoreCreate.setStyle(Paint.Style.FILL);
        paintScoreCreate.setTextSize(180);
        paintScore = paintScoreCreate;

        activePointers = new ArrayList<Integer>();
        nbRondellesJouees = 0;
        this.nbJoueurs = nbJoueurs;
        scoreEquipe1 = 0;
        scoreEquipe2 = 0;
        this.partie = partie;
        dehorsGauche = false;
        dehorsDroite = false;
        dehorsHaut = false;
        dehorsBas = false;
        vitesseTemp = 0;


        switch (nbJoueurs){
            case 2:
                positionXPoussoir1 = new ArrayList<Float>();
                positionYPoussoir1 = new ArrayList<Float>();

                positionXPoussoir2 = new ArrayList<Float>();
                positionYPoussoir2 = new ArrayList<Float>();
                break;
            case 4:
                positionXPoussoir1 = new ArrayList<Float>();
                positionYPoussoir1 = new ArrayList<Float>();

                positionXPoussoir2 = new ArrayList<Float>();
                positionYPoussoir2 = new ArrayList<Float>();

                positionXPoussoir3 = new ArrayList<Float>();
                positionYPoussoir3 = new ArrayList<Float>();

                positionXPoussoir4 = new ArrayList<Float>();
                positionYPoussoir4 = new ArrayList<Float>();
                break;
        }

    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Dessine les différents éléments du terrain en fonction de leurs attributs (coordonnées, couleur, radius...)
     */
    public void dessin(){

        Canvas canvas = getHolder().lockCanvas();
        canvas.drawRect(0,0,getWidth(), getHeight(), paintBlack);

        //Dessine les poussoirs
        for (Poussoir poussoir : getPoussoirs()) {
            paintPoussoir.setColor(poussoir.getCouleur());
            canvas.drawCircle(poussoir.getX(), poussoir.getY(), poussoir.getRadius(), paintPoussoir);
        }

        //Dessine les buts
        for(But but : getButs()){
            paintBut.setColor(but.getCouleur());
            canvas.drawRect(but.getLeft(),but.getTop(),but.getRight(), but.getBottom(), paintBut);
        }

        //Dessine la rondelle
        canvas.drawCircle(maRondelle.getX(), maRondelle.getY(), maRondelle.getRadius(), paintRondelle);

        canvas.rotate(-90, getWidth() /2, getHeight()/2);
        canvas.drawText(scoreEquipe1 + " - " + scoreEquipe2, getWidth()/2 - 200, 800, paintScore);

        getMonSurfaceHolder().unlockCanvasAndPost(canvas);

    }

    /**
     * Instancie les poussoirs, la rondelle et les buts.
     * Place les élements sur les terrain via la méthode reset().
     * Met en place la boucle pour appeler les fonctions updateRondelle() et dessin() toutes les 10ms
     */
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        switch (nbJoueurs){
            case 2:
                this.poussoir1 = new Poussoir(100,100, Color.RED, getWidth()/30);
                this.poussoir2 = new Poussoir(200,200, Color.GREEN, getWidth()/30);
                poussoir1.setPuissPoussoir(0);
                poussoir2.setPuissPoussoir(0);

                break;

            case 4:
                this.joueur1 = new Joueur("Jules", Color.argb(255,253,225,45));
                this.poussoir1 = new Poussoir(100,100, joueur1.getPlayerColor(), getWidth()/30);
                poussoir1.setPuissPoussoir(0);

                this.joueur2 = new Joueur("Pierre", Color.argb(255,253,225,45));
                this.poussoir2 = new Poussoir(200,200, joueur2.getPlayerColor(), getWidth()/30);
                poussoir2.setPuissPoussoir(0);


                this.joueur3 = new Joueur("Antoine", Color.argb(255,127,50,195));
                this.poussoir3 = new Poussoir(300,300, joueur3.getPlayerColor(), getWidth()/30);
                poussoir3.setPuissPoussoir(0);


                this.joueur4 = new Joueur("Nathan", Color.argb(255,127,50,195));
                this.poussoir4 = new Poussoir(400,400, joueur4.getPlayerColor(), getWidth()/30);
                poussoir4.setPuissPoussoir(0);

                break;
        }

        Rondelle maRondelleCreate = new Rondelle((float)(getWidth() /2), (float)(getHeight() /2), getWidth()/36);
        maRondelleCreate.setVitesse(0);
        maRondelle = maRondelleCreate;

        but1 = new But((getWidth() /4) + (getWidth() /8), 0, (getWidth() /2) + (getWidth() /8), 20,Color.WHITE);
        but2 = new But((getWidth() /4) + (getWidth() /8), (getHeight()-20), (getWidth() /2 + getWidth() /8), getHeight(),Color.WHITE);

        switch (nbJoueurs){
            case 2:
                for (int i = 0; i < NB_ELEMENTS_ARRAYLIST; i++) {
                    Log.d("ajoute", "ajout arrayList");
                    positionXPoussoir1.add(poussoir1.getX());
                    positionYPoussoir1.add(poussoir1.getY());

                    positionXPoussoir2.add(poussoir2.getX());
                    positionYPoussoir2.add(poussoir2.getY());
                }
                break;
            case 4:
                for (int i = 0; i < NB_ELEMENTS_ARRAYLIST; i++) {
                    Log.d("ajoute", "ajout arrayList");
                    positionXPoussoir1.add(poussoir1.getX());
                    positionYPoussoir1.add(poussoir1.getY());

                    positionXPoussoir2.add(poussoir2.getX());
                    positionYPoussoir2.add(poussoir2.getY());

                    positionXPoussoir3.add(poussoir3.getX());
                    positionYPoussoir3.add(poussoir3.getY());

                    positionXPoussoir4.add(poussoir4.getX());
                    positionYPoussoir4.add(poussoir4.getY());
                }
                break;
        }


        reset();

        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                updateRondelle();
                dessin();
                if(maRondelle.getVitesse() > 0){
                    maRondelle.setVitesse((float) (maRondelle.getVitesse() - 0.1
                    ));
                }
            }
        }, 0, REFRESH_RATE, TimeUnit.MILLISECONDS);

    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

    /**
     * Replace les poussoirs et la balle sur le terrain de jeu en modifiant leurs coordonnées.
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

    /**
     * Déplace la rondelle en fonction de la direction et la vitesse.
     * Effectue les tests de collision entre la rondelle et les poussoirs.
     * Effectue les tests de collision entre les buts et la rondelle -> but marqué ou non.
     * Effectue les tests de collision entre la rondelle et les murs.
     */
    public void updateRondelle(){
        switch (nbJoueurs){
            case 2:
                positionXPoussoir1.remove(0);
                positionYPoussoir1.remove(0);
                positionXPoussoir1.add(poussoir1.getX());
                positionYPoussoir1.add(poussoir1.getY());

                //Calcule la distance parcourue en NB_ELEMENTS_ARRAYLIST itération
                float distancePoussoir1 = (float)(Math.sqrt(Math.pow(positionXPoussoir1.get(positionXPoussoir1.size() - 1) - positionXPoussoir1.get(0), 2) + Math.pow(positionYPoussoir1.get(positionYPoussoir1.size() -1) - positionYPoussoir1.get(0), 2)));
                float puissance1 = distancePoussoir1 / DIVISEUR_PUISSANCE;
                //Log.d("test1", puissance1 + "");

                if(distancePoussoir1 != 0 && puissance1 < MAX_PUISS){
                    poussoir1.setPuissPoussoir(puissance1);
                }else if(puissance1 > MAX_PUISS){
                    poussoir1.setPuissPoussoir(MAX_PUISS);
                } else if (distancePoussoir1 == 0) {
                    poussoir1.setPuissPoussoir(MIN_PUISS);
                }

                positionXPoussoir2.remove(0);
                positionYPoussoir2.remove(0);
                positionXPoussoir2.add(poussoir2.getX());
                positionYPoussoir2.add(poussoir2.getY());

                //Calcule la distance parcourue en NB_ELEMENTS_ARRAYLIST itération
                float distancePoussoir2 = (float)(Math.sqrt(Math.pow(positionXPoussoir2.get(positionXPoussoir2.size() - 1) - positionXPoussoir2.get(0), 2) + Math.pow(positionYPoussoir2.get(positionYPoussoir2.size() -1) - positionYPoussoir2.get(0), 2)));
                float puissance2 = distancePoussoir2 / DIVISEUR_PUISSANCE;
                //Log.d("test2", puissance2 + "");
                if(distancePoussoir2 != 0 && puissance2 < MAX_PUISS){
                    poussoir2.setPuissPoussoir(puissance2);
                }else if(puissance2 > MAX_PUISS){
                    poussoir2.setPuissPoussoir(MAX_PUISS);
                } else if (distancePoussoir2 == 0) {
                    poussoir2.setPuissPoussoir(MIN_PUISS);
                }
                break;
            case 4:
                positionXPoussoir1.remove(0);
                positionYPoussoir1.remove(0);
                positionXPoussoir1.add(poussoir1.getX());
                positionYPoussoir1.add(poussoir1.getY());
                //Calcule la distance parcourue en NB_ELEMENTS_ARRAYLIST itération
                float distancePouss1 = (float)(Math.sqrt(Math.pow(positionXPoussoir1.get(positionXPoussoir1.size() - 1) - positionXPoussoir1.get(0), 2) + Math.pow(positionYPoussoir1.get(positionYPoussoir1.size() -1) - positionYPoussoir1.get(0), 2)));
                float puiss1 = distancePouss1 / DIVISEUR_PUISSANCE;
               // Log.d("test3", puiss1 + "");

                if(distancePouss1 != 0 && puiss1 < MAX_PUISS){
                    poussoir1.setPuissPoussoir(puiss1);
                }else if(puiss1 > MAX_PUISS){
                    poussoir1.setPuissPoussoir(MAX_PUISS);
                } else if (distancePouss1 == 0) {
                    poussoir1.setPuissPoussoir(MIN_PUISS);
                }

                positionXPoussoir2.remove(0);
                positionYPoussoir2.remove(0);
                positionXPoussoir2.add(poussoir2.getX());
                positionYPoussoir2.add(poussoir2.getY());
                //Calcule la distance parcourue en NB_ELEMENTS_ARRAYLIST itération
                float distancePouss2 = (float)(Math.sqrt(Math.pow(positionXPoussoir2.get(positionXPoussoir2.size() - 1) - positionXPoussoir2.get(0), 2) + Math.pow(positionYPoussoir2.get(positionYPoussoir2.size() -1) - positionYPoussoir2.get(0), 2)));
                float puiss2 = distancePouss2 / DIVISEUR_PUISSANCE;
                //Log.d("test4", puiss2 + "");

                if(distancePouss2 != 0 && puiss2 < MAX_PUISS){
                    poussoir2.setPuissPoussoir(puiss2);
                }else if(puiss2 > MAX_PUISS){
                    poussoir2.setPuissPoussoir(MAX_PUISS);
                } else if (distancePouss2 == 0) {
                    poussoir2.setPuissPoussoir(MIN_PUISS);
                }

                positionXPoussoir3.remove(0);
                positionYPoussoir3.remove(0);
                positionXPoussoir3.add(poussoir3.getX());
                positionYPoussoir3.add(poussoir3.getY());
                //Calcule la distance parcourue en NB_ELEMENTS_ARRAYLIST itération
                float distancePouss3 = (float)(Math.sqrt(Math.pow(positionXPoussoir3.get(positionXPoussoir3.size() - 1) - positionXPoussoir3.get(0), 2) + Math.pow(positionYPoussoir3.get(positionYPoussoir3.size() -1) - positionYPoussoir3.get(0), 2)));
                float puiss3 = distancePouss3 / DIVISEUR_PUISSANCE;
                //Log.d("test5", puiss3 + "");

                if(distancePouss3 != 0 && puiss3 < MAX_PUISS){
                    poussoir3.setPuissPoussoir(puiss3);
                }else if(puiss3 > MAX_PUISS){
                    poussoir3.setPuissPoussoir(MAX_PUISS);
                } else if (distancePouss3 == 0) {
                    poussoir3.setPuissPoussoir(MIN_PUISS);
                }

                positionXPoussoir4.remove(0);
                positionYPoussoir4.remove(0);
                positionXPoussoir4.add(poussoir4.getX());
                positionYPoussoir4.add(poussoir4.getY());
                //Calcule la distance parcourue en NB_ELEMENTS_ARRAYLIST itération
                float distancePouss4 = (float)(Math.sqrt(Math.pow(positionXPoussoir4.get(positionXPoussoir4.size() - 1) - positionXPoussoir4.get(0), 2) + Math.pow(positionYPoussoir4.get(positionYPoussoir4.size() -1) - positionYPoussoir4.get(0), 2)));
                float puiss4 = distancePouss4 / DIVISEUR_PUISSANCE;
                //Log.d("test6", puiss4 + "");

                if(distancePouss4 != 0 && puiss4 < MAX_PUISS){
                    poussoir4.setPuissPoussoir(puiss4);
                }else if(puiss4 > MAX_PUISS){
                    poussoir4.setPuissPoussoir(MAX_PUISS);
                } else if (distancePouss4 == 0) {
                    poussoir4.setPuissPoussoir(MIN_PUISS);
                }
                break;
        }

        //Déplace la balle en fonction de la vitesse et de la direction
        float vitesse = getMaRondelle().getVitesse();

        getMaRondelle().setX((float)(getMaRondelle().getX() + (getProportionX() * (coeffPoussoir * vitesse))));
        getMaRondelle().setY((float)(getMaRondelle().getY() + (getProportionY() * (coeffPoussoir * vitesse))));
        /*
        switch(getMaRondelle().getDirection()){

            case "N":
                getMaRondelle().setY((float)(getMaRondelle().getY() - (1.25 * vitesse)));
                break;
            case "S":
                getMaRondelle().setY((float)(geMIN_PUISStMaRondelle().getY() + (1.25 * vitesse)));
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
            case "ELSE":
                getMaRondelle().setX((float)(getMaRondelle().getX() + (getProportionX() * (1 * vitesse))));
                getMaRondelle().setY((float)(getMaRondelle().getY() + (getProportionY() * (1 * vitesse))));
                break;
        }
        */

        float xRondelle = getMaRondelle().getX();
        float yRondelle = getMaRondelle().getY();
        int radiusRondelle = getMaRondelle().getRadius();
        int hitboxDistance = 0;
        int hitboxDistanceDiag = 0;
        int vitesseBalle = 30;

        // Calcule les coordonnées des points de contact de la rondelle (voir schéma)
        float[] pointNRondelle = new float[2];
        pointNRondelle[0] = xRondelle;
        pointNRondelle[1] = yRondelle - radiusRondelle;

        float[] pointSRondelle = new float[2];
        pointSRondelle[0] = xRondelle;
        pointSRondelle[1] = yRondelle + radiusRondelle;

        float[] pointERondelle = new float[2];
        pointERondelle[0] = xRondelle + radiusRondelle;
        pointERondelle[1] = yRondelle;

        float[] pointORondelle = new float[2];
        pointORondelle[0] = xRondelle - radiusRondelle;
        pointORondelle[1] = yRondelle;

        /*

        float[] pointNORondelle = new float[2];
        pointNORondelle[0] = xRondelle - (radiusRondelle / 2);
        pointNORondelle[1] = yRondelle - (radiusRondelle / 2);

        float[] pointNERondelle = new float[2];
        pointNERondelle[0] = xRondelle + (radiusRondelle / 2);
        pointNERondelle[1] = yRondelle - (radiusRondelle / 2);

        float[] pointSORondelle = new float[2];
        pointSORondelle[0] = xRondelle - (radiusRondelle / 2);
        pointSORondelle[1] = yRondelle + (radiusRondelle / 2);

        float[] pointSERondelle = new float[2];
        pointSERondelle[0] = xRondelle + (radiusRondelle / 2);
        pointSERondelle[1] = yRondelle + (radiusRondelle / 2);

        */

        if(maRondelle.getCompteurAvailable() != 0){
            int currentCompteur = maRondelle.getCompteurAvailable();
            maRondelle.setCompteurAvailable(currentCompteur - 1);
        }else{
            maRondelle.setAvailable(true);
        }

        //Si la rondelle est disponible à la colision
        // -> calcul des coordonnées des points de contact pour chaque poussoir + gestion des collisions (voir schéma)
        if(maRondelle.isAvailable() && maRondelle.getCompteurAvailable() == 0) {
            int compteurAvailable = 15;
            for (Poussoir poussoir : getPoussoirs()) {
                float distanceRondelle = (float) (Math.sqrt(Math.pow(maRondelle.getX() - poussoir.getX(), 2) + Math.pow(maRondelle.getY() - poussoir.getY(), 2)));
                if (distanceRondelle <= maRondelle.getRadius() + poussoir.getRadius()) {
                    float deltaX = maRondelle.getX() - poussoir.getX();
                    float deltaY = maRondelle.getY() - poussoir.getY();
                    float proportionX = deltaX / (maRondelle.getRadius() + poussoir.getRadius());
                    float proportionY = deltaY / (maRondelle.getRadius() + poussoir.getRadius());
                    coeffPoussoir = poussoir.getPuissPoussoir();
                    Log.d("test", coeffPoussoir + "");

                    setProportionX(proportionX);
                    setProportionY(proportionY);
                    getMaRondelle().setCompteurAvailable(compteurAvailable);
                    getMaRondelle().setAvailable(false);
                    maRondelle.setVitesse(vitesseBalle);
                    maRondelle.setDirection("ELSE");

                }
            }
        }

        // Gestion des buts avec les points de contact au Nord et au Sud
        if ((pointNRondelle[0] > getBut1().getLeft() && pointNRondelle[0] < getBut1().getRight()) && (pointNRondelle[1] <= getBut1().getBottom())) {
            //Log.d("résultat" ,"but!");
            this.butMarque(1);
            reset();
        } else if ((pointSRondelle[0] > getBut2().getLeft() && pointSRondelle[0] < getBut2().getRight()) && (pointSRondelle[1] >= getBut2().getTop())) {
            //Log.d("résultat" ,"but!");
            this.butMarque(2);
            reset();

        }


        //Touche le mur haut
        if(pointNRondelle[1] > (0 + 10) && dehorsHaut == true){
            dehorsHaut = false;
            maRondelle.setVitesse(vitesseTemp);
            vitesseTemp = 0;
            //Log.d("removeDehors",  "dehorsHaut = false");

        }else if (pointNRondelle[1] < (0 + 10)) {

            if(dehorsHaut != true){
                vitesseTemp = maRondelle.getVitesse();
                //Log.d("Mur haut", "Mur haut");
                getMaRondelle().setDirection("ELSE");
                setProportionX(getProportionX() * 1);
                setProportionY(getProportionY() * -1);
                dehorsHaut = true;
            }
        }else if ((pointSRondelle[1] < (getHeight() - 10)) && dehorsBas == true) {
            dehorsBas = false;
            maRondelle.setVitesse(vitesseTemp);
            vitesseTemp = 0;
            //Log.d("removeDehors",  "dehorsBas = false");
        }else if ((pointSRondelle[1] > (getHeight() -10))) {
            //Touche le mur bas

            if(dehorsBas != true){
                vitesseTemp = maRondelle.getVitesse();
                //Log.d("Mur bas", "Mur bas");
                getMaRondelle().setDirection("ELSE");
                setProportionX(getProportionX() * 1);
                setProportionY(getProportionY() * -1);
                dehorsBas = true;
            }
        }else if (pointERondelle[0] < (getWidth() -10) && dehorsDroite == true) {
            dehorsDroite = false;
            maRondelle.setVitesse(vitesseTemp);
            vitesseTemp = 0;
            //Log.d("removeDehors",  "dehorsDroite = false");

        } else if (pointERondelle[0] > (getWidth() -10)) {
            //Touche le mur droite

            if(dehorsDroite != true){
                vitesseTemp = maRondelle.getVitesse();
                //Log.d("Mur bas", "Mur droite");
                getMaRondelle().setDirection("ELSE");
                setProportionX(getProportionX() * -1);
                setProportionY(getProportionY() * 1);
                dehorsDroite = true;
            }

        }else if (pointORondelle[0] > (0 + 10) && dehorsGauche == true) {
            dehorsGauche = false;
            maRondelle.setVitesse(vitesseTemp);
            vitesseTemp = 0;
            //Log.d("removeDehors",  "dehorsGauche = false");

        } else if (pointORondelle[0] < 0 + 10) {
            //Touche le mur gauche

            if(dehorsGauche != true){
                vitesseTemp = maRondelle.getVitesse();
                //Log.d("Mur gauche", "Mur gauche");
                getMaRondelle().setDirection("ELSE");
                setProportionX(getProportionX() * -1);
                setProportionY(getProportionY() * 1);
                dehorsGauche = true;

            }
        }

            /*
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

                /*
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

          ANCIENNE VERSION
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

         */

    }

    /**
     * Pour un but marqué, modifie le score et le nombre de palets joués.
     * En fonction du score, appelle la méthode reset() pour replacer les objets ou partie.finPartie()
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
            reset();
        } else if(nbRondellesJouees >= 10 || (scoreEquipe1 >= 7 || scoreEquipe2 >= 7)){
            partie.finPartie();
        }else{
            reset();
        }

    }

    /**
     *  Récupère et place dans un tableau les poussoirs
     * @return un tableau des poussoirs
     */
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

    /**
     *  Récupère et place dans un tableau les buts
     * @return un tableau des buts
     */
    public But[] getButs(){
        But[] buts= new But[2];
        buts[0] = getBut1();
        buts[1] = getBut2();
        return buts;
    }


    public SurfaceHolder getMonSurfaceHolder() {
        return monSurfaceHolder;
    }

    public Rondelle getMaRondelle() {
        return maRondelle;
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

    public Poussoir getPoussoir2() {
        return poussoir2;
    }

    public Poussoir getPoussoir3() {
        return poussoir3;
    }


    public Poussoir getPoussoir4() {
        return poussoir4;
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
        //Log.d("gameview", "idToAdd " + idToAdd + "");
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

    public float getProportionX() {
        return proportionX;
    }

    public void setProportionX(float proportionX) {
        this.proportionX = proportionX;
    }

    public float getProportionY() {
        return proportionY;
    }

    public void setProportionY(float proportionY) {
        this.proportionY = proportionY;
    }

}