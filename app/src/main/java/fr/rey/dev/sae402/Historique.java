package fr.rey.dev.sae402;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import fr.rey.dev.sae402.AppDataBase;
import fr.rey.dev.sae402.Partie;
import fr.rey.dev.sae402.PartieDAO;

public class Historique extends AppCompatActivity {

    private AppDataBase dbAccess;
    private PartieDAO partieDao;
    private ListView partieListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historique_matchs);

        accessDataBase();

        partieListView = findViewById(R.id.Liste_des_matchs);

        new RetrievePartiesTask().execute();
    }

    private class RetrievePartiesTask extends AsyncTask<Void, Void, List<Partie>> {

        @Override
        protected List<Partie> doInBackground(Void... voids) {
            int[] partieIds = partieDao.getAllId();

            // Récupérer les objets Partie correspondants aux identifiants
            List<Partie> parties = new ArrayList<>();
            for (int partieId : partieIds) {
                Partie partie = partieDao.getPartieById(partieId);
                parties.add(partie);
            }

            return parties;
        }

        @Override
        protected void onPostExecute(List<Partie> parties) {
            // Convertir les objets Partie en une liste de chaînes pour l'affichage
            List<String> partiesStrings = new ArrayList<>();
            for (Partie partie : parties) {
                partiesStrings.add(partie.toString());
            }

            // Afficher la liste des parties dans le ListView
            ArrayAdapter<String> partieAdapter = new ArrayAdapter<>(Historique.this, android.R.layout.simple_list_item_1, partiesStrings);
            partieListView.setAdapter(partieAdapter);

            // Exemple : Afficher les parties dans la console
            for (Partie partie : parties) {
                Log.d("Historique", partie.toString());
            }
        }
    }

    public void accessDataBase() {
        dbAccess = AppDataBase.getAppDataBase(this);
        partieDao = dbAccess.getPartieDao();
    }
}
