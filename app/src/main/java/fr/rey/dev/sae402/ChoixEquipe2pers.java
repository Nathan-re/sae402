package fr.rey.dev.sae402;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

public class ChoixEquipe2pers extends AppCompatActivity
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
        setContentView(R.layout.choix_des_equipes2pers);


        Button ButtonRetourChoixMode = (Button) findViewById(R.id.button_retour_choix_Mode1);

        Button lancer_partie2pers = (Button) findViewById(R.id.lancer_partie2pers);
        ButtonRetourChoixMode.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Retour a l'accueil", "Yep !");

                new Thread(() -> {


                    Intent ButtonRetourChoixMode = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(ButtonRetourChoixMode);
                }).start();

            }}
        ));

        lancer_partie2pers.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Retour a l'accueil", "Yep !");

                new Thread(() -> {


                    Intent LancerPartie2Pers = new Intent(getApplicationContext(), PartieClassique.class);
                    startActivity(LancerPartie2Pers);
                }).start();

            }}
        ));


        /*Intent intentHistorique = new Intent(getApplicationContext(), MainActivity.class);

        new Thread(() -> {
            accessDataBase();




            startActivity(intentHistorique);
        }).start();
*/
    }




}












