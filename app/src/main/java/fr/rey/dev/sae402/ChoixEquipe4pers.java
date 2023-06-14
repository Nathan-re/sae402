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

public class ChoixEquipe4pers extends AppCompatActivity
{

    Spinner spinnerAequipe1;
    Spinner spinnerBequipe1;
    Spinner spinnerCequipe2;
    Spinner spinnerDequipe2;
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
            ArrayAdapter<Joueur> adapter = new ArrayAdapter<>(ChoixEquipe4pers.this, android.R.layout.simple_spinner_item, joueurs);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerAequipe1.setAdapter(adapter);
            spinnerBequipe1.setAdapter(adapter);
            spinnerCequipe2.setAdapter(adapter);
            spinnerDequipe2.setAdapter(adapter);
        }
    }

  /*  private class InsertJoueursTask extends AsyncTask<Joueur, Void, Void> {
        @Override
        protected Void doInBackground(Joueur... joueurs) {
            daoQuery.insertJoueur(joueurs[0]);
            daoQuery.insertJoueur(joueurs[1]);
            daoQuery.insertJoueur(joueurs[2]);
            daoQuery.insertJoueur(joueurs[3]);
            return null;
        }
    }*/

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

    public void accessDataBase() {
        dbAccess = AppDataBase.getAppDataBase(this);
        daoQuery = dbAccess.getJoueurDao();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choix_des_equipes4pers);

        accessDataBase();
        spinnerAequipe1 = findViewById(R.id.spinner1_4pers);
        spinnerBequipe1 = findViewById(R.id.spinner2_4pers);
        spinnerCequipe2 = findViewById(R.id.spinner3_4pers);
        spinnerDequipe2 = findViewById(R.id.spinner4_4pers);
        Button lancer_partie4pers = (Button) findViewById(R.id.lancer_partie4pers);
        Button ButtonRetour = (Button) findViewById(R.id.button_retour_choix_Mode2);

        new DatabaseOperationTask().execute();
        ButtonRetour.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Retour a l'accueil", "Yep !");

                new Thread(() -> {


                    Intent retourIntent1 = new Intent(getApplicationContext(), ModeDeJeu4Pers.class);
                    startActivity(retourIntent1);
                }).start();

            }}
        ));


        lancer_partie4pers.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Retour a l'accueil", "Yep !");
                Joueur joueurAequipe1 = (Joueur) spinnerAequipe1.getSelectedItem();
                Joueur joueurBequipe1 = (Joueur) spinnerBequipe1.getSelectedItem();
                Joueur joueurCequipe2 = (Joueur) spinnerCequipe2.getSelectedItem();
                Joueur joueurDequipe2 = (Joueur) spinnerDequipe2.getSelectedItem();

                new InsertJoueursTask().execute(joueurAequipe1, joueurBequipe1, joueurCequipe2, joueurDequipe2);

                Log.i("Choix des équipes", "Joueur A équipe 1: " + joueurAequipe1.getPlayerPseudo());
                Log.i("Choix des équipes", "Joueur B équipe 1: " + joueurBequipe1.getPlayerPseudo());
                Log.i("Choix des équipes", "Joueur C équipe 2: " + joueurCequipe2.getPlayerPseudo());
                Log.i("Choix des équipes", "Joueur D équipe 2: " + joueurDequipe2.getPlayerPseudo());
                new Thread(() -> {

                    Intent LancerPartie4Pers = new Intent(getApplicationContext(), PartieClassique.class);
                    startActivity(LancerPartie4Pers);
                }).start();

            }}
        ));
    }




}