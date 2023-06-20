package fr.rey.dev.sae402;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FinDePartie extends AppCompatActivity {

    private AppDataBase dbAccess;
    private PartieDAO partieDao;
    private JoueurDAO joueurDao;
    private int nbJoueurs;
    private Map<String, Integer> joueurVictoires;

    private TextView TextViewScoreEquipe1;
    private TextView TextViewScoreEquipe2;


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fin_de_partie);
        accessDataBase();

        this.TextViewScoreEquipe1 = findViewById(R.id.scoreEquipe1);
        this.TextViewScoreEquipe2 = findViewById(R.id.scoreEquipe2);
        int scoreEquipe1 = getIntent().getIntExtra("scoreEquipe1", 0);
        int scoreEquipe2 = getIntent().getIntExtra("scoreEquipe2", 0);
        this.TextViewScoreEquipe1.setText(scoreEquipe1 + "");
        this.TextViewScoreEquipe2.setText(scoreEquipe2 + "");

        ListView equipe1JoueurListView = findViewById(R.id.equipe1_joueur);
        ListView equipe2JoueurListView = findViewById(R.id.equipe2_joueur);

        nbJoueurs = getIntent().getIntExtra("nbJoueurs", 4);
        ArrayList<String> equipe1JoueursArray = new ArrayList<>();
        ArrayList<String> equipe2JoueursArray = new ArrayList<>();
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

        InsertPartieAsyncTask insertPartieAsyncTask = new InsertPartieAsyncTask(equipe1JoueursArray, equipe2JoueursArray);
        insertPartieAsyncTask.execute();

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

    private class InsertPartieAsyncTask extends AsyncTask<Void, Void, Void> {

        private ArrayList<String> equipe1JoueursArray;
        private ArrayList<String> equipe2JoueursArray;
        private int scoreEquipe1;
        private int scoreEquipe2;

        public InsertPartieAsyncTask(ArrayList<String> equipe1JoueursArray, ArrayList<String> equipe2JoueursArray) {
            this.equipe1JoueursArray = equipe1JoueursArray;
            this.equipe2JoueursArray = equipe2JoueursArray;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dbAccess = AppDataBase.getAppDataBase(getApplicationContext());
            partieDao = dbAccess.getPartieDao();
            joueurDao = dbAccess.getJoueurDao();

            int lastPartieId = partieDao.getLastPartieId();
            int idPartie = lastPartieId + 1;
            int scorePartie = 1;
            scoreEquipe1 = getIntent().getIntExtra("scoreEquipe1", 0);
            scoreEquipe2 = getIntent().getIntExtra("scoreEquipe2", 0);

            int scoreEquipe1 = getIntent().getIntExtra("scoreEquipe1", 0);
            int scoreEquipe2 = getIntent().getIntExtra("scoreEquipe2", 0);
            Log.d("test", scoreEquipe1 + "");
            Log.d("test", scoreEquipe2 + "");

            Partie partie = new Partie(idPartie, "Classique", Integer.valueOf(1), scoreEquipe1, scoreEquipe2, equipe1JoueursArray, equipe2JoueursArray);
            Log.i("Contenu de la partie", partie.toString());


            if (joueursExist(partie.getEquipe1Joueurs()) && joueursExist(partie.getEquipe2Joueurs())) {
                partieDao.insertPartie(partie);
                updateVictoires();
                Log.i("Contenu de la partie", partie.toString());
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

        private void updateVictoires() {
            joueurVictoires = new HashMap<>();

            if (scoreEquipe1 >= 10) {
                for (String joueur : equipe1JoueursArray) {
                    joueurVictoires.put(joueur, joueurVictoires.getOrDefault(joueur, 0) + 1);
                }
            }

            if (scoreEquipe2 >= 10) {
                for (String joueur : equipe2JoueursArray) {
                    joueurVictoires.put(joueur, joueurVictoires.getOrDefault(joueur, 0) + 1);
                }
            }

            for (Map.Entry<String, Integer> entry : joueurVictoires.entrySet()) {
                String pseudo = entry.getKey();
                int victoires = entry.getValue();
                Joueur joueur = joueurDao.getJoueurByPseudo(pseudo);
                joueur.setNombreVictoires(joueur.getNombreVictoires() + 1);
                joueurDao.updateJoueur(joueur);
            }
        }
    }
}
