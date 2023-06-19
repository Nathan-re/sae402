package fr.rey.dev.sae402;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FinDePartie extends AppCompatActivity {

    private AppDataBase dbAccess;
    private PartieDAO partieDao;
    private JoueurDAO joueurDao;
    private int nbJoueurs;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fin_de_partie);
        accessDataBase();

        ListView equipe1JoueurListView = findViewById(R.id.equipe1_joueur);
        ListView equipe2JoueurListView = findViewById(R.id.equipe2_joueur);

        nbJoueurs = getIntent().getIntExtra("nbJoueurs", 4);
        ArrayList<String> equipe1JoueursArray = new ArrayList<String>();
        ArrayList<String> equipe2JoueursArray = new ArrayList<String>();
        switch(nbJoueurs){
            case 2:
                Joueur joueurAequipe1bis = (Joueur) getIntent().getSerializableExtra("joueurAequipe1");
                Joueur joueurCequipe2bis = (Joueur) getIntent().getSerializableExtra("joueurCequipe2");
                equipe1JoueursArray.add(joueurAequipe1bis.getPlayerPseudo());
                equipe2JoueursArray.add(joueurCequipe2bis.getPlayerPseudo());

                break;
            case 4:
                Joueur joueurAequipe1 = (Joueur) getIntent().getSerializableExtra("joueurAequipe1");
                Joueur joueurBequipe1 = (Joueur) getIntent().getSerializableExtra("joueurBequipe1");
                Joueur joueurCequipe2 = (Joueur) getIntent().getSerializableExtra("joueurCequipe2");
                Joueur joueurDequipe2 = (Joueur) getIntent().getSerializableExtra("joueurDequipe2");
                equipe1JoueursArray.add(joueurAequipe1.getPlayerPseudo());
                equipe1JoueursArray.add(joueurBequipe1.getPlayerPseudo());
                equipe2JoueursArray.add(joueurCequipe2.getPlayerPseudo());
                equipe2JoueursArray.add(joueurDequipe2.getPlayerPseudo());
                break;
        }
        Log.i("Tableau Equipe 1", equipe1JoueursArray.toString());
        Log.d("Tableau Equipe 2", equipe2JoueursArray.toString());
/*
        int idPartie = partieDao.getPartieId();

        int scorePartie = 1;

        Partie partie = new Partie(idPartie, "Classique", 1, scorePartie, equipe1JoueursArray, equipe2JoueursArray);
*/
       /* InsertPartieAsyncTask insertPartieAsyncTask = new InsertPartieAsyncTask(partieDao, joueurDao);
        insertPartieAsyncTask.execute(partie); */

        InsertPartieAsyncTask insertPartieAsyncTask = new InsertPartieAsyncTask();
        insertPartieAsyncTask.execute(equipe1JoueursArray, equipe2JoueursArray);


      /*  Log.i("Partie", partie.toString()); */
        ArrayAdapter<String> equipe1Adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, equipe1JoueursArray);
        equipe1JoueurListView.setAdapter(equipe1Adapter);

        ArrayAdapter<String> equipe2Adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, equipe2JoueursArray);
        equipe2JoueurListView.setAdapter(equipe2Adapter);

        Button ButtonRetourChoixMode = findViewById(R.id.Fin_button_partie2);
        ButtonRetourChoixMode.setOnClickListener(view -> {
            Intent ButtonFinDePartie = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(ButtonFinDePartie);
        });
    }

    public void accessDataBase() {
        dbAccess = AppDataBase.getAppDataBase(this);
        partieDao = dbAccess.getPartieDao();
        joueurDao = dbAccess.getJoueurDao();
    }

    private class InsertPartieAsyncTask extends AsyncTask<ArrayList<String>, Void, Void> {

        @Override
        protected Void doInBackground(ArrayList<String>... arrayLists) {
            ArrayList<String> equipe1JoueursArray = arrayLists[0];
            ArrayList<String> equipe2JoueursArray = arrayLists[1];

            dbAccess = AppDataBase.getAppDataBase(getApplicationContext());
            partieDao = dbAccess.getPartieDao();
            joueurDao = dbAccess.getJoueurDao();

            int lastPartieId = partieDao.getLastPartieId();
            int idPartie = lastPartieId + 1;
            int scorePartie = 1;


         /*   int idPartie = partieDao.getPartieId();
            int scorePartie = 1; */

            Partie partie = new Partie(idPartie, "Classique", 1, scorePartie, equipe1JoueursArray, equipe2JoueursArray);
            Log.i("Contenue de la partie", partie.toString());
            if (joueursExist(partie.getEquipe1Joueurs()) && joueursExist(partie.getEquipe2Joueurs())) {
                partieDao.insertPartie(partie);
                Log.i("Contenue de la partie", partie.toString());
            } else {
                Log.i("Erreur", "Les joueurs n'existent pas");
            }

            return null;
        }

        private boolean joueursExist(List<String> joueurs) {
            for (String pseudo : joueurs) {
                Joueur joueur = joueurDao.getJoueurByPseudo(pseudo);
                if (joueur == null) {
                    return false;
                }
            }
            return true;
        }
    }
}
