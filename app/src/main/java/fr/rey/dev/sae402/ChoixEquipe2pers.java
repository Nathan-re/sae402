package fr.rey.dev.sae402;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.os.AsyncTask;


import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ChoixEquipe2pers extends AppCompatActivity
{
Spinner spinnerequipe1;
Spinner spinnerequipe2;
    private AppDataBase dbAccess;
    private JoueurDAO daoQuery;


    private class DatabaseOperationTask extends AsyncTask<Void, Void, List<Joueur>> {
        @Override
        protected List<Joueur> doInBackground(Void... voids) {
            return daoQuery.getAllJoueursList();
        }

        @Override
        protected void onPostExecute(List<Joueur> joueurs) {
            super.onPostExecute(joueurs);
            ArrayAdapter<Joueur> adapter = new ArrayAdapter<>(ChoixEquipe2pers.this, android.R.layout.simple_spinner_item, joueurs);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerequipe1.setAdapter(adapter);
            spinnerequipe2.setAdapter(adapter);
        }
    }

    public void accessDataBase() {
        dbAccess = AppDataBase.getAppDataBase(this);
        daoQuery = dbAccess.getJoueurDao();
    }

    private class InsertJoueursTask extends AsyncTask<Joueur, Void, Void> {
        @Override
        protected Void doInBackground(Joueur... joueurs) {
            for (Joueur joueur : joueurs) {
                if (daoQuery.getJoueurFromId(joueur.getId()) == null) {
                    daoQuery.insertJoueur(joueur);
                }
            }
            return null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choix_des_equipes2pers);
        accessDataBase();

        spinnerequipe1 = findViewById(R.id.spinner1_2pers);
        spinnerequipe2 = findViewById(R.id.spinner2_2pers);
        Button ButtonRetourChoixMode = findViewById(R.id.button_retour_choix_Mode);
        Button lancer_partie2pers = findViewById(R.id.lancer_partie2pers);


        new DatabaseOperationTask().execute();

        ButtonRetourChoixMode.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Retour a l'accueil", "Yep !");

                new Thread(() -> {


                    Intent ButtonRetourChoixMode = new Intent(getApplicationContext(), ModeDeJeu2Pers.class);
                    startActivity(ButtonRetourChoixMode);
                }).start();

            }}
        ));

        lancer_partie2pers.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Joueur joueurequipe1 = (Joueur) spinnerequipe1.getSelectedItem();
                Joueur joueurequipe2 = (Joueur) spinnerequipe2.getSelectedItem();


                new InsertJoueursTask().execute(joueurequipe1, joueurequipe2);

                Log.i("Choix des équipes", "Joueur  équipe 1: " + joueurequipe1.getPlayerPseudo());
                Log.i("Choix des équipes", "Joueur  équipe 2: " + joueurequipe2.getPlayerPseudo());


                new Thread(() -> {


                    Intent LancerPartie2Pers = new Intent(getApplicationContext(), PartieClassique.class);
                    startActivity(LancerPartie2Pers);
                }).start();

            }}
        ));


        /*Intent intentHistorique = new Intent(getApplicationContext(), MainActivity.class);

        new Thread(() -> {
            accessDataBase();




            startActivity(intentHistorique);
        }).start();
*/
    }




}












