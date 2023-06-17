package fr.rey.dev.sae402;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

public class Historique extends AppCompatActivity
{
    // ATTRIBUTS
    private PartieDAO daoQuery;
    private AppDataBase dbAccess;
    private Cursor parties;
    private ListView listViewHistorique;
    //récupérer id Partie

    public void retour(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
    public void accessDataBase() {
        //dbAccess = AppDataBase.getAppDataBase(this); // Le paramètre est l'objet courant
        //daoQuery = dbAccess.getPartieDao();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historique_matchs);

        Button ButtonRetour = (Button) findViewById(R.id.button_retour);

        this.listViewHistorique = findViewById(R.id.listViewHistorique);

        new Thread(() -> {
            accessDataBase();
            Cursor listeParties = daoQuery.getAllParties();

            runOnUiThread(
                    () -> {
                        String[] columnNames = {"typePartie", "score", ""};
                    }
            );
        });

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












