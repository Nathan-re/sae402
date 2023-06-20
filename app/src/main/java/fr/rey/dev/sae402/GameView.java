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
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder monSurfaceHolder;
    private int nbJoueurs, nbRondellesJouees, scoreEquipe1, scoreEquipe2;
    private float proportionX, proportionY, coeffPoussoir, hitboxExt, hitboxInt;
    private boolean dehorsGauche, dehorsDroite, dehorsHaut, dehorsBas;
    private Paint paintBlack, paintPoussoir, paintRondelle, paintBut, paintScore;
    private Poussoir poussoir1, poussoir2, poussoir3, poussoir4;
    private Rondelle maRondelle;
    private Joueur joueur1, joueur2, joueur3, joueur4;
    private ArrayList<Integer> activePointers;
    private But but1, but2;
    private PartieClassique partie;
    private ArrayList<Float> positionXPoussoir1, positionYPoussoir1, positionXPoussoir2, positionYPoussoir2, positionXPoussoir3, positionYPoussoir3, positionXPoussoir4, positionYPoussoir4;
    private float[] tableauPuiss;
    private final static int REFRESH_RATE = 1;
    private final static int NB_ELEMENTS_ARRAYLIST = 4;
    private final static int DIVISEUR_PUISSANCE = 30;
    private final static float MIN_PUISS = (float) (0.3);
    private final static float MAX_PUISS = (float) (5);
    private final static float VITESSE_REMOVE = (float) (0.2);

    private AppDataBase dbAccess;
    private PartieDAO partieDao;
    private JoueurDAO joueurDao;


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

        switch (nbJoueurs) {
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
    public void accessDataBase() {
        dbAccess = AppDataBase.getAppDataBase(this.getContext());
        joueurDao = dbAccess.getJoueurDao();
        partieDao = dbAccess.getPartieDao();
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
    public void dessin() {

        Canvas canvas = getHolder().lockCanvas();
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintBlack);

        //Dessine les poussoirs
        for (Poussoir poussoir : getPoussoirs()) {
            paintPoussoir.setColor(poussoir.getCouleur());
            canvas.drawCircle(poussoir.getX(), poussoir.getY(), poussoir.getRadius(), paintPoussoir);
        }

        //Dessine les buts
        for (But but : getButs()) {
            paintBut.setColor(but.getCouleur());
            canvas.drawRect(but.getLeft(), but.getTop(), but.getRight(), but.getBottom(), paintBut);
        }

        //Dessine la rondelle
        //Log.d("avant rotate", getWidth() + " & " + getHeight());

        canvas.drawCircle(maRondelle.getX(), maRondelle.getY(), maRondelle.getRadius(), paintRondelle);

        canvas.rotate(-90, getWidth() / 2, getHeight() / 2);

        //Log.d("apres rotate", getWidth() + " & " + getHeight());
        canvas.drawText(scoreEquipe1 + " - " + scoreEquipe2, getWidth() / 2 -200, 1100, paintScore);

        /*
        canvas.drawText(((int)(poussoir1.getPuissPoussoir()) + ""), getWidth()/2 + 400, getHeight()/2, paintScore);
        canvas.drawText(((int)(poussoir2.getPuissPoussoir()) + ""), getWidth()/2 + 200, getHeight()/2, paintScore);
        canvas.drawText(((int)(poussoir3.getPuissPoussoir()) + ""), getWidth()/2 - 200, getHeight()/2, paintScore);
        canvas.drawText(((int)(poussoir4.getPuissPoussoir()) + ""), getWidth()/2 - 400, getHeight()/2, paintScore);
        */

        getMonSurfaceHolder().unlockCanvasAndPost(canvas);


    }

    /**
     * Instancie les poussoirs, la rondelle et les buts.
     * Place les élements sur les terrain via la méthode reset().
     * Met en place la boucle pour appeler les fonctions updateRondelle() et dessin() toutes les 10ms
     */
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        switch (nbJoueurs) {
            case 2:
                this.poussoir1 = new Poussoir(100, 100, Color.argb(255, 253, 225, 45), getWidth() / 24, 0);
                this.poussoir2 = new Poussoir(200, 200, Color.argb(255, 127, 50, 195), getWidth() / 24, 0);
                break;

            case 4:
                this.joueur1 = new Joueur("Jules", Color.argb(255, 253, 225, 45), 0, 0, 0);
                this.poussoir1 = new Poussoir(100, 100, joueur1.getPlayerColor(), getWidth() / 24, 0);

                this.joueur2 = new Joueur("Pierre", Color.argb(255, 253, 225, 45), 0, 0, 0);
                this.poussoir2 = new Poussoir(200, 200, joueur2.getPlayerColor(), getWidth() / 24, 0);


                this.joueur3 = new Joueur("Antoine", Color.argb(255, 127, 50, 195), 0, 0, 0);
                this.poussoir3 = new Poussoir(300, 300, joueur3.getPlayerColor(), getWidth() / 24, 0);


                this.joueur4 = new Joueur("Nathan", Color.argb(255, 127, 50, 195), 0, 0, 0);
                this.poussoir4 = new Poussoir(400, 400, joueur4.getPlayerColor(), getWidth() / 24, 0);

                break;
        }

        Rondelle maRondelleCreate = new Rondelle((float) (getWidth() / 2), (float) (getHeight() / 2), getWidth() / 28);
        maRondelleCreate.setVitesse(0);
        maRondelle = maRondelleCreate;

        but1 = new But((getWidth() / 4) + (getWidth() / 8), 0, (getWidth() / 2) + (getWidth() / 8), 20, Color.WHITE);
        but2 = new But((getWidth() / 4) + (getWidth() / 8), (getHeight() - 20), (getWidth() / 2 + getWidth() / 8), getHeight(), Color.WHITE);

        switch (nbJoueurs) {
            case 2:
                for (int i = 0; i < NB_ELEMENTS_ARRAYLIST; i++) {
                    //Log.d("ajoute", "ajout arrayList");
                    positionXPoussoir1.add(poussoir1.getX());
                    positionYPoussoir1.add(poussoir1.getY());

                    positionXPoussoir2.add(poussoir2.getX());
                    positionYPoussoir2.add(poussoir2.getY());
                }
                break;
            case 4:
                for (int i = 0; i < NB_ELEMENTS_ARRAYLIST; i++) {
                    //Log.d("ajoute", "ajout arrayList");
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
                if (maRondelle.getVitesse() >= VITESSE_REMOVE) {
                    maRondelle.setVitesse((float) (maRondelle.getVitesse() - VITESSE_REMOVE));
                } else {
                    maRondelle.setVitesse(0);

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
    public void reset() {

        switch (nbJoueurs) {
            case 2:
                poussoir1.setX(getWidth() / 2);
                poussoir1.setY(getHeight() / 4);

                poussoir2.setX(getWidth() / 2);
                poussoir2.setY(getHeight() / 4 + getHeight() / 2);
                break;
            case 4:
                poussoir1.setX(getWidth() / 4);
                poussoir1.setY(getHeight() / 4);

                poussoir2.setX(getWidth() / 4 + getWidth() / 2);
                poussoir2.setY(getHeight() / 4);

                poussoir3.setX(getWidth() / 4);
                poussoir3.setY(getHeight() / 4 + getHeight() / 2);

                poussoir4.setX(getWidth() / 4 + getWidth() / 2);
                poussoir4.setY(getHeight() / 4 + getHeight() / 2);
                break;
        }


        maRondelle.setX((float) (getWidth() / 2));
        maRondelle.setY((float) (getHeight() / 2));
        maRondelle.setVitesse(0);
    }

    /**
     * Déplace la rondelle en fonction de la direction et la vitesse.
     * Effectue les tests de collision entre la rondelle et les poussoirs.
     * Effectue les tests de collision entre les buts et la rondelle -> but marqué ou non.
     * Effectue les tests de collision entre la rondelle et les murs.
     */
    public void updateRondelle() {
        //Pour chaque arraylist, à chaque refresh de la page ->
        // on supprime la plus ancienne position et on rajoute une nouvelle position correspondant à la plus récente
        switch (nbJoueurs) {
            case 2:

                positionXPoussoir1.remove(0);
                positionYPoussoir1.remove(0);
                positionXPoussoir1.add(poussoir1.getX());
                positionYPoussoir1.add(poussoir1.getY());
                //Calcule la distance parcourue en NB_ELEMENTS_ARRAYLIST itération

                float distFromLastPos = (float) (Math.sqrt(Math.pow(positionXPoussoir1.get(positionXPoussoir1.size() - 1) - positionXPoussoir1.get(positionXPoussoir1.size() - 2), 2) + Math.pow(positionYPoussoir1.get(positionYPoussoir1.size() - 1) - positionYPoussoir1.get(positionYPoussoir1.size() - 2), 2)));
                //float distFromLastPos2 = (float)(Math.sqrt(Math.pow(positionXPoussoir1.get(positionXPoussoir1.size() - 2) - positionXPoussoir1.get(positionXPoussoir1.size() - 3), 2) + Math.pow(positionYPoussoir1.get(positionYPoussoir1.size() -2) - positionYPoussoir1.get(positionYPoussoir1.size() -3), 2)));

                float coeff = distFromLastPos / DIVISEUR_PUISSANCE;
                if (coeff > MIN_PUISS && coeff < MAX_PUISS) {
                    poussoir1.setPuissPoussoir(coeff);
                } else if (coeff > MAX_PUISS) {
                    poussoir1.setPuissPoussoir(MAX_PUISS);
                } else {
                    poussoir1.setPuissPoussoir(MIN_PUISS);
                }


                positionXPoussoir2.remove(0);
                positionYPoussoir2.remove(0);
                positionXPoussoir2.add(poussoir2.getX());
                positionYPoussoir2.add(poussoir2.getY());
                //Calcule la distance parcourue en NB_ELEMENTS_ARRAYLIST itération

                float distFromLastPos3 = (float) (Math.sqrt(Math.pow(positionXPoussoir2.get(positionXPoussoir2.size() - 1) - positionXPoussoir2.get(positionXPoussoir2.size() - 2), 2) + Math.pow(positionYPoussoir2.get(positionYPoussoir2.size() - 1) - positionYPoussoir2.get(positionYPoussoir2.size() - 2), 2)));
                float distFromLastPos4 = (float) (Math.sqrt(Math.pow(positionXPoussoir2.get(positionXPoussoir2.size() - 2) - positionXPoussoir2.get(positionXPoussoir2.size() - 3), 2) + Math.pow(positionYPoussoir2.get(positionYPoussoir2.size() - 2) - positionYPoussoir2.get(positionYPoussoir2.size() - 3), 2)));

                coeff = distFromLastPos3 / DIVISEUR_PUISSANCE;
                if (coeff > MIN_PUISS && coeff < MAX_PUISS) {
                    poussoir2.setPuissPoussoir(coeff);
                } else if (coeff > MAX_PUISS) {
                    poussoir2.setPuissPoussoir(MAX_PUISS);
                } else {
                    poussoir2.setPuissPoussoir(MIN_PUISS);
                }
                break;
            case 4:
                positionXPoussoir1.remove(0);
                positionYPoussoir1.remove(0);
                positionXPoussoir1.add(poussoir1.getX());
                positionYPoussoir1.add(poussoir1.getY());
                //Calcule la distance parcourue en NB_ELEMENTS_ARRAYLIST itération

                float distFromLast = (float) (Math.sqrt(Math.pow(positionXPoussoir1.get(positionXPoussoir1.size() - 1) - positionXPoussoir1.get(positionXPoussoir1.size() - 2), 2) + Math.pow(positionYPoussoir1.get(positionYPoussoir1.size() - 1) - positionYPoussoir1.get(positionYPoussoir1.size() - 2), 2)));
                //float distFromLast2 = (float)(Math.sqrt(Math.pow(positionXPoussoir1.get(positionXPoussoir1.size() - 2) - positionXPoussoir1.get(positionXPoussoir1.size() - 3), 2) + Math.pow(positionYPoussoir1.get(positionYPoussoir1.size() -2) - positionYPoussoir1.get(positionYPoussoir1.size() -3), 2)));
                float distFromLast2 = (float) (Math.sqrt(Math.pow(positionXPoussoir1.get(positionXPoussoir1.size() - 1) - positionXPoussoir1.get(positionXPoussoir1.size() - 3), 2) + Math.pow(positionYPoussoir1.get(positionYPoussoir1.size() - 1) - positionYPoussoir1.get(positionYPoussoir1.size() - 3), 2)));

                coeff = distFromLast2 / DIVISEUR_PUISSANCE;
                if (coeff > MIN_PUISS && coeff < MAX_PUISS) {
                    poussoir1.setPuissPoussoir(coeff);
                } else if (coeff > MAX_PUISS) {
                    poussoir1.setPuissPoussoir(MAX_PUISS);
                } else {
                    poussoir1.setPuissPoussoir(MIN_PUISS);
                }



                positionXPoussoir2.remove(0);
                positionYPoussoir2.remove(0);
                positionXPoussoir2.add(poussoir2.getX());
                positionYPoussoir2.add(poussoir2.getY());

                //Calcule la distance parcourue en NB_ELEMENTS_ARRAYLIST itération

                float distFromLast3 = (float) (Math.sqrt(Math.pow(positionXPoussoir2.get(positionXPoussoir2.size() - 1) - positionXPoussoir2.get(positionXPoussoir2.size() - 2), 2) + Math.pow(positionYPoussoir2.get(positionYPoussoir2.size() - 1) - positionYPoussoir2.get(positionYPoussoir2.size() - 2), 2)));
                //float distFromLast4 = (float) (Math.sqrt(Math.pow(positionXPoussoir2.get(positionXPoussoir2.size() - 2) - positionXPoussoir2.get(positionXPoussoir2.size() - 3), 2) + Math.pow(positionYPoussoir2.get(positionYPoussoir2.size() - 2) - positionYPoussoir2.get(positionYPoussoir2.size() - 3), 2)));

                coeff = distFromLast3 / DIVISEUR_PUISSANCE;
                if (coeff > MIN_PUISS && coeff < MAX_PUISS) {
                    poussoir2.setPuissPoussoir(coeff);
                } else if (coeff > MAX_PUISS) {
                    poussoir2.setPuissPoussoir(MAX_PUISS);
                } else {
                    poussoir2.setPuissPoussoir(MIN_PUISS);
                }

                positionXPoussoir3.remove(0);
                positionYPoussoir3.remove(0);
                positionXPoussoir3.add(poussoir3.getX());
                positionYPoussoir3.add(poussoir3.getY());
                //Calcule la distance parcourue en NB_ELEMENTS_ARRAYLIST itération

                float distFromLast5 = (float) (Math.sqrt(Math.pow(positionXPoussoir3.get(positionXPoussoir3.size() - 1) - positionXPoussoir3.get(positionXPoussoir3.size() - 2), 2) + Math.pow(positionYPoussoir3.get(positionYPoussoir3.size() - 1) - positionYPoussoir3.get(positionYPoussoir3.size() - 2), 2)));
                //float distFromLast6 = (float) (Math.sqrt(Math.pow(positionXPoussoir3.get(positionXPoussoir3.size() - 2) - positionXPoussoir3.get(positionXPoussoir3.size() - 3), 2) + Math.pow(positionYPoussoir3.get(positionYPoussoir3.size() - 2) - positionYPoussoir3.get(positionYPoussoir3.size() - 3), 2)));

                coeff = distFromLast5 / DIVISEUR_PUISSANCE;
                if (coeff > MIN_PUISS && coeff < MAX_PUISS) {
                    poussoir3.setPuissPoussoir(coeff);
                } else if (coeff > MAX_PUISS) {
                    poussoir3.setPuissPoussoir(MAX_PUISS);
                } else {
                    poussoir3.setPuissPoussoir(MIN_PUISS);
                }

                positionXPoussoir4.remove(0);
                positionYPoussoir4.remove(0);
                positionXPoussoir4.add(poussoir4.getX());
                positionYPoussoir4.add(poussoir4.getY());
                //Calcule la distance parcourue en NB_ELEMENTS_ARRAYLIST itération

                float distFromLast7 = (float) (Math.sqrt(Math.pow(positionXPoussoir4.get(positionXPoussoir4.size() - 1) - positionXPoussoir4.get(positionXPoussoir4.size() - 2), 2) + Math.pow(positionYPoussoir4.get(positionYPoussoir4.size() - 1) - positionYPoussoir4.get(positionYPoussoir4.size() - 2), 2)));
                //float distFromLast8 = (float) (Math.sqrt(Math.pow(positionXPoussoir4.get(positionXPoussoir4.size() - 2) - positionXPoussoir4.get(positionXPoussoir4.size() - 3), 2) + Math.pow(positionYPoussoir4.get(positionYPoussoir4.size() - 2) - positionYPoussoir4.get(positionYPoussoir4.size() - 3), 2)));

                coeff = distFromLast7 / DIVISEUR_PUISSANCE;
                if (coeff > MIN_PUISS && coeff < MAX_PUISS) {
                    poussoir4.setPuissPoussoir(coeff);
                } else if (coeff > MAX_PUISS) {
                    poussoir4.setPuissPoussoir(MAX_PUISS);
                } else {
                    poussoir4.setPuissPoussoir(MIN_PUISS);
                }

                break;
        }

        //Déplace la balle en fonction de la vitesse et de la direction
        float vitesse = getMaRondelle().getVitesse();

        //Déplace la rondelle
        getMaRondelle().setX((float) (getMaRondelle().getX() + (getProportionX() * (coeffPoussoir * vitesse))));
        getMaRondelle().setY((float) (getMaRondelle().getY() + (getProportionY() * (coeffPoussoir * vitesse))));

        float xRondelle = getMaRondelle().getX();
        float yRondelle = getMaRondelle().getY();
        int radiusRondelle = getMaRondelle().getRadius();
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

        if (maRondelle.getCompteurAvailable() != 0) {
            int currentCompteur = maRondelle.getCompteurAvailable();
            maRondelle.setCompteurAvailable(currentCompteur - 1);
        } else {
            maRondelle.setAvailable(true);
        }

        //Si la rondelle est disponible à la colision
        //Pour chaque poussoir, il y a contact si distance entre rondelle et poussoir est < que le radius de la rondelle + le radius du poussoir
        if (maRondelle.isAvailable() && maRondelle.getCompteurAvailable() == 0) {
            int compteurAvailable = 8;
            for (Poussoir poussoir : getPoussoirs()) {
                float distanceRondelle = (float) (Math.sqrt(Math.pow(maRondelle.getX() - poussoir.getX(), 2) + Math.pow(maRondelle.getY() - poussoir.getY(), 2)));
                float hitboxInterieur = (5 * poussoir.getPuissPoussoir() + 10);
                float hitboxExterieure = (30 * poussoir.getPuissPoussoir());

                if ((distanceRondelle <= maRondelle.getRadius() + poussoir.getRadius() + hitboxExterieure) && (distanceRondelle >= 0)) {
                    hitboxExt = hitboxExterieure;
                    hitboxInt = hitboxInterieur;

                    //Explication de la suite du code :
                    //Etant donné qu'il n'y a contact que quand la distance séparant les deux centres des cercles (Rondelle et poussoir),
                    //le maximum de distance est le radius de la rondelle + le radius du poussoir.
                    //De plus, plus le deltaX (xB - xA) / deltaY est élevé, plus cela veut dire qu'on tape dans l'autre direction (X -> Y)
                    //Exemple : On tape à l'horizontal dans la rondelle
                    //-> Le deltaY est très peu élevé
                    //-> Le deltaX est très élevé (tend vers la distance max)
                    //Ainsi, en divisant deltaX et deltaY par la distance max, on obtient un chiffre entre 0 et 1,
                    // soit la proportion de mouvement à appliquer dans chaque direction (0.70 -> 70% en X et 0.30 -> 30% en Y)
                    float deltaX = maRondelle.getX() - poussoir.getX();
                    float deltaY = maRondelle.getY() - poussoir.getY();
                    float proportionX = deltaX / (maRondelle.getRadius() + poussoir.getRadius());
                    float proportionY = deltaY / (maRondelle.getRadius() + poussoir.getRadius());

                    coeffPoussoir = poussoir.getPuissPoussoir();
                    //Log.d("test", coeffPoussoir + "");

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

        //Gère les contacts entre la rondelle et les murs
        if (pointNRondelle[1] > (0 + 10) && dehorsHaut == true) {
            dehorsHaut = false;
            //Log.d("removeDehors",  "dehorsHaut = false");

        } else if (pointNRondelle[1] < (0 + 10)) {

            if (dehorsHaut != true) {
                //Log.d("Mur haut", "Mur haut");
                getMaRondelle().setDirection("ELSE");
                setProportionX(getProportionX() * 1);
                setProportionY(getProportionY() * -1);
                dehorsHaut = true;
            }
        } else if ((pointSRondelle[1] < (getHeight() - 10)) && dehorsBas == true) {
            dehorsBas = false;
            //Log.d("removeDehors",  "dehorsBas = false");
        } else if ((pointSRondelle[1] > (getHeight() - 10))) {
            //Touche le mur bas

            if (dehorsBas != true) {
                //Log.d("Mur bas", "Mur bas");
                getMaRondelle().setDirection("ELSE");
                setProportionX(getProportionX() * 1);
                setProportionY(getProportionY() * -1);
                dehorsBas = true;
            }
        } else if (pointERondelle[0] < (getWidth() - 10) && dehorsDroite == true) {
            dehorsDroite = false;
            //Log.d("removeDehors",  "dehorsDroite = false");

        } else if (pointERondelle[0] > (getWidth() - 10)) {
            //Touche le mur droite
            if (dehorsDroite != true) {
                //Log.d("Mur bas", "Mur droite");
                getMaRondelle().setDirection("ELSE");
                setProportionX(getProportionX() * -1);
                setProportionY(getProportionY() * 1);
                dehorsDroite = true;
            }

        } else if (pointORondelle[0] > (0 + 10) && dehorsGauche == true) {
            dehorsGauche = false;

        } else if (pointORondelle[0] < 0 + 10) {
            //Touche le mur gauche
            if (dehorsGauche != true) {
                //Log.d("Mur gauche", "Mur gauche");
                getMaRondelle().setDirection("ELSE");
                setProportionX(getProportionX() * -1);
                setProportionY(getProportionY() * 1);
                dehorsGauche = true;
            }
        }
    }

    /**
     * Pour un but marqué, modifie le score et le nombre de palets joués.
     * En fonction du score, appelle la méthode reset() pour replacer les objets ou partie.finPartie()
     */
    public void butMarque(int idEquipe) {
        nbRondellesJouees = nbRondellesJouees + 1;
        if (idEquipe == 1) {
            scoreEquipe1 = scoreEquipe1 + 1;
            Log.i("score equipe 1", String.valueOf(scoreEquipe1));
        } else if (idEquipe == 2) {
            scoreEquipe2 = scoreEquipe2 + 1;
            Log.i("score equipe 2", String.valueOf(scoreEquipe2));
        }

        if (scoreEquipe1 == 10 || scoreEquipe2 == 10) {
            //Log.d("finPartie", "finPartie");
            //Log.d("finPartie2", "finPartie");
            partie.finPartie();
        } else {
            reset();
        }

    }

    /*
    public void ajoutVictoirePlayer() {
        // Récupérer les joueurs actuels de chaque équipe
        List<Joueur> equipe1Joueurs = joueurDao.getJoueursByEquipe1("equipe1");
        List<Joueur> equipe2Joueurs = joueurDao.getJoueursByEquipe2("equipe2");

        if (scoreEquipe1 >= 10) {
            for (Joueur joueur : equipe1Joueurs) {
                // Augmenter la valeur de playerNbVictoire pour chaque joueur de l'équipe 1
                joueur.setPlayerNbVictoire(joueur.getPlayerNbVictoire() + 1);
                int nbPoints = joueur.getPlayerNbPtsTotal() + scoreEquipe1;
                joueur.setPlayerNbPtsTotal(nbPoints);
                Log.d("nbPoints", nbPoints + "");

                joueurDao.updateJoueur(joueur);
            }
            Log.d("scoreEquipe2", scoreEquipe2 + "");

            for (Joueur joueur : equipe2Joueurs) {
                // Augmenter la valeur de playerNbPtsTotal pour chaque joueur de l'équipe 2
                int nbPoints = joueur.getPlayerNbPtsTotal() + scoreEquipe2;
                joueur.setPlayerNbPtsTotal(nbPoints);
                Log.d("nbPoints", nbPoints + "");

                joueurDao.updateJoueur(joueur);
            }
        } else if (scoreEquipe2 >= 10) {
            for (Joueur joueur : equipe2Joueurs) {
                // Augmenter la valeur de playerNbVictoire pour chaque joueur de l'équipe 2
                joueur.setPlayerNbVictoire(joueur.getPlayerNbVictoire() + 1);
                int nbPoints = joueur.getPlayerNbPtsTotal() + scoreEquipe2;
                joueur.setPlayerNbPtsTotal(nbPoints);
                Log.d("nbPoints", nbPoints + "");

                joueurDao.updateJoueur(joueur);
            }
            Log.d("scoreEquipe2", scoreEquipe2 + "");

            for (Joueur joueur : equipe1Joueurs) {
                // Augmenter la valeur de playerNbPtsTotal pour chaque joueur de l'équipe 2
                joueur.setPlayerNbPtsTotal(joueur.getPlayerNbPtsTotal() + scoreEquipe1);

                joueurDao.updateJoueur(joueur);
            }
        }
    }
    */



    /**
     * Récupère et place dans un tableau les poussoirs
     *
     * @return un tableau des poussoirs
     */
    public Poussoir[] getPoussoirs() {
        Poussoir[] poussoirs = new Poussoir[nbJoueurs];
        switch (nbJoueurs) {
            case 2:
                poussoirs[0] = poussoir1;
                poussoirs[1] = poussoir2;
                break;
            case 4:
                poussoirs[0] = poussoir1;
                poussoirs[1] = poussoir2;
                poussoirs[2] = poussoir3;
                poussoirs[3] = poussoir4;
                break;
        }

        return poussoirs;
    }

    /**
     * Récupère et place dans un tableau les buts
     *
     * @return un tableau des buts
     */
    public But[] getButs() {
        But[] buts = new But[2];
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