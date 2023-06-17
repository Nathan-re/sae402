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

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fin_de_partie);
        accessDataBase();

        ListView equipe1JoueurListView = findViewById(R.id.equipe1_joueur);
        ListView equipe2JoueurListView = findViewById(R.id.equipe2_joueur);

        // Récupérer les joueurs sélectionnés dans les spinners
        Joueur joueurAequipe1bis = (Joueur) getIntent().getSerializableExtra("joueurAequipe1");
        Joueur joueurBequipe1bis = (Joueur) getIntent().getSerializableExtra("joueurBequipe1");
        Joueur joueurCequipe2bis = (Joueur) getIntent().getSerializableExtra("joueurCequipe2");
        Joueur joueurDequipe2bis = (Joueur) getIntent().getSerializableExtra("joueurDequipe2");
        ArrayList<String> equipe1JoueursArray = new ArrayList<>(Arrays.asList(joueurAequipe1bis.getPlayerPseudo(), joueurBequipe1bis.getPlayerPseudo()));
        ArrayList<String> equipe2JoueursArray = new ArrayList<>(Arrays.asList(joueurCequipe2bis.getPlayerPseudo(), joueurDequipe2bis.getPlayerPseudo()));

        Log.i("Tableau Equipe 1", equipe1JoueursArray.toString());
        Log.d("Tableau Equipe 2", equipe2JoueursArray.toString());

        Partie partie = new Partie(1, "Classique", 1, 1, equipe1JoueursArray, equipe2JoueursArray);

        InsertPartieAsyncTask insertPartieAsyncTask = new InsertPartieAsyncTask(partieDao, joueurDao);
        insertPartieAsyncTask.execute(partie);
        Log.i("Partie", partie.toString());
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

    private static class InsertPartieAsyncTask extends AsyncTask<Partie, Void, Void> {
        private PartieDAO partieDao;
        private JoueurDAO joueurDao;

        public InsertPartieAsyncTask(PartieDAO partieDao, JoueurDAO joueurDao) {
            this.partieDao = partieDao;
            this.joueurDao = joueurDao;
        }

        @Override
        protected Void doInBackground(Partie... parties) {
            Partie partie = parties[0];
            if (joueursExist(partie.getEquipe1Joueurs()) && joueursExist(partie.getEquipe2Joueurs())) {
                partieDao.insertPartie(partie);
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
