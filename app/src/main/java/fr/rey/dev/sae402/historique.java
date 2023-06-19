package fr.rey.dev.sae402;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

public class historique extends AppCompatActivity
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



    public void retour(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

    }
    public void accessDataBase() {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historique_matchs);
        accessDataBase();

        ListView listeMatchsView = (ListView) findViewById(R.id.Liste_des_matchs);

        Button ButtonRetour = (Button) findViewById(R.id.button_retour);

/*
        ArrayAdapter<String> listeMatchsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, );
        listeMatchsView.setAdapter(listeMatchsAdapter);

*/
        ButtonRetour.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Retour a l'accueil", "Yep !");

                new Thread(() -> {


                    Intent retourIntent1 = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(retourIntent1);
                }).start();

            }}
        ));
    }




}












