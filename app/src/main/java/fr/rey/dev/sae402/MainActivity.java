package fr.rey.dev.sae402;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil_rondelles);
        Button ButtonJoueurs = (Button) findViewById(R.id.commencer_Joueurs);
        Button ButtonHistorique = (Button) findViewById(R.id.button_historique);

        ButtonJoueurs.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("la partie est lancée", "Hey !");

                new Thread(() -> {


                    Intent intent3 = new Intent(getApplicationContext(), choixNombreJoueurs.class);
                    startActivity(intent3);
                }).start();

//<<<<<<< HEAD

//=======
    }

}
//>>>>>>> a2503125b0b703d2c55e2e96c636e02ca595f606
        ));
                ButtonHistorique.setOnClickListener((new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.i("get clicked", "ça clique sur historique ");
                                new Thread(() -> {


                                    Intent intent2 = new Intent(getApplicationContext(), historique.class);
                                    startActivity(intent2);
                                }).start();
                            }}

                ));
      /*  Intent intent = new Intent(getApplicationContext(), PartieClassique.class);
        startActivity(intent);*/




    }
}



