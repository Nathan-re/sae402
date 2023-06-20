package fr.rey.dev.sae402;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import fr.rey.dev.sae402.AppDataBase;
import fr.rey.dev.sae402.Joueur;
import fr.rey.dev.sae402.JoueurDAO;

public class ListeDeJoueurs extends AppCompatActivity {

    private AppDataBase dbAccess;
    private JoueurDAO joueurDao;
    private ListView joueurListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liste_des_joueurs);
        Button retour = findViewById(R.id.button_retourList);
        accessDataBase();

        joueurListView = findViewById(R.id.liste_des_jouer_view);

        new RetrieveJoueursTask().execute();

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Retour a l'accueil", "Yep !");
                Intent retourAccueil = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(retourAccueil);
                Log.i("TAG", "onClick: ");
            }
        });
    }

    private class RetrieveJoueursTask extends AsyncTask<Void, Void, List<Joueur>> {

        @Override
        protected List<Joueur> doInBackground(Void... voids) {
            int[] joueurIds = joueurDao.getAllId();

            // Récupérer les objets Joueur correspondants aux identifiants
            List<Joueur> joueurs = new ArrayList<>();
            for (int joueurId : joueurIds) {
                Joueur joueur = joueurDao.getJoueurFromId(joueurId);
                joueurs.add(joueur);
            }

            return joueurs;
        }

        @Override
        protected void onPostExecute(List<Joueur> joueurs) {
            // Convertir les objets Joueur en une liste de chaînes pour l'affichage
            List<String> joueursStrings = new ArrayList<>();
            for (Joueur joueur : joueurs) {
                joueursStrings.add(joueur.toSimpleString());
            }

            // Afficher la liste des joueurs dans le ListView
            ArrayAdapter<String> joueurAdapter = new ArrayAdapter<>(ListeDeJoueurs.this, android.R.layout.simple_list_item_1, joueursStrings);
            joueurListView.setAdapter(joueurAdapter);

            // Exemple : Afficher les joueurs dans la console
            for (Joueur joueur : joueurs) {
                Log.d("Historique", joueur.toSimpleString());
            }
        }
    }

    public void accessDataBase() {
        dbAccess = AppDataBase.getAppDataBase(this);
        joueurDao = dbAccess.getJoueurDao();
    }
}
