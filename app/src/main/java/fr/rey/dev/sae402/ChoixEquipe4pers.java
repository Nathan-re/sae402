package fr.rey.dev.sae402;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

public class ChoixEquipe4pers extends AppCompatActivity
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
        setContentView(R.layout.choix_des_equipes4pers);

        Button lancer_partie4pers = (Button) findViewById(R.id.lancer_partie4pers);
        Button ButtonRetour = (Button) findViewById(R.id.button_retour_choix_Mode2);

        ButtonRetour.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Retour a l'accueil", "Yep !");

                new Thread(() -> {


                    Intent retourIntent1 = new Intent(getApplicationContext(), ModeDeJeu4Pers.class);
                    startActivity(retourIntent1);
                }).start();

            }}
        ));


        lancer_partie4pers.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Retour a l'accueil", "Yep !");

                new Thread(() -> {


                    Intent LancerPartie4Pers = new Intent(getApplicationContext(), PartieClassique.class);
                    startActivity(LancerPartie4Pers);
                }).start();

            }}
        ));
    }




}












