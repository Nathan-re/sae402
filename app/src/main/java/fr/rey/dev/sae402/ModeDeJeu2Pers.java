package fr.rey.dev.sae402;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

public class ModeDeJeu2Pers extends AppCompatActivity
{


    private JoueurDAO daoQuery;


    private Joueur joeueur;
    private TextView textView;
    private Button button;
    private EditText editText;
    private String nom;
    private String prenom;
    private String score;
    private String date;
    private String heure;
    private String duree;
    private String gagnant;
    private String perdant;
    private String scoreGagnant;
    private String scorePerdant;



    public void accessDataBase() {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mode_de_jeu_2personnes);


        Button ButtonRetour2Pers = (Button) findViewById(R.id.retour2personnes);
        Button ButtonJouer2Pers1 = (Button) findViewById(R.id.choixMode1pers2);
        Button ButtonJouer2Pers2 = (Button) findViewById(R.id.choixMode2pers2);

        ButtonRetour2Pers.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Retour au choix du nombres de personnes", "Yep !");

                new Thread(() -> {


                    Intent retourIntent2pers = new Intent(getApplicationContext(), choixNombreJoueurs.class);
                    startActivity(retourIntent2pers);
                }).start();

            }}
        ));

        ButtonJouer2Pers1.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Retour au choix du nombres de personnes", "Yep !");

                new Thread(() -> {


                    Intent listeJoueur2pers1 = new Intent(getApplicationContext(), ChoixEquipe2pers.class);
                    startActivity(listeJoueur2pers1);
                }).start();

            }}
        ));

        ButtonJouer2Pers2.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Retour au choix du nombres de personnes", "Yep !");

                new Thread(() -> {


                    Intent listeJoueur2pers2 = new Intent(getApplicationContext(), ChoixEquipe2pers.class);
                    startActivity(listeJoueur2pers2);
                }).start();

            }}
        ));


    }




}












