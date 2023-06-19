package fr.rey.dev.sae402;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;


@Dao // Création de l'interfacte DAO pour partie avec @Dao (liaison)
public interface PartieDAO {
    @Insert // @Insert pour la requête d'ajout
    public void insertPartie(Partie aPartie);
    @Update //@Update pour la requête de modification
    public void updatePartie(Partie aPartie);
    @Delete // @Delete pour la requête de suppression
    public void deletePartie(Partie aPartie);

    @Query("SELECT _id FROM Partie")
    public int [] getAllId();

    @Query("SELECT MAX(_id) FROM Partie")
    int getLastPartieId();

    @Query("SELECT _id FROM Partie")
    public  int getPartieId();
    @Query("SELECT * FROM Partie WHERE _id = :partieId")
    Partie getPartieById(int partieId);


    /* @Query("SELECT score FROM Partie WHERE _id = :idPartie ")*/

}
