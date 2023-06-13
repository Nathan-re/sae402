package fr.rey.dev.sae402;

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
    public Partie(int _id, String typePartie, int idJoueurGagnant, int score) {
        this._id = _id;
        this.typePartie = typePartie;
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

    // SETTERS

}

