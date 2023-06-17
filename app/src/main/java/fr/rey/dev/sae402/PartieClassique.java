package fr.rey.dev.sae402;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import fr.rey.dev.sae402.AppDataBase;
import fr.rey.dev.sae402.JoueurDAO;
import fr.rey.dev.sae402.Joueur;


public class PartieClassique extends AppCompatActivity implements View.OnTouchListener {

    private LinearLayout layout;
    private GameView maGameView;

    private AppDataBase dbAccess;
    private JoueurDAO daoQuery;




    private int compteur;

    private int nbJoueurs;
    private int x;
    private int y;

    private String[] equipe1;
    private String[] equipe2;


    public void accessDataBase() {
        dbAccess = AppDataBase.getAppDataBase(this);
        daoQuery = dbAccess.getJoueurDao();
    }





    private final static int RADIUS_TOUCHE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partie_classique);

        accessDataBase();


        equipe1 = getIntent().getStringArrayExtra("equipe1");
        equipe2 = getIntent().getStringArrayExtra("equipe2");

       // Log.i("Choix des équipes", "Joueur D équipe 2: " + equipe1.getEquipe());
        //Log.i("Choix des équipes", "Joueur D équipe 2: " + equipe2.getEquipe());
        this.nbJoueurs = 4;

        GameView maGameView = new GameView(this, nbJoueurs, this); // coucou c'est klara ^^
        maGameView.setOnTouchListener(this);

        setMaGameView(maGameView);

        this.setLayout(findViewById(R.id.layout));
        layout.addView(maGameView, 0);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        compteur = 0;



    }

    /**
     * S'éxecute à chaque touche, mouvement ou à un lâcher de touche.
     *Pour chaque pointeur, recherche un poussoir à côté de la touche,
     *s'il y en a un la méthode modifie ses coordonnées pour correspondre à celle de la touche.
     *
     * @return true
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //Log.d("test", view.getHeight() + "");
        int nbPointers = motionEvent.getPointerCount();

        if (nbPointers > nbJoueurs + 4){
            nbPointers = nbJoueurs + 4;
        }else{
            int activePointer = motionEvent.getActionIndex();

            switch(motionEvent.getAction() & MotionEvent.ACTION_MASK){
                case MotionEvent.ACTION_DOWN:
                    getMaGameView().addActivePointer(0);

                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    int idToAdd = (int)(motionEvent.getActionIndex());
                    boolean result = getMaGameView().addActivePointer(idToAdd);
                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    getMaGameView().removeActivePointer(motionEvent.getActionIndex());
                    //Log.d("test", "Autre pointer supprimé !!!");
                    break;

                case MotionEvent.ACTION_UP:
                    getMaGameView().setActivePointers(new ArrayList<Integer>());
                    //Log.d("test", "Premier pointer suprimé !!!");
                    break;

                case MotionEvent.ACTION_MOVE:
                    //Log.d("test", "Action move déclenché !!!");
                    break;
            }
        }

        Poussoir[] poussoirs = maGameView.getPoussoirs();;

        for (int i = 0; i < nbPointers; i++) {
            float littleDistance = 999999;
            float littleX = 0;
            float littleY = 0;
            Poussoir currentPoussoir = new Poussoir(0,0);

            for (Poussoir poussoir : poussoirs) {
                if((poussoir.getX() - poussoir.getRadius() >= 0) || (poussoir.getY() - poussoir.getRadius() >= 0) || (poussoir.getY() + poussoir.getRadius() <= view.getHeight()) || (poussoir.getX() + poussoir.getRadius() <= view.getWidth())){
                    if((motionEvent.getX(i) > poussoir.getX() - RADIUS_TOUCHE) && (motionEvent.getX(i) < poussoir.getX() + RADIUS_TOUCHE) && (motionEvent.getY(i) > poussoir.getY() - RADIUS_TOUCHE) && (motionEvent.getY(i) < poussoir.getY() + RADIUS_TOUCHE)){
                        float distance = (float)(Math.sqrt((float)(Math.pow(poussoir.getX() - motionEvent.getX(i),2)) + (float)(Math.pow(poussoir.getY() - motionEvent.getY(i),2))));
                        if(distance < littleDistance){
                            currentPoussoir = poussoir;
                            littleX = motionEvent.getX(i);
                            littleY = motionEvent.getY(i);
                            littleDistance = distance;
                        }
                    }
                }
            }
            currentPoussoir.setX(littleX);
            currentPoussoir.setY(littleY);
        }
        return true;
    }

    public void finPartie(){

        new Thread(() -> {
            // Insérer les joueurs de l'équipe 1 dans la base de données
            for (String pseudo : equipe1) {
                Joueur joueur = new Joueur();
                joueur.setPlayerPseudo(pseudo);
                daoQuery.insertJoueur(joueur);
            }

            // Insérer les joueurs de l'équipe 2 dans la base de données
            for (String pseudo : equipe2) {
                Joueur joueur = new Joueur();
                joueur.setPlayerPseudo(pseudo);
                daoQuery.insertJoueur(joueur);
            }
            Log.i("Equipe 1", Arrays.toString(equipe1));
            Log.i("Equipe 2", Arrays.toString(equipe2));
        }).start();


        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public LinearLayout getLayout() {
        return layout;
    }

    public void setLayout(LinearLayout layout) {
        this.layout = layout;
    }

    public GameView getMaGameView() {
        return maGameView;
    }

    public void setMaGameView(GameView maGameView) {
        this.maGameView = maGameView;
    }

    public String[] getEquipe1() {
        return equipe1;
    }

    public String[] getEquipe2() {
        return equipe2;
    }}

