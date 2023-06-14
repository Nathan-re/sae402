package fr.rey.dev.sae402;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

public class Historique extends AppCompatActivity
{
    private JoueurDAO daoQuery;

// score equipe pseudo, récup id partie avec clé étrangère
    private Joueur joueur;
    private TextView textView;
    private Button button;
    private EditText editText;
    private String nom;
    private String prenom;
    private String score;

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


        Button ButtonRetour = (Button) findViewById(R.id.button_retour);

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


        /*Intent intentHistorique = new Intent(getApplicationContext(), MainActivity.class);

        new Thread(() -> {
            accessDataBase();




            startActivity(intentHistorique);
        }).start();
*/
    }




}












