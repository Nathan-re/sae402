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

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private LinearLayout layout;
    private GameView maGameView;

    private int nbJoueurs;
    private int x;
    private int y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Rondelle maRondelle = new Rondelle();
        GameView maGameView = new GameView(this, maRondelle);
        maGameView.setOnTouchListener(this);

        setMonPongView(maGameView);

        this.setLayout(findViewById(R.id.layout));
        layout.addView(maGameView);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.nbJoueurs = 4;

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        float[][] coords = new float[nbJoueurs][2];
        int nbPointers = motionEvent.getPointerCount();
        if (nbPointers > nbJoueurs){
            nbPointers = nbJoueurs;
        }

        for (int i = 0; i < nbPointers; i++) {
            coords[i][0] = motionEvent.getX(i);
            coords[i][1] = motionEvent.getY(i);
        }

        getMonPongView().dessin(coords);

        return true;

    }




    public LinearLayout getLayout() {
        return layout;
    }

    public void setLayout(LinearLayout layout) {
        this.layout = layout;
    }

    public GameView getMonPongView() {
        return maGameView;
    }

    public void setMonPongView(GameView monPongView) {
        this.maGameView = monPongView;
    }
}