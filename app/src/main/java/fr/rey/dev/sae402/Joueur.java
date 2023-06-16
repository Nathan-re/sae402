package fr.rey.dev.sae402;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import  androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.io.Serializable;


@Entity // Création de la table (Joueur) avec @Entity
public class Joueur  implements Serializable {

    // ATTRIBUTS
    @ColumnInfo(name = "_id") // Nommage de la colonne pour la foreign key dans Partie.java
    @PrimaryKey(autoGenerate = true)
    @NonNull // Création de la clé primaire de la table (id) avec @Entity
    public int _id;
    @ColumnInfo(name = "playerPseudo")
    public String playerPseudo;
    @ColumnInfo(name = "playerColor")
    public int playerColor;
    @ColumnInfo(name = "playerNbDefaite")
    public int playerNbDefaite;
    @ColumnInfo(name = "playerNbVictoire")
    public int playerNbVictoire;
    @ColumnInfo(name = "playerNbPtsTotal")
    public int playerNbPtsTotal;

    @ColumnInfo(name = "equipe")
    public String equipe;

    // CONSTRUCTEUR

     public Joueur () {

        }
    public Joueur (String playerPseudo , int playerColor) {
        this.playerPseudo = playerPseudo;
        this.playerColor = playerColor;
        // On initialise le nombre de victoires et défaites à 0
        this.playerNbDefaite = 0;
        this.playerNbVictoire = 0;
        this.playerNbPtsTotal = 0;

    }
    @Override
    public String toString() {
        return playerPseudo;
    }



    // GETTERS
    public int get_id() { return _id; }
    public String getPlayerPseudo() { return playerPseudo; }
    public int getPlayerColor() { return playerColor; }
    public int getPlayerNbDefaite() { return playerNbDefaite; }
    public int getPlayerNbVictoire() { return playerNbVictoire; }
    public int getPlayerNbPtsTotal() { return playerNbPtsTotal; }

    // SETTERS
    public void setPlayerPseudo(String playerPseudo) { this.playerPseudo = playerPseudo; }
    public void setPlayerColor(int playerColor) {
        this.playerColor = playerColor;
    }
    public void setPlayerNbDefaite(int playerNbDefaite) {
        this.playerNbDefaite = playerNbDefaite;
    }
    public void setPlayerNbVictoire(int playerNbVictoire) { this.playerNbVictoire = playerNbVictoire; }
    public void setPlayerNbPtsTotal(int playerNbPtsTotal) { this.playerNbPtsTotal = playerNbPtsTotal; }
    public void set_id(int _id) {
        this._id = _id;
    }


    public int getId() {
        return _id;
    }


    public String getPseudo() {
        return playerPseudo;
    }

    public String getNom() {
        return playerPseudo;
    }


}


