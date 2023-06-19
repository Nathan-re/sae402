package fr.rey.dev.sae402;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.util.List;

@Entity(
        foreignKeys = {
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
                )
        },
        indices = {
                @Index("idJoueur1"),
                @Index("idJoueur2"),
                @Index("idJoueur3"),
                @Index("idJoueur4")
        }
)
@TypeConverters(ListConverter.class)
public class Partie {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int _id;

    //Attributs
    private String typePartie;
    private Integer idJoueur1;
    private Integer idJoueur2;
    private Integer idJoueur3;
    private Integer idJoueur4;
    private Integer idJoueurGagnant;
    private int score;

    @ColumnInfo(typeAffinity = ColumnInfo.TEXT)
    private List<String> equipe1Joueurs;

    @ColumnInfo(typeAffinity = ColumnInfo.TEXT)
    private List<String> equipe2Joueurs;

    public Partie(int _id, String typePartie, Integer idJoueurGagnant, int score, List<String> equipe1Joueurs, List<String> equipe2Joueurs) {
        this._id = _id;
        this.typePartie = typePartie;
        this.idJoueurGagnant = idJoueurGagnant;
        this.score = score;
        this.equipe1Joueurs = equipe1Joueurs;
        this.equipe2Joueurs = equipe2Joueurs;
    }
    public Partie() {


    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setTypePartie(String typePartie) {
        this.typePartie = typePartie;
    }

    public Integer getIdJoueur1() {
        return idJoueur1;
    }

    public void setIdJoueur1(Integer idJoueur1) {
        this.idJoueur1 = idJoueur1;
    }

    public Integer getIdJoueur2() {
        return idJoueur2;
    }

    public void setIdJoueur2(Integer idJoueur2) {
        this.idJoueur2 = idJoueur2;
    }

    public Integer getIdJoueur3() {
        return idJoueur3;
    }

    public void setIdJoueur3(Integer idJoueur3) {
        this.idJoueur3 = idJoueur3;
    }

    public Integer getIdJoueur4() {
        return idJoueur4;
    }

    public void setIdJoueur4(Integer idJoueur4) {
        this.idJoueur4 = idJoueur4;
    }

    public void setIdJoueurGagnant(Integer idJoueurGagnant) {
        this.idJoueurGagnant = idJoueurGagnant;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int get_id() {
        return _id;
    }

    public String getTypePartie() {
        return typePartie;
    }

    public Integer getIdJoueurGagnant() {
        return idJoueurGagnant;
    }

    public int getScore() {
        return score;
    }

    public List<String> getEquipe1Joueurs() {
        return equipe1Joueurs;
    }

    public void setEquipe1Joueurs(List<String> equipe1Joueurs) {
        this.equipe1Joueurs = equipe1Joueurs;
    }

    public List<String> getEquipe2Joueurs() {
        return equipe2Joueurs;
    }

    public void setEquipe2Joueurs(List<String> equipe2Joueurs) {
        this.equipe2Joueurs = equipe2Joueurs;
    }

    @Override
    public String toString() {
        return  "#" + _id +
                " " + typePartie + '\'' +
                " " + equipe1Joueurs +
                ", score :" + score +
                " " + equipe2Joueurs;
    }
/*    @Override
    public String toString() {
        return "Partie{" +
                "id=" + _id +
                ", mode='" + typePartie + '\'' +
                ", idJoueurGagnant=" + idJoueurGagnant +
                ", score=" + score +
                ", equipe1Joueurs=" + equipe1Joueurs +
                ", equipe2Joueurs=" + equipe2Joueurs +
                // Ajoutez les autres champs de la classe Partie ici
                '}';
    }*/
    public String getEquipes() {
        return equipe1Joueurs.toString() + equipe2Joueurs.toString();
    }


    public String getModeJeu() {
        return typePartie;
    }

    public String getId() {
        return String.valueOf(_id);
    }




    public void setModeJeu(String modeJeu) {
        this.typePartie = modeJeu;
    }
}
