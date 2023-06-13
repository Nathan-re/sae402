package fr.rey.dev.sae402;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import java.util.List;

public class ChoixEquipe2pers extends AppCompatActivity
{
Spinner spinnerequipe1;
Spinner spinnerequipe2;
    private AppDataBase dbAccess;
    private JoueurDAO daoQuery;




    public void accessDataBase() {
        dbAccess = AppDataBase.getAppDataBase(this);
        daoQuery = dbAccess.getJoueurDao();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choix_des_equipes2pers);
        accessDataBase();
       /* spinnerequipe1 = findViewById(R.id.spinner1_2pers);
        spinnerequipe2 = findViewById(R.id.spinner2_2pers);
        Button ButtonRetourChoixMode = (Button) findViewById(R.id.button_retour_choix_Mode1);
        Button lancer_partie2pers = (Button) findViewById(R.id.lancer_partie2pers);


        List<Joueur> joueurs = daoQuery.getAllJoueurs();

        ArrayAdapter<Joueur> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, joueurs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Associer l'adapter au Spinner
        spinnerequipe1.setAdapter(adapter);
        spinnerequipe2.setAdapter(adapter);*/

        spinnerequipe1 = findViewById(R.id.spinner1_2pers);
        spinnerequipe2 = findViewById(R.id.spinner2_2pers);
        Button ButtonRetourChoixMode = findViewById(R.id.button_retour_choix_Mode1);
        Button lancer_partie2pers = findViewById(R.id.lancer_partie2pers);

        Cursor joueurCursor = daoQuery.getAllJoueurs();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_spinner_item,
                joueurCursor,
                new String[]{"playerPseudo"},
                new int[]{android.R.id.text1}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Associer l'adapter au Spinner
        spinnerequipe1.setAdapter(adapter);
        spinnerequipe2.setAdapter(adapter);

        ButtonRetourChoixMode.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Retour a l'accueil", "Yep !");

                new Thread(() -> {


                    Intent ButtonRetourChoixMode = new Intent(getApplicationContext(), ModeDeJeu2Pers.class);
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












