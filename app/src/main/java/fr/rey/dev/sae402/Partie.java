package fr.rey.dev.sae402;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Arrays;
import java.util.List;


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
    // Equipes
    private String[] equipe1Joueurs;

    private String[] equipe2Joueurs;



    // CONSTRUCTEUR
   /* public Partie(int _id, String typePartie, int idJoueurGagnant, int score, String equipe1, String equipe2) {
        this._id = _id;
        this.typePartie = typePartie;
        this.idJoueurGagnant = idJoueurGagnant;
        this.score = score;
        this.equipe1 = equipe1;
        this.equipe2 = equipe2;
    } */

    public Partie(int _id, String typePartie, int idJoueurGagnant, int score, String[] equipe1Joueurs, String[] equipe2Joueurs) {
        this._id = _id;
        this.typePartie = typePartie;
        this.idJoueurGagnant = idJoueurGagnant;
        this.score = score;
this.equipe1Joueurs = equipe1Joueurs;
this.equipe2Joueurs = equipe2Joueurs;
    }


   /*
    public Partie(int id, String classique, int idJoueurGagnant, int score, String equipe11, String equipe22) {
        this._id = _id;
        this.typePartie = typePartie;
        this.idJoueurGagnant = idJoueurGagnant;
        this.score = score;
        this.equipe1 = equipe1;
        this.equipe2 = equipe2;
    }*/






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
        this.idJoueur1 = idJoueur1;
    }

    public int getIdJoueur2() {
        return idJoueur2;
    }

    public void setIdJoueur2(int idJoueur2) {
        this.idJoueur2 = idJoueur2;
    }

    public int getIdJoueur3() {
        return idJoueur3;
    }

    public void setIdJoueur3(int idJoueur3) {
        this.idJoueur3 = idJoueur3;
    }

    public int getIdJoueur4() {
        return idJoueur4;
    }

    public void setIdJoueur4(int idJoueur4) {
        this.idJoueur4 = idJoueur4;
    }

    public void setIdJoueurGagnant(int idJoueurGagnant) {
        this.idJoueurGagnant = idJoueurGagnant;
    }

    public void setScore(int score) {
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

    public String[] getEquipe1Joueurs() {
        return equipe1Joueurs;
    }

    public void setEquipe1Joueurs(String[] equipe1Joueurs) {
        this.equipe1Joueurs = equipe1Joueurs;
    }

    public String[] getEquipe2Joueurs() {
        return equipe2Joueurs;
    }

    public void setEquipe2Joueurs(String[] equipe2Joueurs) {
        this.equipe2Joueurs = equipe2Joueurs;
    }
//


    // SETTERS




}

