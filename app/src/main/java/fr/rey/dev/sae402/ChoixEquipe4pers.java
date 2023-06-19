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

import java.util.ArrayList;
import java.util.List;

import fr.rey.dev.sae402.AppDataBase;
import fr.rey.dev.sae402.Joueur;
import fr.rey.dev.sae402.JoueurDAO;
import fr.rey.dev.sae402.ModeDeJeu4Pers;
import fr.rey.dev.sae402.Partie;
import fr.rey.dev.sae402.PartieClassique;
import fr.rey.dev.sae402.PartieDAO;
import fr.rey.dev.sae402.R;

public class ChoixEquipe4pers extends AppCompatActivity {

    Spinner spinnerAequipe1;
    Spinner spinnerBequipe1;
    Spinner spinnerCequipe2;
    Spinner spinnerDequipe2;
    private AppDataBase dbAccess;
    private JoueurDAO daoQuery;
    private PartieDAO partieDAO;

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
        partieDAO = dbAccess.getPartieDao();
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
        Button lancer_partie4pers = findViewById(R.id.lancer_partie4pers);
        Button buttonRetour = findViewById(R.id.button_retour_choix_Mode2);

        new DatabaseOperationTask().execute();
        buttonRetour.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Retour à l'accueil", "Yep !");
                Intent retourIntent1 = new Intent(getApplicationContext(), ModeDeJeu4Pers.class);
                startActivity(retourIntent1);
            }
        }));

        lancer_partie4pers.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Lancer la partie", "Yep !");
                Joueur joueurAequipe1 = (Joueur) spinnerAequipe1.getSelectedItem();
                Joueur joueurBequipe1 = (Joueur) spinnerBequipe1.getSelectedItem();
                Joueur joueurCequipe2 = (Joueur) spinnerCequipe2.getSelectedItem();
                Joueur joueurDequipe2 = (Joueur) spinnerDequipe2.getSelectedItem();

                String[] equipe1String = new String[]{joueurAequipe1.getPlayerPseudo(), joueurBequipe1.getPlayerPseudo()};
                String[] equipe2String = new String[]{joueurCequipe2.getPlayerPseudo(), joueurDequipe2.getPlayerPseudo()};


                new InsertJoueursTask().execute(joueurAequipe1, joueurBequipe1, joueurCequipe2, joueurDequipe2);
                Log.i("Choix des équipes", "Joueur A équipe 1: " + joueurAequipe1.getPlayerPseudo());
                Log.i("Choix des équipes", "Joueur B équipe 1: " + joueurBequipe1.getPlayerPseudo());
                Log.i("Choix des équipes", "Joueur C équipe 2: " + joueurCequipe2.getPlayerPseudo());
                Log.i("Choix des équipes", "Joueur D équipe 2: " + joueurDequipe2.getPlayerPseudo());

                new Thread(() -> {
                    Intent lancerPartie4Pers = new Intent(getApplicationContext(), PartieClassique.class);
                    lancerPartie4Pers.putExtra("equipe1String", equipe1String);
                    lancerPartie4Pers.putExtra("equipe2String", equipe2String);
                    String[] equipe1 = new String[]{joueurAequipe1.getPlayerPseudo(), joueurBequipe1.getPlayerPseudo()};
                    String[] equipe2 = new String[]{joueurCequipe2.getPlayerPseudo(), joueurDequipe2.getPlayerPseudo()};

                    int[] equipe1Id = new int[]{joueurAequipe1.getId(), joueurBequipe1.getId()};
                    int[] equipe2Id = new int[]{joueurCequipe2.getId(), joueurDequipe2.getId()};
                    Log.i("TABLEAU EQUIPE 1", String.valueOf(equipe1[0]));
                    Log.i("TABLEAU EQUIPE 1", String.valueOf(equipe1[1]));
                    Log.i("TABLEAU EQUIPE 2", String.valueOf(equipe2[0]));
                    Log.i("TABLEAU EQUIPE 2", String.valueOf(equipe2[1]));

                    Log.i("TABLEAU EQUIPE 1 ID", String.valueOf(equipe1Id[0]));
                    Log.i("TABLEAU EQUIPE 1 ID", String.valueOf(equipe1Id[1]));
                    Log.i("TABLEAU EQUIPE 2 ID", String.valueOf(equipe2Id[0]));
                    Log.i("TABLEAU EQUIPE 2 ID", String.valueOf(equipe2Id[1]));


                    lancerPartie4Pers.putExtra("joueurAequipe1", joueurAequipe1);
                    lancerPartie4Pers.putExtra("joueurBequipe1", joueurBequipe1);
                    lancerPartie4Pers.putExtra("joueurCequipe2", joueurCequipe2);
                    lancerPartie4Pers.putExtra("joueurDequipe2", joueurDequipe2);

                    lancerPartie4Pers.putExtra("nbJoueurs", (int) (4));

                    Log.d("test", "before intent partie");

                    startActivity(lancerPartie4Pers);

                }).start();
            }
        }));
    }
}
