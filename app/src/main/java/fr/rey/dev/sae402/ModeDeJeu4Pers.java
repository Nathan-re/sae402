package fr.rey.dev.sae402;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

public class ModeDeJeu4Pers extends AppCompatActivity
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
        setContentView(R.layout.mode_de_jeu_4personnes);


        Button ButtonRetour2Pers = (Button) findViewById(R.id.retour4personnes);
      /*  Button ButtonJouer4Pers1 = (Button) findViewById(R.id.choixMode2pers4); */
        Button ButtonJouer4Pers2 = (Button) findViewById(R.id.choixMode1pers4);

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

      /*  ButtonJouer4Pers1.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Retour au choix du nombres de personnes", "Yep !");

                new Thread(() -> {


                    Intent listeJoueur4pers1 = new Intent(getApplicationContext(), ChoixEquipe4pers.class);
                    startActivity(listeJoueur4pers1);
                }).start();

            }}
        ));*/

        ButtonJouer4Pers2.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Retour au choix du nombres de personnes", "Yep !");

                new Thread(() -> {


                    Intent listeJoueur4pers2 = new Intent(getApplicationContext(), ChoixEquipe4pers.class);
                    startActivity(listeJoueur4pers2);
                }).start();

            }}
        ));


    }




}












