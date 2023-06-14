package fr.rey.dev.sae402;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import fr.rey.dev.sae402.AppDataBase;


import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class choixNombreJoueurs extends AppCompatActivity
{
    private AppDataBase dbAccess;
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
        dbAccess = AppDataBase.getAppDataBase(this);
        daoQuery = dbAccess.getJoueurDao();

    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choix_nombres_de_joueurs);


Button Button2Pers = (Button) findViewById(R.id.choixJoueurs2);
        Button Button4Pers = (Button) findViewById(R.id.choixJoueurs4);
Button ButtonInscriptionJoueur = (Button) findViewById(R.id.InscriptionButton);
        Button ButtonRetourChoixNombreJoueurs = (Button) findViewById(R.id.button_retour_choix_Joueur);

        ButtonRetourChoixNombreJoueurs.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Retour a l'accueil", "Yep !");

                new Thread(() -> {


                    Intent retourIntent2 = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(retourIntent2);
                }).start();

            }}
        ));

        Button2Pers.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Allez ", "Yep !");

                new Thread(() -> {


                    Intent versPageMode2Pers = new Intent(getApplicationContext(), ModeDeJeu2Pers.class);
                    startActivity(versPageMode2Pers);
                }).start();

            }}
        ));

        Button4Pers.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Allez ", "Yep !");

                new Thread(() -> {


                    Intent versPageMode4Pers = new Intent(getApplicationContext(), ModeDeJeu4Pers.class);
                    startActivity(versPageMode4Pers);
                }).start();

            }}
        ));

        ButtonInscriptionJoueur.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Allez ", "Yep !");

                new Thread(() -> {

                    Intent versPageInscriptionJoueur = new Intent(getApplicationContext(), IncriptionJoueur.class);
                    startActivity(versPageInscriptionJoueur);
                }).start();
            }}
        ));

    }




}












