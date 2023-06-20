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
        daoQuery = dbAccess.getJoueurDao();
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


                String playerPseudo = inscriptionPseudo.getText().toString();
                Joueur joueur = new Joueur(playerPseudo, 1, 0, 0, 0);

                Log.i("Nom du joueur", inscriptionPseudo.getText().toString());
                new Thread(() -> {
                    accessDataBase();
                    daoQuery.insertJoueur(joueur);
                    Intent InscriptionEtRetour = new Intent(getApplicationContext(), choixNombreJoueurs.class);
                    startActivity(InscriptionEtRetour);
                }).start();

               /* Contact contact =  new Contact(nom, prenom, adresse, codePostal, ville, pays, telephone);

                new Thread(() -> {
                    accessDataBase();

                    daoQuery.insertContact(contact);
                    Log.i("Le contenu de contact est", String.valueOf(contact));
                    startActivity(intent3);
                }).start();*

                */
            }
        }));

    }
}
