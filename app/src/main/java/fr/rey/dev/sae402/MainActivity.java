package fr.rey.dev.sae402;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private LinearLayout layout;
    private GameView maGameView;
    private int compteur;

    private int nbJoueurs;
    private int x;
    private int y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.nbJoueurs = 4;
        GameView maGameView = new GameView(this, nbJoueurs);
        maGameView.setOnTouchListener(this);

        setMaGameView(maGameView);

        this.setLayout(findViewById(R.id.layout));
        layout.addView(maGameView);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        compteur = 0;

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        int nbPointers = motionEvent.getPointerCount();

        if (nbPointers > nbJoueurs){
            nbPointers = nbJoueurs;
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
                    Log.d("test", "Autre pointer supprimé !!!");
                    break;

                case MotionEvent.ACTION_UP:
                    getMaGameView().setActivePointers(new ArrayList<Integer>());
                    Log.d("test", "Premier pointer suprimé !!!");
                    break;

                case MotionEvent.ACTION_MOVE:
                    Log.d("test", "Action move déclenché !!!");

                    setCompteur(getCompteur() + 1);
                    break;
            }
        }

        Log.d("test", getCompteur() + "");



        Poussoir[] poussoirs = getMaGameView().getPoussoirs();

        for (int i = 0; i < nbPointers; i++) {
            float littleDistance = 999999;
            float littleX = 0;
            float littleY = 0;
            Poussoir currentPoussoir = new Poussoir(0,0);

            for (Poussoir poussoir : poussoirs) {
                if((motionEvent.getX(i) > poussoir.getX() - 300) && (motionEvent.getX(i) < poussoir.getX() + 300) && (motionEvent.getY(i) > poussoir.getY() - 300) && (motionEvent.getY(i) < poussoir.getY() + 300)){
                    float distance = (float)(Math.sqrt((float)(Math.pow(poussoir.getX() - motionEvent.getX(i),2)) + (float)(Math.pow(poussoir.getY() - motionEvent.getY(i),2))));
                    if(distance < littleDistance){
                        currentPoussoir = poussoir;
                        littleX = motionEvent.getX(i);
                        littleY = motionEvent.getY(i);
                        littleDistance = distance;

                    }
                }

            }
            currentPoussoir.setX(littleX);
            currentPoussoir.setY(littleY);
        }

        return true;

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

    public int getCompteur() {
        return compteur;
    }

    public void setCompteur(int compteur) {
        this.compteur = compteur;
    }
}