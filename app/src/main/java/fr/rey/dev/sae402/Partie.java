package fr.rey.dev.sae402;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

// Création de la table (Partie) avec @Entity et et des foreign keys (id des joueurs) avec @ForeignKeys
@Entity (foreignKeys = {
        @ForeignKey(
                entity = Joueur.class,
                parentColumns = "_id",
                childColumns = "idJoueur1"
        ),
        @ForeignKey(
                entity = Joueur.class,
                parentColumns = "_id",
                childColumns = "idJoueur2"
        ),
        @ForeignKey(
                entity = Joueur.class,
                parentColumns = "_id",
                childColumns = "idJoueur3"
        ),
        @ForeignKey(
                entity = Joueur.class,
                parentColumns = "_id",
                childColumns = "idJoueur4"
        )})
public class Partie {
    // ATTRIBUTS
    @PrimaryKey(autoGenerate = true) @NonNull
    private int _id;
    private String typePartie;
    // Id des joueurs
    private int idJoueur1;
    private int idJoueur2;
    private int idJoueur3;
    private int idJoueur4;
    private int idJoueurGagnant;
    private int score;



    // CONSTRUCTEUR
    public Partie(int _id, String typePartie, int idJoueur1, int idJoueur2, int idJoueur3, int idJoueur4, int idJoueurGagnant, int score) {
        this._id = _id;
        this.typePartie = typePartie;
<<<<<<< HEAD
=======
        this.idJoueurGagnant = idJoueurGagnant;
        this.score = score;
    }




    public void set_id(int _id) {
        this._id = _id;
    }

    public void setTypePartie(String typePartie) {
        this.typePartie = typePartie;
    }

    public int getIdJoueur1() {
        return idJoueur1;
    }

    public void setIdJoueur1(int idJoueur1) {
>>>>>>> de5312720739f8254b27d5acf151d3ecde2d6712
        this.idJoueur1 = idJoueur1;
        this.idJoueur2 = idJoueur2;
        this.idJoueur3 = idJoueur3;
        this.idJoueur4 = idJoueur4;
        this.idJoueurGagnant = idJoueurGagnant;
        this.score = score;
    }

    // GETTERS
    public int get_id() {
        return _id;
    }

    public String getTypePartie() {
        return typePartie;
    }

    public int getIdJoueurGagnant() {
        return idJoueurGagnant;
    }

    public int getScore() {
        return score;
    }
//


    // SETTERS
    public void setIdJoueurGagnant(int idJoueurGagnant) {
        this.idJoueurGagnant = idJoueurGagnant;
    }
    public void setScore(int score) { this.score = score; }
    public void setTypePartie(String typePartie) { this.typePartie = typePartie; }
}

