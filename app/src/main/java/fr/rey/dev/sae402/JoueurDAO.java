package fr.rey.dev.sae402;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;

import java.util.List;

@Dao // Création de l'interfacte DAO pour joueur avec @Dao (liaison)
public interface JoueurDAO {
    @Insert // @Insert pour la requête d'ajout
    public void insertJoueur(Joueur aJoueur);

  //  @Insert
    //public  void InscriptionJoueur(Joueur aJoueur);

    @Update //@Update pour la requête de modification
    public void updateJoueur(Joueur aJoueur);
    @Delete // @Delete pour la requête de suppression
    public void deleteJoueur(Joueur aJoueur);
    @Query("SELECT * FROM Joueur") // @Query pour la requête parsonnalisée
    Cursor getAllJoueurs();

    @Query("SELECT * FROM Joueur") // @Query pour la requête parsonnalisée
    List<Joueur> getAllJoueursList();
    @Query("SELECT _id FROM Joueur")
    public int [] getAllId();
    @Query("SELECT * FROM Joueur WHERE _id=:idJoueur")
    public Joueur getJoueurFromId(int idJoueur);


    @Query("SELECT * FROM Joueur WHERE equipe = :equipe11")
    List<Joueur> getJoueursByEquipe1(String equipe11);

    @Query("SELECT * FROM Joueur WHERE equipe = :equipe22")
    List<Joueur> getJoueursByEquipe2(String equipe22);

    @Query("SELECT _id FROM Joueur WHERE playerPseudo = :pseudo")
    int getJoueurId(String pseudo);

    @Query("SELECT * FROM Joueur WHERE playerPseudo = :pseudo LIMIT 1")
    Joueur getJoueurByPseudo(String pseudo);

}