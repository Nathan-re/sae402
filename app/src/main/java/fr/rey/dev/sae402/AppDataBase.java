package fr.rey.dev.sae402;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Joueur.class}, version = 3)
public abstract class AppDataBase extends RoomDatabase {
<<<<<<< HEAD
    // on crée une instance de la classe qui est statique : pattern singleton qui garantie l'existence d'une seule instance
    private static AppDataBase bddInstance = null;
    // on crée un accès pour chaque DAO
=======

    private static AppDataBase bddInstance;

>>>>>>> de5312720739f8254b27d5acf151d3ecde2d6712
    public abstract JoueurDAO getJoueurDao();

    public static AppDataBase getAppDataBase(Context context) {
        if (bddInstance == null) {
            synchronized (AppDataBase.class) {
                if (bddInstance == null) {
                    bddInstance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDataBase.class, "joueur-partie")
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return bddInstance;
    }

    static final Migration MIGRATION_1_2 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Créer la nouvelle table avec la structure mise à jour
            database.execSQL("CREATE TABLE IF NOT EXISTS `Joueur` (`_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `playerPseudo` TEXT, `playerColor` INTEGER NOT NULL, `playerNbDefaite` INTEGER NOT NULL, `playerNbVictoire` INTEGER NOT NULL, `playerNbPtsTotal` INTEGER NOT NULL, `equipe` TEXT)");

            // Copier les données de l'ancienne table vers la nouvelle table
            database.execSQL("INSERT INTO `Joueur` (`_id`, `playerPseudo`, `playerColor`, `playerNbDefaite`, `playerNbVictoire`, `playerNbPtsTotal`) SELECT `_id`, `playerPseudo`, `playerColor`, `playerNbDefaite`, `playerNbVictoire`, `playerNbPtsTotal` FROM `Joueur_old`");

            // Supprimer l'ancienne table
            database.execSQL("DROP TABLE IF EXISTS `Joueur_old`");
        }
    };

}

