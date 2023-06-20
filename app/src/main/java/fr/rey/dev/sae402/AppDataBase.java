package fr.rey.dev.sae402;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;



@Database(entities = {Joueur.class, Partie.class}, version = 4)
@TypeConverters(ListConverter.class)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase bddInstance;

    public abstract JoueurDAO getJoueurDao();
public abstract PartieDAO getPartieDao();

    public static AppDataBase getAppDataBase(Context context) {
        if (bddInstance == null) {
            synchronized (AppDataBase.class) {
                if (bddInstance == null) {
                    bddInstance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDataBase.class, "joueur-partie")
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

