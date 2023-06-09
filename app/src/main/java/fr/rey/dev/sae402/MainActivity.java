package fr.rey.dev.sae402;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choix_des_equipes);
        Button ButtonPlay = (Button) findViewById(R.id.lancer_partie);

        ButtonPlay.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("la partie est lancée", "Hey !");

                new Thread(() -> {


                    Intent intent3 = new Intent(getApplicationContext(), PartieClassique.class);
                    startActivity(intent3);
                }).start();

    }

}
        ));

    }
}

