package fr.rey.dev.sae402;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class IncriptionJoueur extends AppCompatActivity {

    private AppDataBase dbAccess;
    private JoueurDAO daoQuery;

    EditText inscriptionPseudo;


    public void accessDataBase() {

        dbAccess = AppDataBase.getAppDataBase(this);
        //daoQuery = dbAccess.getJoueurDAO();



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inscription_joueur);

        Button ButtonInscriptionJoueur = (Button) findViewById(R.id.button_envoie_inscription_joueur);
        Button ButtonRetourInscriptionJoueur = (Button) findViewById(R.id.button_retour_nombre_joueurs_2);

        inscriptionPseudo  = (EditText) findViewById(R.id.inscriptionPseudo) ;

        ButtonRetourInscriptionJoueur.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Retour a la page précédente ", "Yep !");

                new Thread(() -> {
                    Intent RetourChoixNbreJoueur = new Intent(getApplicationContext(), choixNombreJoueurs.class);
                    startActivity(RetourChoixNbreJoueur);
                }).start();
            }
        }));

        ButtonInscriptionJoueur.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Inscription du joueur", "Yep !");

                new Thread(() -> {
                    Intent InscriptionEtRetour = new Intent(getApplicationContext(), choixNombreJoueurs.class);
                    startActivity(InscriptionEtRetour);
                }).start();
            }
        }));

    }
}
