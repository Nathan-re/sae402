package fr.rey.dev.sae402;



import android.content.Context;

import androidx.room.Database;
import androidx.room.TypeConverters;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Joueur.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    // on crée une instance de la classe qui est statique : pattern singleton
// qui garantie l'existence d'une seule instance
    private static AppDataBase bddInstance;
    // on crée un accès pour chaque DAO
    public abstract JoueurDAO getContactDAO();
    public static AppDataBase getAppDataBase(Context context) {
        if (bddInstance==null) { // Si la base n'est pas déjsà instanciée
            synchronized (AppDataBase.class) { // Garantie qu'aucun autre processus tente
// de faire le traitement en même temps
                if (bddInstance == null) {
                     bddInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDataBase.class, "nom_de_la_base").build();
                }
            }
        }
        return bddInstance;
    }


}


