package fr.rey.dev.sae402;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class FinDePartie extends AppCompatActivity {

    private AppDataBase dbAccess;
    private JoueurDAO daoQuery;

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

        String[] equipe1Joueurs = {joueurAequipe1bis.getPlayerPseudo(), joueurBequipe1bis.getPlayerPseudo()};
        String[] equipe2Joueurs = {joueurCequipe2bis.getPlayerPseudo(), joueurDequipe2bis.getPlayerPseudo()};

        String[] equipe1JoueursArray = {joueurAequipe1bis.getPlayerPseudo(), joueurBequipe1bis.getPlayerPseudo()};
        String[] equipe2JoueursArray = {joueurCequipe2bis.getPlayerPseudo(), joueurDequipe2bis.getPlayerPseudo()};

        Partie partie = new Partie(1, "Classique", 1,1, equipe1JoueursArray, equipe2JoueursArray );
        dbAccess.getPartieDao().insertPartie(partie);

        ArrayAdapter<String> equipe1Adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, equipe1Joueurs);
        equipe1JoueurListView.setAdapter(equipe1Adapter);

        ArrayAdapter<String> equipe2Adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, equipe2Joueurs);
        equipe2JoueurListView.setAdapter(equipe2Adapter);

        Button ButtonRetourChoixMode = findViewById(R.id.Fin_button_partie2);
        ButtonRetourChoixMode.setOnClickListener(view -> {
            Intent ButtonFinDePartie = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(ButtonFinDePartie);
        });
    }

    public void accessDataBase() {
        dbAccess = AppDataBase.getAppDataBase(this);
        daoQuery = dbAccess.getJoueurDao();
    }
}
